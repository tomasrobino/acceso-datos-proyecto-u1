package repository;

import model.Matricula;

import java.util.ArrayList;

public class MatriculaRepository extends Database<Matricula, Integer> {
    @Override
    public Matricula find(Integer id) {
        return null;
    }

    @Override
    public ArrayList<Matricula> findAll() {
        return null;
    }

    @Override
    public boolean insert(Matricula model) {
        return false;
    }

    @Override
    public boolean update(Matricula model) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
