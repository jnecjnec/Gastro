package cz.unicode.gastro.tcpip.message;

public class message {
	public enum messageType {
		mtUnknown, mtResponse, mtGastro
	};

	private messageType msgType = messageType.mtUnknown;
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
	public messageType getMsgType() {
		return msgType;
	}

	/**
	 * @param msgType the msgType to set
	 */
	public void setMsgType(messageType msgType) {
		this.msgType = msgType;
	}	
}
