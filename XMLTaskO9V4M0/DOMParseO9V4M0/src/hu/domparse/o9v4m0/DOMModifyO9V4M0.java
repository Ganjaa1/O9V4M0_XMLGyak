package hu.domparse.o9v4m0;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

public class DOMModifyO9V4M0 {
    public static void main(String argv[]) throws SAXException, IOException, ParserConfigurationException {
        File xmlFile = new File("./XMLO9V4M0.xml");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        
        //A második határidõ napló bevételének módosítása.
        NodeList hatarIdoNaplok = doc.getElementsByTagName("hatarido_naplo");
        Node naplo = hatarIdoNaplok.item(1);
        naplo.getAttributes().getNamedItem("bevetel").setTextContent("30000");
        
        //Kiadások összegének ÁFÁ-s növelése.
        NodeList kiadasok = doc.getElementsByTagName("kiadas");
        for(int i = 0; i< kiadasok.getLength();i++) {
        	Node kiadas = kiadasok.item(i);
        	int osszeg = Integer.parseInt(kiadas.getAttributes().getNamedItem("osszeg").toString());
        	String osszegString = ""+osszeg*1.27;
        	kiadas.getAttributes().getNamedItem("osszeg").setTextContent(osszegString);
        }
        
        //Az orvosok fizetésének emelése 32%-al.
        NodeList orvosokFizetesLista = doc.getElementsByTagName("dolgozik");
        multiplyListElement(orvosokFizetesLista,"fizetes",1.32);

        //Megrendelések darab elementjének törlése
        NodeList megrendelesekLista = doc.getElementsByTagName("megrendelesek");
        for (int i = 0; i < megrendelesekLista.getLength(); i++) {
        	Node megrendeles = megrendelesekLista.item(i);
        	NodeList childNodes = megrendeles.getChildNodes();
        	for (int j = 0; j < childNodes.getLength(); j++) {
        		 Node item = childNodes.item(j);
                 if (item.getNodeType() == Node.ELEMENT_NODE) {
                     if ("darab".equalsIgnoreCase(item.getNodeName())) {  
                    	 megrendeles.removeChild(item);

                     }
                 }
			}
		}
        
        //Fájl mentése
        try (FileOutputStream output = new FileOutputStream("O9V4M0modified.xml")) 
        {
            writeXml(doc, output);
        } catch (TransformerException e)
        {
            e.printStackTrace();
        }
    }
    
    private static void multiplyListElement(NodeList nodeList, String tagName, double percentageValue) {
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                int newVal = Integer.parseInt(node.getAttributes().getNamedItem(tagName).toString());
                String newValString = "" + newVal * percentageValue;
                Element elem = (Element) node;
                try {
                    elem.getElementsByTagName(tagName).item(0).setTextContent(newValString);
                } catch (DOMException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    //A módosított XML fájlba írása.
    private static void writeXml(Document doc, OutputStream output) throws TransformerException, UnsupportedEncodingException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer(new StreamSource(new File("DOMParseO9V4M0/O9V4M0styling.xslt")));
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no"); 
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);

    }
}
