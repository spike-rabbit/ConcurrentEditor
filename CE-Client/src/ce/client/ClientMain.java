package ce.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * opens a new client interface
 * @author Florian.Loddenkemper
 *
 */
public class ClientMain extends Application {

	private static Scene root;

	private static Stage stage;
	
	/**
	 * opens client interface
	 * @param args
	 */
	public static void main(String[] args) {
		ClientMain.launch(args);
	}

	/**
	 * opens a new stage for connecting to server
	 */
	@Override
	public void start(Stage arg0) throws Exception {
		Scene scene = new Scene(FXMLLoader.load(ClientMain.class.getResource("Root.fxml")));
		arg0.setScene(scene);
		arg0.show();
		ClientMain.root = scene;
		ClientMain.stage = arg0;

	}

	/**
	 * changes to editor ui
	 * @param scene next user interface(editor or connect to server)
	 */
	public static void setScene(Scene scene) {
		stage.setScene(scene);
	}
	
	/**
	 * starts Connection interface
	 */
	public static void setRoot() {
		Platform.runLater(() -> stage.setScene(root));
	}
}
