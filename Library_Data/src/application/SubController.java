package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SubController {

	@FXML
	private ChoiceBox<String> Age_Group;

	@FXML
	private TableColumn<Subscriber, String> Col_Address;

	@FXML
	private TableColumn<Subscriber, String> Col_Age_Group;

	@FXML
	private TableColumn<Subscriber, String> Col_Date_Birth;

	@FXML
	private TableColumn<Subscriber, String> Col_Email;

	@FXML
	private TableColumn<Subscriber, String> Col_Gender;

	@FXML
	private TableColumn<Subscriber, String> Col_Name;

	@FXML
	private TableColumn<Subscriber, Integer> Col_id;

	@FXML
	private TextField Data_Birth;

	@FXML
	private ChoiceBox<String> Gender;
	@FXML
	private Button Back;
	@FXML
	private Button S_Phone;

	@FXML
	private TextField Id;

	@FXML
	private TextField S_Address;

	@FXML
	private TextField S_Email;

	@FXML
	private TextField S_Name;

	@FXML
	private TableView<Subscriber> TableSub;

	@FXML
	void OnActionPhone(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) S_Phone.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("Phone.fxml"));
			Scene scene = new Scene(root, 850, 650);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {

		}
	}

	@FXML
	void ADD(MouseEvent event) {
		ObservableList<String> B_Age = FXCollections.observableArrayList("4-12Y", "13_18Y", "19_orOlder");
		Age_Group.setItems(B_Age);
	}

	@FXML
	void ADDG(MouseEvent event) {
		ObservableList<String> B_G = FXCollections.observableArrayList("Male", "Female");
		Gender.setItems(B_G);
	}

	private ObservableList<Subscriber> List = FXCollections.observableArrayList();
	int index = -1;

	@FXML
	void getSelectCell(MouseEvent event) {
		index = TableSub.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}
		Id.setText(Col_id.getCellData(index).toString());
		S_Name.setText(Col_Name.getCellData(index).toString());
		Data_Birth.setText(Col_Date_Birth.getCellData(index).toString());
		S_Email.setText(Col_Email.getCellData(index).toString());
		S_Address.setText(Col_Address.getCellData(index).toString());
	}

	public void initialize() throws Exception {
		Col_id.setCellValueFactory(new PropertyValueFactory<Subscriber, Integer>("S_ID"));
		Col_Name.setCellValueFactory(new PropertyValueFactory<Subscriber, String>("S_Name"));
		Col_Date_Birth.setCellValueFactory(new PropertyValueFactory<Subscriber, String>("Date_Birth"));
		Col_Age_Group.setCellValueFactory(new PropertyValueFactory<Subscriber, String>("Age_Group"));
		Col_Email.setCellValueFactory(new PropertyValueFactory<Subscriber, String>("Email"));
		Col_Address.setCellValueFactory(new PropertyValueFactory<Subscriber, String>("Address"));
		Col_Gender.setCellValueFactory(new PropertyValueFactory<Subscriber, String>("Gender"));
		getData();
		TableSub.setItems(List);

	}

	private void getData() throws ClassNotFoundException {
		List.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Subscriber order by s_id");
			while (rs.next()) {
				List.add(new Subscriber(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7)));
			}

			con.close();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean validateInputsForAdd() {
// check if ID is empty
		if (Id.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("ID cannot be empty");
			alert.showAndWait();
			return false;
		}
// check if Name is empty
		if (S_Name.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Name cannot be empty");
			alert.showAndWait();
			return false;
		}
		return true;
	}

	@FXML
	void OnAction_Insert(ActionEvent event) throws ClassNotFoundException {
		if (validateInputsForAdd()) {
			Subscriber s = new Subscriber(Integer.parseInt(Id.getText()), S_Name.getText(), Data_Birth.getText(),
					Age_Group.getValue().toString(), S_Email.getText(), S_Address.getText(),
					Gender.getValue().toString());
			Subscriber.sub = s;
			insertData(s);
			getData();
			TableSub.refresh();
			Id.clear();
			S_Name.clear();
			Data_Birth.clear();
			S_Email.clear();
			S_Address.clear();
		}
	}

	private void insertData(Subscriber s) {
		try {
			String sql = "insert into Subscriber(s_id, s_name , dataOfBirth, s_ageGroub , s_email ,s_address ,s_gender ) values(?,?,?,?,?,?,?)";
			PreparedStatement ps = (PreparedStatement) Connector.a.connectDB().prepareStatement(sql);
			ps.setInt(1, s.getS_ID());
			ps.setString(2, s.getS_Name());
			ps.setString(3, s.getDate_Birth());
			ps.setString(4, s.getAge_Group());
			ps.setString(5, s.getEmail());
			ps.setString(6, s.getAddress());
			ps.setString(7, s.getGender());
			ps.execute();
			List.add(s);
			getData();
			TableSub.refresh();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to insert Subscriber");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	private void deleteSubFromDB(int id) throws ClassNotFoundException {
		try (Connection con = Connector.a.connectDB();
				PreparedStatement ps = con.prepareStatement("DELETE FROM Subscriber WHERE s_id = ?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to delete Subscriber");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void OnActionDelete(ActionEvent event) {
		if (Id.getText().trim().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("ID cannot be empty");
			alert.showAndWait();
		} else {
			try {
				int id = Integer.parseInt(Id.getText());
				deleteSubFromDB(id);
				getData();
				TableSub.refresh();
				Id.clear();
				S_Name.clear();
				Data_Birth.clear();
				S_Email.clear();
				S_Address.clear();
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

	@FXML
	void OnActionSearch(ActionEvent event) {
		if (Id.getText().trim().isEmpty() && S_Name.getText().trim().isEmpty()) {
			try {
				getData();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TableSub.refresh();
		} else if (Id.getText().trim().isEmpty()) {
			try {
				String name = S_Name.getText();
				searchSub(name);
				TableSub.refresh();
				S_Name.clear();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else if (S_Name.getText().trim().isEmpty()) {
			try {
				int id = Integer.parseInt(Id.getText());
				searchSub(id);
				TableSub.refresh();
				Id.clear();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			int id = Integer.parseInt(Id.getText());
			String name = S_Name.getText();
			try {
				searchSub(id, name);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TableSub.refresh();
			Id.clear();
			S_Name.clear();
		}
	}

	private void searchSub(int id) throws ClassNotFoundException {
		List.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			String sql = "SELECT * FROM Subscriber WHERE s_id=" + id;
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				List.add(new Subscriber(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7)));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void searchSub(String name) throws ClassNotFoundException {
		List.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			String sql = "SELECT * FROM Subscriber where s_name='" + name + "'";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				List.add(new Subscriber(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7)));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void searchSub(int id, String name) throws ClassNotFoundException {
		List.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			String sql = "SELECT * FROM Subscriber B where s.name='" + name + "'and s_id=" + id;
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				List.add(new Subscriber(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7)));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void OnActionBack(ActionEvent event) {
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
	void OnActionUpdate(ActionEvent event) {
		try {
			Connection con = Connector.a.connectDB();
			String v1 = Id.getText();
			String v2 = S_Name.getText();
			String v3 = Data_Birth.getText();
			String v4 = Age_Group.getValue();
			String v5 = S_Email.getText();
			String v6 = S_Address.getText();
			String v7 = Gender.getValue();
			String sql = "update  Subscriber set s_id='" + v1 + "',s_name='" + v2 + "', dataOfBirth='" + v3
					+ "', s_ageGroub='" + v4 + "', s_email='" + v5 + "',s_address='" + v6 + "',s_gender='" + v7
					+ "'where s_id='" + v1 + "'";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.execute();
			getData();
			TableSub.refresh();
			Id.clear();
			S_Name.clear();
			Data_Birth.clear();
			S_Email.clear();
			S_Address.clear();
		} catch (SQLException e) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to update Subscriber");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
