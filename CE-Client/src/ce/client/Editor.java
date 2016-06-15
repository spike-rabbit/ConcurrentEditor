package ce.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ce.shared.ChangeSubmit;
import ce.shared.Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class Editor extends BorderPane implements Initializable {

	private Connection connection;

	private boolean changesApplied = false;

	@FXML
	private TextArea textField;

	public Editor(String ip, String port, String username) throws IOException {
		FXMLLoader loader = new FXMLLoader(Editor.class.getResource("Editor.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		loader.load();

		this.connection = new Connection(ip, port, username, this::messageReceived, this::onDisconnect);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.textField.textProperty().addListener((prop, newT, oldT) -> {
			if (this.changesApplied) {
				this.changesApplied = false;
			} else {

			}
		});

	}

	private void messageReceived(Object change) {
		if (change instanceof ChangeSubmit) {
			ChangeSubmit cast = (ChangeSubmit) change;
			this.textField.setText(cast.getText());
		}
	}

	private void onDisconnect(Connection connection) {
		ClientMain.setRoot();
	}

	@FXML
	private void onClose(ActionEvent event) {
		this.connection.close();
	}

}
