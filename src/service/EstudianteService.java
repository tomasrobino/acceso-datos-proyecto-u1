package service;

import model.Estudiante;
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
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        ArrayList<?> modelos = repository.findAll();

        if (modelos != null) {
            for (Object modelo : modelos) {
                if (modelo instanceof Estudiante) {
                    estudiantes.add((Estudiante) modelo);
                }
            }
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