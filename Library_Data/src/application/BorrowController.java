package application;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

public class BorrowController {
	   @FXML
	    private ImageView B;
	@FXML
    void OnActionReport(ActionEvent event) {
		 try {
				Stage stage;
				stage = (Stage)Report.getScene().getWindow();
				stage.close();
				BorderPane root = FXMLLoader.load(getClass().getResource("report.fxml"));
				Scene scene = new Scene(root, 1000, 700);
				stage.setScene(scene);
				stage.show();
			} catch (Exception e1) {

			}
	    }

	 @FXML
	    private Button Report;

	@FXML
	private TextField Bid;
	@FXML
	private TextField Sid;
	@FXML
	private TableView<Borrow> TableBorrow;
	@FXML
	private TableColumn<Borrow,Integer> Col_Bid;
	@FXML
	private TableColumn<Borrow,Integer> Col_Sid;
	@FXML
	private TableColumn <Borrow,Date>Col_Sdate;
	@FXML
	private TableColumn<Borrow,Date> Col_Edate;
	 private ObservableList<Borrow> bow = FXCollections.observableArrayList();
	    public void initialize() throws ClassNotFoundException {
	    	Col_Bid.setCellValueFactory(new PropertyValueFactory<Borrow,Integer>("bid"));
	    	Col_Sid.setCellValueFactory(new PropertyValueFactory<Borrow,Integer>("sid"));
	    	Col_Sdate.setCellValueFactory(new PropertyValueFactory<Borrow, Date>("startDate"));
	 	    Col_Edate.setCellValueFactory(new PropertyValueFactory<Borrow, Date>("endDate"));

	        load();
	        TableBorrow.setItems(bow);
	    }
	    private void load() throws ClassNotFoundException {
	        bow.clear();
	        try {
	            Connection con = Connector.a.connectDB();
	            Statement statement = con.createStatement();
	            ResultSet resultSet = statement.executeQuery("SELECT * FROM B_Sub_Borrow");
	            while (resultSet.next()) {
	                int Bookid = resultSet.getInt("B_id");
	                int Sid = resultSet.getInt("s_id");
	                int num=resultSet.getInt("numOfBook");
                     Date startDate = resultSet.getDate("startDate");
                     Date endDate = resultSet.getDate("endDate");
	                bow.add(new Borrow(Bookid, Sid,startDate,endDate,num));
	            }
	            con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    private boolean canSubscribeBorrowMoreBooks(int subscriberID) {
			// Check if the subscriber has already borrowed 6 or more books
			int count = 0;
			for (Borrow b : bow) {
			if (b.getSid() == subscriberID) {
			count += b.getBorrowedBooks();
			}
			}
			return count < 6;
			}
		private boolean isBookIDValid(int bookID) throws ClassNotFoundException {
		      try {
		         Connection con = Connector.a.connectDB();
		         Statement statement = con.createStatement();
		         ResultSet resultSet = statement.executeQuery("SELECT * FROM book WHERE B_id = " + bookID);
		         boolean isValid = resultSet.next();
		         con.close();
		         return isValid;
		      } catch (SQLException e) {
		         e.printStackTrace();
		         return false;
		      }
		   }

		   private boolean isSubscriberIDValid(int subscriberID) throws ClassNotFoundException {
		      try {
		         Connection con = Connector.a.connectDB();
		         Statement statement = con.createStatement();
		         ResultSet resultSet = statement.executeQuery("SELECT * FROM subscriber WHERE s_id = " + subscriberID);
		         boolean isValid = resultSet.next();
		         con.close();
		         return isValid;
		      } catch (SQLException e) {
		         e.printStackTrace();
		         return false;
		      }
		   }
	
	@FXML
	public void insert(MouseEvent event) throws ClassNotFoundException {
	    int bookID = Integer.parseInt(Bid.getText());
	    int subscriberID = Integer.parseInt(Sid.getText());
	    java.util.Date today = new java.util.Date();
	    Date startDate = new Date(today.getTime());
	    // Calculate end date as 14 days from start date
	    long millisInDay = 24 * 60 * 60 * 1000L;
	    long endTime = startDate.getTime() + (14 * millisInDay);
	    Date endDate = new Date(endTime);
	    if (!isBookIDValid(bookID) || !isSubscriberIDValid(subscriberID)) {
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Invalid Input");
	        alert.setContentText("Book ID or Subscriber ID is not valid");
	        alert.showAndWait();
	    } 
	    else if (!canSubscribeBorrowMoreBooks(subscriberID)) {
	        // Show error message
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Limit Reached");
	        alert.setContentText("The subscriber has already borrowed 6 or more books");
	        alert.showAndWait();
	    } else {
	        boolean hasBorrowed = false;
	        for (Borrow b : bow) {
	            if (b.getSid() == subscriberID && b.getBid() == bookID) {
	                hasBorrowed = true;
	               
	                break;
	            }
	        }
	        if (!hasBorrowed) {
	            int numOfBooks = 1; // initialize to 1
	            for (Borrow b : bow) {
	                if (b.getSid() == subscriberID) {
	                    numOfBooks = b.getBorrowedBooks() + 1;
	                    break;
	                }
	            }
	            
	            try (Connection con = Connector.a.connectDB();
	                    PreparedStatement pstmt = con.prepareStatement("INSERT INTO B_Sub_Borrow  (B_id, s_id, startDate, endDate, numOfBook) VALUES (?, ?, ?, ?, ?)")) {
	                pstmt.setInt(1, bookID);
	                pstmt.setInt(2, subscriberID);
	                pstmt.setObject(3, startDate);
	                pstmt.setObject(4, endDate);
	                pstmt.setInt(5, numOfBooks);
	                pstmt.executeUpdate();
	                con.close();
	                load();
	                decrementBookCount(bookID);
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        } else {
	            // Show error message
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error");
	            alert.setHeaderText("Book Already Borrowed");
	            alert.setContentText("The subscriber has already borrowed this book");
	            alert.showAndWait();
	        }
	        }
	        }
	private void decrementBookCount(int bookID) throws ClassNotFoundException {
	    try (Connection con = Connector.a.connectDB();
	            PreparedStatement pstmt = con.prepareStatement("UPDATE book SET number_of_Copy = number_of_Copy - 1 WHERE B_id = ?")) {
	        pstmt.setInt(1, bookID);
	        pstmt.executeUpdate();
	        con.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	private void incrementBookCount(int bookID) throws ClassNotFoundException {
	    try (Connection con = Connector.a.connectDB();
	            PreparedStatement pstmt = con.prepareStatement("UPDATE book SET number_of_Copy = number_of_Copy +1 WHERE B_id = ?")) {
	        pstmt.setInt(1, bookID);
	        pstmt.executeUpdate();
	        con.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	public void deleteRecord(int bid, int sid) throws ClassNotFoundException {
	        try {
            Connection con = Connector.a.connectDB();
            PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM B_Sub_Borrow WHERE B_id = ? AND s_id = ?");
            preparedStatement.setInt(1, bid);
            preparedStatement.setInt(2, sid);
            preparedStatement.executeUpdate();
            con.close();
            load();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	        }
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void delete(MouseEvent event) throws ClassNotFoundException {
		 if (Bid.getText().isEmpty() || Sid.getText().isEmpty()) {
			    Alert alert = new Alert(AlertType.ERROR);
			    alert.setTitle("Error");
			    alert.setHeaderText("Empty Fields");
			    alert.setContentText("Please enter a Book ID and Subscriber ID");
			    alert.showAndWait();
			    return;
			}
		  if (!isBookIDValid(Integer.parseInt(Bid.getText())) || !isSubscriberIDValid(Integer.parseInt(Sid.getText()))) {
			    Alert alert = new Alert(AlertType.ERROR);
			    alert.setTitle("Error");
			    alert.setHeaderText("Invalid Book ID or Subscriber ID");
			    alert.setContentText("Please enter a valid Book ID and Subscriber ID");
			    alert.showAndWait();
			    return;
			}
		 else {
				 int bookID = Integer.parseInt(Bid.getText());
				 int subscriberID = Integer.parseInt(Sid.getText());
		         deleteRecord(bookID, subscriberID);
		         incrementBookCount(bookID);
}
	}
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void search(MouseEvent event) throws Exception, Exception {
		if (!Bid.getText().isEmpty() && !Sid.getText().isEmpty()) {
		searchByID();
		} else if (!Bid.getText().isEmpty()) {
		searchByBookID();
		} else if (!Sid.getText().isEmpty()) {
		searchBySubscriberID();
		} else {
		showAlert("Enter a valid Book ID or Subscriber ID");
		}
		}
	public void searchByBookID() throws ClassNotFoundException, SQLException {
		int bookID = Integer.parseInt(Bid.getText());

	
		    if (!isBookIDValid(bookID)) {
		        showAlert("Book ID is not valid");
		        return;
		    }

		    Connection con = Connector.a.connectDB();
		    PreparedStatement statement = con.prepareStatement("SELECT * FROM B_Sub_Borrow WHERE B_id = ?");
		    statement.setInt(1, bookID);
		    ResultSet resultSet = statement.executeQuery();

		    bow.clear();
		    while (resultSet.next()) {
		        int Bookid = resultSet.getInt("B_id");
		        int Sid = resultSet.getInt("s_id");
		        int num = resultSet.getInt("numOfBook");
		        Date startDate = resultSet.getDate("startDate");
		        Date endDate = resultSet.getDate("endDate");
		        bow.add(new Borrow(Bookid, Sid, startDate, endDate, num));
		    }

		    con.close();
		    TableBorrow.setItems(bow);
		}
		public void searchBySubscriberID() throws ClassNotFoundException, SQLException {
		int subscriberID = Integer.parseInt(Sid.getText());
		    if (!isSubscriberIDValid(subscriberID)) {
		        showAlert("Subscriber ID is not valid");
		        return;
		    }

		    Connection con = Connector.a.connectDB();
		    PreparedStatement statement = con.prepareStatement("SELECT * FROM B_Sub_Borrow WHERE s_id = ?");
		    statement.setInt(1, subscriberID);
		    ResultSet resultSet = statement.executeQuery();

		    bow.clear();
		    while (resultSet.next()) {
		        int Bookid = resultSet.getInt("B_id");
		        int Sid = resultSet.getInt("s_id");
		        int num = resultSet.getInt("numOfBook");
		        Date startDate = resultSet.getDate("startDate");
		        Date endDate = resultSet.getDate("endDate");
		        bow.add(new Borrow(Bookid, Sid, startDate, endDate, num));
		    }

		    con.close();
		    TableBorrow.setItems(bow);
		}
		public void searchByID() throws ClassNotFoundException, SQLException {
		    int subscriberID = Integer.parseInt(Sid.getText());
		    int bookID = Integer.parseInt(Bid.getText());

		    if (!isSubscriberIDValid(subscriberID)) {
		        showAlert("Subscriber ID is not valid");
		        return;
		    }

		    if (!isBookIDValid(bookID)) {
		        showAlert("Book ID is not valid");
		        return;
		    }

		    Connection con = Connector.a.connectDB();
		    PreparedStatement statement = con.prepareStatement("SELECT * FROM B_Sub_Borrow WHERE s_id = ? AND B_id = ?");
		    statement.setInt(1, subscriberID);
		    statement.setInt(2, bookID);
		    ResultSet resultSet = statement.executeQuery();

		    bow.clear();
		    while (resultSet.next()) {
		        int Bookid = resultSet.getInt("B_id");
		        int Sid = resultSet.getInt("s_id");
		        int num = resultSet.getInt("numOfBook");
		        Date startDate = resultSet.getDate("startDate");
		        Date endDate = resultSet.getDate("endDate");
		        bow.add(new Borrow(Bookid, Sid, startDate, endDate, num));
		    }

		    con.close();
		    TableBorrow.setItems(bow);
		}
	private void showAlert(String message) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	private void updateEndDate(int bookID, int subscriberID) throws ClassNotFoundException {
        if (!isBookIDValid(bookID) || !isSubscriberIDValid(subscriberID)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid input");
            alert.setHeaderText("Invalid book ID or subscriber ID");
            alert.setContentText("Please enter a valid book ID and subscriber ID");
            alert.showAndWait();
            return;
        }
        Borrow b = null;
        for (Borrow br : bow) {
            if (br.getBid() == bookID && br.getSid() == subscriberID) {
                b = br;
                break;
            }
        }
        if (b == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid input");
            alert.setHeaderText("No record found");
            alert.setContentText("No record found for the specified book ID and subscriber ID");
            alert.showAndWait();
            return;
        }
        try {
            Connection con = Connector.a.connectDB();
            PreparedStatement statement = con.prepareStatement("UPDATE B_Sub_Borrow SET endDate = ? WHERE B_id = ? and s_id = ?");
            java.util.Date endDate = b.getEndDate();
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(endDate);
            c.add(java.util.Calendar.DATE, 7);
            endDate = c.getTime();
            statement.setDate(1, new Date(endDate.getTime()));
            statement.setInt(2, bookID);
            statement.setInt(3, subscriberID);
            statement.executeUpdate();
            con.close();
            load();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	// Event Listener on ImageView.onMouseClicked
	@FXML
	public void update(MouseEvent event) throws ClassNotFoundException {
		 int bookID = Integer.parseInt(Bid.getText());
		  int subscriberID = Integer.parseInt(Sid.getText());
		  updateEndDate(bookID, subscriberID);
		  
	}
	// Event Listener on  ImageView.onMouseClicked
	@FXML
	public void back(MouseEvent event) {
		try {
			Stage stage;
			stage = (Stage) B.getScene().getWindow();
			stage.close();
			AnchorPane root = FXMLLoader.load(getClass().getResource("Bage2.fxml"));
			Scene scene = new Scene(root, 1000, 700);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {

		}
	}
}