package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class queryController {
	@FXML
	private Button Back;
	@FXML
	void OnActionBack(ActionEvent event) {
		try {
			Stage stage;
			stage = (Stage) Back.getScene().getWindow();
			stage.close();
			BorderPane root = FXMLLoader.load(getClass().getResource("report.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);

			stage.show();

		} catch (Exception e1) {

		}
	}
	@FXML
	private BarChart<String, Integer> barchart;
	private ObservableList<XYChart.Data<String, Integer>> borrowData = FXCollections.observableArrayList();
	public void initialize() throws ClassNotFoundException, SQLException {
		Connection conn = Connector.a.connectDB();
		ObservableList<XYChart.Data<String, Integer>> borrowData = FXCollections.observableArrayList();
		String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		for (String month : months) {
			borrowData.add(new XYChart.Data<>(month, 0));
		}
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MONTHNAME(startDate) AS month, COUNT(*) AS count FROM B_Sub_Borrow GROUP BY startDate");
			while (rs.next()) {
				String month = rs.getString("month");
				int count = rs.getInt("count");
				for (XYChart.Data<String, Integer> data : borrowData) {
					if (data.getXValue().equals(month)) {
						data.setYValue(count);
					}
				}
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		XYChart.Series<String, Integer> series = new XYChart.Series<>(borrowData);
		series.setName("Monthly Borrows");
		barchart.getData().add(series);
	}}