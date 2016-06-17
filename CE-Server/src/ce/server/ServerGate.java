package ce.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ce.shared.Change;
import ce.shared.ChangeSubmit;
import ce.shared.Connection;

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
		this.socket = new ServerSocket(80);
		this.acceptRunner = new Thread(this::acceptClients);
		this.acceptRunner.start();
		System.out.println("ServerGate started!");

	}

	private void acceptClients() {
		while (true) {
			try {
				Socket client = this.socket.accept();
				Connection con;
				this.clients.add(con = new Connection(client, "unknown", this::onMessage, t -> this.clients.remove(t)));
				con.sendChangeSubmit(new ChangeSubmit(FileHandler.getInstance().getCurrent(),
						FileHandler.getInstance().getCurrentV()));
				System.out.println("Client accepted!");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendAll(ChangeSubmit cs) {
		this.clients.forEach(client -> client.sendChangeSubmit(cs));
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
