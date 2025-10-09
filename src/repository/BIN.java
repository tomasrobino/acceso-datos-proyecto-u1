package repository;

import model.*;

import java.io.File;

public class BIN extends BDInterfaz {
    public BIN(File archivo) {

    }

    @Override
    public String[] find(int id) {
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
}
