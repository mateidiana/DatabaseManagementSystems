package org.example.repo;
import org.example.model.Borrowed;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowedDBRepository extends DBRepository<Borrowed>{
    public BorrowedDBRepository(String dbUrl, String dbUser, String dbPassword) throws Exception {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(Borrowed obj) throws Exception {
        String sql = "INSERT INTO borrowed(id, bookId, userId) " +
                "  VALUES(?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, obj.getId());
            statement.setInt(2, obj.getBookId());
            statement.setInt(3, obj.getUserId());
            statement.execute();
        } catch (SQLException ex) {
            throw new Exception("Database error");
        }
    }

    @Override
    public Borrowed read(int id) throws Exception {
        String sql = "SELECT * FROM enrolled WHERE id = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return extractFromResultSet(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new Exception("Database error");
        }
    }

    @Override
    public void update(Borrowed obj) throws Exception {
        String sql = "UPDATE borrowed SET bookId = ?, "
                + " userId = ? WHERE ID = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, obj.getBookId());
            statement.setInt(2, obj.getUserId());
            statement.setInt(3, obj.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new Exception("Database error");
        }

    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM borrowed WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new Exception("Database error");
        }
    }

    @Override
    public List<Borrowed> getAll() throws Exception {
        String sql = "SELECT * FROM borrowed";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Borrowed> borroweds = new ArrayList<>();
            while(resultSet.next()){
                borroweds.add(extractFromResultSet(resultSet));
            }
            return borroweds;
        } catch (SQLException e) {
            throw new Exception("Database error");
        }
    }

    private Borrowed extractFromResultSet(ResultSet resultSet) throws SQLException {
        Borrowed thing=new Borrowed(resultSet.getInt("id"),resultSet.getInt("bookId"),resultSet.getInt("userId"));
        return thing;
    }

}
