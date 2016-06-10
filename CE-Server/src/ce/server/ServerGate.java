package ce.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerGate {
	private final ServerSocket socket;
	private final Thread runner;
	private final List<Object> clients = new CopyOnWriteArrayList<>();

	public ServerGate() throws IOException {
		this.socket = new ServerSocket(666);
		this.runner = new Thread(this::acceptClients);
		this.runner.start();

	}

	private void acceptClients() {
		try {
			Socket client = this.socket.accept();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void exit() {
		this.runner.interrupt();
	}
}
