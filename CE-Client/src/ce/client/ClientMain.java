package ce.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

	private static Scene scene;

	public static void main(String[] args) {
		ClientMain.launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		Scene scene = new Scene(FXMLLoader.load(ClientMain.class.getResource("Root.fxml")));
		arg0.setScene(scene);
		arg0.show();
		ClientMain.scene = scene;

	}

	public static void setRoot(Parent parent) {
		scene.setRoot(parent);
	}
}
