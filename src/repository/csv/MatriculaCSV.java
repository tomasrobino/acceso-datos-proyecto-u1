package repository.csv;

import model.Asignatura;
import model.Matricula;
import model.Model;
import repository.BDInterfaz;

import java.io.*;
import java.util.ArrayList;

public class MatriculaCSV extends CSV {
    private static MatriculaCSV miMatriculaCSV = null;

    public BDInterfaz get(String uri) {
        if (miMatriculaCSV == null) {
            miMatriculaCSV = new MatriculaCSV(uri);
        }
        return miMatriculaCSV;
    }

    public MatriculaCSV(String uri) {
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
                    return new Matricula(id, Double.parseDouble(data[1]), data[2], new Asignatura(Integer.parseInt(data[3])));
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
                lista.add(new Matricula(Integer.parseInt(data[0]), Double.parseDouble(data[1]), data[2], new Asignatura(Integer.parseInt(data[3]))));
            }
            br.close();
            return lista;
        } catch (IOException e) {
            return null;
        }
    }
}
