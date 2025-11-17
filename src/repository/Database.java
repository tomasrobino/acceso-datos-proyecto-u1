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

    public ArrayList<Model> findAll() {

    }

    public boolean insert(Model model) {

    }

    public boolean update(Model model) {

    }

    public boolean delete(int id) {

    }

    public String getUri() {
        return uri;
    }
}
