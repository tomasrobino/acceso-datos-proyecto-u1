package model;

import java.util.Date;

public class Matricula extends Model {
    private double nota;
    private final Date fecha;
    private final Asignatura asignatura;

    public Matricula(double nota, Date fecha, Asignatura asignatura, String xmlName) {
        this.nota = nota;
        this.fecha = fecha;
        this.asignatura = asignatura;
        this.xmlName = xmlName;
    }

    public int getId() {
        return id;
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

    @Override
    public String stringifyCSV() {
        return "";
    }

    @Override
    public String stringifyXML() {
        return "";
    }
}
