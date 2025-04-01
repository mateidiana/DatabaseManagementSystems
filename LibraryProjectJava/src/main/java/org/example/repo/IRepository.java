package org.example.repo;

import java.util.List;

public interface IRepository<T> {

    public void create(T obj) throws Exception;

    public T read(int id) throws Exception;

    public void update(T obj) throws Exception;

    public void delete(int id) throws Exception;

    public List<T> getAll() throws Exception;
}

