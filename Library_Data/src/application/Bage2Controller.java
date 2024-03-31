package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class Bage2Controller {
	@FXML
	private Button S1;
	@FXML
	private Button B1;
	@FXML
	private Button A;
	@FXML
	private Button B2;
	@FXML
	private Button S2;
	@FXML
	private Button P;
	@FXML
	private Button P2;
	@FXML
	private Button Back;

	@FXML
	public void OnActionOpenSub(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) S1.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("Lib_Sub.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}

	@FXML
	public void OnActionOpenBook(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) B1.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("Book.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}

	@FXML
	public void OnActionOpenAuthor(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) A.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("AuthorPage.fxml"));
			Scene scene = new Scene(root, 860, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}

	@FXML
	public void OnActionOpenBorrow(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) B2.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("Borrow.fxml"));
			Scene scene = new Scene(root, 900, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}

	@FXML
	public void OnActionOpenSec(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) S2.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("secPage.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}

	@FXML
	public void OnActionOpenPH(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) P.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("PulishingPage.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}

	@FXML
	public void OnActionOpenP(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) P2.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("Publish_Book.fxml"));
			Scene scene = new Scene(root, 850, 690);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}

	@FXML
	public void OnActionBack(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) Back.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("Page1.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}
}
