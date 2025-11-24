package repository;

import model.Estudiante;

import java.util.ArrayList;

public class EstudianteRepository extends Database<Estudiante, Integer>{

    @Override
    public Estudiante find(Integer id) {
        return null;
    }

    @Override
    public ArrayList<Estudiante> findAll() {
        return null;
    }

    @Override
    public boolean insert(Estudiante model) {
        return false;
    }

    @Override
    public boolean update(Estudiante model) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
