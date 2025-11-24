package model;

public abstract class Model {
    protected int id;

    public abstract String stringifyCSV();
    public int getId() {
        return id;
    }
}
