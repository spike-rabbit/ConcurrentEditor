package ce.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ce.shared.Connection;
import ce.shared.UserAccept;

public class ServerGate {
	private final ServerSocket socket;
	private final Thread runner;
	private final List<Connection> clients = new CopyOnWriteArrayList<>();

	public ServerGate() throws IOException {
		this.socket = new ServerSocket(666);
		this.runner = new Thread(this::acceptClients);
		this.runner.start();

	}

	private void acceptClients() {
		try {
			Socket client = this.socket.accept();
			ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
			UserAccept ua = (UserAccept) ois.readObject();
			this.clients.add(new Connection(client, ua.getUserName(), ServerGate::onMessage));
			ois.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		this.runner.interrupt();
		this.clients.forEach(Connection::close);
	}

	private static void onMessage(Object change) {

	}
}
