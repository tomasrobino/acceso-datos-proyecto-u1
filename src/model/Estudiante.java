package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Estudiante extends Model {
    private final String nombre;
    private String email;
    private final List<Matricula> matriculas;

    public Estudiante(int id, String nombre, String email, List<Matricula> matriculas, String xmlName) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.matriculas = matriculas;
        this.xmlName = xmlName;
    }

    public Estudiante(int id, String nombre, String email, String xmlName) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.matriculas = new ArrayList<>();
        this.xmlName = xmlName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estudiante Estudiante = (Estudiante) o;
        return Objects.equals(id, Estudiante.id) && Objects.equals(nombre, Estudiante.nombre);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
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

    public void addMatricula(Matricula matricula) {
        this.matriculas.add(matricula);
    }

    public boolean removeMatricula(Matricula matricula) {
        return this.matriculas.remove(matricula);
    }

    @Override
    public String stringifyCSV() {
        return "";
    }

    @Override
    public Model unstringifyCSV(String string) {
        return null;
    }

    @Override
    public String stringifyXML() {
        return "";
    }

    @Override
    public Model unstringifyXML(String string) {
        return null;
    }
}
