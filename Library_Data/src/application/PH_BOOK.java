package application;

public class PH_BOOK {
	private int B_id;
	private int P_id;
	private String Year_Publication;

	public PH_BOOK(int b_id, int p_id, String year_Publication) {
		super();
		B_id = b_id;
		P_id = p_id;
		Year_Publication = year_Publication;
	}

	public int getB_id() {
		return B_id;
	}

	public void setB_id(int b_id) {
		B_id = b_id;
	}

	public int getP_id() {
		return P_id;
	}

	public void setP_id(int p_id) {
		P_id = p_id;
	}

	public String getYear_Publication() {
		return Year_Publication;
	}

	public void setYear_Publication(String year_Publication) {
		Year_Publication = year_Publication;
	}

}
