package hamza.maharmeh.database.dao;

import hamza.maharmeh.database.DefaultDataSourceConfig;
import hamza.maharmeh.exceptions.NoSuchUserException;
import hamza.maharmeh.model.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<User,String>{

    private DataSource dataSource;
    public UserDao() {
        dataSource = DefaultDataSourceConfig.getDataSource();
    }
    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public void save(User user) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = """
                        INSERT INTO users(username,password) VALUES(?,?) 
                    """;
            try(PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1,user.username());
                statement.setString(2,user.password());
                statement.executeUpdate();
            }
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public boolean doesExist(String username) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = """
                            select * from users where username=? 
                        """;
            try(PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet set = statement.executeQuery();
                return set.next();
            }
        }catch(SQLException e) {

        }
        return false;
    }
    public boolean doesExist(User user) {
        return this.doesExist(user.username());
    }
    public void delete(String username) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = """
                        DELETE FROM users WHERE username=? 
                    """;
            try(PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.executeUpdate();
            }
        }catch(SQLException e) {

        }
    }
    public User get(String username) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = """
                       SELECT password from users where username=?
                        """;
            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet set = stmt.executeQuery();

                if(!set.next()) throw new NoSuchUserException(username);

                String password = set.getString("password");
                return new User(username,password);
            }
        }catch(SQLException e) {

        }
        return null;
    }

    public List<User> getAll() {
        try(Connection conn = dataSource.getConnection()) {
            String sql = """
                        Select username,password from users
                    """;

            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                List<User> usersList = new ArrayList<>();
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    usersList.add(new User(username,password));
                }
                return usersList;
            }
        }catch(SQLException e) {

        }
        return null;
    }

}
