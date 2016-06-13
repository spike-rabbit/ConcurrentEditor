package ce.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class Connection {
	private final Socket socket;
	private String name;
	private Thread readerRunner;
	private final Consumer<Object> messageHandler;

	public Connection(String address, String port, String name, Consumer<Object> messageHandler) throws IOException {
		this(new Socket(address, Integer.parseInt(port)), name, messageHandler);
	}

	public Connection(Socket socket, String name, Consumer<Object> messageHandler) {
		this.socket = socket;
		this.name = name;
		this.messageHandler = messageHandler;
		init();
	}

	private void init() {
		this.readerRunner = new Thread(() -> {
			try {
				ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
				while (true) {
					Object change = ois.readObject();
					this.messageHandler.accept(change);
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO
			}
		});
		this.readerRunner.start();
	}

	public void sendChange(Change change) {

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
	}

}
