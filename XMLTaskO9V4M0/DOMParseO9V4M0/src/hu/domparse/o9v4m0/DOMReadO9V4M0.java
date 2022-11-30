package hu.domparse.o9v4m0;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class DOMReadO9V4M0 {

    private static Document doc;

    public static void main(String argv[]) throws SAXException, IOException, ParserConfigurationException {
        File xmlFile = new File("./XMLO9V4M0.xml");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
        //Az összes megrendelés kiírása.
        readAllMegrendeles();
        
        //Az összes kiadás kiiírása.
        readAllKiadas();
        
        //Határidõ naplók kiírása.
        readAllHatarido_naplo();
        
        //Orvosok listájának kiírása.
        readAllOrvos();
    }
    
    private static void readAllMegrendeles() {
        NodeList megrendelesekList = doc.getElementsByTagName("megrendelesek");
        for (int i = 0; i < megrendelesekList.getLength(); i++) {

            Node nNode = megrendelesekList.item(i);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) nNode;
                
                String rid = element.getAttribute("RID");

                //Fájlba írás
                try (FileWriter fw = new FileWriter("O9V4M0DOMRead.txt", true);) {
                    fw.write("Azonosito: "+ rid +"\n");
                    fw.write("Megnevezes: "+ element.getElementsByTagName("megnevezes").item(0).getTextContent() +"\n");
                    fw.write("Kiadas: "+element.getElementsByTagName("penzkiadas").item(0).getTextContent()+ "\n");
                    fw.write("Darab: "+element.getElementsByTagName("darab").item(0).getTextContent()+ "\n\n");
                
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void readAllKiadas() {
        NodeList kiadasokList = doc.getElementsByTagName("kiadas");
        for (int i = 0; i < kiadasokList.getLength(); i++) {

            Node nNode = kiadasokList.item(i);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) nNode;
                
                String kid = element.getAttribute("KID");
                String secondId = "";
                
                if(element.getAttribute("TID") != null) {
                	secondId = element.getAttribute("TID");
                }else if(element.getAttribute("RID") != null) {
                	secondId = element.getAttribute("RID");
                }

                //Fájba írás
                try (FileWriter fw = new FileWriter("O9V4M0DOMRead.txt", true);) {
                    fw.write("Azonositok: "+ kid + " "+secondId +"\n");
                    fw.write("Megnevezes: "+ element.getElementsByTagName("megnevezes").item(0).getTextContent() +"\n");
                    fw.write("Osszeg: "+element.getElementsByTagName("osszeg").item(0).getTextContent()+ "\n\n");
                
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void readAllHatarido_naplo() {
    	NodeList hataridoList = doc.getElementsByTagName("hatarido_naplo");
        for (int i = 0; i < hataridoList.getLength(); i++) {

            Node nNode = hataridoList.item(i);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) nNode;
                
                String hid = element.getAttribute("HID");               

                //Fájba írás
                try (FileWriter fw = new FileWriter("O9V4M0DOMRead.txt", true);) {
                	Node paciens = (Node) element.getElementsByTagName("paciens");
                    fw.write("Azonosito: "+ hid +"\n");
                    fw.write("Idopont: " + element.getElementsByTagName("idopont").item(0).getTextContent() + "\n");
                    fw.write("Bevetel: " + element.getElementsByTagName("bevetel").item(0).getTextContent() + "\n");  
                    fw.write("Szakorvos: " + element.getElementsByTagName("szakorv").item(0).getTextContent() + "\n");
                    fw.write("Szakrendelo: " + element.getElementsByTagName("szakrend").item(0).getTextContent() + "\n");
                    fw.write("Paciens neve: " + paciens.getAttributes().getNamedItem("nev").getTextContent() + "\n");
                    fw.write("Paciens TAJszama: " + paciens.getAttributes().getNamedItem("TAJszam").getTextContent() + "\n");
                    fw.write("Paciens telefonszama: " + paciens.getAttributes().getNamedItem("telefonszam").getTextContent() + "\n");
                    fw.write("Paciens szuletesi ideje: " + element.getElementsByTagName("Szul_ido").item(0).getTextContent() + "\n\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void readAllOrvos(){
    	NodeList orvosokList = doc.getElementsByTagName("orvosok");
        for (int i = 0; i < orvosokList.getLength(); i++) {

            Node nNode = orvosokList.item(i);
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) nNode;
                
                String orvID = element.getAttribute("OrvID");               

                //Fájba írás
                try (FileWriter fw = new FileWriter("O9V4M0DOMRead.txt", true);) {
                	Node orvos = (Node) element.getElementsByTagName("orvos");
                    fw.write("Azonosito: "+ orvID +"\n");
                    fw.write("Nev: " + orvos.getAttributes().getNamedItem("nev").getTextContent() + "\n");
                    fw.write("Szuletesi ido: " + orvos.getAttributes().getNamedItem("Szul_ido").getTextContent() + "\n");  
                    fw.write("TAJ szam: " + orvos.getAttributes().getNamedItem("TAJszam").getTextContent() + "\n");
                    fw.write("Telefonszam: " + orvos.getAttributes().getNamedItem("Telefonszam").getTextContent() + "\n");
                    fw.write("Szakterulet: " + element.getElementsByTagName("szakter").item(0).getTextContent() + "\n\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
