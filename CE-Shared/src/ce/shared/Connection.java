package ce.shared;

import java.net.Socket;
import java.util.function.Consumer;

public class Connection {
	private Socket socket;
	private String name;
	private Thread readerRunner;
	private Consumer<Change> messageHandler;
	
	public Connection(String address, String port, String name, Consumer<Change> messageHandler){
		
	}
	
	public Connection(){
		
	}
	
	public void sendChange(Change change){
		
	}
	
	public Thread getReaderRunner(){

		return null;
	}
	
	public String getName(){

		return null;
	}
	
	public Consumer<Change> getMessageHandler(){

		return null;
	}
}
