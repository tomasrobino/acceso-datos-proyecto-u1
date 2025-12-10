package model;

import java.util.ArrayList;

public class Profesor extends Model {
    private String nombre;
    private String especialidad;
    private ArrayList<Clase> clases;

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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public ArrayList<Clase> getClases() {
        return clases;
    }

    public void setClases(ArrayList<Clase> clases) {
        this.clases = clases;
    }
}