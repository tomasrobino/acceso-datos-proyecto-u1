package repository;

import java.util.List;

public interface DatabaseInterface<T, K> {
    T find(K id);
    List<T> findAll();
    boolean insert(T model);
    boolean update(T model);
    boolean delete(K id);
}
