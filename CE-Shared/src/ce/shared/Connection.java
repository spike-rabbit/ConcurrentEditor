package ce.shared;

import java.net.Socket;
import java.util.function.Consumer;

public class Connection {
	private Socket socket;
	private String name;
	private Thread readerRunner;
	private Consumer<Change> messageHandler;
	
	public Connection(String address, String name, Consumer<Change> messageHandler){
		
	}
	
	public void sendChange(Change change){
		
	}
	
	public Thread getReaderRunner(){
		
	}
	
	public String getName(){
		
	}
}
