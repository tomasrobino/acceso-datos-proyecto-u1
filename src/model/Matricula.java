package model;

import java.util.Date;

public class Matricula {
    private final double nota;
    private Date fecha;
    private Asignatura asignatura;

    public Matricula(double nota, Date fecha, Asignatura asignatura) {
        this.nota = nota;
        this.fecha = fecha;
        this.asignatura = asignatura;
    }
}
