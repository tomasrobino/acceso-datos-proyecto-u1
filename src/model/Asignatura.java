package model;

public class Asignatura {
    private final int id;
    private String nombre;
    private int creditos;

    public Asignatura(int id, String nombre, int creditos) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
    }
}
