package org.example.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DBRepository<T> implements IRepository<T>, AutoCloseable {
    protected final Connection connection;

    public DBRepository(String dbUrl, String dbUser, String dbPassword) throws Exception {
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            throw new Exception("Database error");
        }
    }
    public void close() throws Exception {
        connection.close();
    }

    @Override
    public void create(T obj) throws Exception {

    }

    @Override
    public T read(int id) throws Exception {
        return null;
    }

    @Override
    public void update(T obj) throws Exception {

    }

    @Override
    public void delete(int id) throws Exception{

    }

    @Override
    public List<T> getAll() throws Exception {
        return List.of();
    }
}
