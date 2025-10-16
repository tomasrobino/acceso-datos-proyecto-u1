package repository;

import model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class XML extends BDInterfaz {
    private static XML miXML;

    public static XML getCSV(String uri) {
        if (miXML == null) {
            miXML = new XML(uri);
        }
        return miXML;
    }

    private XML(String uri) {
        this.uri = uri;
    }

    @Override
    public String find(int id) throws IOException{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document documento = dBuilder.parse(new File(uri));
            Node nodoRaiz = documento.getDocumentElement();
            // get xmlName
            NodeList lista = nodoRaiz.getChildNodes();
            for (int i = 0; i < lista.getLength(); i++) {
                if (Integer.parseInt(lista.item(i).getAttributes().getNamedItem("id").getTextContent()) == id ) {
                    return lista.item(i).toString();
                }
            }
            return null;
        } catch (ParserConfigurationException | SAXException e) {
            return null;
        }
    }

    @Override
    void insert(Model model) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(uri));
        bw.write("\n"+model.stringifyXML());
        bw.close();
    }

    @Override
    boolean update(Model model) throws IOException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("coches.xml"));
        transformer.transform(source, result);
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document documento = dBuilder.parse(new File(uri));
            Node nodoRaiz = documento.getDocumentElement();
            // get xmlName
            NodeList lista = nodoRaiz.getChildNodes();
            for (int i = 0; i < lista.getLength(); i++) {
                if (Integer.parseInt(lista.item(i).getAttributes().getNamedItem("id").getTextContent()) == model.getId() ) {
                    lista.item(i)
                    return true;
                }
            }
            return false;
        } catch (ParserConfigurationException | SAXException e) {
            return false;
        }
    }

    @Override
    public boolean delete(int id) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(uri));
        String line;
        String buffer = "";
        // Reading root node
        if ((line = br.readLine()) != null && line.contains(">")) {
            if ((line = br.readLine()) != null && line.contains("<")) {
                // Finding out xmlName
                int start = line.indexOf("<") + 1;
                int end = line.indexOf(">");
                String xmlName = line.substring(start, end);

                do {

                }
            }
        }
        br.close();
        return null;
    }
}
