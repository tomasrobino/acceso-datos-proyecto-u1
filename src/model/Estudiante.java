package model;

import java.util.ArrayList;
import java.util.Objects;

public class Estudiante extends Model {
    private final String nombre;
    private final String email;
    private ArrayList<Matricula> matriculas;

    public Estudiante(int id, String nombre, String email, ArrayList<Matricula> matriculas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.matriculas = matriculas;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Matricula> getMatriculas() {
        return matriculas;
    }
    public void setMatriculas(ArrayList<Matricula> matriculas) {
        this.matriculas = matriculas;
    }
}
