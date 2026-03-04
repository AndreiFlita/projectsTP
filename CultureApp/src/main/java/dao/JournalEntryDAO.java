package dao;

import connection.ConnectionFactory;
import model.JournalEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JournalEntryDAO {
    private static final Logger LOGGER = Logger.getLogger(JournalEntryDAO.class.getName());

    public JournalEntry insert(JournalEntry entry) {
        String query = "INSERT INTO journal_entries (user_id, tip_activitate, titlu, descriere, data_experientei, rating) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entry.getUserId());
            statement.setString(2, entry.getTipActivitate());
            statement.setString(3, entry.getTitlu());
            statement.setString(4, entry.getDescriere());
            statement.setDate(5, Date.valueOf(entry.getDataExperientei()));
            statement.setInt(6, entry.getRating());

            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                entry.setEntryId(rs.getInt(1));
            }

            return entry;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "JournalEntryDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public JournalEntry findById(int id) {
        String query = "SELECT * FROM journal_entries WHERE entry_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                return extractEntryFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "JournalEntryDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public List<JournalEntry> findByUserId(int userId) {
        String query = "SELECT * FROM journal_entries WHERE user_id = ? ORDER BY data_experientei DESC";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<JournalEntry> entries = new ArrayList<>();

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            rs = statement.executeQuery();

            while (rs.next()) {
                entries.add(extractEntryFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "JournalEntryDAO:findByUserId " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return entries;
    }

    public List<JournalEntry> findByUserAndType(int userId, String tipActivitate) {
        String query = "SELECT * FROM journal_entries WHERE user_id = ? AND tip_activitate = ? ORDER BY data_experientei DESC";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<JournalEntry> entries = new ArrayList<>();

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            statement.setString(2, tipActivitate);
            rs = statement.executeQuery();

            while (rs.next()) {
                entries.add(extractEntryFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "JournalEntryDAO:findByUserAndType " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return entries;
    }

    public void update(JournalEntry entry) {
        String query = "UPDATE journal_entries SET tip_activitate = ?, titlu = ?, descriere = ?, data_experientei = ?, rating = ? WHERE entry_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, entry.getTipActivitate());
            statement.setString(2, entry.getTitlu());
            statement.setString(3, entry.getDescriere());
            statement.setDate(4, Date.valueOf(entry.getDataExperientei()));
            statement.setInt(5, entry.getRating());
            statement.setInt(6, entry.getEntryId());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "JournalEntryDAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public void delete(int entryId) {
        String query = "DELETE FROM journal_entries WHERE entry_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, entryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "JournalEntryDAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    private JournalEntry extractEntryFromResultSet(ResultSet rs) throws SQLException {
        JournalEntry entry = new JournalEntry();
        entry.setEntryId(rs.getInt("entry_id"));
        entry.setUserId(rs.getInt("user_id"));
        entry.setTipActivitate(rs.getString("tip_activitate"));
        entry.setTitlu(rs.getString("titlu"));
        entry.setDescriere(rs.getString("descriere"));

        Date date = rs.getDate("data_experientei");
        if (date != null) {
            entry.setDataExperientei(date.toLocalDate());
        }

        entry.setRating(rs.getInt("rating"));

        return entry;
    }
}
