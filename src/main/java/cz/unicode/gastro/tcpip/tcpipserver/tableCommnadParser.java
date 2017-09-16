package cz.unicode.gastro.tcpip.tcpipserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import cz.unicode.gastro.model.table.table;

public class tableCommnadParser {
	private static enum commandType {
		ctUnknown, ctAdd
	};

	private static class parsedCommand {
		
		public commandType cmdType = commandType.ctUnknown;
		public table tbl = null;
	}

	public static boolean parseCommand(String command) {
		parsedCommand aParsedCommand = parseCommandType(command);

		switch (aParsedCommand.cmdType) {
		case ctAdd: {
			addTable(aParsedCommand.tbl);
		}
			break;
		case ctUnknown:
			break;
		}
		return true;
	}

	private static parsedCommand parseCommandType(String command) {
		parsedCommand aParsedCommand = new parsedCommand();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append(command);
		ByteArrayInputStream input = null;
		try {
			input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = builder.parse(input);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (doc.getDocumentElement().getNodeName().equals("Gastro")) {
			if (doc.getDocumentElement().getAttribute("commandType").equals("add")) {
				aParsedCommand.cmdType = commandType.ctAdd;
				
				//doc.getDocumentElement().getFirstChild().getNodeName().equals("table")

				String acommand = nodeToString(doc.getDocumentElement().getFirstChild());
				aParsedCommand.tbl = parseTable(acommand);
			}
		}

		return aParsedCommand;
	}

	private static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		return sw.toString();
	}

	private static table parseTable(String command) {
		table aTable = null;
		XmlMapper xmlMapper = new XmlMapper();
		try {
			aTable = xmlMapper.readValue(command, table.class);

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return aTable;
	}

	private static boolean addTable(table pTable) {
		return true;
	}

}
