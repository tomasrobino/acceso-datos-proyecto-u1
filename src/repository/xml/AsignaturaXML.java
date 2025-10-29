package repository.xml;

import model.*;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import repository.BDInterfaz;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;

public class AsignaturaXML extends XML {
    private static AsignaturaXML miAsignaturaXML;

    public BDInterfaz get(String uri) {
        if (miAsignaturaXML == null) {
            miAsignaturaXML = new AsignaturaXML(uri);
        }
        return miAsignaturaXML;
    }

    private AsignaturaXML(String uri) {
        this.uri = uri;
    }

    @Override
    public Model find(int id) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document documento = dBuilder.parse(new File(uri));
            Node nodoRaiz = documento.getDocumentElement();
            NodeList lista = nodoRaiz.getChildNodes();
            for (int i = 0; i < lista.getLength(); i++) {
                NamedNodeMap item = lista.item(i).getAttributes();
                if (Integer.parseInt(item.getNamedItem("id").getTextContent()) == id ) {
                    return new Asignatura(
                            Integer.parseInt(item.getNamedItem("id").getTextContent()),
                            item.getNamedItem("nombre").getTextContent(),
                            Integer.parseInt(item.getNamedItem("creditos").getTextContent())
                    );
                }
            }
            return null;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        }
    }

    @Override
    public ArrayList<Model> findAll() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document documento = dBuilder.parse(new File(uri));
            Node nodoRaiz = documento.getDocumentElement();
            NodeList lista = nodoRaiz.getChildNodes();
            ArrayList<Model> listaAsignaturas = new ArrayList<>();
            for (int i = 0; i < lista.getLength(); i++) {
                NamedNodeMap item = lista.item(i).getAttributes();
                listaAsignaturas.add(new Asignatura(
                        Integer.parseInt(item.getNamedItem("id").getTextContent()),
                        item.getNamedItem("nombre").getTextContent(),
                        Integer.parseInt(item.getNamedItem("creditos").getTextContent())
                ));
            }
            return listaAsignaturas;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        }
    }

    @Override
    public boolean insert(Model model) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(uri));
            bw.write("\n"+model.stringifyXML());
            bw.close();
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    @Override
    public boolean update(Model model) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(uri));
            transformer.transform(source, result);
            Node nodoRaiz = doc.getDocumentElement();
            NodeList lista = nodoRaiz.getChildNodes();
            for (int i = 0; i < lista.getLength(); i++) {
                if (Integer.parseInt(lista.item(i).getAttributes().getNamedItem("id").getTextContent()) == model.getId() ) {
                    nodoRaiz.removeChild(lista.item(i));
                    nodoRaiz.appendChild(  db.parse(new ByteArrayInputStream(model.stringifyXML().getBytes())).getDocumentElement()  );
                    return true;
                }
            }
            return false;
        } catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
            return false;
        }
    }
}
