package hu.domparse.o9v4m0;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DOMQuerryO9V4M0 {
    private static XPath xPath = XPathFactory.newInstance().newXPath();
    private static Document document;

    public static void main(String[] args) {
    	try {
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();

            document = dBuilder.parse("./XMLO9V4M0.xml");

            document.getDocumentElement().normalize();
            
            //Az összes teljesítmény igazolás lekérése.
            queryTeljesitmenyIgazolas();
            
            System.out.printf("---------------------\n\n");
            
            //Megrendelések szûrése ID alapján
            queryMegrendelesekByID("RID02");
            
            System.out.printf("---------------------\n\n");
            
            //3-as számú orvos elvégzett rendeléseinek lekérése
            queryHataridoNaploByDoctor(3);
            
            System.out.printf("---------------------\n\n");
            
            //18000Ft fölötti bevételek lekérdezése
            queryBevetelByOsszeg(18000);
            
    	} catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void queryTeljesitmenyIgazolas(){
        String expression = String.format("/egeszsegugy/teljesitmeny_igazolas");
    	try {
    		System.out.println("Osszes Teljesitmeny_Igazolas lekerdezes:");
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node nNode = nodeList.item(i);;

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) nNode;
                    String tid = element.getAttribute("TID");
                    System.out.println("Azonosito: "+ tid +
                    		"%n\t Orvos azonosito: " + element.getElementsByTagName("orvos").item(0).getTextContent() +
                    		"%n\t Fizetes: " + element.getElementsByTagName("fizetes").item(0).getTextContent() +
                    		"%n\t Datum: " + element.getElementsByTagName("datum").item(0).getTextContent());
                }
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

    }
    
    private static void queryMegrendelesekByID(String ID) {
    	String expression = String.format("/egeszsegugy/megrendelesek[RID = '%s']", ID);
        try {
            System.out.printf("Id alapjan lekerdezes: \n");

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node nNode = nodeList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) nNode;
                    String rid = element.getAttribute("RID");
                    System.out.println("Azonosito: "+ rid +
                    		"%n\t Megnevezes: " + element.getElementsByTagName("megnevezes").item(0).getTextContent() +
                    		"%n\t Datum: " + element.getElementsByTagName("datum").item(0).getTextContent() +
                    		"%n\t Penzkiadas: " + element.getElementsByTagName("penzkiadas").item(0).getTextContent() +
                    		"%n\t Darab: " + element.getElementsByTagName("darab").item(0).getTextContent());
                }
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
    
    private static void queryHataridoNaploByDoctor(int ID){
    	String expression = String.format("/egeszsegugy/hatarido_naplo");
    	try {
            System.out.printf("3-as szamu orvos elvegzett rendelesei: \n");

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node nNode = nodeList.item(i);
                
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                	 Element element = (Element) nNode;
                	 if(Integer.parseInt(element.getElementsByTagName("szakorv").item(0).getTextContent()) == ID) {
                		 Node paciens = (Node) element.getElementsByTagName("paciens");
                		 System.out.println(
                				 "Idopont: "+element.getElementsByTagName("idopont").item(0).getTextContent()+
                				 "Bevetel: "+element.getElementsByTagName("bevetel").item(0).getTextContent()+
                				 "Szakrendelo: "+element.getElementsByTagName("szakrend").item(0).getTextContent()+
                				 "Paciens neve: "+paciens.getAttributes().getNamedItem("nev").getTextContent()+
                				 "Paciens TAJszama: "+paciens.getAttributes().getNamedItem("TAJszam").getTextContent()+
                				 "Paciens telefonszama: "+paciens.getAttributes().getNamedItem("telefonszam").getTextContent()
                				 );
                	 }
                }
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    	
    }
    
    private static void queryBevetelByOsszeg(int minimumPrice) {
    	String expression = String.format("/egeszsegugy/bevetel[osszeg>%s]",minimumPrice);
    	try {
            System.out.printf("%s -tol nagyobb bevetelu szakrendelesek: \n");

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {

                Node nNode = nodeList.item(i);
                
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                	 Element element = (Element) nNode;
                     String bid = element.getAttribute("BID");
                     String hid = element.getAttribute("HID");
                     System.out.println(
                    		 "Azonositok: "+ bid + " "+hid+
                     		"%n\t Megnevezes: " + element.getElementsByTagName("megnevezes").item(0).getTextContent() +
                     		"%n\t Osszeg: " + element.getElementsByTagName("osszeg").item(0).getTextContent());
                }
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }
    
    
    
}
