package repository;

import model.Model;

import java.io.*;

public abstract class BDInterfaz {
    protected String uri;

    abstract String find(int id);
    abstract void insert(Model model) throws IOException;
    abstract boolean update(Model model);
    abstract boolean delete(int id);
}
