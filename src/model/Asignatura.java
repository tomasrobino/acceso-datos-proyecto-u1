package model;

public class Asignatura extends Model {
    private String nombre;
    private int creditos;

    public Asignatura(int id, String nombre, int creditos) {
        this.id = id;
        this.nombre = nombre;
        this.creditos = creditos;
    }

    @Override
    public String stringifyCSV() {
        return id+","+nombre+","+"creditos";
    }

    @Override
    public String stringifyXML() {
        return "<"+xmlName+">\n\t<id>"+id+"</id>\n\t<nombre>"+nombre+"</nombre>\n\t<creditos>"+creditos+"</creditos>\n</"+xmlName+">";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
}
