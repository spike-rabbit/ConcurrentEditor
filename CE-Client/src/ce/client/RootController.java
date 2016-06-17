package ce.client;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

/**
 * 
 * @author Florian.Loddenkemper
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
	private void onConnect(ActionEvent e) {
		try {
			Editor editor = new Editor(this.ip.getText(), this.port.getText(), this.username.getText());
			ClientMain.setScene(new Scene(editor));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
