package application;

public class Publishing_house {
	int ID;
	String Name;
	String Address;
	String Email;
	
	// static Publishing_house PH;
	public static Publishing_house PH;

	public Publishing_house(int iD, String name, String address,String email) {
		super();
		ID = iD;
		Name = name;
		Address = address;
		Email = email;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
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

}
