package application;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;

public class BookController {
	@FXML
	private TextField T_B_id;
	@FXML
	private TextField T_B_name;
	@FXML
	private TextField T_B_description;
	@FXML
	private TextField T_number_of_pages;
	@FXML
	private TextField T_year_of_issue;
	@FXML
	private TextField T_Sec_id;
	@FXML
	private TextField TAid1;
	@FXML
	private TextField T_number_of_Copy;
	@FXML
	private Button Back;
	@FXML
	private TableView<Book> table_Book;

	@FXML
	private TableColumn<Book, Integer> B_id;

	@FXML
	private TableColumn<Book, String> B_name;

	@FXML
	private TableColumn<Book, String> B_description;

	@FXML
	private TableColumn<Book, Integer> number_of_pages;

	@FXML
	private TableColumn<Book, String> year_of_issue;

	@FXML
	private TableColumn<Book, Integer> Sec_id;

	@FXML
	private TableColumn<Book, Integer> Aid1;
	@FXML
	private TableColumn<Book, Integer> number_of_Copy;
	@FXML
	private Button Delete;
	@FXML
	private Button Add;
	@FXML
	private Button Search;
	@FXML
	private Button Update;

	private ObservableList<Book> Book1 = FXCollections.observableArrayList();
	int index = -1;

