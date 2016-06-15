package ce.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ce.shared.Change;
import ce.shared.Connection;
import ce.shared.UserAccept;

public class ServerGate {

	private static ServerGate instance;

	public static ServerGate getInstance() {
		if (instance == null) {
			try {
				instance = new ServerGate();
			} catch (IOException e) {
				CommandLineInterface.shutdownOnError(e);
			}
		}

		return instance;
	}

	private final ServerSocket socket;
	private final Thread acceptRunner;
	private final List<Connection> clients = new CopyOnWriteArrayList<>();

	private ServerGate() throws IOException {
		this.socket = new ServerSocket(666);
		this.acceptRunner = new Thread(this::acceptClients);
		this.acceptRunner.start();
		System.out.println("ServerGate started!");

	}

	private void acceptClients() {
		try {
			Socket client = this.socket.accept();
			ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
			UserAccept ua = (UserAccept) ois.readObject();
			this.clients.add(new Connection(client, ua.getUserName(), this::onMessage, t -> this.clients.remove(t)));
			ois.close();
			System.out.println("Client " + ua.getUserName() + " accepted!");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		this.acceptRunner.interrupt();
		this.clients.forEach(Connection::close);
	}

	private void onMessage(Object change) {
		if (change instanceof Change) {
			Change cast = (Change) change;
			FileHandler.getInstance().getChanges().add(cast);
		} else {
			// TODO Log error
		}
	}

}
