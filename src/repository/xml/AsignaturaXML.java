package repository.xml;

import model.*;
import org.w3c.dom.*;
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

    public AsignaturaXML(String uri) {
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
                Element item = (Element) lista.item(i);
                if (Integer.parseInt(item.getElementsByTagName("id").item(0).getTextContent()) == id ) {
                    return new Asignatura(
                            Integer.parseInt(item.getElementsByTagName("id").item(0).getTextContent()),
                            item.getElementsByTagName("nombre").item(0).getTextContent(),
                            Integer.parseInt(item.getElementsByTagName("creditos").item(0).getTextContent())
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
                Element item = (Element) lista.item(i);
                listaAsignaturas.add(new Asignatura(
                        Integer.parseInt(item.getElementsByTagName("id").item(0).getTextContent()),
                        item.getElementsByTagName("nombre").item(0).getTextContent(),
                        Integer.parseInt(item.getElementsByTagName("creditos").item(0).getTextContent())
                ));
            }
            return listaAsignaturas;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        }
    }

    @Override
    public boolean insert(Model model) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(uri));

            Node nodoRaiz = doc.getDocumentElement();
            Element asignatura = doc.createElement("asignatura");
            Element id = doc.createElement("id");
            id.appendChild(doc.createTextNode( String.valueOf(model.getId())));
            asignatura.appendChild(id);
            Element nombre = doc.createElement("nombre");
            nombre.appendChild(doc.createTextNode(((Asignatura)model).getNombre()));
            asignatura.appendChild(nombre);
            Element creditos = doc.createElement("creditos");
            creditos.appendChild(doc.createTextNode(String.valueOf(((Asignatura)model).getCreditos())));
            asignatura.appendChild(creditos);
            
            nodoRaiz.appendChild(asignatura);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(uri));
            transformer.transform(source, result);
            return true;
        } catch (TransformerException | ParserConfigurationException | IOException | SAXException e) {
            return false;
        }
    }

    @Override
    public boolean update(Model model) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(uri));

            NodeList lista = doc.getElementsByTagName("asignatura");
            for (int i = 0; i < lista.getLength(); i++) {
                Element asignatura = (Element) lista.item(i);
                if (asignatura.getElementsByTagName("id").item(0).getTextContent().equals(String.valueOf(model.getId()))) {
                    asignatura.getElementsByTagName("nombre").item(0).setTextContent(((Asignatura)model).getNombre());
                    asignatura.getElementsByTagName("creditos").item(0).setTextContent(String.valueOf(((Asignatura)model).getCreditos()));
                    
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(uri));
                    transformer.transform(source, result);
                    return true;
                }
            }
            return false;
        } catch (TransformerException | ParserConfigurationException | IOException | SAXException e) {
            return false;
        }
    }
}
