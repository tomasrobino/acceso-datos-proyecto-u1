package repository;

import model.Asignatura;
import model.Estudiante;
import model.Matricula;
import model.Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSV implements BDInterfaz{
    private final String uri;
    private static CSV miCSV = null;
    private final BufferedReader br;

    public static CSV getCSV(String uri) throws FileNotFoundException {
        if (miCSV == null) {
            miCSV = new CSV(uri);
        }
        return miCSV;
    }

    private CSV(String uri) throws FileNotFoundException {
        this.uri = uri;
        br = new BufferedReader(new FileReader(uri));
    }

    @Override
    public Model find(int id) {
        return null;
    }

    @Override
    public boolean insert(Matricula matricula) {
        return false;
    }

    @Override
    public boolean insert(Estudiante estudiante) {
        return false;
    }

    @Override
    public boolean insert(Asignatura asignatura) {
        return false;
    }

    @Override
    public boolean update(Matricula matricula) {
        return false;
    }

    @Override
    public boolean update(Estudiante estudiante) {
        return false;
    }

    @Override
    public boolean update(Asignatura asignatura) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    public boolean destroy() {
        try {
            br.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String getUri() {
        return uri;
    }
}
