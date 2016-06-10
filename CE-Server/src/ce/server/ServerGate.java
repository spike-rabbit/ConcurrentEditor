package ce.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ce.shared.Change;
import ce.shared.Connection;

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
			this.clients.add(new Connection(client, client.toString(), ServerGate::onMessage));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void exit() {
		this.runner.interrupt();
	}

	private static void onMessage(Change change) {

	}
}
