package ce.shared;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

/***
 *
 * @author Florian.Loddenkemper
 *
 */
public class Connection {
	private final Socket socket;
	private final ObjectOutputStream sender;
	private String name;
	private Thread readerRunner;
	private final Consumer<Object> messageHandler;
	private final Consumer<Connection> onClose;

	/***
	 * creates new
	 *
	 * @param address
	 * @param port
	 * @param name
	 * @param messageHandler
	 * @throws IOException
	 */
	public Connection(String address, String port, String name, Consumer<Object> messageHandler,
			Consumer<Connection> onClose) throws IOException {
		this(new Socket(address, Integer.parseInt(port)), name, messageHandler, onClose);
	}

	/***
	 *
	 * @param socket
	 * @param name
	 * @param messageHandler
	 * @throws IOException
	 */
	public Connection(Socket socket, String name, Consumer<Object> messageHandler, Consumer<Connection> onClose)
			throws IOException {
		this.socket = socket;
		this.name = name;
		this.messageHandler = messageHandler;
		this.onClose = onClose;
		this.sender = new ObjectOutputStream(this.socket.getOutputStream());
		init();
	}

	/***
	 *
	 */
	private void init() {
		this.readerRunner = new Thread(() -> {
			try {
				ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
				while (true) {
					Object change;
					try {
						change = ois.readObject();
						System.out.println("Message received");
						this.messageHandler.accept(change);
					} catch (EOFException e) {
						// Noting to do here
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Log e
				close();
			}
		});
		this.readerRunner.start();
		sendObject(new UserAccept(this.name));
	}

	/***
	 *
	 * @param change
	 */
	public void sendChange(Change change) {
		System.out.println("Send Change: " + change);
		sendObject(change);
	}

	public void sendChangeSubmit(ChangeSubmit cs) {
		sendObject(cs);
	}

	private void sendObject(Object object) {
		try {
			this.sender.writeObject(object);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(this.socket.getLocalPort());
			System.out.println(this.socket.getPort());
			close();
		}
	}

	/***
	 *
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/***
	 *
	 * @return
	 */
	public Thread getReaderRunner() {
		return this.readerRunner;
	}

	/***
	 *
	 * @return
	 */
	public Consumer<Object> getMessageHandler() {
		return this.messageHandler;
	}

	/***
	 *
	 */
	public void close() {
		this.readerRunner.interrupt();
		try {
			this.sender.close();
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.onClose.accept(this);
	}

}
