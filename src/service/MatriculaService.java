package service;

import model.Matricula;
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
        ArrayList<Matricula> matriculas = new ArrayList<>();
        ArrayList<?> modelos = repository.findAll();

        if (modelos != null) {
            for (Object modelo : modelos) {
                if (modelo instanceof Matricula) {
                    matriculas.add((Matricula) modelo);
                }
            }
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