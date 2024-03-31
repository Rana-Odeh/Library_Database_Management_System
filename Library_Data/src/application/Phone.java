package application;

public class Phone {
	int S_id;
	int phone;

	public Phone(int s_id, int phone) {
		super();
		S_id = s_id;
		this.phone = phone;
	}

	public int getS_id() {
		return S_id;
	}

	public void setS_id(int s_id) {
		S_id = s_id;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

}
