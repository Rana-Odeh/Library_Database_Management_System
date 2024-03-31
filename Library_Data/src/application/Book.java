package application;

public class Book {
	private int B_id, number_of_pages, Sec_id, A_id, number_of_Copy;
	private String B_name, B_description, year_of_issue;

	public Book() {
		super();
	}

	public Book(int B_id, String B_name, String B_description, int number_of_pages, String year_of_issue, int Sec_id,
			int A_id, int number_of_Copy) {
		super();
		this.B_id = B_id;
		this.B_name = B_name;
		this.number_of_pages = number_of_pages;
		this.B_description = B_description;
		this.Sec_id = Sec_id;
		this.A_id = A_id;
		this.year_of_issue = year_of_issue;
		this.number_of_Copy = number_of_Copy;
	}

	public int getB_id() {
		return B_id;
	}

	public void setB_id(int b_id) {
		B_id = b_id;
	}

	public int getNumber_of_pages() {
		return number_of_pages;
	}

	public void setNumber_of_pages(int number_of_pages) {
		this.number_of_pages = number_of_pages;
	}

	public int getSec_id() {
		return Sec_id;
	}

	public void setSec_id(int sec_id) {
		Sec_id = sec_id;
	}

	public int getA_id() {
		return A_id;
	}

	public void setA_id(int a_id) {
		A_id = a_id;
	}

	public String getB_name() {
		return B_name;
	}

	public void setB_name(String b_name) {
		B_name = b_name;
	}

	public String getB_description() {
		return B_description;
	}

	public void setB_description(String b_description) {
		B_description = b_description;
	}

	public String getYear_of_issue() {
		return year_of_issue;
	}

	public void setYear_of_issue(String year_of_issue) {
		this.year_of_issue = year_of_issue;
	}

	public int getNumber_of_Copy() {
		return number_of_Copy;
	}

	public void setNumber_of_Copy(int number_of_Copy) {
		this.number_of_Copy = number_of_Copy;
	}

}