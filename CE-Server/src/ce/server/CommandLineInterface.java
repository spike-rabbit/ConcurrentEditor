package ce.server;

import java.io.IOException;
import java.util.Scanner;

/**
 * Main for starting server
 *
 * @author Florian.Loddenkemper
 * @author Maximilian.Koeller
 *
 *
 */
public class CommandLineInterface {

	private static Scanner scanner = new Scanner(System.in);
	/**
	 *
	 */
	private static String destination = "default.txt";

	/**
	 * starts server and listens for stop command
	 *
	 * @param args
	 *            if null start server with start and filepath else use filepath
	 *            from args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length > 0) {
			destination = args[0];
		}

		boolean running = true;
		while (running) {
			String[] line = scanner.nextLine().split(" ");

			switch (line[0]) {
			case "start":
				if (line.length > 1) {
					destination = line[1];
				}
				ServerGate.getInstance();
				break;
			case "stop":
				FileHandler.getInstance().saveFile();
				running = false;
				shutdown();
				break;
			default:
				System.out.println("Unknown Command");
				break;
			}
		}

	}

	/**
	 * normal servershutdown
	 */
	public static void shutdown() {
		scanner.close();
		ServerGate.getInstance().close();
		FileHandler.getInstance().close();
		System.exit(0);
	}

	/**
	 * prints error and closes server
	 *
	 * @param t
	 *            error causes server to fail
	 */
	public static void shutdownOnError(Throwable t) {
		t.printStackTrace();
		shutdown();
	}

	public static String getDestination() {
		return destination;
	}

}
