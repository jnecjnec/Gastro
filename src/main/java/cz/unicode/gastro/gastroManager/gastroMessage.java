package cz.unicode.gastro.gastroManager;

public class gastroMessage {
	
	public enum gastroMessageType {
		gmtUnknown, gmtTable, gmtItem
	};

	private gastroMessageType msgType = gastroMessageType.gmtUnknown;
	private Object object = null;
	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}


	/**
	 * @return the msgType
	 */
	public gastroMessageType getMsgType() {
		return msgType;
	}

	/**
	 * @param msgType the msgType to set
	 */
	public void setMsgType(gastroMessageType msgType) {
		this.msgType = msgType;
	}	

}
