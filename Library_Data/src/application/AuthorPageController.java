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

public class AuthorPageController {

	@FXML
	private TableColumn<Author, Integer> AId_Col;
	@FXML
	private TableColumn<Author, String> AName_Col;
	@FXML
	private TableView<Author> AuthorTable;
	private ObservableList<Author> authors = FXCollections.observableArrayList();

	public void initialize() throws ClassNotFoundException {
		AId_Col.setCellValueFactory(new PropertyValueFactory<Author, Integer>("id"));
		AName_Col.setCellValueFactory(new PropertyValueFactory<Author, String>("name"));
		loadAuthors();
		AuthorTable.setItems(authors);
	}

	private void loadAuthors() throws ClassNotFoundException {
		authors.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM author");
			while (resultSet.next()) {
				int id = resultSet.getInt("A_id");
				String name = resultSet.getString("A_name");
				authors.add(new Author(id, name));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private TextField IDTextAdd;

	@FXML
	private TextField IDTextDel;

	@FXML
	private TextField IDTextUp;

	@FXML
	private TextField IdtEXTScerch;

	@FXML
	private TextField NameTextAdd;

	@FXML
	private TextField NameTextUp;
	@FXML
	private ImageView B1;

	@FXML
	void Back(MouseEvent event) {
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
	void DeleteAuthor(MouseEvent event) {
		// check if ID text field is empty
		if (IDTextDel.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("ID cannot be empty");
			alert.showAndWait();
		} else {
			try {
				// retrieve the value in the text field and parse it as an integer
				int id = Integer.parseInt(IDTextDel.getText());
				// delete the corresponding author from the database
				deleteAuthorFromDB(id);
				// refresh the table with the updated data
				loadAuthors();
				AuthorTable.refresh();
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

	private void deleteAuthorFromDB(int id) throws ClassNotFoundException {
		try (Connection con = Connector.a.connectDB();
				PreparedStatement pstmt = con.prepareStatement("DELETE FROM author WHERE A_id = ?")) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to delete author");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void Search(MouseEvent event) {
		String query = IdtEXTScerch.getText();
		if (query.trim().isEmpty()) {
			try {
				loadAuthors();
				AuthorTable.refresh();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				int id = Integer.parseInt(query);
				searchAuthors(id);
				AuthorTable.refresh();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}

	private void searchAuthors(int id) throws ClassNotFoundException {
		authors.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			String sql = "SELECT * FROM author WHERE A_id=" + id;
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				int id1 = resultSet.getInt("A_id");
				String name = resultSet.getString("A_name");
				authors.add(new Author(id1, name));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void UpadteAuthor(MouseEvent event) {
		// check if ID text field is empty
		if (IDTextUp.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("ID cannot be empty");
			alert.showAndWait();
		} else {
			try {
				// retrieve the values in the text fields and parse the ID as an integer
				int id = Integer.parseInt(IDTextUp.getText());
				String name = NameTextUp.getText();
				// update the corresponding author in the database
				updateAuthorInDB(id, name);
				// refresh the table with the updated data
				loadAuthors();
				AuthorTable.refresh();
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

	private void updateAuthorInDB(int id, String name) throws ClassNotFoundException {
		try (Connection con = Connector.a.connectDB();
				PreparedStatement pstmt = con.prepareStatement("UPDATE author SET A_name = ? WHERE A_id = ?")) {
			pstmt.setString(1, name);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to update author");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void addAuthor(MouseEvent event) throws Exception {
		if (validateInputsForAdd()) {
			int id = Integer.parseInt(IDTextAdd.getText());
			String name = NameTextAdd.getText();
			insertAuthor(id, name);
			loadAuthors();
			AuthorTable.refresh();
		}
	}

	private boolean validateInputsForAdd() {
		// check if ID is empty
		if (IDTextAdd.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("ID cannot be empty");
			alert.showAndWait();
			return false;
		}
		// check if Name is empty
		if (NameTextAdd.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Name cannot be empty");
			alert.showAndWait();
			return false;
		}
		return true;
	}

	private void insertAuthor(int id, String name) throws ClassNotFoundException {
		try (Connection con = Connector.a.connectDB();
				PreparedStatement pstmt = con.prepareStatement("INSERT INTO author (A_id, A_name) VALUES (?, ?)")) {
			pstmt.setInt(1, id);
			pstmt.setString(2, name);
			pstmt.executeUpdate();
			// add the new author to the authors list
			authors.add(new Author(id, name));
			// refresh the table to display the new data
			loadAuthors();
			AuthorTable.refresh();
		} catch (SQLException e) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to insert author");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
}
