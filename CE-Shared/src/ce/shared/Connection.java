package ce.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class Connection {
	private final Socket socket;
	private String name;
	private Thread readerRunner;
	private final Consumer<Object> messageHandler;
	private final Consumer<Connection> onClose;

	public Connection(String address, String port, String name, Consumer<Object> messageHandler,
			Consumer<Connection> onClose) throws IOException {
		this(new Socket(address, Integer.parseInt(port)), name, messageHandler, onClose);
	}

	public Connection(Socket socket, String name, Consumer<Object> messageHandler, Consumer<Connection> onClose) {
		this.socket = socket;
		this.name = name;
		this.messageHandler = messageHandler;
		this.onClose = onClose;
		init();
	}

	private void init() {
		this.readerRunner = new Thread(() -> {
			try {
				ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
				while (true) {
					Object change;
					try {
						change = ois.readObject();
						this.messageHandler.accept(change);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Log e
				close();
			}
		});
		this.readerRunner.start();
	}

	public void sendChange(Change change) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
			oos.writeObject(change);

		} catch (IOException e) {
			// TODO Log e
			close();
		}
	}

	public String getName() {
		return this.name;
	}

	public Thread getReaderRunner() {
		return this.readerRunner;
	}

	public Consumer<Object> getMessageHandler() {
		return this.messageHandler;
	}

	public void close() {
		this.readerRunner.interrupt();
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.onClose.accept(this);
	}

}
