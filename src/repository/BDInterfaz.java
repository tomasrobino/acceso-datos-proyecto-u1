package repository;

import model.Asignatura;
import model.Estudiante;
import model.Matricula;

public interface BDInterfaz {
    Object find(int id);
    boolean insert(Matricula matricula);
    boolean insert(Estudiante estudiante);
    boolean insert(Asignatura asignatura);
    boolean update(Matricula matricula);
    boolean update(Estudiante estudiante);
    boolean update(Asignatura asignatura);
    boolean delete(int id);
}
