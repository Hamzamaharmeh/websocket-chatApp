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
                        INSERT INTO users(id,username,password) VALUES(?,?,?) 
                    """;
            try(PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1,user.id());
                statement.setString(2,user.username());
                statement.setString(3,user.password());
                statement.executeUpdate();
            }
        }catch(SQLException e) {

        }
    }
    public void delete(String id) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = """
                        DELETE FROM users WHERE id=? 
                    """;
            try(PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, id);
                statement.executeUpdate();
            }
        }catch(SQLException e) {

        }
    }
    public User get(String id) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = """
                       SELECT username,password from users where id=?
                        """;
            try(Statement stmt = conn.createStatement()) {
                ResultSet set = stmt.executeQuery(sql);

                if(!set.next()) throw new NoSuchUserException(id);

                String username = set.getString("username");
                String password = set.getString("password");
                return new User(username,password,id);
            }
        }catch(SQLException e) {

        }
        return null;
    }

    public List<User> getAll() {
        try(Connection conn = dataSource.getConnection()) {
            String sql = """
                        Select id,username,password from users
                    """;

            try(Statement stmt = conn.createStatement()) {
                List<User> usersList = new ArrayList<>();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()) {
                    String id = rs.getString("id");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    usersList.add(new User(username,password,id));
                }
                return usersList;
            }
        }catch(SQLException e) {

        }
        return null;
    }

}
