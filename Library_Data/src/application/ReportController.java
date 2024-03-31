package application;

 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;

 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.event.ActionEvent;
 import javafx.fxml.FXML;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Scene;
 import javafx.scene.chart.PieChart;
 import javafx.scene.control.Button;
 import javafx.scene.control.Label;
 import javafx.scene.layout.AnchorPane;
 import javafx.stage.Stage;

 public class ReportController {

 	@FXML
 	private Label totalSubscribers;

 	@FXML
 	private Label BNB;

 	@FXML
 	private Label BSN;

 	@FXML
 	private Label totalBook;

 	@FXML
 	private PieChart AgeGoup;

 	@FXML
 	private PieChart Gender;

     @FXML
     private Button B;
     @FXML
     private Button B1;

     


     @FXML
     void Back(ActionEvent event) {
     	try {
 			Stage stage;
 			stage = (Stage) B.getScene().getWindow();
 			stage.close();
 			AnchorPane root = FXMLLoader.load(getClass().getResource("Borrow.fxml"));
 			Scene scene = new Scene(root, 1000, 700);
 			stage.setScene(scene);

 			stage.show();

 		} catch (Exception e1) {

 		}
     }
 	@FXML
 	public void initialize() {

 		try {
 			int num1 = 0;
 			Connector.a.connectDB();
 			PreparedStatement st;
 			st = Connector.a.connectDB().prepareStatement("select sum(number_of_Copy) from Book;");
 			ResultSet r = st.executeQuery();
 			if (r.next()) {
 				num1 = r.getInt(1);
 			}
 			totalBook.setText(num1 + "");

 			int num2 = 0;
 			PreparedStatement st1;
 			st1 = Connector.a.connectDB().prepareStatement("select count(*) from Subscriber");
 			ResultSet r1 = st1.executeQuery();
 			if (r1.next()) {
 				num2 = r1.getInt(1);
 			}
 			totalSubscribers.setText(num2 + "");

 			PreparedStatement st2;
 			int num3 = 0;
 			st2 = Connector.a.connectDB()
 					.prepareStatement("select DISTINCT count(DISTINCT Br.s_id ) from B_Sub_Borrow Br");
 			ResultSet r2 = st2.executeQuery();
 			if (r2.next()) {
 				num3 = r2.getInt(1);
 			}
 			BSN.setText(num3 + "");
 			int num4 = 0;
 			PreparedStatement st3;
 			st3 = Connector.a.connectDB().prepareStatement("select count(Br.B_id ) from B_Sub_Borrow Br");
 			ResultSet r3 = st3.executeQuery();
 			if (r3.next()) {
 				num4 = r3.getInt(1);
 			}
 			BNB.setText(num4 + "");
 			int First = 0;
 			int second = 0;
 			int Third = 0;
 			PreparedStatement st4;
 			st4 = Connector.a.connectDB()
 					.prepareStatement("select count(*) from Subscriber S where S.s_ageGroub = '4-12Y';");
 			ResultSet r4 = st4.executeQuery();
 			if (r4.next()) {
 				First = r4.getInt(1);
 			}
 			PreparedStatement st5;
 			st5 = Connector.a.connectDB()
 					.prepareStatement("select count(*) from Subscriber S where S.s_ageGroub = '13-18Y';");
 			ResultSet r5 = st5.executeQuery();
 			if (r5.next()) {
 				second = r5.getInt(1);
 			}
 			PreparedStatement st6;
 			st6 = Connector.a.connectDB()
 					.prepareStatement("select count(*) from Subscriber S where S.s_ageGroub = '19-orOlder';");
 			ResultSet r6 = st6.executeQuery();
 			if (r6.next()) {
 				Third = r6.getInt(1);
 			}

 			ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
 					new PieChart.Data("4-12Y", First), new PieChart.Data("13-18Y", second),
 					new PieChart.Data("19-orOlder", Third));
 			AgeGoup.setData(pieChartData);

 			int First1 = 0;
 			int second2 = 0;

 			PreparedStatement st7;
 			st7 = Connector.a.connectDB()
 					.prepareStatement("select count(*) from Subscriber S where S.s_gender = 'male';");
 			ResultSet r7 = st7.executeQuery();
 			if (r7.next()) {
 				First1 = r7.getInt(1);
 			}
 			PreparedStatement st8;
 			st8 = Connector.a.connectDB().prepareStatement("select count(*) from Subscriber S where S.s_gender = 'female';");
 			ResultSet r8 = st8.executeQuery();
 			if (r8.next()) {
 				second2 = r8.getInt(1);
 			}

 			ObservableList<PieChart.Data> pieChartData1 = FXCollections.observableArrayList(new PieChart.Data("male", First1), new PieChart.Data("female", second2));
 			Gender.setData(pieChartData1);

 			Connector.a.connectDB().close();
 		} catch (ClassNotFoundException e) {
 			e.printStackTrace();
 		} catch (SQLException e) {
 			e.printStackTrace();
 		}

 	}
 	

 	    @FXML
 	    void annual_report(ActionEvent event) {
 	    	try {
 				Stage stage;
 				stage = (Stage) B1.getScene().getWindow();
 				stage.close();
 				AnchorPane root = FXMLLoader.load(getClass().getResource("query.fxml"));
 				Scene scene = new Scene(root, 1000, 700);
 				stage.setScene(scene);

 				stage.show();

 			} catch (Exception e1) {

 			}
 	    	

 	    }

 }