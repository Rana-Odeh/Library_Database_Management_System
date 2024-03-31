package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {

		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(Main.class.getResource("MainL.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
			primaryStage.setTitle("Library System ");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		launch(args);
	}
}
