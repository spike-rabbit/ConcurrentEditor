package ce.shared;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class Connection {
	private Socket socket;
	private String name;
	private Thread readerRunner;
	private Consumer<Change> messageHandler;
	
	public Connection(String address, String port, String name, Consumer<Change> messageHandler){
		try{
			socket = new Socket(address, Integer.parseInt(port));
			
		}catch (Exception e){
			
		}
		
	}
	
	public Connection(Socket socket, String name, Consumer<Change> messageHandler) {
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
					Change change = (Change) ois.readObject();
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

	public Consumer<Change> getMessageHandler() {
		return this.messageHandler;
	}

}
