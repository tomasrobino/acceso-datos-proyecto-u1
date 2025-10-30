package model;

public class Asignatura extends Model {
    private final String nombre;
    private final int creditos;

    public Asignatura(int id, String nombre, int creditos) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
    }

    public Asignatura(int id) {
        this.id = id;
        this.nombre = null;
        this.creditos = 0;
    }

    @Override
    public String stringifyCSV() {
        return id+","+nombre+","+creditos;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCreditos() {
        return creditos;
    }
}
