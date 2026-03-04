package dao;

import connection.ConnectionFactory;
import model.ForumPost;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ForumPostDAO {
    private static final Logger LOGGER = Logger.getLogger(ForumPostDAO.class.getName());

    public ForumPost insert(ForumPost post) {
        String query = "INSERT INTO forum_posts (topic_id, user_id, continut) VALUES (?, ?, ?)";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, post.getTopicId());
            statement.setInt(2, post.getUserId());
            statement.setString(3, post.getContinut());

            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                post.setPostId(rs.getInt(1));
            }

            return post;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ForumPostDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public List<ForumPost> findByTopicId(int topicId) {
        String query = "SELECT fp.*, u.nume as nume_autor " +
                "FROM forum_posts fp " +
                "LEFT JOIN users u ON fp.user_id = u.user_id " +
                "WHERE fp.topic_id = ? " +
                "ORDER BY fp.data_postarii ASC";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<ForumPost> posts = new ArrayList<>();

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, topicId);
            rs = statement.executeQuery();

            while (rs.next()) {
                posts.add(extractPostFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ForumPostDAO:findByTopicId " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return posts;
    }

    public void delete(int postId) {
        String query = "DELETE FROM forum_posts WHERE post_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, postId);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ForumPostDAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    private ForumPost extractPostFromResultSet(ResultSet rs) throws SQLException {
        ForumPost post = new ForumPost();
        post.setPostId(rs.getInt("post_id"));
        post.setTopicId(rs.getInt("topic_id"));
        post.setUserId(rs.getInt("user_id"));
        post.setContinut(rs.getString("continut"));

        Timestamp timestamp = rs.getTimestamp("data_postarii");
        if (timestamp != null) {
            post.setDataPostarii(timestamp.toLocalDateTime());
        }

        post.setNumeAutor(rs.getString("nume_autor"));

        return post;
    }
}