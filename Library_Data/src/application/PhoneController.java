package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class PhoneController {
	@FXML
	private TableView<Phone> TablePhone;
	@FXML
	private TableColumn<Phone, Integer> ID;
	@FXML
	private TableColumn<Phone, Integer> phone;
	@FXML
	private Button Back;
	@FXML
	private TextField SID;
	@FXML
	private TextField phoneN;

	private ObservableList<Phone> List = FXCollections.observableArrayList();

	public void initialize() throws Exception {
		ID.setCellValueFactory(new PropertyValueFactory<Phone, Integer>("S_id"));
		phone.setCellValueFactory(new PropertyValueFactory<Phone, Integer>("phone"));
		getData();
		TablePhone.setItems(List);
	}

	private void getData() throws ClassNotFoundException {
		List.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Sub_Phone order by s_id");
			while (rs.next()) {
				List.add(new Phone(rs.getInt(1), rs.getInt(2)));
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
		if (SID.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("ID cannot be empty");
			alert.showAndWait();
			return false;
		}
// check if phone is empty
		if (phoneN.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Phone cannot be empty");
			alert.showAndWait();
			return false;
		}
		return true;
	}

	@FXML
	void Insert(ActionEvent event) {

		if (validateInputsForAdd()) {
			Phone s = new Phone(Integer.parseInt(SID.getText()), Integer.parseInt(phoneN.getText()));
			insertData(s);
			try {
				getData();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			TablePhone.refresh();
			SID.clear();
			phoneN.clear();
		}
	}

	private void insertData(Phone s) {
		try {
			String sql = "insert into Sub_Phone(s_id,Phone ) values(?,?)";
			PreparedStatement ps = (PreparedStatement) Connector.a.connectDB().prepareStatement(sql);
			ps.setInt(1, s.getS_id());
			ps.setInt(2, s.getPhone());

			ps.execute();
			List.add(s);
			getData();
			TablePhone.refresh();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to insert Phone");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void back(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) Back.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("Lib_Sub.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}

}
