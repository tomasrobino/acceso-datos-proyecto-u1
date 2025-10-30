package service;

import model.Asignatura;
import model.Model;
import repository.BDInterfaz;

import java.util.ArrayList;

public class AsignaturaService {
    private final BDInterfaz repository;

    public AsignaturaService(BDInterfaz repository) {
        this.repository = repository;
    }

    public Asignatura buscarPorId(int id) {
        return (Asignatura) repository.find(id);
    }

    public ArrayList<Asignatura> listarTodas() {
        ArrayList<Model> modelos = repository.findAll();
        ArrayList<Asignatura> asignaturas = new ArrayList<>();
        if (modelos == null) {
            return asignaturas;
        }        for (int i = 0; i < modelos.size(); i++) {
            asignaturas.set(i, (Asignatura) modelos.get(i));
        }
        return asignaturas;
    }

    public boolean crear(Asignatura asignatura) {
        if (asignatura == null) {
            return false;
        }
        return repository.insert(asignatura);
    }

    public boolean actualizar(Asignatura asignatura) {
        if (asignatura == null || asignatura.getId() <= 0) {
            return false;
        }

        Asignatura existente = (Asignatura) repository.find(asignatura.getId());
        if (existente == null) {
            return false;
        }

        return repository.update(asignatura);
    }

    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }
        return repository.delete(id);
    }
}