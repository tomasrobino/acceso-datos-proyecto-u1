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

public class MatriculaXML extends XML {
    private static MatriculaXML miMatriculaXML;

    public BDInterfaz get(String uri) {
        if (miMatriculaXML == null) {
            miMatriculaXML = new MatriculaXML(uri);
        }
        return miMatriculaXML;
    }

    private MatriculaXML(String uri) {
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
                    NamedNodeMap asignatura = item.getNamedItem("asignatura").getAttributes();
                    return new Matricula(
                            Integer.parseInt(item.getNamedItem("id").getTextContent()),
                            Double.parseDouble(item.getNamedItem("nota").getTextContent()),
                            item.getNamedItem("fecha").getTextContent(),
                            new Asignatura(
                                    Integer.parseInt(asignatura.getNamedItem("id").getTextContent()),
                                    asignatura.getNamedItem("nombre").getTextContent(),
                                    Integer.parseInt(asignatura.getNamedItem("creditos").getTextContent())
                            )
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
            ArrayList<Model> listaMatriculas = new ArrayList<>();
            for (int i = 0; i < lista.getLength(); i++) {
                NamedNodeMap item = lista.item(i).getAttributes();
                NamedNodeMap asignatura = item.getNamedItem("asignatura").getAttributes();
                listaMatriculas.add(new Matricula(
                        Integer.parseInt(item.getNamedItem("id").getTextContent()),
                        Double.parseDouble(item.getNamedItem("nota").getTextContent()),
                        item.getNamedItem("fecha").getTextContent(),
                        new Asignatura(
                                Integer.parseInt(asignatura.getNamedItem("id").getTextContent()),
                                asignatura.getNamedItem("nombre").getTextContent(),
                                Integer.parseInt(asignatura.getNamedItem("creditos").getTextContent())
                        )
                ));
            }
            return listaMatriculas;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        }
    }

    @Override
    public boolean insert(Model model) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Node nodoRaiz = doc.getDocumentElement();
            Element matricula = doc.createElement("matricula");
            matricula.appendChild(doc.createElement("id").appendChild(
                    doc.createTextNode( String.valueOf(model.getId()) )
            ));
            matricula.appendChild(doc.createElement("nota").appendChild(
                    doc.createTextNode(String.valueOf( ((Matricula)model).getNota() )) )
            );
            matricula.appendChild(doc.createElement("fecha").appendChild(
                    doc.createTextNode( ((Matricula)model).getFecha() )
            ));
            Element asignatura = doc.createElement("asignatura");
            asignatura.appendChild(doc.createElement("id").appendChild(
                    doc.createTextNode( String.valueOf(((Matricula)model).getAsignatura().getId()) )
            ));
            asignatura.appendChild(doc.createElement("nombre").appendChild(
                    doc.createTextNode(((Matricula)model).getAsignatura().getNombre())
            ));
            asignatura.appendChild(doc.createElement("creditos").appendChild(
                    doc.createTextNode(String.valueOf(((Matricula)model).getAsignatura().getCreditos()))
            ));
            matricula.appendChild(asignatura);
            nodoRaiz.appendChild(matricula);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(uri));
            transformer.transform(source, result);
            return true;
        } catch (TransformerException | ParserConfigurationException e) {
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
