package gui;

import dao.ProductDAO;
import model.Product;
import model.User;
import utils.ModernUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class MarketplacePanel extends JPanel {
    private User currentUser;
    private ProductDAO productDAO;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public MarketplacePanel(User user) {
        this.currentUser = user;
        this.productDAO = new ProductDAO();
        initComponents();
        loadProducts();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(ModernUI.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = ModernUI.createTitleLabel("🛍️ Marketplace Cultural");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton addProductBtn = ModernUI.createPrimaryButton("➕ Adaugă Produs");
        addProductBtn.addActionListener(e -> openAddProductDialog());

        JButton refreshBtn = ModernUI.createSecondaryButton("🔄 Reîncarcă");
        refreshBtn.addActionListener(e -> loadProducts());

        buttonPanel.add(refreshBtn);
        if (currentUser.getRol().equals("artist") || currentUser.getRol().equals("admin")) {
            buttonPanel.add(addProductBtn);
        }

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Filter Panel
        JPanel filterPanel = createFilterPanel();

        // Table
        String[] columns = {"ID", "Titlu", "Artist", "Categorie", "Preț (RON)", "Stoc"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
        productTable.setFont(ModernUI.BODY_FONT);
        productTable.setRowHeight(40);
        productTable.getTableHeader().setFont(ModernUI.HEADING_FONT);
        productTable.getTableHeader().setBackground(ModernUI.PRIMARY_COLOR);
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.setSelectionBackground(new Color(220, 220, 240));

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton viewBtn = ModernUI.createPrimaryButton("👁️ Vezi Detalii");
        viewBtn.addActionListener(e -> viewProductDetails());

        JButton purchaseBtn = ModernUI.createPrimaryButton("🛒 Cumpără");
        purchaseBtn.setBackground(ModernUI.SUCCESS_COLOR);
        purchaseBtn.addActionListener(e -> purchaseProduct());

        actionPanel.add(viewBtn);
        actionPanel.add(purchaseBtn);

        // Artist-specific buttons
        if (currentUser.getRol().equals("artist") || currentUser.getRol().equals("admin")) {
            JButton editBtn = ModernUI.createSecondaryButton("✏️ Editează");
            editBtn.addActionListener(e -> editProduct());

            JButton deleteBtn = ModernUI.createDangerButton("🗑️ Șterge");
            deleteBtn.addActionListener(e -> deleteProduct());

            actionPanel.add(editBtn);
            actionPanel.add(deleteBtn);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(filterPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(ModernUI.CARD_COLOR);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        filterPanel.setPreferredSize(new Dimension(200, 600));

        JLabel filterLabel = ModernUI.createHeadingLabel("Filtre");
        filterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel categoryLabel = ModernUI.createBodyLabel("Categorie:");
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] categories = {"Toate", "Pictură", "Sculptură", "Fotografie", "Grafică", "Artizanat"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setFont(ModernUI.BODY_FONT);
        categoryBox.setMaximumSize(new Dimension(180, 35));
        categoryBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton filterBtn = ModernUI.createPrimaryButton("Aplică");
        filterBtn.setMaximumSize(new Dimension(180, 40));
        filterBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterBtn.addActionListener(e -> {
            String category = categoryBox.getSelectedItem().toString();
            if (category.equals("Toate")) {
                loadProducts();
            } else {
                loadProductsByCategory(category);
            }
        });

        filterPanel.add(filterLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        filterPanel.add(categoryLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        filterPanel.add(categoryBox);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        filterPanel.add(filterBtn);

        return filterPanel;
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        List<Product> products = productDAO.findAll();

        for (Product product : products) {
            Object[] row = {
                    product.getProductId(),
                    product.getTitlu(),
                    product.getNumeArtist() != null ? product.getNumeArtist() : "N/A",
                    product.getCategorie(),
                    product.getPret(),
                    product.getStoc()
            };
            tableModel.addRow(row);
        }
    }

    private void loadProductsByCategory(String category) {
        tableModel.setRowCount(0);
        List<Product> products = productDAO.findByCategory(category);

        for (Product product : products) {
            Object[] row = {
                    product.getProductId(),
                    product.getTitlu(),
                    product.getNumeArtist() != null ? product.getNumeArtist() : "N/A",
                    product.getCategorie(),
                    product.getPret(),
                    product.getStoc()
            };
            tableModel.addRow(row);
        }
    }

    private void openAddProductDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adaugă Produs", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(ModernUI.BACKGROUND_COLOR);

        JTextField titleField = ModernUI.createTextField("");
        JTextArea descArea = ModernUI.createTextArea();
        descArea.setRows(4);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(400, 100));

        JTextField priceField = ModernUI.createTextField("");
        String[] categories = {"Pictură", "Sculptură", "Fotografie", "Grafică", "Artizanat"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setFont(ModernUI.BODY_FONT);

        JTextField stockField = ModernUI.createTextField("");

        addFieldToPanel(panel, "Titlu:", titleField);
        panel.add(ModernUI.createBodyLabel("Descriere:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(descScroll);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        addFieldToPanel(panel, "Preț (RON):", priceField);
        panel.add(ModernUI.createBodyLabel("Categorie:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(categoryBox);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        addFieldToPanel(panel, "Stoc:", stockField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton saveBtn = ModernUI.createPrimaryButton("Salvează");
        JButton cancelBtn = ModernUI.createSecondaryButton("Anulează");

        saveBtn.addActionListener(e -> {
            try {
                Product product = new Product();
                product.setArtistId(1); // Simplified - should get from artist table
                product.setTitlu(titleField.getText());
                product.setDescriere(descArea.getText());
                product.setPret(new BigDecimal(priceField.getText()));
                product.setCategorie(categoryBox.getSelectedItem().toString());
                product.setStoc(Integer.parseInt(stockField.getText()));

                productDAO.insert(product);
                ModernUI.showSuccessMessage(dialog, "Produs adăugat cu succes!");
                loadProducts();
                dialog.dispose();
            } catch (Exception ex) {
                ModernUI.showErrorMessage(dialog, "Eroare la adăugarea produsului: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(buttonPanel);

        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }

    private void addFieldToPanel(JPanel panel, String label, JComponent field) {
        panel.add(ModernUI.createBodyLabel(label));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private void viewProductDetails() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un produs!");
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        Product product = productDAO.findById(productId);

        if (product != null) {
            String details = String.format(
                    "Titlu: %s\n\nArtist: %s\n\nCategorie: %s\n\nPreț: %.2f RON\n\nStoc: %d bucăți\n\nDescriere:\n%s",
                    product.getTitlu(),
                    product.getNumeArtist(),
                    product.getCategorie(),
                    product.getPret(),
                    product.getStoc(),
                    product.getDescriere()
            );

            JTextArea textArea = new JTextArea(details);
            textArea.setFont(ModernUI.BODY_FONT);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Detalii Produs", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void purchaseProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un produs pentru achiziție!");
            return;
        }

        int productId = (int) tableModel.getValueAt(selectedRow, 0);
        Product product = productDAO.findById(productId);

        if (product != null) {
            // Check stock
            if (product.getStoc() <= 0) {
                ModernUI.showErrorMessage(this, "Produsul nu mai este în stoc!");
                return;
            }

            // Create purchase confirmation dialog
            String message = String.format(
                    "Doriți să achiziționați:\n\n" +
                            "Produs: %s\n" +
                            "Artist: %s\n" +
                            "Preț: %.2f RON\n\n" +
                            "Confirmare achiziție?",
                    product.getTitlu(),
                    product.getNumeArtist(),
                    product.getPret()
            );

            int result = ModernUI.showConfirmDialog(this, message);

            if (result == JOptionPane.YES_OPTION) {
                // Update stock
                product.setStoc(product.getStoc() - 1);
                productDAO.update(product);

                // Show success message with order details
                String successMessage = String.format(
                        "✅ Achiziție realizată cu succes!\n\n" +
                                "Produs: %s\n" +
                                "Preț plătit: %.2f RON\n\n" +
                                "Vă mulțumim pentru comandă!\n" +
                                "Veți fi contactat în curând de către artist.",
                        product.getTitlu(),
                        product.getPret()
                );

                ModernUI.showSuccessMessage(this, successMessage);
                loadProducts(); // Refresh table to show updated stock
            }
        }
    }

    private void editProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un produs!");
            return;
        }

        ModernUI.showWarningMessage(this, "Funcționalitate în dezvoltare!");
    }

    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un produs!");
            return;
        }

        int result = ModernUI.showConfirmDialog(this, "Sigur doriți să ștergeți acest produs?");
        if (result == JOptionPane.YES_OPTION) {
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            productDAO.delete(productId);
            ModernUI.showSuccessMessage(this, "Produs șters cu succes!");
            loadProducts();
        }
    }
}