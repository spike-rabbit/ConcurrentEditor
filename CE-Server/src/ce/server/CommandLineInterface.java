package ce.server;

import java.io.IOException;
import java.util.Scanner;

public class CommandLineInterface {

	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		boolean running = true;
		ServerGate.getInstance();
		while (running) {
			String line = scanner.nextLine();
			if (line.startsWith("stop")) {
				running = false;
			}
		}

		shutdown();

	}

	public static void shutdown() {
		scanner.close();
		ServerGate.getInstance().close();
		FileHandler.getInstance().close();
	}

	public static void shutdownOnError(Throwable t) {
		t.printStackTrace();
		shutdown();
	}

}
