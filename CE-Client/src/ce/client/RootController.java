package ce.client;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Florian.Loddenkemper
 * @author Maximilian.Koeller
 *
 */
public class RootController {

	@FXML
	private TextField username;

	@FXML
	private TextField ip;

	@FXML
	private TextField port;

	@FXML
	private Label error;

	@FXML
	private void onConnect(ActionEvent e) {
		try {
			Editor editor = new Editor(this.ip.getText(), this.port.getText(), this.username.getText());
			this.error.setText(null);
			ClientMain.setScene(new Scene(editor));
		} catch (IOException e1) {
			this.error.setText("Failed to Connect");
			e1.printStackTrace();
		}
	}
}
