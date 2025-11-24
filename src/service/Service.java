package service;

import model.Model;
import repository.Database;
import java.util.ArrayList;

public class Service<T> {
    private final Database<T, Integer> repository;

    public Service(Database<T, Integer> repository) {
        this.repository = repository;
    }

    public T buscarPorId(int id) {
        return repository.find(id);
    }

    public ArrayList<T> listarTodas() {
        ArrayList<T> modelos = repository.findAll();
        ArrayList<T> Ts = new ArrayList<>();
        if (modelos == null) {
            return Ts;
        }
        for (int i = 0; i < modelos.size(); i++) {
            Ts.add(i, modelos.get(i));
        }
        return Ts;
    }

    public boolean crear(T model) {
        if (model == null) {
            return false;
        }
        return repository.insert(model);
    }

    public boolean actualizar(T model) {
        if (model == null || ((Model)model).getId() <= 0) {
            return false;
        }

        T existente = repository.find(((Model)model).getId());
        if (existente == null) {
            return false;
        }

        return repository.update(model);
    }

    public boolean eliminar(int id) {
        if (id <= 0) {
            return false;
        }
        return repository.delete(id);
    }
}
