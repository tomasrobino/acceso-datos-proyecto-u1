package model;

import java.util.Date;

public class Matricula implements Model {
    private double nota;
    private final Date fecha;
    private final Asignatura asignatura;

    public Matricula(double nota, Date fecha, Asignatura asignatura) {
        this.nota = nota;
        this.fecha = fecha;
        this.asignatura = asignatura;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public Date getFecha() {
        return fecha;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }
}
