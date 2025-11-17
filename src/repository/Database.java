package repository;

import model.Model;
import java.util.ArrayList;

public class Database<T> {
    private final String uri;

    public Database(String uri) {
        this.uri = uri;
    }

    public Model find(int id) {

    }

    public ArrayList<T> findAll() {

    }

    public boolean insert(T model) {

    }

    public boolean update(T model) {

    }

    public boolean delete(int id) {

    }

    public String getUri() {
        return uri;
    }
}
