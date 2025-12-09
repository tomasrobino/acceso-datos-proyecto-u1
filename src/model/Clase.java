package model;

import java.util.ArrayList;

public class Clase extends Model {
    private final String nombre;
    private final String horario;
    private final ArrayList<Profesor> profesores;

    public Clase(int id, String nombre, String horario, ArrayList<Profesor> profesores) {
        this.id = id;
        this.nombre = nombre;
        this.horario = horario;
        this.profesores = profesores;
    }

    public String getNombre() {
        return nombre;
    }

    public String getHorario() {
        return horario;
    }

    public ArrayList<Profesor> getProfesores() {
        return profesores;
    }
}