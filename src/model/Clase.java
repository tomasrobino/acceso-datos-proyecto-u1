package model;

import java.util.ArrayList;

public class Clase extends Model {
    private String nombre;
    private String horario;
    private ArrayList<Profesor> profesores;

    public Clase(String nombre, String horario, ArrayList<Profesor> profesores) {
        this.nombre = nombre;
        this.horario = horario;
        this.profesores = profesores;
    }

    public Clase(int id, String nombre, String horario, ArrayList<Profesor> profesores) {
        this.id = id;
        this.nombre = nombre;
        this.horario = horario;
        this.profesores = profesores;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public ArrayList<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(ArrayList<Profesor> profesores) {
        this.profesores = profesores;
    }
}