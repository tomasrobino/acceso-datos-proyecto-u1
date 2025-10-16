package repository;

import model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
    }

    @Override
    boolean update(Model model) throws IOException {
        File file1 = new File(uri);
        File file2 = new File(uri+"_temp");
        BufferedReader br = new BufferedReader(new FileReader(file1));
        BufferedWriter bw = new BufferedWriter(new FileWriter(file2));
        String line;
        String buffer = "";
        boolean ret = false;
        while ( (line = br.readLine()) != null ) {
            if (line.contains("<"+model.getXmlName()+">") || !buffer.isEmpty()) {
                buffer += line;
            }

            if (line.contains("</"+model.getXmlName()+">")) {
                // Having read the whole entry into buffer, find id tag
                int index = Integer.parseInt(buffer.substring(buffer.indexOf("<id>") + 4, buffer.indexOf("</id>")));
                if (index == model.getId()) {
                    bw.write(model.stringifyXML());
                    ret = true;
                } else {
                    bw.write(buffer);
                }

                buffer = "";
            }
        }
        br.close();
        bw.close();
        if (!file1.delete() || !file2.renameTo(file1)) throw new IOException();
        return ret;
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
