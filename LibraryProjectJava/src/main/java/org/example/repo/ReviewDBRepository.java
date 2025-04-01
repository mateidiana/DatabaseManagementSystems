package org.example.repo;

import org.example.model.Borrowed;
import org.example.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDBRepository extends DBRepository<Review>{
    public ReviewDBRepository(String dbUrl, String dbUser, String dbPassword) throws Exception {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(Review obj) throws Exception {
        String sql = "INSERT INTO review(id, userId, bookId, rating) " +
                "  VALUES(?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, obj.getId());
            statement.setInt(2, obj.getUserId());
            statement.setInt(3, obj.getBookId());
            statement.setFloat(4, obj.getRating());
            statement.execute();
        } catch (SQLException ex) {
            throw new Exception("Database error");
        }
    }

    @Override
    public Review read(int id) throws Exception {
        String sql = "SELECT * FROM review WHERE id = ?";

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
    public void update(Review obj) throws Exception {
        String sql = "UPDATE review SET userId = ?, "
                + " bookId = ?, rating = ? WHERE ID = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, obj.getUserId());
            statement.setInt(2, obj.getBookId());
            statement.setFloat(3, obj.getRating());
            statement.setInt(4, obj.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new Exception("Database error");
        }

    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM review WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new Exception("Database error");
        }
    }

    @Override
    public List<Review> getAll() throws Exception {
        String sql = "SELECT * FROM review";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Review> reviews = new ArrayList<>();
            while(resultSet.next()){
                reviews.add(extractFromResultSet(resultSet));
            }
            return reviews;
        } catch (SQLException e) {
            throw new Exception("Database error");
        }
    }

    private Review extractFromResultSet(ResultSet resultSet) throws SQLException {
        Review review=new Review(resultSet.getInt("id"),resultSet.getInt("userId"),resultSet.getInt("bookId"),resultSet.getFloat("rating"));
        return review;
    }

}
