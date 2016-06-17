package ce.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ce.shared.Change;
import ce.shared.ChangeSubmit;
import ce.shared.Connection;
import ce.shared.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
/**
 * Controller for Cleint Editor UI
 * @author Florian.Loddenkemper
 *
 */
public class Editor extends BorderPane implements Initializable {

	private Connection connection;

	private boolean changesApplied = false;

	private long serverV = 0;

	@FXML
	private TextArea textField;

	/**
	 * connects the editor to the server
	 * @param ip server ip
	 * @param port server port
	 * @param username clients username
	 * @throws IOException is thrown if connecting fails
	 */
	public Editor(String ip, String port, String username) throws IOException {
		FXMLLoader loader = new FXMLLoader(Editor.class.getResource("Editor.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		loader.load();

		this.connection = new Connection(ip, port, username, this::messageReceived, this::onDisconnect);
	}

	/**
	 * creates the interface for ui and addes files content
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.textField.textProperty().addListener((prop, oldT, newT) -> {
			if (this.changesApplied) {
				this.changesApplied = false;
			} else {

				boolean deleteCandidate = newT.length() < oldT.length();
				int index = -1;
				String text;

				for (int i = 0; i < (deleteCandidate ? newT.length() : oldT.length()); i++) {
					if (newT.charAt(i) != oldT.charAt(i)) {
						index = i;
						if (deleteCandidate) {
							text = oldT.substring(index, oldT.length() - newT.length() + index);
							// TODO detect replace
							this.connection
									.sendChange(new Change(text, index, Type.DELETE, oldT.hashCode(), this.serverV));
							break;
						} else {
							text = newT.substring(index, newT.length() - oldT.length() + index);
							this.connection
									.sendChange(new Change(text, index, Type.INSERT, oldT.hashCode(), this.serverV));
							break;
						}
					}
				}

				if (index == -1 && !deleteCandidate) {
					this.connection.sendChange(new Change(newT.substring(oldT.length()), oldT.length(), Type.INSERT,
							oldT.hashCode(), this.serverV));
				}

			}
		});

	}

	/**
	 * displays received messages in controller
	 * @param change
	 */
	private void messageReceived(Object change) {
		if (change instanceof ChangeSubmit) {
			ChangeSubmit cast = (ChangeSubmit) change;
			this.textField.setText(cast.getText());
			this.serverV = cast.getServerID();
		}
	}

	/**
	 * event for shutdown to show connect ui
	 * @param connection not handeld with, but needed for correct calling
	 */
	private void onDisconnect(Connection connection) {
		ClientMain.setRoot();
	}

	/**
	 * closes connection if window closes
	 * @param event not handeld with, but needed for correct calling
	 */
	@FXML
	private void onClose(ActionEvent event) {
		this.connection.close();
	}

}
