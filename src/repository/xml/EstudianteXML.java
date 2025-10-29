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

public class EstudianteXML extends XML {
    private static EstudianteXML miEstudianteXML;

    public BDInterfaz get(String uri) {
        if (miEstudianteXML == null) {
            miEstudianteXML = new EstudianteXML(uri);
        }
        return miEstudianteXML;
    }

    private EstudianteXML(String uri) {
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
                    return new Estudiante(
                            Integer.parseInt(item.getNamedItem("id").getTextContent()),
                            item.getNamedItem("nombre").getTextContent(),
                            item.getNamedItem("email").getTextContent()
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
            ArrayList<Model> listaEstudiantes = new ArrayList<>();
            for (int i = 0; i < lista.getLength(); i++) {
                NamedNodeMap item = lista.item(i).getAttributes();
                listaEstudiantes.add(new Estudiante(
                        Integer.parseInt(item.getNamedItem("id").getTextContent()),
                        item.getNamedItem("nombre").getTextContent(),
                        item.getNamedItem("email").getTextContent()
                ));
            }
            return listaEstudiantes;
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
            Element estudiante = doc.createElement("estudiante");
            estudiante.appendChild(doc.createElement("id").appendChild(
                    doc.createTextNode( String.valueOf(model.getId()) )
            ));
            estudiante.appendChild(doc.createElement("nombre").appendChild(
                    doc.createTextNode(((Estudiante)model).getNombre())
            ));
            estudiante.appendChild(doc.createElement("email").appendChild(
                    doc.createTextNode(((Estudiante)model).getEmail())
            ));

            ArrayList<Matricula> listaMatriculas = ((Estudiante)model).getMatriculas();
            Element nodoMatriculas = (Element) estudiante.appendChild(doc.createElement("matriculas"));

            for (int i = 0; i < listaMatriculas.size(); i++) {
                Element matricula = (Element) nodoMatriculas.appendChild(doc.createElement("matricula"));
                matricula.appendChild(doc.createElement("id").appendChild(
                        doc.createTextNode( String.valueOf(listaMatriculas.get(i).getId()) )
                ));
                matricula.appendChild(doc.createElement("nota").appendChild(
                        doc.createTextNode(String.valueOf(listaMatriculas.get(i).getNota())) )
                );
                matricula.appendChild(doc.createElement("fecha").appendChild(
                        doc.createTextNode( listaMatriculas.get(i).getFecha() )
                ));
                Element asignatura = (Element) matricula.appendChild(doc.createElement("asignatura"));
                asignatura.appendChild(doc.createElement("id").appendChild(
                        doc.createTextNode( String.valueOf(listaMatriculas.get(i).getAsignatura().getId()) )
                ));
                asignatura.appendChild(doc.createElement("nombre").appendChild(
                        doc.createTextNode(listaMatriculas.get(i).getAsignatura().getNombre())
                ));
                asignatura.appendChild(doc.createElement("creditos").appendChild(
                        doc.createTextNode(String.valueOf(listaMatriculas.get(i).getAsignatura().getCreditos()))
                ));
            }

            nodoRaiz.appendChild(estudiante);

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
