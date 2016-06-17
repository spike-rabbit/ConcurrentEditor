package ce.shared;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

/***
 * handles connections for server an client
 * defines way of communication
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
	 * creates new connection
	 * @param address ip to connect
	 * @param port port used to connect
	 * @param name client's name
	 * @param messageHandler takes messages from network and transfers them to ui
	 * @throws IOException if connection can't be established
	 */
	public Connection(String address, String port, String name, Consumer<Object> messageHandler,
			Consumer<Connection> onClose) throws IOException {
		this(new Socket(address, Integer.parseInt(port)), name, messageHandler, onClose);
	}

	/***
	 *
	 * @param socket socket for connection
	 * @param name cleint's name
	 * @param messageHandler  takes messages from network and transfers them to ui
	 * @throws IOException if connection can't be established
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
	 * starts new connection with new thread
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
	 * sends change object
	 * @param change change from client
	 */
	public void sendChange(Change change) {
		System.out.println("Send Change: " + change);
		sendObject(change);
	}

	public void sendChangeSubmit(ChangeSubmit cs) {
		sendObject(cs);
	}

	/**
	 * sends objects via network
	 * @param object will be sended
	 */
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

	/**
	 * String
	 * @return client's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Thread
	 * @return thread for handeling connection
	 */
	public Thread getReaderRunner() {
		return this.readerRunner;
	}

	/**
	 * Consumer for Messages
	 * @return Changes and ChangeSubmits
	 */
	public Consumer<Object> getMessageHandler() {
		return this.messageHandler;
	}

	/**
	 * closes connection 
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
