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

public class Publish_BookController {

	@FXML
	private Button Back;

	@FXML
	private TextField Bid;

	@FXML
	private TableColumn<PH_BOOK, Integer> Col_Bid;

	@FXML
	private TableColumn<PH_BOOK, Integer> Col_P_Id;

	@FXML
	private TableColumn<PH_BOOK, String> Col_Year;

	@FXML
	private TextField P_date;

	@FXML
	private TextField Pid;

	@FXML
	private TableView<PH_BOOK> TablePublish;

	private ObservableList<PH_BOOK> List = FXCollections.observableArrayList();
	int index = -1;

	@FXML
	void getSelectCell(MouseEvent event) {
		index = TablePublish.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}
		Bid.setText(Col_Bid.getCellData(index).toString());
		Pid.setText(Col_P_Id.getCellData(index).toString());
		P_date.setText(Col_Year.getCellData(index).toString());

	}

	public void initialize() throws Exception {
		Col_Bid.setCellValueFactory(new PropertyValueFactory<PH_BOOK, Integer>("B_id"));
		Col_P_Id.setCellValueFactory(new PropertyValueFactory<PH_BOOK, Integer>("P_id"));
		Col_Year.setCellValueFactory(new PropertyValueFactory<PH_BOOK, String>("Year_Publication"));
		getData();
		TablePublish.setItems(List);
	}

	private void getData() throws ClassNotFoundException {
		List.clear();
		try {
			Connection con = Connector.a.connectDB();
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM PH_B_publishes order by P_id");
			while (rs.next()) {
				List.add(new PH_BOOK(rs.getInt(1), rs.getInt(2), rs.getString(3)));
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
		// check if Publishing house id is empty
		if (Pid.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Publishing house id cannot be empty");
			alert.showAndWait();
			return false;
		}
		// check if BOOK ID is empty
		if (Bid.getText().trim().isEmpty()) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Book id cannot be empty");
			alert.showAndWait();
			return false;
		}
		return true;
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
	void OnActionDelete(ActionEvent event) {

	}

	@FXML
	void OnActionSearch(ActionEvent event) throws ClassNotFoundException {
		try {
			Connection con = Connector.a.connectDB();
			PreparedStatement stmt;

			if (Pid.getText().trim().isEmpty() && Bid.getText().trim().isEmpty() && P_date.getText().trim().isEmpty()) {
				getData();
				TablePublish.refresh();
			} else if (Pid.getText().trim().isEmpty() && Bid.getText().trim().isEmpty()) {
				String Year_Publication = P_date.getText();
				List.clear();
				stmt = con.prepareStatement("SELECT * FROM PH_B_publishes P where  Year_Publication=?");
				stmt.setString(1, Year_Publication);
				ResultSet result = stmt.executeQuery();

				while (result.next()) {
					List.add(new PH_BOOK(result.getInt("B_id"), result.getInt("P_id"),
							result.getString("Year_Publication")));
				}
				TablePublish.refresh();
			} else if (Pid.getText().trim().isEmpty() && P_date.getText().trim().isEmpty()) {
				int B_id = Integer.parseInt(Bid.getText());
				List.clear();
				stmt = con.prepareStatement("SELECT * FROM PH_B_publishes P where  B_id =?");
				stmt.setInt(1, B_id);
				ResultSet result = stmt.executeQuery();

				while (result.next()) {
					List.add(new PH_BOOK(result.getInt("B_id"), result.getInt("P_id"),
							result.getString("Year_Publication")));
				}
				TablePublish.refresh();
			} else if (Bid.getText().trim().isEmpty() && P_date.getText().trim().isEmpty()) {
				int P_id = Integer.parseInt(Pid.getText());
				List.clear();
				stmt = con.prepareStatement("SELECT * FROM PH_B_publishes P where  P_id =?");
				stmt.setInt(1, P_id);
				ResultSet result = stmt.executeQuery();

				while (result.next()) {
					List.add(new PH_BOOK(result.getInt("B_id"), result.getInt("P_id"),
							result.getString("Year_Publication")));
				}
				TablePublish.refresh();
			} else if (Pid.getText().trim().isEmpty()) {
				int B_id = Integer.parseInt(Bid.getText());
				String Year_Publication = P_date.getText();
				List.clear();
				stmt = con.prepareStatement("SELECT * FROM PH_B_publishes P where  B_id =? and Year_Publication= ?");
				stmt.setInt(1, B_id);
				stmt.setString(2, Year_Publication);
				ResultSet result = stmt.executeQuery();

				while (result.next()) {
					List.add(new PH_BOOK(result.getInt("B_id"), result.getInt("P_id"),
							result.getString("Year_Publication")));
				}
				TablePublish.refresh();
			} else if (Bid.getText().trim().isEmpty()) {
				int P_id = Integer.parseInt(Pid.getText());
				String Year_Publication = P_date.getText();
				List.clear();
				stmt = con.prepareStatement("SELECT * FROM PH_B_publishes P where P_id =? and Year_Publication= ?");
				stmt.setInt(1, P_id);
				stmt.setString(2, Year_Publication);
				ResultSet result = stmt.executeQuery();

				while (result.next()) {
					List.add(new PH_BOOK(result.getInt("B_id"), result.getInt("P_id"),
							result.getString("Year_Publication")));
				}
				TablePublish.refresh();
			} else if (P_date.getText().trim().isEmpty()) {
				int P_id = Integer.parseInt(Pid.getText());
				int B_id = Integer.parseInt(Bid.getText());
				List.clear();
				stmt = con.prepareStatement("SELECT * FROM PH_B_publishes P where P_id =? and B_id= ?");
				stmt.setInt(1, P_id);
				stmt.setInt(2, B_id);
				ResultSet result = stmt.executeQuery();

				while (result.next()) {
					List.add(new PH_BOOK(result.getInt("B_id"), result.getInt("P_id"),
							result.getString("Year_Publication")));
				}
				TablePublish.refresh();
			} else {
				int P_id = Integer.parseInt(Pid.getText());
				String Year_Publication = P_date.getText();
				int B_id = Integer.parseInt(Bid.getText());
				List.clear();
				stmt = con.prepareStatement(
						"SELECT * FROM PH_B_publishes P where P_id =? and Year_Publication= ? and B_id= ? ");
				stmt.setInt(1, P_id);
				stmt.setString(2, Year_Publication);
				stmt.setInt(3, B_id);
				ResultSet result = stmt.executeQuery();

				while (result.next()) {
					List.add(new PH_BOOK(result.getInt("B_id"), result.getInt("P_id"),
							result.getString("Year_Publication")));
				}
				TablePublish.refresh();
			}

		} catch (SQLException e) {
			// Handle exception here, such as by displaying an error message to the user
			e.printStackTrace();
		}
	}

	@FXML
	void OnActionUpdate(ActionEvent event) {
		try {
			Connection con = Connector.a.connectDB();
			String v1 = Bid.getText();
			String v2 = Pid.getText();
			String v3 = P_date.getText();
			String sql = "update PH_B_publishes set Year_Publication='" + v3 + "'where B_id='" + v1 + "'and P_id='" + v2
					+ "'";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.execute();
			getData();
			TablePublish.refresh();
			Bid.clear();
			Pid.clear();
			P_date.clear();
		} catch (SQLException e) {
			// display an error message
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to update PH_BOOK");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void OnAction_Insert(ActionEvent event) throws ClassNotFoundException {

		if (validateInputsForAdd()) {
			PH_BOOK P = new PH_BOOK(Integer.parseInt(Bid.getText()), Integer.parseInt(Pid.getText()), P_date.getText());
			insertData(P);
			getData();
			TablePublish.refresh();
			Bid.clear();
			Pid.clear();
			P_date.clear();
		}
	}

	private void insertData(PH_BOOK P) {
		try {
			String sql = "insert into PH_B_publishes(B_id,P_id,Year_Publication ) values(?,?,?)";
			PreparedStatement ps = (PreparedStatement) Connector.a.connectDB().prepareStatement(sql);
			ps.setInt(1, P.getB_id());
			ps.setInt(2, P.getP_id());
			ps.setString(3, P.getYear_Publication());
			ps.execute();
			List.add(P);
			getData();
			TablePublish.refresh();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to insert into PH_Book");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}

}