package model;

public class Matricula extends Model {
    private final double nota;
    private final String fecha;
    private Asignatura asignatura;

    public Matricula(int id, double nota, String fecha, Asignatura asignatura) {
        this.id = id;
        this.nota = nota;
        this.fecha = fecha;
        this.asignatura = asignatura;
    }

    public double getNota() {
        return nota;
    }

    public String getFecha() {
        return fecha;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }
    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    @Override
    public String stringifyCSV() {
        return id+","+nota+","+fecha+","+asignatura.getId();
    }

}
