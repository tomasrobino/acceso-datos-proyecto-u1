package model;

import java.io.InputStream;
import java.util.ArrayList;

public class Estudiante extends Model {
    private final String nombre;
    private final String email;
    private final InputStream foto;
    private final ArrayList<Matricula> matriculas;

    public Estudiante(String nombre, String email, InputStream foto, ArrayList<Matricula> matriculas) {
        this.nombre = nombre;
        this.email = email;
        this.foto = foto;
        this.matriculas = matriculas;
    }

    public Estudiante(int id, String nombre, String email, InputStream foto, ArrayList<Matricula> matriculas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.foto = foto;
        this.matriculas = matriculas;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public InputStream getFoto() {
        return foto;
    }

    public ArrayList<Matricula> getMatriculas() {
        return matriculas;
    }
}