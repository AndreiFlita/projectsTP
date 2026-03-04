package dao;

import connection.ConnectionFactory;
import model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventDAO {
    private static final Logger LOGGER = Logger.getLogger(EventDAO.class.getName());

    public Event insert(Event event) {
        String query = "INSERT INTO events (organizer_id, titlu, descriere, locatie, data_start, data_end, numar_locuri, pret_bilet) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, event.getOrganizerId());
            statement.setString(2, event.getTitlu());
            statement.setString(3, event.getDescriere());
            statement.setString(4, event.getLocatie());
            statement.setTimestamp(5, Timestamp.valueOf(event.getDataStart()));
            statement.setTimestamp(6, event.getDataEnd() != null ? Timestamp.valueOf(event.getDataEnd()) : null);
            statement.setInt(7, event.getNumarLocuri());
            statement.setBigDecimal(8, event.getPretBilet());

            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                event.setEventId(rs.getInt(1));
            }

            return event;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "EventDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public Event findById(int id) {
        String query = "SELECT e.*, u.nume as nume_organizator, " +
                "(e.numar_locuri - COALESCE((SELECT COUNT(*) FROM bookings WHERE event_id = e.event_id AND status = 'rezervat'), 0)) as locuri_ramase " +
                "FROM events e " +
                "LEFT JOIN users u ON e.organizer_id = u.user_id " +
                "WHERE e.event_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                return extractEventFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "EventDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public List<Event> findAll() {
        String query = "SELECT e.*, u.nume as nume_organizator, " +
                "(e.numar_locuri - COALESCE((SELECT COUNT(*) FROM bookings WHERE event_id = e.event_id AND status = 'rezervat'), 0)) as locuri_ramase " +
                "FROM events e " +
                "LEFT JOIN users u ON e.organizer_id = u.user_id " +
                "ORDER BY e.data_start ASC";
        Connection connection = ConnectionFactory.getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<Event> events = new ArrayList<>();

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()) {
                events.add(extractEventFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "EventDAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return events;
    }

    public List<Event> findUpcoming() {
        String query = "SELECT e.*, u.nume as nume_organizator, " +
                "(e.numar_locuri - COALESCE((SELECT COUNT(*) FROM bookings WHERE event_id = e.event_id AND status = 'rezervat'), 0)) as locuri_ramase " +
                "FROM events e " +
                "LEFT JOIN users u ON e.organizer_id = u.user_id " +
                "WHERE e.data_start > NOW() " +
                "ORDER BY e.data_start ASC";
        Connection connection = ConnectionFactory.getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<Event> events = new ArrayList<>();

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()) {
                events.add(extractEventFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "EventDAO:findUpcoming " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return events;
    }

    public void update(Event event) {
        String query = "UPDATE events SET titlu = ?, descriere = ?, locatie = ?, data_start = ?, data_end = ?, numar_locuri = ?, pret_bilet = ? WHERE event_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, event.getTitlu());
            statement.setString(2, event.getDescriere());
            statement.setString(3, event.getLocatie());
            statement.setTimestamp(4, Timestamp.valueOf(event.getDataStart()));
            statement.setTimestamp(5, event.getDataEnd() != null ? Timestamp.valueOf(event.getDataEnd()) : null);
            statement.setInt(6, event.getNumarLocuri());
            statement.setBigDecimal(7, event.getPretBilet());
            statement.setInt(8, event.getEventId());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "EventDAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public void delete(int eventId) {
        String query = "DELETE FROM events WHERE event_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, eventId);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "EventDAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    private Event extractEventFromResultSet(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("event_id"));
        event.setOrganizerId(rs.getInt("organizer_id"));
        event.setTitlu(rs.getString("titlu"));
        event.setDescriere(rs.getString("descriere"));
        event.setLocatie(rs.getString("locatie"));

        Timestamp startTime = rs.getTimestamp("data_start");
        if (startTime != null) {
            event.setDataStart(startTime.toLocalDateTime());
        }

        Timestamp endTime = rs.getTimestamp("data_end");
        if (endTime != null) {
            event.setDataEnd(endTime.toLocalDateTime());
        }

        event.setNumarLocuri(rs.getInt("numar_locuri"));
        event.setPretBilet(rs.getBigDecimal("pret_bilet"));
        event.setNumeOrganizator(rs.getString("nume_organizator"));
        event.setLocuriRamase(rs.getInt("locuri_ramase"));

        return event;
    }
}
