package ce.server;

import java.io.IOException;
import java.util.Scanner;

public class CommandLineInterface {

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		ServerGate gate = new ServerGate();
		boolean running = true;
		while (running) {
			String line = scanner.nextLine();
			if (line.startsWith("stop")) {
				running = false;
			}
		}

		scanner.close();
		gate.close();
	}

}
