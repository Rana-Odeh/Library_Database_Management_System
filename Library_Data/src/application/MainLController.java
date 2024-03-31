package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class MainLController {
	@FXML
	private Button Qattan_Library;
	@FXML
	private Button B2;
	@FXML
	private Button B1;

	// Event Listener on Button[#B2].onAction
	@FXML
	public void Exit(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) B2.getScene().getWindow();
			stage.close();
		} catch (Exception e1) {

		}
	}

	// Event Listener on Button[#B1].onAction
	@FXML
	public void LogIn(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) B1.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("Page1.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}

}