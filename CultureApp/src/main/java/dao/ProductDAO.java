package dao;

import connection.ConnectionFactory;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAO {
    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    public Product insert(Product product) {
        String query = "INSERT INTO products (artist_id, titlu, descriere, pret, categorie, imagine, stoc) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, product.getArtistId());
            statement.setString(2, product.getTitlu());
            statement.setString(3, product.getDescriere());
            statement.setBigDecimal(4, product.getPret());
            statement.setString(5, product.getCategorie());
            statement.setString(6, product.getImagine());
            statement.setInt(7, product.getStoc());

            statement.executeUpdate();

            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                product.setProductId(rs.getInt(1));
            }

            return product;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public Product findById(int id) {
        String query = "SELECT p.*, u.nume as nume_artist FROM products p " +
                "LEFT JOIN artists a ON p.artist_id = a.artist_id " +
                "LEFT JOIN users u ON a.user_id = u.user_id " +
                "WHERE p.product_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();

            if (rs.next()) {
                return extractProductFromResultSet(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    public List<Product> findAll() {
        String query = "SELECT p.*, u.nume as nume_artist FROM products p " +
                "LEFT JOIN artists a ON p.artist_id = a.artist_id " +
                "LEFT JOIN users u ON a.user_id = u.user_id " +
                "ORDER BY p.data_postarii DESC";
        Connection connection = ConnectionFactory.getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return products;
    }

    public List<Product> findByCategory(String category) {
        String query = "SELECT p.*, u.nume as nume_artist FROM products p " +
                "LEFT JOIN artists a ON p.artist_id = a.artist_id " +
                "LEFT JOIN users u ON a.user_id = u.user_id " +
                "WHERE p.categorie = ? ORDER BY p.data_postarii DESC";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, category);
            rs = statement.executeQuery();

            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:findByCategory " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return products;
    }

    public List<Product> findByArtist(int artistId) {
        String query = "SELECT p.*, u.nume as nume_artist FROM products p " +
                "LEFT JOIN artists a ON p.artist_id = a.artist_id " +
                "LEFT JOIN users u ON a.user_id = u.user_id " +
                "WHERE p.artist_id = ? ORDER BY p.data_postarii DESC";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<Product> products = new ArrayList<>();

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, artistId);
            rs = statement.executeQuery();

            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:findByArtist " + e.getMessage());
        } finally {
            ConnectionFactory.close(rs);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return products;
    }

    public void update(Product product) {
        String query = "UPDATE products SET titlu = ?, descriere = ?, pret = ?, categorie = ?, imagine = ?, stoc = ? WHERE product_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, product.getTitlu());
            statement.setString(2, product.getDescriere());
            statement.setBigDecimal(3, product.getPret());
            statement.setString(4, product.getCategorie());
            statement.setString(5, product.getImagine());
            statement.setInt(6, product.getStoc());
            statement.setInt(7, product.getProductId());

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public void delete(int productId) {
        String query = "DELETE FROM products WHERE product_id = ?";
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setArtistId(rs.getInt("artist_id"));
        product.setTitlu(rs.getString("titlu"));
        product.setDescriere(rs.getString("descriere"));
        product.setPret(rs.getBigDecimal("pret"));
        product.setCategorie(rs.getString("categorie"));
        product.setImagine(rs.getString("imagine"));
        product.setStoc(rs.getInt("stoc"));

        Timestamp timestamp = rs.getTimestamp("data_postarii");
        if (timestamp != null) {
            product.setDataPostarii(timestamp.toLocalDateTime());
        }

        product.setNumeArtist(rs.getString("nume_artist"));

        return product;
    }
}
