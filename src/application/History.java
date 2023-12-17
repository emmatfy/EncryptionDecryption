package application;

public class History {

	private int number;
	private String time;
	private String cipherText;
	private String encryMode;
	private int itemID;

	public History(int number, String time, String cipherText, String encryMode, int itemID) {
		super();
		this.number = number;
		this.time = time;
		this.cipherText = cipherText;
		this.encryMode = encryMode;
		this.itemID = itemID;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getEncryMode() {
		return encryMode;
	}

	public void setEncryMode(String encryMode) {
		this.encryMode = encryMode;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCipherText() {
		return cipherText;
	}

	public void setCipherText(String cipherText) {
		this.cipherText = cipherText;
	}

}
