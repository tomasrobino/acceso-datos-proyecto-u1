package service;

import model.Matricula;
import model.Model;
import repository.BDInterfaz;

import java.util.ArrayList;

public class MatriculaService {
    private final BDInterfaz repository;

    public MatriculaService(BDInterfaz repository) {
        this.repository = repository;
    }

    public Matricula buscarPorId(int id) {
        return (Matricula) repository.find(id);
    }

    public ArrayList<Matricula> listarTodas() {
        ArrayList<Model> modelos = repository.findAll();
        ArrayList<Matricula> matriculas = new ArrayList<>();
        if (modelos == null) {
            return matriculas;
        }
        for (int i = 0; i < modelos.size(); i++) {
            matriculas.set(i, (Matricula) modelos.get(i));
        }
        return matriculas;
    }

    public boolean crear(Matricula matricula) {
        if (matricula == null) {
            return false;
        }
        return repository.insert(matricula);
    }

    public boolean actualizar(Matricula matricula) {
        if (matricula == null || matricula.getId() <= 0) {
            return false;
        }

        Matricula existente = (Matricula) repository.find(matricula.getId());
        if (existente == null) {
            return false;
        }

        return repository.update(matricula);
    }

    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }
        return repository.delete(id);
    }
}