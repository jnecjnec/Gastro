package cz.unicode.gastro.tcpip.message;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class messageDecoderEncoder {

	private static XMLDecoder decoder;

	public static String encodemessageToCommand(message pMessage) {
		XMLEncoder xmlencoder = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		xmlencoder = new XMLEncoder(baos);
		xmlencoder.writeObject(pMessage);
		xmlencoder.close();
		return new String(baos.toByteArray());
	}

	public static message decodemessageToCommand(String pCommand) {
		message aMessage = null;
		decoder = new XMLDecoder(new ByteArrayInputStream(pCommand.getBytes()));
		aMessage = (message) decoder.readObject();
		return aMessage;
	}

}
