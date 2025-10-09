package repository;

import model.Asignatura;
import model.Estudiante;
import model.Matricula;
import model.Model;

public interface BDInterfaz {
    Model find(int id);
    boolean insert(Matricula matricula);
    boolean insert(Estudiante estudiante);
    boolean insert(Asignatura asignatura);
    boolean update(Matricula matricula);
    boolean update(Estudiante estudiante);
    boolean update(Asignatura asignatura);
    boolean delete(int id);
}
