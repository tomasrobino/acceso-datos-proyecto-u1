package repository;

import model.Clase;

import java.util.ArrayList;

public class ClaseRepository  extends Database<Clase, Integer>{
    @Override
    public Clase find(Integer id) {
        return null;
    }

    @Override
    public ArrayList<Clase> findAll() {
        return null;
    }

    @Override
    public boolean insert(Clase model) {
        return false;
    }

    @Override
    public boolean update(Clase model) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
