package repository.csv;

import model.Asignatura;
import model.Model;
import repository.BDInterfaz;

import java.io.*;
import java.util.ArrayList;

public class AsignaturaCSV extends CSV {
    private static AsignaturaCSV miAsignaturaCSV = null;

    public BDInterfaz get(String uri) {
        if (miAsignaturaCSV == null) {
            miAsignaturaCSV = new AsignaturaCSV(uri);
        }
        return miAsignaturaCSV;
    }

    public AsignaturaCSV(String uri) {
        this.uri = uri;
    }

    @Override
    public Model find(int id) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(uri));
            String line;
            while ( (line = br.readLine()) != null ) {
                line = line.substring(0, line.length()-1);
                String[] data = line.split(",");
                if (Integer.parseInt(data[0]) == id) {
                    br.close();
                    return new Asignatura(id, data[1], Integer.parseInt(data[2]));
                }
            }
            br.close();
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public ArrayList<Model> findAll() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(uri));
            String line;
            ArrayList<Model> lista = new ArrayList<>();
            while ( (line = br.readLine()) != null ) {
                line = line.substring(0, line.length()-1);
                String[] data = line.split(",");
                lista.add(new Asignatura(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2])));
            }
            br.close();
            return lista;
        } catch (IOException e) {
            return null;
        }
    }
}
