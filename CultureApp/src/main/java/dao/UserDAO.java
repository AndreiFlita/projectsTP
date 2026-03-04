package dao;

import connection.ConnectionFactory;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    public User insert(User user) {
        String query = "INSERT INTO users (nume, email, parola_hash, rol, bio, interese, state) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getNume());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getParola());
            statement.setString(4, user.getRol());
            statement.setString(5, user.getBio());
            statement.setString(6, user.getInterese());
            statement.setString(7, user.getState() != null ? user.getState() : "active");

            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
            }

            return user;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "UserDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public User findById(int id) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "UserDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public User findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            rs = statement.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "UserDAO:findByEmail " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public User authenticate(String email, String password) {
        User user = findByEmail(email);
        if (user != null && user.getParola().equals(password)) {
            return user;
        }
        return null;
    }

    public List<User> findAll() {
        String query = "SELECT * FROM users ORDER BY data_inregistrarii DESC";
        Connection connection = ConnectionFactory.getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "UserDAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return users;
    }

    public void update(User user) {
        String query = "UPDATE users SET nume = ?, email = ?, bio = ?, interese = ?, rol = ?, state = ? WHERE user_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getNume());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getBio());
            statement.setString(4, user.getInterese());
            statement.setString(5, user.getRol());
            statement.setString(6, user.getState() != null ? user.getState() : "active");
            statement.setInt(7, user.getUserId());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "UserDAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public void delete(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "UserDAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setNume(rs.getString("nume"));
        user.setEmail(rs.getString("email"));
        user.setParola(rs.getString("parola_hash"));
        user.setRol(rs.getString("rol"));
        user.setBio(rs.getString("bio"));
        user.setInterese(rs.getString("interese"));

        String state = rs.getString("state");
        user.setState(state != null ? state : "active");

        Timestamp timestamp = rs.getTimestamp("data_inregistrarii");
        if (timestamp != null) {
            user.setDataInregistrarii(timestamp.toLocalDateTime());
        }

        return user;
    }
}