package repository;

import model.*;

import java.io.File;

public class BIN implements BDInterfaz{
    public BIN(File archivo, TiposModelo tipoModelo) {

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
}
