package repository;

import model.*;

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
        BufferedReader br = new BufferedReader(new FileReader(uri));
        String line;
        String buffer = "";
        while ( (line = br.readLine()) != null ) {
            if (line.contains("<id>") || !buffer.isEmpty()) {
                buffer += line;
            }

            if (line.contains("</id>")) {
                // Procesar y vaciar buffer
                int start = 0;
                int end = 0;
                for (int i = 0; i < buffer.length(); i++) {
                    if (buffer.charAt(i) == '>' && start == 0) {
                        start = i+1;
                    } else if (buffer.charAt(i) == '<' && start != 0) {
                        end = i;
                    }
                }
                String sub = buffer.substring(start, end);
                int index = Integer.parseInt(sub);
                if (index == id) {
                    br.close();
                    return sub;
                };
                buffer = "";
            }
        }
        br.close();
        return null;
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
        while ( (line = br.readLine()) != null ) {
            if (line.contains("<"+model.getXmlName()+">") || !buffer.isEmpty()) {
                buffer += line;
            }

            if (line.contains("</"+model.getXmlName()+">")) {
                // Having read the whole entry into buffer, find id tag
                int index = Integer.parseInt(buffer.substring(buffer.indexOf("<id>") + 4, buffer.indexOf("</id>")));
                if (index == model.getId()) {
                    bw.write(model.stringifyXML());
                } else {
                    bw.write(buffer);
                }
            }
        }
        br.close();
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
