package org.example.repo;


import org.example.model.Book;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDBRepository extends DBRepository<Book> {
    public BookDBRepository(String dbUrl, String dbUser, String dbPassword) throws Exception {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(Book obj) throws Exception {
        String sql = "INSERT INTO BOOK(id, title, " +
                " author, summary, isbn, availability) VALUES(?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, obj.getId());
            statement.setString(2, obj.getTitle());
            statement.setString(3, obj.getAuthor());
            statement.setString(4, obj.getSummary());
            statement.setString(5, obj.getIsbn());
            statement.setBoolean(6, obj.isAvailability());
            statement.execute();
        } catch (SQLException ex) {
            throw new Exception("Database error");
        }
    }

    @Override
    public Book read(int id) throws Exception {
        String sql = "SELECT * FROM BOOK WHERE id = ?";

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
    public void update(Book obj) throws Exception {
        String sql = "UPDATE BOOK SET title = ?, "
                + " author = ?, summary = ?, isbn = ?, availability = ? WHERE ID = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getTitle());
            statement.setString(2, obj.getAuthor());
            statement.setString(3, obj.getSummary());
            statement.setString(4, obj.getIsbn());
            statement.setBoolean(5, obj.isAvailability());
            statement.setInt(6, obj.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new Exception("Database error");
        }

    }

    @Override
    public void delete(int id) throws Exception{
        String sql = "DELETE FROM BOOK WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new Exception("Database error");
        }
    }

    @Override
    public List<Book> getAll() throws Exception{
        String sql = "SELECT * FROM BOOK";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<Book> books = new ArrayList<>();
            while(resultSet.next()){
                books.add(extractFromResultSet(resultSet));
            }
            return books;
        } catch (SQLException e) {
            throw new Exception("Database error");
        }
    }

    private Book extractFromResultSet(ResultSet resultSet) throws SQLException {
        Book book=new Book(resultSet.getInt("id"),resultSet.getString("title"),resultSet.getString("author"), resultSet.getString("summary"),resultSet.getString("isbn"),resultSet.getBoolean("availability"));
        return book;
    }
}
