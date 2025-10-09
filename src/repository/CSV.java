package repository;

import model.Asignatura;
import model.Estudiante;
import model.Matricula;

import java.io.FileNotFoundException;

public class CSV extends BDInterfaz {
    private static CSV miCSV = null;

    public static CSV getCSV(String uri) {
        if (miCSV == null) {
            miCSV = new CSV(uri);
        }
        return miCSV;
    }

    private CSV(String uri) {
        this.uri = uri;
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

    public String getUri() {
        return uri;
    }
}