	@FXML
	void getSelectCell(MouseEvent event) {
		index = table_Book.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}
		T_B_id.setText(B_id.getCellData(index).toString());
		T_B_name.setText(B_name.getCellData(index).toString());
		T_B_description.setText(B_description.getCellData(index).toString());
		T_number_of_pages.setText(number_of_pages.getCellData(index).toString());
		T_year_of_issue.setText(year_of_issue.getCellData(index).toString());
		T_Sec_id.setText(Sec_id.getCellData(index).toString());
		TAid1.setText(Aid1.getCellData(index).toString());
		T_number_of_Copy.setText(number_of_Copy.getCellData(index).toString());
	}

	// private ObservableList<Book> Book1 = FXCollections.observableArrayList();

	public void initialize() throws ClassNotFoundException {
		B_id.setCellValueFactory(new PropertyValueFactory<Book, Integer>("B_id"));
		B_name.setCellValueFactory(new PropertyValueFactory<Book, String>("B_name"));
		B_description.setCellValueFactory(new PropertyValueFactory<Book, String>("B_description"));
		number_of_pages.setCellValueFactory(new PropertyValueFactory<Book, Integer>("number_of_pages"));
		year_of_issue.setCellValueFactory(new PropertyValueFactory<Book, String>("year_of_issue"));
		Sec_id.setCellValueFactory(new PropertyValueFactory<Book, Integer>("Sec_id"));
		Aid1.setCellValueFactory(new PropertyValueFactory<Book, Integer>("A_id"));
		number_of_Copy.setCellValueFactory(new PropertyValueFactory<Book, Integer>("number_of_Copy"));
		loadBooks();
		table_Book.setItems(Book1);
	}

	private void loadBooks() throws ClassNotFoundException {
		Book1.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM BooK");
			while (result.next()) {
				Book1.add(new Book(result.getInt("B_id"), result.getString("Bname"), result.getString("Bdescription"),
						result.getInt("number_of_pages"), result.getString("year_of_issue"), result.getInt("Sec_id"),
						result.getInt("A_id"), result.getInt("number_of_Copy")));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void Back(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) Back.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("Bage2.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}

	@FXML
	public void deleteBook(ActionEvent event) throws ClassNotFoundException {

		if (T_B_id.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("error");
			alert.setHeaderText("ID cannot be empty");
			alert.showAndWait();
		} else {
			try {
				int id = Integer.parseInt(T_B_id.getText());
				Book1.clear();
				Connection con = Connector.a.connectDB();
				PreparedStatement pstmt = con.prepareStatement("DELETE FROM BooK B WHERE  B.B_id = ?");
				pstmt.setInt(1, id);
				pstmt.executeUpdate();

				con.close();
			} catch (SQLException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("error");
				alert.setHeaderText("failed to delete Book");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
			}
			loadBooks();
			table_Book.refresh();

		}

	}

	// Event Listener on Button[#Add].onAction
	@FXML
	public void addbook(ActionEvent event) {
		try {

			Book B;
			B = new Book(Integer.parseInt(T_B_id.getText()), T_B_name.getText(), T_B_description.getText(),
					Integer.parseInt(T_number_of_pages.getText()), T_year_of_issue.getText(),
					Integer.parseInt(T_Sec_id.getText()), Integer.parseInt(TAid1.getText()),
					Integer.parseInt(T_number_of_Copy.getText()));
			Book1.add(B);
			insertData(B);
			table_Book.refresh();

			T_B_id.clear();
			T_B_name.clear();
			T_B_description.clear();
			T_number_of_pages.clear();
			T_year_of_issue.clear();
			T_Sec_id.clear();
			TAid1.clear();
			T_number_of_Copy.clear();

		} catch (Exception e) {

		}
	}

	private void insertData(Book b) throws ClassNotFoundException {
		try (Connection con = Connector.a.connectDB();
				PreparedStatement p = con.prepareStatement("insert into  BooK values (?,?,?,?,?,?,?,?)")) {
			p.setInt(1, b.getB_id());
			p.setString(2, b.getB_name());
			p.setString(3, b.getB_description());
			p.setInt(4, b.getNumber_of_pages());
			p.setString(5, b.getYear_of_issue());
			p.setInt(6, b.getSec_id());
			p.setInt(7, b.getA_id());
			p.setInt(8, b.getNumber_of_Copy());
			p.executeUpdate();
		} catch (SQLException e) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to insert Book");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	public void Search(ActionEvent event) throws ClassNotFoundException {
		try {
			Connection con = Connector.a.connectDB();
			PreparedStatement stmt;

			if (T_B_id.getText().trim().isEmpty() && T_B_name.getText().trim().isEmpty()) {
				loadBooks();
				table_Book.refresh();
			} else if (T_B_id.getText().trim().isEmpty()) {
				String name = T_B_name.getText();
				Book1.clear();
				stmt = con.prepareStatement("SELECT * FROM BooK B where B.Bname=?");
				stmt.setString(1, name);
				ResultSet result = stmt.executeQuery();

				while (result.next()) {
					Book1.add(
							new Book(result.getInt("B_id"), result.getString("Bname"), result.getString("Bdescription"),
									result.getInt("number_of_pages"), result.getString("year_of_issue"),
									result.getInt("Sec_id"), result.getInt("A_id"), result.getInt("number_of_Copy")));
				}
				table_Book.refresh();
			} else if (T_B_name.getText().trim().isEmpty()) {
				int id = Integer.parseInt(T_B_id.getText());
				Book1.clear();
				stmt = con.prepareStatement("SELECT * FROM BooK B where B.B_id=?");
				stmt.setInt(1, id);
				ResultSet result = stmt.executeQuery();

				while (result.next()) {
					Book1.add(
							new Book(result.getInt("B_id"), result.getString("Bname"), result.getString("Bdescription"),
									result.getInt("number_of_pages"), result.getString("year_of_issue"),
									result.getInt("Sec_id"), result.getInt("A_id"), result.getInt("number_of_Copy")));
				}
				table_Book.refresh();
			} else {
				String name = T_B_name.getText();
				int id = Integer.parseInt(T_B_id.getText());
				Book1.clear();
				stmt = con.prepareStatement("SELECT * FROM BooK B where B.Bname =? and B.B_id= ?");
				stmt.setString(1, name);
				stmt.setInt(2, id);
				ResultSet result = stmt.executeQuery();

				while (result.next()) {
					Book1.add(
							new Book(result.getInt("B_id"), result.getString("Bname"), result.getString("Bdescription"),
									result.getInt("number_of_pages"), result.getString("year_of_issue"),
									result.getInt("Sec_id"), result.getInt("A_id"), result.getInt("number_of_Copy")));
				}
				table_Book.refresh();
			}
			con.close();
		} catch (SQLException e) {
			// Handle exception here, such as by displaying an error message to the user
			e.printStackTrace();
		}
	}

	// Event Listener on Button[#Update].onAction

	@FXML
	void UpdateData(ActionEvent event) throws ClassNotFoundException {
		try (Connection con = Connector.a.connectDB()) {
			String id = T_B_id.getText();
			String name = T_B_name.getText();
			String description = T_B_description.getText();
			String number_of_pages = T_number_of_pages.getText();
			String year_of_issue = T_year_of_issue.getText();
			String Sec_id = T_Sec_id.getText();
			String A_id = TAid1.getText();
			String number_of_Copy = T_number_of_Copy.getText();

			String sql = "update BooK set Bname = ?, Bdescription= ?,number_of_pages = ?, year_of_issue = ?,Sec_id = ?, A_id = ?, number_of_Copy = ? where B_id = ? ";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(8, id);
			st.setString(1, name);
			st.setString(2, description);
			st.setString(3, number_of_pages);
			st.setString(4, year_of_issue);
			st.setString(5, Sec_id);
			st.setString(6, A_id);
			st.setString(7, number_of_Copy);
			st.executeUpdate();
			loadBooks();
			table_Book.refresh();
			T_B_id.clear();
			T_B_name.clear();
			T_B_description.clear();
			T_number_of_pages.clear();
			T_year_of_issue.clear();
			T_Sec_id.clear();
			TAid1.clear();
			T_number_of_Copy.clear();
		} catch (SQLException e) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to update Books");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}