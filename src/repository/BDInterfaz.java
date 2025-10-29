package repository;

import model.Model;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class BDInterfaz {
    protected String uri;

    public abstract BDInterfaz get(String uri);
    public abstract Model find(int id);
    public abstract ArrayList<Model> findAll();
    public abstract boolean insert(Model model);
    public abstract boolean update(Model model);
    public abstract boolean delete(int id);
}
