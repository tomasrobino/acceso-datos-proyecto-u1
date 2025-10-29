package service;

import model.Asignatura;
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
        ArrayList<Asignatura> asignaturas = new ArrayList<>();
        ArrayList<?> modelos = repository.findAll();

        if (modelos != null) {
            for (Object modelo : modelos) {
                if (modelo instanceof Asignatura) {
                    asignaturas.add((Asignatura) modelo);
                }
            }
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