package repository;

import model.Asignatura;
import model.Estudiante;
import model.Matricula;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class BDInterfaz {
    protected String uri;

    protected String[] find(int id) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(uri));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] data = line.split(",");
            if (Integer.parseInt(data[0]) == id) {
                br.close();
                return data;
            }
        }
        br.close();
        return null;
    }
    abstract boolean insert(Matricula matricula);
    abstract boolean insert(Estudiante estudiante);
    abstract boolean insert(Asignatura asignatura);
    abstract boolean update(Matricula matricula);
    abstract boolean update(Estudiante estudiante);
    abstract boolean update(Asignatura asignatura);
    abstract boolean delete(int id);
}
