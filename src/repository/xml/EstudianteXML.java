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
                Element item = (Element) lista.item(i);
                if (Integer.parseInt(item.getElementsByTagName("id").item(0).getTextContent()) == id ) {
                    Element matriculas = (Element) item.getElementsByTagName("matriculas").item(0);
                    ArrayList<Matricula> listaMatriculas = new ArrayList<>();
                    for (int j = 0; j < matriculas.getChildNodes().getLength(); j++) {
                        Element matricula = (Element) matriculas.getChildNodes().item(j);
                        Element asignatura = (Element) matricula.getElementsByTagName("asignatura").item(0);
                        listaMatriculas.add(new Matricula(
                                Integer.parseInt(matricula.getElementsByTagName("id").item(0).getTextContent()),
                                Double.parseDouble(matricula.getElementsByTagName("nota").item(0).getTextContent()),
                                matricula.getElementsByTagName("fecha").item(0).getTextContent(),
                                new Asignatura(
                                        Integer.parseInt(asignatura.getElementsByTagName("id").item(0).getTextContent()),
                                        asignatura.getElementsByTagName("nombre").item(0).getTextContent(),
                                        Integer.parseInt(asignatura.getElementsByTagName("creditos").item(0).getTextContent())
                                )
                        ));
                    }

                    return new Estudiante(
                            Integer.parseInt(item.getElementsByTagName("id").item(0).getTextContent()),
                            item.getElementsByTagName("nombre").item(0).getTextContent(),
                            item.getElementsByTagName("email").item(0).getTextContent(),
                            listaMatriculas
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
                Element item = (Element) lista.item(i);
                Element matriculas = (Element) item.getElementsByTagName("matriculas").item(0);
                ArrayList<Matricula> listaMatriculas = new ArrayList<>();
                for (int j = 0; j < matriculas.getChildNodes().getLength(); j++) {
                    Element matricula = (Element) matriculas.getChildNodes().item(j);
                    Element asignatura = (Element) matricula.getElementsByTagName("asignatura").item(0);
                    listaMatriculas.add(new Matricula(
                            Integer.parseInt(matricula.getElementsByTagName("id").item(0).getTextContent()),
                            Double.parseDouble(matricula.getElementsByTagName("nota").item(0).getTextContent()),
                            matricula.getElementsByTagName("fecha").item(0).getTextContent(),
                            new Asignatura(
                                    Integer.parseInt(asignatura.getElementsByTagName("id").item(0).getTextContent()),
                                    asignatura.getElementsByTagName("nombre").item(0).getTextContent(),
                                    Integer.parseInt(asignatura.getElementsByTagName("creditos").item(0).getTextContent())
                            )
                    ));
                }

                listaEstudiantes.add(new Estudiante(
                        Integer.parseInt(item.getElementsByTagName("id").item(0).getTextContent()),
                        item.getElementsByTagName("nombre").item(0).getTextContent(),
                        item.getElementsByTagName("email").item(0).getTextContent(),
                        listaMatriculas
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

            Node nodoRaiz = doc.getDocumentElement();
            NodeList lista = nodoRaiz.getChildNodes();
            for (int i = 0; i < lista.getLength(); i++) {
                Element estudiante = (Element) lista.item(i);
                if (Integer.parseInt(estudiante.getElementsByTagName("id").item(0).getTextContent()) == model.getId() ) {
                    estudiante.getElementsByTagName("nombre").item(0).setTextContent(((Estudiante)model).getNombre());
                    estudiante.getElementsByTagName("email").item(0).setTextContent(((Estudiante)model).getEmail());
                    Element matriculas = (Element) estudiante.getElementsByTagName("matriculas").item(0);
                    ArrayList<Matricula> listaMatriculas = ((Estudiante)model).getMatriculas();
                    for (int j = 0; j < listaMatriculas.size(); j++) {
                        Element matricula = (Element) matriculas.getElementsByTagName("matricula").item(j);
                        matricula.getElementsByTagName("nota").item(0).setTextContent(String.valueOf(listaMatriculas.get(j).getNota()));
                        matricula.getElementsByTagName("fecha").item(0).setTextContent(listaMatriculas.get(j).getFecha());
                        Element asignatura = (Element) matricula.getElementsByTagName("asignatura").item(0);
                        asignatura.getElementsByTagName("id").item(0).setTextContent(String.valueOf(listaMatriculas.get(j).getAsignatura().getId()));
                        asignatura.getElementsByTagName("nombre").item(0).setTextContent(listaMatriculas.get(j).getAsignatura().getNombre());
                        asignatura.getElementsByTagName("creditos").item(0).setTextContent(String.valueOf(listaMatriculas.get(j).getAsignatura().getCreditos()));
                    }

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
