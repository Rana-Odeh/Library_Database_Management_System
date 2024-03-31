package application;

import java.sql.Date;
public class Borrow {
    private int bid;
    private int sid;
    private Date startDate;
    private Date endDate;
    private int borrowedBooks;
    public Borrow(int bid, int sid, Date startDate, Date endDate,int borrowedBooks) {
        this.bid = bid;
        this.sid = sid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.borrowedBooks=borrowedBooks;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int  getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public int getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(int borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}