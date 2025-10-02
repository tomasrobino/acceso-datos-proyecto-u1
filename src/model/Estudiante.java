package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Estudiante {
    private final int id;
    private String nombre;
    private String email;
    private final List<Matricula> matriculas;

    public Estudiante(int id, String nombre, String email, List<Matricula> matriculas) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.matriculas = matriculas;
    }

    public Estudiante(int id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.matriculas = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estudiante Estudiante = (Estudiante) o;
        return Objects.equals(id, Estudiante.id) &&
                Objects.equals(nombre, Estudiante.nombre);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void addMatriculas(Matricula matricula) {
        this.matriculas.add(matricula);
    }

    public void removeMatriculas(Matricula matricula) {
        this.matriculas.remove(matricula);
    }
}
