package repository;

import model.Model;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class BDInterfaz {
    protected String uri;

    abstract BDInterfaz get(String uri);
    abstract Model find(int id);
    abstract ArrayList<Model> findAll();
    abstract boolean insert(Model model);
    abstract boolean update(Model model);
    abstract boolean delete(int id);
}
