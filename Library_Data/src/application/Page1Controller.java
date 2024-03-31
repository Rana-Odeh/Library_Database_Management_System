package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

public class Page1Controller {
	@FXML
	private TextField T1;
	@FXML
	private TextField T2;
	@FXML
	private Button B1;
	@FXML
	private Button B2;

	@FXML
	void OnActionBack(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) B2.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("MainL.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}

	// Event Listener on Button[#B1].onAction
	@FXML
	public void Login(ActionEvent event) {

		if (T1.getText().trim().isEmpty() && T2.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("error");
			alert.setHeaderText("Username and Password cannot be empty");
			alert.showAndWait();
		} else if (T1.getText().trim().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("error");
			alert.setHeaderText("Username  cannot be empty");
			alert.showAndWait();

		}

		else if (T2.getText().trim().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("error");
			alert.setHeaderText("Password  is empty");
			alert.showAndWait();

		} else {

			String dbUsername = Connector.a.getdbUsername();
			String Password = Connector.a.getdbPassword();
			String r1 = T1.getText();
			String r2 = T2.getText();

			if (dbUsername.equals(r1) && Password.equals(r2)) {
				try {
					Stage stage;
					stage = (Stage) B1.getScene().getWindow();
					stage.close();
					AnchorPane root = FXMLLoader.load(getClass().getResource("Bage2.fxml"));
					Scene scene = new Scene(root, 1000, 700);
					stage.setScene(scene);

					stage.show();

				} catch (Exception e1) {

				}
			} else if (!(dbUsername.equals(r1)) && Password.equals(r2)) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("error");
				alert.setHeaderText("The Username is False");
				alert.showAndWait();

			} else if (dbUsername.equals(r1) && !(Password.equals(r2))) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("error");
				alert.setHeaderText("The Password is False");
				alert.showAndWait();

			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("error");
				alert.setHeaderText("The Password  and Username are False");
				alert.showAndWait();
			}

		}
	}
}