package cz.unicode.gastro.model.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "table")
public class tableimpl implements table {
	
	@DatabaseField(canBeNull = false)
	private int number = 5;
	@DatabaseField(canBeNull = false)
	private int userId = 10;
	@DatabaseField(canBeNull = false)
	private boolean isTaken = false;
	
	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean getIsTaken() {
		return this.isTaken;
	}

	public void setIsTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}

}
