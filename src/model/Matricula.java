package model;

public class Matricula extends Model {
    private final double nota;
    private final String fecha;

    public Matricula(int id, double nota, String fecha) {
        this.id = id;
        this.nota = nota;
        this.fecha = fecha;
    }

    public double getNota() {
        return nota;
    }

    public String getFecha() {
        return fecha;
    }

    @Override
    public String stringifyCSV() {
        return id+","+nota+","+fecha;
    }

}
