package ce.server;

import java.io.IOException;
import java.util.Scanner;

/**
 * Main for starting server
 * @author Florian.Loddenkemper
 *
 */
public class CommandLineInterface {

	private static Scanner scanner = new Scanner(System.in);

	/**
	 * starts server and listens for stop command
	 * @param args if null start server with start and filepath else use filepath in args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if(args.length != 0){
			boolean running = true;
			ServerGate.getInstance();
			while (running) {
				String line = scanner.nextLine();
				if (line.startsWith("stop")) {
					running = false;
					FileHandler.getInstance().saveFile(args[0]);
				}
			}
		}else{
			String line = scanner.nextLine();
			if(line.split(" ")[0].equals("start")){
				boolean running = true;
				ServerGate.getInstance();
				while (running) {
					String endline = scanner.nextLine();
					if (endline.startsWith("stop")) {
						running = false;
						FileHandler.getInstance().saveFile(line.split(" ")[1]);
					}
				}
			}
		}
		shutdown();

	}

	/**
	 * normal servershutdown
	 */
	public static void shutdown() {
		scanner.close();
		ServerGate.getInstance().close();
		FileHandler.getInstance().close();
	}
	
	/**
	 * prints error and closes server
	 * @param t error made server to fail
	 */
	public static void shutdownOnError(Throwable t) {
		t.printStackTrace();
		shutdown();
	}

}
