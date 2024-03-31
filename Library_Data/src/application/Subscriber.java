package application;

public class Subscriber {
	private int S_ID;
	private String S_Name;
	private String Date_Birth;
	private String Gender;
	private String Email;
	private String Address;
	private String age_Group;

	static Subscriber sub;

	public Subscriber(int s_ID, String s_Name, String date_Birth, String age_Group, String email, String address,
			String gender) {
		super();
		S_ID = s_ID;
		S_Name = s_Name;
		Date_Birth = date_Birth;
		Gender = gender;
		Email = email;
		Address = address;
		this.age_Group = age_Group;
	}

	public int getS_ID() {
		return S_ID;
	}

	public void setS_ID(int s_ID) {
		S_ID = s_ID;
	}

	public String getS_Name() {
		return S_Name;
	}

	public void setS_Name(String s_Name) {
		S_Name = s_Name;
	}

	public String getDate_Birth() {
		return Date_Birth;
	}

	public void setDate_Birth(String date_Birth) {
		Date_Birth = date_Birth;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getAge_Group() {
		return age_Group;
	}

	public void setAge_Group(String age_Group) {
		this.age_Group = age_Group;
	}

}
