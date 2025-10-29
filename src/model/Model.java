package model;

import java.io.Serializable;

public abstract class Model implements Serializable {
    protected int id;

    public abstract String stringifyCSV();
    public int getId() {
        return id;
    }
}
