package gui;

import dao.JournalEntryDAO;
import model.JournalEntry;
import model.User;
import utils.ModernUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JournalPanel extends JPanel {
    private User currentUser;
    private JournalEntryDAO journalDAO;
    private JTable journalTable;
    private DefaultTableModel tableModel;

    public JournalPanel(User user) {
        this.currentUser = user;
        this.journalDAO = new JournalEntryDAO();
        initComponents();
        loadEntries();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(ModernUI.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = ModernUI.createTitleLabel("📓 Jurnalul Meu Cultural");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton addEntryBtn = ModernUI.createPrimaryButton("➕ Adaugă Intrare");
        addEntryBtn.addActionListener(e -> openAddEntryDialog());

        JButton refreshBtn = ModernUI.createSecondaryButton("🔄 Reîncarcă");
        refreshBtn.addActionListener(e -> loadEntries());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(addEntryBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Statistics Panel
        JPanel statsPanel = createStatsPanel();

        // Table
        String[] columns = {"ID", "Tip", "Titlu", "Data", "Rating", "Descriere"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        journalTable = new JTable(tableModel);
        journalTable.setFont(ModernUI.BODY_FONT);
        journalTable.setRowHeight(40);
        journalTable.getTableHeader().setFont(ModernUI.HEADING_FONT);
        journalTable.getTableHeader().setBackground(ModernUI.SUCCESS_COLOR);
        journalTable.getTableHeader().setForeground(Color.WHITE);
        journalTable.setSelectionBackground(new Color(220, 240, 220));

        // Hide ID column
        journalTable.getColumnModel().getColumn(0).setMinWidth(0);
        journalTable.getColumnModel().getColumn(0).setMaxWidth(0);

        JScrollPane scrollPane = new JScrollPane(journalTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton viewBtn = ModernUI.createPrimaryButton("👁️ Vezi Detalii");
        viewBtn.addActionListener(e -> viewEntryDetails());

        JButton editBtn = ModernUI.createSecondaryButton("✏️ Editează");
        editBtn.addActionListener(e -> editEntry());

        JButton deleteBtn = ModernUI.createDangerButton("🗑️ Șterge");
        deleteBtn.addActionListener(e -> deleteEntry());

        actionPanel.add(viewBtn);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        List<JournalEntry> allEntries = journalDAO.findByUserId(currentUser.getUserId());
        int totalBooks = (int) allEntries.stream().filter(e -> e.getTipActivitate().equals("carte")).count();
        int totalMovies = (int) allEntries.stream().filter(e -> e.getTipActivitate().equals("film")).count();
        int totalExhibitions = (int) allEntries.stream().filter(e -> e.getTipActivitate().equals("expoziție")).count();

        statsPanel.add(createStatCard("📚 Cărți", String.valueOf(totalBooks), ModernUI.PRIMARY_COLOR));
        statsPanel.add(createStatCard("🎬 Filme", String.valueOf(totalMovies), ModernUI.ACCENT_COLOR));
        statsPanel.add(createStatCard("🎨 Expoziții", String.valueOf(totalExhibitions), ModernUI.SUCCESS_COLOR));

        return statsPanel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = ModernUI.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(250, 100));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ModernUI.BODY_FONT);
        titleLabel.setForeground(ModernUI.TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private void loadEntries() {
        tableModel.setRowCount(0);
        List<JournalEntry> entries = journalDAO.findByUserId(currentUser.getUserId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (JournalEntry entry : entries) {
            String stars = "⭐".repeat(entry.getRating());
            String shortDesc = entry.getDescriere() != null && entry.getDescriere().length() > 50
                    ? entry.getDescriere().substring(0, 50) + "..."
                    : entry.getDescriere();

            Object[] row = {
                    entry.getEntryId(),
                    getActivityIcon(entry.getTipActivitate()) + " " + entry.getTipActivitate(),
                    entry.getTitlu(),
                    entry.getDataExperientei().format(formatter),
                    stars,
                    shortDesc
            };
            tableModel.addRow(row);
        }
    }

    private String getActivityIcon(String tip) {
        switch (tip.toLowerCase()) {
            case "carte": return "📚";
            case "film": return "🎬";
            case "expoziție": return "🎨";
            default: return "📝";
        }
    }

    private void openAddEntryDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Adaugă Intrare", true);
        dialog.setSize(500, 650);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(ModernUI.BACKGROUND_COLOR);

        String[] types = {"carte", "film", "expoziție"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setFont(ModernUI.BODY_FONT);
        typeBox.setMaximumSize(new Dimension(400, 40));

        JTextField titleField = ModernUI.createTextField("");

        JTextArea descArea = ModernUI.createTextArea();
        descArea.setRows(5);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(400, 120));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        datePanel.setBackground(ModernUI.BACKGROUND_COLOR);
        datePanel.setMaximumSize(new Dimension(400, 40));

        JTextField dayField = new JTextField(5);
        JTextField monthField = new JTextField(5);
        JTextField yearField = new JTextField(8);

        datePanel.add(new JLabel("Zi:"));
        datePanel.add(dayField);
        datePanel.add(new JLabel("Lună:"));
        datePanel.add(monthField);
        datePanel.add(new JLabel("An:"));
        datePanel.add(yearField);

        JSlider ratingSlider = new JSlider(1, 5, 5);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.setMaximumSize(new Dimension(400, 60));

        JLabel ratingLabel = new JLabel("Rating: ⭐⭐⭐⭐⭐");
        ratingLabel.setFont(ModernUI.BODY_FONT);
        ratingSlider.addChangeListener(e -> {
            int rating = ratingSlider.getValue();
            ratingLabel.setText("Rating: " + "⭐".repeat(rating));
        });

        panel.add(ModernUI.createBodyLabel("Tip Activitate:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(typeBox);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        addFieldToPanel(panel, "Titlu:", titleField);

        panel.add(ModernUI.createBodyLabel("Descriere:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(descScroll);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(ModernUI.createBodyLabel("Data Experienței (zi/lună/an):"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(datePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(ratingLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(ratingSlider);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton saveBtn = ModernUI.createPrimaryButton("Salvează");
        JButton cancelBtn = ModernUI.createSecondaryButton("Anulează");

        saveBtn.addActionListener(e -> {
            try {
                int day = Integer.parseInt(dayField.getText());
                int month = Integer.parseInt(monthField.getText());
                int year = Integer.parseInt(yearField.getText());
                LocalDate date = LocalDate.of(year, month, day);

                JournalEntry entry = new JournalEntry();
                entry.setUserId(currentUser.getUserId());
                entry.setTipActivitate(typeBox.getSelectedItem().toString());
                entry.setTitlu(titleField.getText());
                entry.setDescriere(descArea.getText());
                entry.setDataExperientei(date);
                entry.setRating(ratingSlider.getValue());

                journalDAO.insert(entry);
                ModernUI.showSuccessMessage(dialog, "Intrare adăugată cu succes!");
                loadEntries();
                dialog.dispose();
            } catch (Exception ex) {
                ModernUI.showErrorMessage(dialog, "Eroare la adăugarea intrării: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
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

    private void viewEntryDetails() {
        int selectedRow = journalTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați o intrare!");
            return;
        }

        int entryId = (int) tableModel.getValueAt(selectedRow, 0);
        JournalEntry entry = journalDAO.findById(entryId);

        if (entry != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String stars = "⭐".repeat(entry.getRating());

            String details = String.format(
                    "%s %s\n\nTitlu: %s\n\nData: %s\n\nRating: %s (%d/5)\n\nDescriere:\n%s",
                    getActivityIcon(entry.getTipActivitate()),
                    entry.getTipActivitate().toUpperCase(),
                    entry.getTitlu(),
                    entry.getDataExperientei().format(formatter),
                    stars,
                    entry.getRating(),
                    entry.getDescriere()
            );

            JTextArea textArea = new JTextArea(details);
            textArea.setFont(ModernUI.BODY_FONT);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Detalii Intrare", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editEntry() {
        int selectedRow = journalTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați o intrare!");
            return;
        }

        ModernUI.showWarningMessage(this, "Funcționalitate în dezvoltare!");
    }

    private void deleteEntry() {
        int selectedRow = journalTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați o intrare!");
            return;
        }

        int result = ModernUI.showConfirmDialog(this, "Sigur doriți să ștergeți această intrare?");
        if (result == JOptionPane.YES_OPTION) {
            int entryId = (int) tableModel.getValueAt(selectedRow, 0);
            journalDAO.delete(entryId);
            ModernUI.showSuccessMessage(this, "Intrare ștearsă cu succes!");
            loadEntries();
        }
    }
}
