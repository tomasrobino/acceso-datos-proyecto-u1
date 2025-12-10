package model;

import java.util.ArrayList;

public class Profesor extends Model {
    private final String nombre;
    private final String especialidad;
    private final ArrayList<Clase> clases;

    public Profesor(String nombre, String especialidad, ArrayList<Clase> clases) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.clases = clases;
    }

    public Profesor(int id, String nombre, String especialidad, ArrayList<Clase> clases) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.clases = clases;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public ArrayList<Clase> getClases() {
        return clases;
    }
}