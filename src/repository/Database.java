package repository;

import java.util.ArrayList;

public abstract class Database<T, K> implements DatabaseInterface<T, K> {
    static protected final String uri = "jdbc:mariadb://localhost:3306/proyecto_u2";
    static protected final String usuario = "root";
    static protected final String password = "";

    @Override
    public abstract T find(K id);
    @Override
    public abstract ArrayList<T> findAll();
    @Override
    public abstract boolean insert(T model);
    @Override
    public abstract boolean update(T model);
    @Override
    public abstract boolean delete(K id);
}
