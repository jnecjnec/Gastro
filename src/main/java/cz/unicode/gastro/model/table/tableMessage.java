package cz.unicode.gastro.model.table;

public class tableMessage {
	public enum commandType {
		ctUnknown, ctAdd, ctDel, ctEdit
	};

	private commandType _commandType = commandType.ctUnknown;
	private table _table = null;
	/**
	 * @return the _commandType
	 */
	public commandType get_commandType() {
		return _commandType;
	}
	/**
	 * @param _commandType the _commandType to set
	 */
	public void set_commandType(commandType _commandType) {
		this._commandType = _commandType;
	}
	/**
	 * @return the _table
	 */
	public table get_table() {
		return _table;
	}
	/**
	 * @param _table the _table to set
	 */
	public void set_table(table _table) {
		this._table = _table;
	}
}
