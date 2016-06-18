package ce.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ce.shared.Change;
import ce.shared.ChangeSubmit;
import ce.shared.Connection;
import ce.shared.Type;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

/**
 * Controller for Client Editor UI
 *
 * @author Florian.Loddenkemper
 * @author Maximilian.Koeller
 *
 */
public class Editor extends BorderPane implements Initializable {

	private Connection connection;

	private volatile boolean changesApplied = false;

	private long serverV = 0;

	@FXML
	private TextArea textField;

	/**
	 * Connects the editor to the server
	 *
	 * @param ip
	 *            server ip
	 * @param port
	 *            server port
	 * @param username
	 *            client username
	 * @throws IOException
	 *             is thrown if connecting fails
	 */
	public Editor(String ip, String port, String username) throws IOException {
		FXMLLoader loader = new FXMLLoader(Editor.class.getResource("Editor.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		loader.load();

		this.connection = new Connection(ip, port, username, this::messageReceived, this::onDisconnect);
	}

	/**
	 * Post-Constructor initilization
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.textField.textProperty().addListener(this::onTextChange);
	}

	/**
	 * Eventhandler for received Messages
	 *
	 * @param change
	 *            Change Object
	 */
	private void messageReceived(Object change) {
		if (change instanceof ChangeSubmit) {
			Platform.runLater(() -> {
				ChangeSubmit cast = (ChangeSubmit) change;
				System.out.println("Server Submit: " + cast.getText());
				int pos = this.textField.getCaretPosition();
				if (!this.textField.getText().equals(cast.getText())) {
					this.changesApplied = true;
					this.textField.setText(cast.getText());
					if (pos > cast.getIndex()) {
						switch (cast.getType()) {
						case INSERT:
							pos += cast.getLength();
							break;
						case DELETE:
							pos -= cast.getLength();
						default:
							break;
						}
					}
				}
				this.serverV = cast.getServerID();
				this.textField.positionCaret(pos);
			});
		}
	}

	/**
	 * event for shutdown to show connect ui
	 *
	 * @param connection
	 *            not handeld with, but needed for correct calling
	 */
	private void onDisconnect(Connection connection) {
		ClientMain.setRoot();
	}

	/**
	 * Eventhandler for Changed Text.
	 *
	 * @param observable
	 * @param oldValue
	 * @param newValue
	 */
	private void onTextChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		// Was the change initiated by the Client
		if (this.changesApplied) {
			this.changesApplied = false;
		} else {
			boolean deleteCandidate = newValue.length() < oldValue.length();
			int index = -1;
			String text;

			for (int i = 0; i < (deleteCandidate ? newValue.length() : oldValue.length()); i++) {
				if (newValue.charAt(i) != oldValue.charAt(i)) {
					index = i;
					if (deleteCandidate) {
						boolean replaceIndex = false;
						int diffLengt = oldValue.length() - newValue.length();
						for (int j = newValue.length() - 1; j >= index && !replaceIndex; j--) {
							replaceIndex = newValue.charAt(j) != oldValue.charAt(j + diffLengt);
						}
						if (!replaceIndex) {
							text = oldValue.substring(index, diffLengt + index);
							this.connection.sendChange(
									new Change(text, index, Type.DELETE, oldValue.hashCode(), this.serverV));
						} else {
							text = newValue.substring(index);
							this.connection.sendChange(
									new Change(text, index, Type.REPLACE, oldValue.hashCode(), this.serverV));
						}

						break;
					} else {
						boolean isReplace = false;
						int diffLength = newValue.length() - oldValue.length();
						for (int j = index; j < oldValue.length() && !isReplace; j++) {
							isReplace = oldValue.charAt(index) != newValue.charAt(index + diffLength);
						}
						if (isReplace) {
							newValue.substring(index);
							this.connection.sendChange(new Change(newValue.substring(index), index, Type.REPLACE,
									oldValue.hashCode(), this.serverV));
						} else {
							text = newValue.substring(index, newValue.length() - oldValue.length() + index);
							this.connection.sendChange(
									new Change(text, index, Type.INSERT, oldValue.hashCode(), this.serverV));
						}
						break;
					}
				}
			}

			if (index == -1 && deleteCandidate) {
				this.connection.sendChange(new Change(oldValue.substring(newValue.length()), newValue.length(),
						Type.DELETE, oldValue.hashCode(), this.serverV));
			}
			if (index == -1 && !deleteCandidate) {
				this.connection.sendChange(new Change(newValue.substring(oldValue.length()), oldValue.length(),
						Type.INSERT, oldValue.hashCode(), this.serverV));
			}

		}

	}

	/**
	 * closes connection if window closes
	 *
	 * @param event
	 *            not handeld with, but needed for correct calling
	 */
	@FXML
	private void onClose(ActionEvent event) {
		this.connection.close();
	}

}
