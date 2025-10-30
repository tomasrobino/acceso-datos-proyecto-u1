package service;

import model.Asignatura;
import model.Matricula;
import model.Model;
import repository.BDInterfaz;

import java.util.ArrayList;

public class MatriculaService {
    private final BDInterfaz repository;
    private final BDInterfaz asignaturaRepository;

    public MatriculaService(BDInterfaz repository, BDInterfaz asignaturaRepository) {
        this.repository = repository;
        this.asignaturaRepository = asignaturaRepository;
    }

    public Matricula buscarPorId(int id) {
        Matricula matricula = (Matricula) repository.find(id);
        matricula.setAsignatura((Asignatura) asignaturaRepository.find(matricula.getAsignatura().getId()));
        return matricula;
    }

    public ArrayList<Matricula> listarTodas() {
        ArrayList<Model> modelos = repository.findAll();
        ArrayList<Matricula> matriculas = new ArrayList<>();
        if (modelos == null) {
            return matriculas;
        }
        for (int i = 0; i < modelos.size(); i++) {
            matriculas.add(i, (Matricula) modelos.get(i));
            matriculas.get(i).setAsignatura((Asignatura) asignaturaRepository.find(matriculas.get(i).getAsignatura().getId()));
        }
        return matriculas;
    }

    public boolean crear(Matricula matricula) {
        if (matricula == null) {
            return false;
        }
        return repository.insert(matricula) && asignaturaRepository.update(matricula.getAsignatura());
    }

    public boolean actualizar(Matricula matricula) {
        if (matricula == null || matricula.getId() <= 0) {
            return false;
        }

        Matricula existente = (Matricula) repository.find(matricula.getId());
        if (existente == null) {
            return false;
        }

        return repository.update(matricula) && asignaturaRepository.update(matricula.getAsignatura());
    }

    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }
        return repository.delete(id);
    }
}