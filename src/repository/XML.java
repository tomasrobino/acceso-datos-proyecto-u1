package repository;

import model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    public String[] find(int id) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(uri));
        String line;
        while ( (line = br.readLine()) != null ) {
            if (line == "<"++">")
        }
        br.close();
        return null;
    }

    @Override
    void insert(Model model) throws IOException {

    }

    @Override
    boolean update(Model model) throws IOException {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
