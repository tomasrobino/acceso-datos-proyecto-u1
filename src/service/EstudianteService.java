package service;

import model.Estudiante;
import model.Model;
import repository.BDInterfaz;

import java.util.ArrayList;

public class EstudianteService {
    private final BDInterfaz repository;

    public EstudianteService(BDInterfaz repository) {
        this.repository = repository;
    }

    public Estudiante buscarPorId(int id) {
        return (Estudiante) repository.find(id);
    }

    public ArrayList<Estudiante> listarTodas() {
        ArrayList<Model> modelos = repository.findAll();
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        for (int i = 0; i < modelos.size(); i++) {
            estudiantes.set(i, (Estudiante) modelos.get(i));
        }
        return estudiantes;
    }

    public boolean crear(Estudiante estudiante) {
        if (estudiante == null) {
            return false;
        }
        return repository.insert(estudiante);
    }

    public boolean actualizar(Estudiante estudiante) {
        if (estudiante == null || estudiante.getId() <= 0) {
            return false;
        }

        Estudiante existente = (Estudiante) repository.find(estudiante.getId());
        if (existente == null) {
            return false;
        }

        return repository.update(estudiante);
    }

    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }
        return repository.delete(id);
    }
}