package domo9v4m0;

import java.io.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DomReadO9V4M0 {
	public static void main(String argv[]) throws SAXException, IOException, ParserConfigurationException {
		String[] fields = {
			"firstname",
			"lastname",
			"profession"
		};
		
		File xmlFile = new File("usersO9V4M0.xml");
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = factory.newDocumentBuilder();
		
		Document doc = dBuilder.parse(xmlFile);
		
		doc.getDocumentElement().normalize();
		
		System.out.println("Root element:" + doc.getDocumentElement().getNodeName());
		
		NodeList nList = doc.getElementsByTagName("user");
		
		for (int i=0; i<nList.getLength(); i++){
			Node nNode = nList.item(i);
			
			System.out.println("\nCurrent Element: " + nNode.getNodeName());
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) nNode;
				String uid = elem.getAttribute("id");
				System.out.println("User id: " + uid);
				Write("User id: " + uid);
				for(String field : fields){
					Node node = elem.getElementsByTagName(field).item(0);
					String data = node.getTextContent();
					System.out.println(field + ": " + data);
					Write(field + ": " + data);
				}
			}
		}
	}
	public static void Write(String input) throws IOException{
		BufferedWriter out = null;
		try {
		    FileWriter fstream = new FileWriter("usersO9V4M0.txt", true);
		    out = new BufferedWriter(fstream);
		    out.write(input);
		    out.newLine();
		}
		catch (IOException e) {
		    System.err.println("Error: " + e.getMessage());
		}
		finally {
		    if(out != null) {
		        out.close();
		    }
		}
	}
}