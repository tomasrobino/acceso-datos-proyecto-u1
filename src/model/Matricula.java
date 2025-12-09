package model;

public class Matricula extends Model {
    private final double nota;
    private final String fecha;
    private final int estudiante_id;

    public Matricula(int id, double nota, String fecha, int estudiante_id) {
        this.id = id;
        this.nota = nota;
        this.fecha = fecha;
        this.estudiante_id = estudiante_id;
    }

    public double getNota() {
        return nota;
    }

    public String getFecha() {
        return fecha;
    }

    public int getEstudiante_id() {
        return estudiante_id;
    }
}
