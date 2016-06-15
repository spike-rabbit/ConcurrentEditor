package ce.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

	private static Scene root;

	private static Stage stage;

	public static void main(String[] args) {
		ClientMain.launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		Scene scene = new Scene(FXMLLoader.load(ClientMain.class.getResource("Root.fxml")));
		arg0.setScene(scene);
		arg0.show();
		ClientMain.root = scene;
		ClientMain.stage = arg0;

	}

	public static void setScene(Scene scene) {
		stage.setScene(scene);
	}

	public static void setRoot() {
		Platform.runLater(() -> stage.setScene(root));
	}
}
