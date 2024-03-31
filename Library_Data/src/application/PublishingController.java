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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PublishingController {

	@FXML
	private TextField Address;
	@FXML
	private Button Back;
	@FXML
	private TextField Email;

	@FXML
	private TextField Id;

	@FXML
	private TextField Name;

	@FXML
	private TableColumn<Publishing_house, String> colAddress;

	@FXML
	private TableColumn<Publishing_house, String> colEmail;

	@FXML
	private TableColumn<Publishing_house, Integer> colID;

	@FXML
	private TableColumn<Publishing_house, String> colName;

	@FXML
	private TableView<Publishing_house> tablePublishing;

	private ObservableList<Publishing_house> List = FXCollections.observableArrayList();
	int index = -1;

	@FXML
	void getSelectCell(MouseEvent event) {
		index = tablePublishing.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}
		Id.setText(colID.getCellData(index).toString());
		Name.setText(colName.getCellData(index).toString());
		Address.setText(colAddress.getCellData(index).toString());
		Email.setText(colEmail.getCellData(index).toString());
	}

	public void initialize() throws Exception {
		colID.setCellValueFactory(new PropertyValueFactory<Publishing_house, Integer>("ID"));
		colName.setCellValueFactory(new PropertyValueFactory<Publishing_house, String>("Name"));
		colAddress.setCellValueFactory(new PropertyValueFactory<Publishing_house, String>("Address"));
		colEmail.setCellValueFactory(new PropertyValueFactory<Publishing_house, String>("Email"));
		getData();
		tablePublishing.setItems(List);

	}

	private void getData() throws ClassNotFoundException {
		List.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Publishing_house");
			while (rs.next()) {
				List.add(new Publishing_house(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
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

	private void deletePubFromDB(int id) throws ClassNotFoundException {
		try (Connection con = Connector.a.connectDB();
				PreparedStatement ps = con.prepareStatement("DELETE FROM Publishing_house WHERE P_id = ?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to delete Publishing house");
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
				deletePubFromDB(id);
				getData();
				tablePublishing.refresh();
				Id.clear();
				Name.clear();
				Email.clear();
				Address.clear();
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
		if (Name.getText().trim().isEmpty()) {
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
	void OnActionInsert(ActionEvent event) throws ClassNotFoundException {
		if (validateInputsForAdd()) {
			Publishing_house p = new Publishing_house(Integer.parseInt(Id.getText()), Name.getText(), Address.getText(),
					Email.getText());
			// Publishing_house.PH = p;
			insertData(p);
			getData();
			tablePublishing.refresh();
			Id.clear();
			Name.clear();
			Email.clear();
			Address.clear();
		}
	}

	private void insertData(Publishing_house s) {
		try {
			String sql = " insert into Publishing_house ( P_id,P_name, P_address , P_email  )values(?,?,?,?)";
			PreparedStatement ps = (PreparedStatement) Connector.a.connectDB().prepareStatement(sql);
			ps.setInt(1, s.getID());
			ps.setString(2, s.getName());
			ps.setString(3, s.getAddress());
			ps.setString(4, s.getEmail());
			ps.execute();
			List.add(s);
			getData();
			tablePublishing.refresh();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to insert Publishing_house");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	void OnActionSearch(ActionEvent event) {
		if (Id.getText().trim().isEmpty() && Name.getText().trim().isEmpty()) {
			try {
				getData();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tablePublishing.refresh();
		} else if (Id.getText().trim().isEmpty()) {
			try {
				String name = Name.getText();
				searchPub(name);
				tablePublishing.refresh();
				Name.clear();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else if (Name.getText().trim().isEmpty()) {
			try {
				int id = Integer.parseInt(Id.getText());
				searchPub(id);
				tablePublishing.refresh();
				Id.clear();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			int id = Integer.parseInt(Id.getText());
			String name = Name.getText();
			try {
				searchPub(id, name);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tablePublishing.refresh();
			Id.clear();
			Name.clear();
		}
	}

	private void searchPub(int id) throws ClassNotFoundException {
		List.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			String sql = "SELECT * FROM Publishing_house WHERE P_id=" + id;
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				List.add(new Publishing_house(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void searchPub(String name) throws ClassNotFoundException {
		List.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			String sql = "SELECT * FROM Publishing_house where P_name='" + name + "'";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				List.add(new Publishing_house(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void searchPub(int id, String name) throws ClassNotFoundException {
		List.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			String sql = "SELECT * FROM Publishing_house B where s.name='" + name + "'and s_id=" + id;
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				List.add(new Publishing_house(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void OnActionUpdate(ActionEvent event) {
		try {
			Connection con = Connector.a.connectDB();
			String v1 = Id.getText();
			String v2 = Name.getText();
			String v3 = Address.getText();
			String v4 = Email.getText();
			String sql = "update  Publishing_house set P_id='" + v1 + "',P_name='" + v2 + "',P_address='" + v3
					+ "', P_email='" + v4 + "'where P_id='" + v1 + "'";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.execute();
			getData();
			Id.clear();
			Name.clear();
			Email.clear();
			Address.clear();
			tablePublishing.refresh();
		} catch (SQLException e) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to update Publishing House");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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

}
