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

    public MatriculaXML(String uri) {
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
                    Element asignatura = (Element) item.getElementsByTagName("asignatura").item(0);
                    return new Matricula(
                            Integer.parseInt(item.getElementsByTagName("id").item(0).getTextContent()),
                            Double.parseDouble(item.getElementsByTagName("nota").item(0).getTextContent()),
                            item.getElementsByTagName("fecha").item(0).getTextContent(),
                            new Asignatura(
                                    Integer.parseInt(asignatura.getElementsByTagName("id").item(0).getTextContent()),
                                    asignatura.getElementsByTagName("nombre").item(0).getTextContent(),
                                    Integer.parseInt(asignatura.getElementsByTagName("creditos").item(0).getTextContent())
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
                Element item = (Element) lista.item(i);
                Element asignatura = (Element) item.getElementsByTagName("asignatura").item(0);
                listaMatriculas.add(new Matricula(
                        Integer.parseInt(item.getElementsByTagName("id").item(0).getTextContent()),
                        Double.parseDouble(item.getElementsByTagName("nota").item(0).getTextContent()),
                        item.getElementsByTagName("fecha").item(0).getTextContent(),
                        new Asignatura(
                                Integer.parseInt(asignatura.getElementsByTagName("id").item(0).getTextContent()),
                                asignatura.getElementsByTagName("nombre").item(0).getTextContent(),
                                Integer.parseInt(asignatura.getElementsByTagName("creditos").item(0).getTextContent())
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

            NodeList lista = doc.getElementsByTagName("matricula");
            for (int i = 0; i < lista.getLength(); i++) {
                Element matricula = (Element) lista.item(i);
                if (matricula.getElementsByTagName("id").item(0).getTextContent().equals(String.valueOf(model.getId()))) {
                    matricula.getElementsByTagName("nota").item(0).setTextContent(String.valueOf(((Matricula)model).getNota()));
                    matricula.getElementsByTagName("fecha").item(0).setTextContent(((Matricula)model).getFecha());

                    Element asignatura = (Element) matricula.getElementsByTagName("asignatura").item(0);
                    asignatura.getElementsByTagName("id").item(0).setTextContent(String.valueOf(((Matricula)model).getAsignatura().getId()));
                    asignatura.getElementsByTagName("nombre").item(0).setTextContent(((Matricula)model).getAsignatura().getNombre());
                    asignatura.getElementsByTagName("creditos").item(0).setTextContent(String.valueOf(((Matricula)model).getAsignatura().getCreditos()));


                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(uri));
                    transformer.transform(source, result);
                    return true;
                }
            }
            return false;
        } catch (TransformerException | ParserConfigurationException e) {
            return false;
        }
    }
}
