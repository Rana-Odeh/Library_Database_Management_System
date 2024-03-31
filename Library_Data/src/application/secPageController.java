package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class secPageController {

	@FXML
	private ImageView B1;
	@FXML
	private TextField IddelText;

	@FXML
	private TextField NameAddText;

	@FXML
	private TableView<section> SectionTabel;

	@FXML
	private TextField idAddText;

	@FXML
	private TableColumn<section, Integer> sceId;
	@FXML
	private TextField searchId;

	@FXML
	private TextField searchName;

	@FXML
	private TableColumn<section, String> secName;

	@FXML
	private TextField updateTextId;

	@FXML
	private TextField updateTextName;
	private ObservableList<section> sec = FXCollections.observableArrayList();

	public void initialize() throws ClassNotFoundException {
		sceId.setCellValueFactory(new PropertyValueFactory<section, Integer>("id"));
		secName.setCellValueFactory(new PropertyValueFactory<section, String>("name"));
		loadSection();
		SectionTabel.setItems(sec);
	}

	private void loadSection() throws ClassNotFoundException {
		sec.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Section");
			while (resultSet.next()) {
				int id = resultSet.getInt("Sec_id");
				String name = resultSet.getString("Sec_name");
				sec.add(new section(id, name));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void DeleteSec(MouseEvent event) {
		// check if ID text field is empty
		if (IddelText.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("ID cannot be empty");
			alert.showAndWait();
		} else {
			try {
				// retrieve the value in the text field and parse it as an integer
				int id = Integer.parseInt(IddelText.getText());
				deleteSecFromDB(id);
				// refresh the table with the updated data
				loadSection();
				SectionTabel.refresh();
			} catch (NumberFormatException e) {
				// display an error message
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid input");
				alert.setContentText("ID must be a number");
				alert.showAndWait();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void deleteSecFromDB(int id) throws ClassNotFoundException {
		try (Connection con = Connector.a.connectDB();
				PreparedStatement selectStmt = con.prepareStatement("SELECT Sec_id FROM Section WHERE Sec_id = ?");
				PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM Section WHERE Sec_id = ?")) {
			selectStmt.setInt(1, id);
			ResultSet resultSet = selectStmt.executeQuery();
			if (resultSet.next()) {
				deleteStmt.setInt(1, id);
				deleteStmt.executeUpdate();
			} else {
				// display an error message
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid ID");
				alert.setContentText("ID not found in the database");
				alert.showAndWait();
			}
		} catch (SQLException e) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to delete section");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void SearchSec(MouseEvent event) {
		try {
			sec.clear();
			if (!searchId.getText().isEmpty()) {
				int id = Integer.parseInt(searchId.getText());
				Connection con = Connector.a.connectDB();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM Section WHERE Sec_id = ?");
				statement.setInt(1, id);
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					int sId = resultSet.getInt("Sec_id");
					String name = resultSet.getString("Sec_name");
					sec.add(new section(sId, name));
				}
				con.close();
				SectionTabel.setItems(sec);
			} else if (!searchName.getText().isEmpty()) {
				Connection con = Connector.a.connectDB();
				PreparedStatement statement = con.prepareStatement("SELECT * FROM Section WHERE Sec_name = ?");
				statement.setString(1, searchName.getText());
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					int sId = resultSet.getInt("Sec_id");
					String name = resultSet.getString("Sec_name");
					sec.add(new section(sId, name));
				}
				con.close();
				SectionTabel.setItems(sec);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid input");
			alert.setContentText("ID must be a number");
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void addSec(MouseEvent event) throws ClassNotFoundException {
		if (validateInputsForAdd()) {
			int id = Integer.parseInt(idAddText.getText());
			String name = NameAddText.getText();
			insertSec(id, name);
			loadSection();
			SectionTabel.refresh();
		}
	}

	private boolean validateInputsForAdd() {
		// check if ID is empty
		if (idAddText.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("ID cannot be empty");
			alert.showAndWait();
			return false;
		}
		// check if Name is empty
		if (NameAddText.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Name cannot be empty");
			alert.showAndWait();
			return false;
		}
		return true;
	}

	private void insertSec(int id, String name) throws ClassNotFoundException {
		try (Connection con = Connector.a.connectDB();
				PreparedStatement pstmt = con
						.prepareStatement("INSERT INTO Section (Sec_id, Sec_name) VALUES (?, ?)")) {
			pstmt.setInt(1, id);
			pstmt.setString(2, name);
			pstmt.executeUpdate();
			// add the new author to the authors list
			sec.add(new section(id, name));
			// refresh the table to display the new data
			loadSection();
			SectionTabel.refresh();
		} catch (SQLException e) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to insert section");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void back(MouseEvent event) {
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

	}

	@FXML
	void updateSce(MouseEvent event) {
		if (updateTextId.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("ID cannot be empty");
			alert.showAndWait();
		} else {
			try {
				// retrieve the values in the text fields and parse the ID as an integer
				int id = Integer.parseInt(updateTextId.getText());
				String name = updateTextName.getText();
				// update the corresponding author in the database
				updateSecInDB(id, name);
				// refresh the table with the updated data
				loadSection();
				SectionTabel.refresh();
			} catch (NumberFormatException e) {
				// display an error message
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid input");
				alert.setContentText("ID must be a number");
				alert.showAndWait();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateSecInDB(int id, String name) throws ClassNotFoundException {
		try (Connection con = Connector.a.connectDB();
				PreparedStatement pstmt = con.prepareStatement("SELECT * FROM Section WHERE Sec_id = ?")) {
			pstmt.setInt(1, id);
			ResultSet resultSet = pstmt.executeQuery();
			if (!resultSet.next()) {
				// ID not found in database
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid ID");
				alert.setContentText("The input ID is not in the database.");
				alert.showAndWait();
				return;
			}
			// ID found in database, continue with update statement
			try (PreparedStatement pstmt2 = con.prepareStatement("UPDATE Section SET Sec_name = ? WHERE Sec_id = ?")) {
				pstmt2.setString(1, name);
				pstmt2.setInt(2, id);
				pstmt2.executeUpdate();
			} catch (SQLException e) {
				// display an error message
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Failed to update Section");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}