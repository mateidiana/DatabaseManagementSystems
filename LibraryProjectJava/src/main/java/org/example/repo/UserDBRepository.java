package org.example.repo;
import org.example.model.Book;
import org.example.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDBRepository extends DBRepository<User>{
    public UserDBRepository(String dbUrl, String dbUser, String dbPassword) throws Exception {
        super(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void create(User obj) throws Exception {
        String sql = "INSERT INTO libraryUser(id, username, " +
                " email, userPassword, userRole) VALUES(?, ?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, obj.getId());
            statement.setString(2, obj.getUsername());
            statement.setString(3, obj.getEmail());
            statement.setString(4, obj.getPassword());
            statement.setString(5, obj.getRole());
            statement.execute();
        } catch (SQLException ex) {
            throw new Exception("Database error");
        }
    }

    @Override
    public User read(int id) throws Exception {
        String sql = "SELECT * FROM libraryUser WHERE id = ?";

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
    public void update(User obj) throws Exception {
        String sql = "UPDATE libraryUser SET username = ?, "
                + " email = ?, userPassword = ?, userRole = ? WHERE ID = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getUsername());
            statement.setString(2, obj.getEmail());
            statement.setString(3, obj.getPassword());
            statement.setString(4, obj.getRole());
            statement.setInt(5, obj.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new Exception("Database error");
        }

    }

    @Override
    public void delete(int id) throws Exception{
        String sql = "DELETE FROM libraryUser WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new Exception("Database error");
        }
    }

    @Override
    public List<User> getAll() throws Exception{
        String sql = "SELECT * FROM libraryUser";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while(resultSet.next()){
                users.add(extractFromResultSet(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new Exception("Database error");
        }
    }

    private User extractFromResultSet(ResultSet resultSet) throws SQLException {
        User user=new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("email"), resultSet.getString("userPassword"), resultSet.getString("userRole"));
        return user;
    }
}
