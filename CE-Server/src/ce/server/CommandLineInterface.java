package ce.server;

import java.util.Scanner;

public class CommandLineInterface {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		boolean running = true;
		while (running) {
			String line = scanner.nextLine();
			if (line.startsWith("stop")) {
				running = false;
			}
		}

		scanner.close();
	}

}
