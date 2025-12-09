package repository;

import model.Profesor;

import java.util.ArrayList;

public class ProfesorRepository  extends Database<Profesor, Integer>{
    @Override
    public Profesor find(Integer id) {
        return null;
    }

    @Override
    public ArrayList<Profesor> findAll() {
        return null;
    }

    @Override
    public boolean insert(Profesor model) {
        return false;
    }

    @Override
    public boolean update(Profesor model) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
