package cz.unicode.gastro.gastroManager;

import java.util.ArrayList;

import cz.unicode.gastro.model.table.table;

public class gastroManager {

	private ArrayList<table> tables = new ArrayList<table>();

	public boolean AddTable(table pTable) {
		boolean result = false;
		tables.add(pTable);
		return result;
	}
	
	public boolean EditTable(table pTable) {
		boolean result = false;
		
		return result;
	}
	
	public boolean DelTable(table pTable) {
		boolean result = false;
		
		return result;
	}
	
	public boolean AddItem(){
		boolean result = false;
		
		return result;
	}
}
