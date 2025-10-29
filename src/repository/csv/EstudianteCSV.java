package repository.csv;

import model.Estudiante;
import model.Model;
import repository.BDInterfaz;

import java.io.*;
import java.util.ArrayList;

public class EstudianteCSV extends CSV {
    private static EstudianteCSV miEstudianteCSV = null;

    public BDInterfaz get(String uri) {
        if (miEstudianteCSV == null) {
            miEstudianteCSV = new EstudianteCSV(uri);
        }
        return miEstudianteCSV;
    }

    private EstudianteCSV(String uri) {
        this.uri = uri;
    }

    @Override
    public Model find(int id) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(uri));
            String line;
            while ( (line = br.readLine()) != null ) {
                String[] data = line.split(",");
                if (Integer.parseInt(data[0]) == id) {
                    br.close();
                    return new Estudiante(id, data[1], data[2], data[3]);
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
                String[] data = line.split(",");
                lista.add(new Estudiante(Integer.parseInt(data[0]), data[1], data[2], data[3]));
            }
            br.close();
            return lista;
        } catch (IOException e) {
            return null;
        }
    }
}
