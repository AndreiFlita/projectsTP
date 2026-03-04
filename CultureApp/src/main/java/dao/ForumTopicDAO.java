package dao;

import connection.ConnectionFactory;
import model.ForumTopic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForumTopicDAO {
    private static final Logger LOGGER = Logger.getLogger(ForumTopicDAO.class.getName());

    public ForumTopic insert(ForumTopic topic) {
        String query = "INSERT INTO forum_topics (titlu, categorie, creat_de) VALUES (?, ?, ?)";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, topic.getTitlu());
            statement.setString(2, topic.getCategorie());
            statement.setInt(3, topic.getCreatDe());

            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                topic.setTopicId(rs.getInt(1));
            }

            return topic;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ForumTopicDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public ForumTopic findById(int id) {
        String query = "SELECT ft.*, u.nume as nume_creator, " +
                "(SELECT COUNT(*) FROM forum_posts WHERE topic_id = ft.topic_id) as numar_postari " +
                "FROM forum_topics ft " +
                "LEFT JOIN users u ON ft.creat_de = u.user_id " +
                "WHERE ft.topic_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                return extractTopicFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ForumTopicDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public List<ForumTopic> findAll() {
        String query = "SELECT ft.*, u.nume as nume_creator, " +
                "(SELECT COUNT(*) FROM forum_posts WHERE topic_id = ft.topic_id) as numar_postari " +
                "FROM forum_topics ft " +
                "LEFT JOIN users u ON ft.creat_de = u.user_id " +
                "ORDER BY ft.data_crearii DESC";
        Connection connection = ConnectionFactory.getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<ForumTopic> topics = new ArrayList<>();

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()) {
                topics.add(extractTopicFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ForumTopicDAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return topics;
    }

    public List<ForumTopic> findByCategory(String category) {
        String query = "SELECT ft.*, u.nume as nume_creator, " +
                "(SELECT COUNT(*) FROM forum_posts WHERE topic_id = ft.topic_id) as numar_postari " +
                "FROM forum_topics ft " +
                "LEFT JOIN users u ON ft.creat_de = u.user_id " +
                "WHERE ft.categorie = ? " +
                "ORDER BY ft.data_crearii DESC";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<ForumTopic> topics = new ArrayList<>();

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, category);
            rs = statement.executeQuery();

            while (rs.next()) {
                topics.add(extractTopicFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ForumTopicDAO:findByCategory " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return topics;
    }

    public void delete(int topicId) {
        String query = "DELETE FROM forum_topics WHERE topic_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, topicId);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ForumTopicDAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    private ForumTopic extractTopicFromResultSet(ResultSet rs) throws SQLException {
        ForumTopic topic = new ForumTopic();
        topic.setTopicId(rs.getInt("topic_id"));
        topic.setTitlu(rs.getString("titlu"));
        topic.setCategorie(rs.getString("categorie"));
        topic.setCreatDe(rs.getInt("creat_de"));

        Timestamp timestamp = rs.getTimestamp("data_crearii");
        if (timestamp != null) {
            topic.setDataCrearii(timestamp.toLocalDateTime());
        }

        topic.setNumeCreator(rs.getString("nume_creator"));
        topic.setNumarPostari(rs.getInt("numar_postari"));

        return topic;
    }
}