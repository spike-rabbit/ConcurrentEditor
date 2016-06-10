package ce.shared;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class Connection {
	private Socket socket;
	private String name;
	private Thread readerRunner;
	private Consumer<Object> messageHandler;
	
	public Connection(String address, String port, String name, Consumer<Object> messageHandler){
		try{
			socket = new Socket(address, Integer.parseInt(port));
			this.name = name;
			this.messageHandler = messageHandler;
			this.init();
		}catch (Exception e){
			
		}
		
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
					Object Object = (Object) ois.readObject();
					this.messageHandler.accept(Object);
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO
			}
		});
		this.readerRunner.start();
	}

	public void sendObject(Object Object) {
		
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
