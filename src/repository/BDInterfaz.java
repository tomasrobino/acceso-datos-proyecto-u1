package repository;

import model.Model;

import java.io.Serializable;

public abstract class BDInterfaz implements Serializable {
    protected String uri;

    abstract BDInterfaz get(String uri);
    abstract String find(int id);
    abstract boolean insert(Model model);
    abstract boolean update(Model model);
    abstract boolean delete(int id);
}
