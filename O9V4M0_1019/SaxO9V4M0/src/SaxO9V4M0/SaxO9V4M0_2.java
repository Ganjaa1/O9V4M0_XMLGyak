package SaxO9V4M0;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxO9V4M0_2 {

	public static void main(String[] args) {
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			
			SAXParser saxParser = saxParserFactory.newSAXParser();
			
			SaxHandler_2 handler = new SaxHandler_2();
			
			saxParser.parse(new File("JMT_kurzusfelvetel.xml"), handler);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
}

class SaxHandler_2 extends DefaultHandler {
	
	private int indent = 0;
	private int oneLine = 2;
	
	private String formatAttributes(Attributes attributes) {
		int attLength = attributes.getLength();
		if (attLength == 0)
			return "";
		StringBuilder sb = new StringBuilder(", {");
		for (int i=0; i<attLength; i++) {
			sb.append(attributes.getLocalName(i) + "=" + attributes.getValue(i));
			if (i < attLength - 1) {
				sb.append(", ");
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
	private void indent() {
		for (int i=0; i<indent; i++) {
			System.out.print("  ");
		}
	}
	
	@Override
	public void startElement(String uti, String localName, String qName, Attributes attributes) {
		indent++;
		indent();
		if (indent <= oneLine)
			System.out.println(qName + formatAttributes(attributes) + " start ");
		else
			System.out.print(qName + formatAttributes(attributes) + " start ");
	}
	
	@Override
	public void endElement(String uri, String localName, String qName){
		indent--;
		if (indent <= oneLine) {
			indent();
			indent();
			System.out.println(qName + " end ");
		}
		else
			System.out.print(qName + " end ");
	}
	
	@Override
	public void characters(char ch[], int start, int length) {
		String chars = new String(ch, start, length).trim();
		if (!chars.isEmpty()) {
			if (indent <= oneLine) {
				System.out.println(chars);
			} else {
				System.out.print(chars);	
			}
		}
	}
}






