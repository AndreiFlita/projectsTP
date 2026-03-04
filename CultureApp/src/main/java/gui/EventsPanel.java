package gui;

import dao.EventDAO;
import model.Event;
import model.User;
import utils.ModernUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventsPanel extends JPanel {
    private User currentUser;
    private EventDAO eventDAO;
    private JTable eventTable;
    private DefaultTableModel tableModel;

    public EventsPanel(User user) {
        this.currentUser = user;
        this.eventDAO = new EventDAO();
        initComponents();
        loadEvents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(ModernUI.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = ModernUI.createTitleLabel("🎭 Evenimente Culturale");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton addEventBtn = ModernUI.createPrimaryButton("➕ Creează Eveniment");
        addEventBtn.addActionListener(e -> openAddEventDialog());

        JButton refreshBtn = ModernUI.createSecondaryButton("🔄 Reîncarcă");
        refreshBtn.addActionListener(e -> loadEvents());

        buttonPanel.add(refreshBtn);
        if (currentUser.getRol().equals("artist") || currentUser.getRol().equals("admin")) {
            buttonPanel.add(addEventBtn);
        }

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Titlu", "Organizator", "Locație", "Data Start", "Locuri Libere", "Preț (RON)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        eventTable = new JTable(tableModel);
        eventTable.setFont(ModernUI.BODY_FONT);
        eventTable.setRowHeight(45);
        eventTable.getTableHeader().setFont(ModernUI.HEADING_FONT);
        eventTable.getTableHeader().setBackground(ModernUI.ACCENT_COLOR);
        eventTable.getTableHeader().setForeground(Color.WHITE);
        eventTable.setSelectionBackground(new Color(255, 230, 220));

        JScrollPane scrollPane = new JScrollPane(eventTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton viewBtn = ModernUI.createPrimaryButton("👁️ Vezi Detalii");
        viewBtn.addActionListener(e -> viewEventDetails());

        JButton bookBtn = ModernUI.createPrimaryButton("🎫 Rezervă Bilet");
        bookBtn.setBackground(ModernUI.SUCCESS_COLOR);
        bookBtn.addActionListener(e -> bookEvent());

        JButton deleteBtn = ModernUI.createDangerButton("🗑️ Șterge");
        deleteBtn.addActionListener(e -> deleteEvent());

        actionPanel.add(viewBtn);
        actionPanel.add(bookBtn);
        if (currentUser.getRol().equals("artist") || currentUser.getRol().equals("admin")) {
            actionPanel.add(deleteBtn);
        }

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private void loadEvents() {
        tableModel.setRowCount(0);
        List<Event> events = eventDAO.findAll(); // Changed from findUpcoming() to show all events
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (Event event : events) {
            Object[] row = {
                    event.getEventId(),
                    event.getTitlu(),
                    event.getNumeOrganizator() != null ? event.getNumeOrganizator() : "N/A",
                    event.getLocatie(),
                    event.getDataStart().format(formatter),
                    event.getLocuriRamase(),
                    event.getPretBilet()
            };
            tableModel.addRow(row);
        }
    }

    private void openAddEventDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Creează Eveniment", true);
        dialog.setSize(550, 700);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(ModernUI.BACKGROUND_COLOR);

        JTextField titleField = ModernUI.createTextField("");
        JTextArea descArea = ModernUI.createTextArea();
        descArea.setRows(4);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(450, 100));

        JTextField locationField = ModernUI.createTextField("");

        // Date and Time fields
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        datePanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JTextField dayField = new JTextField(5);
        JTextField monthField = new JTextField(5);
        JTextField yearField = new JTextField(8);
        JTextField hourField = new JTextField(5);
        JTextField minuteField = new JTextField(5);

        datePanel.add(new JLabel("Zi:"));
        datePanel.add(dayField);
        datePanel.add(new JLabel("Lună:"));
        datePanel.add(monthField);
        datePanel.add(new JLabel("An:"));
        datePanel.add(yearField);
        datePanel.add(new JLabel("Oră:"));
        datePanel.add(hourField);
        datePanel.add(new JLabel("Min:"));
        datePanel.add(minuteField);

        JTextField seatsField = ModernUI.createTextField("");
        JTextField priceField = ModernUI.createTextField("");

        addFieldToPanel(panel, "Titlu:", titleField);
        panel.add(ModernUI.createBodyLabel("Descriere:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(descScroll);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        addFieldToPanel(panel, "Locație:", locationField);
        panel.add(ModernUI.createBodyLabel("Data Start (zi/lună/an oră:min):"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(datePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        addFieldToPanel(panel, "Număr Locuri:", seatsField);
        addFieldToPanel(panel, "Preț Bilet (RON):", priceField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton saveBtn = ModernUI.createPrimaryButton("Salvează");
        JButton cancelBtn = ModernUI.createSecondaryButton("Anulează");

        saveBtn.addActionListener(e -> {
            try {
                int day = Integer.parseInt(dayField.getText());
                int month = Integer.parseInt(monthField.getText());
                int year = Integer.parseInt(yearField.getText());
                int hour = Integer.parseInt(hourField.getText());
                int minute = Integer.parseInt(minuteField.getText());

                LocalDateTime startDate = LocalDateTime.of(year, month, day, hour, minute);

                Event event = new Event();
                event.setOrganizerId(currentUser.getUserId());
                event.setTitlu(titleField.getText());
                event.setDescriere(descArea.getText());
                event.setLocatie(locationField.getText());
                event.setDataStart(startDate);
                event.setNumarLocuri(Integer.parseInt(seatsField.getText()));
                event.setPretBilet(new BigDecimal(priceField.getText()));

                eventDAO.insert(event);
                ModernUI.showSuccessMessage(dialog, "Eveniment creat cu succes!");
                loadEvents();
                dialog.dispose();
            } catch (Exception ex) {
                ModernUI.showErrorMessage(dialog, "Eroare la crearea evenimentului: " + ex.getMessage());
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

    private void viewEventDetails() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un eveniment!");
            return;
        }

        int eventId = (int) tableModel.getValueAt(selectedRow, 0);
        Event event = eventDAO.findById(eventId);

        if (event != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");

            String details = String.format(
                    "Titlu: %s\n\nOrganizator: %s\n\nLocație: %s\n\nData Start: %s\n\n" +
                            "Locuri Disponibile: %d / %d\n\nPreț Bilet: %.2f RON\n\nDescriere:\n%s",
                    event.getTitlu(),
                    event.getNumeOrganizator(),
                    event.getLocatie(),
                    event.getDataStart().format(formatter),
                    event.getLocuriRamase(),
                    event.getNumarLocuri(),
                    event.getPretBilet(),
                    event.getDescriere()
            );

            JTextArea textArea = new JTextArea(details);
            textArea.setFont(ModernUI.BODY_FONT);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(450, 350));

            JOptionPane.showMessageDialog(this, scrollPane, "Detalii Eveniment", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void bookEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un eveniment!");
            return;
        }

        int locuriLibere = (int) tableModel.getValueAt(selectedRow, 5);
        if (locuriLibere <= 0) {
            ModernUI.showErrorMessage(this, "Nu mai sunt locuri disponibile!");
            return;
        }

        int result = ModernUI.showConfirmDialog(this, "Doriți să rezervați un bilet pentru acest eveniment?");
        if (result == JOptionPane.YES_OPTION) {
            ModernUI.showSuccessMessage(this, "Bilet rezervat cu succes!");
            loadEvents();
        }
    }

    private void deleteEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un eveniment!");
            return;
        }

        int result = ModernUI.showConfirmDialog(this, "Sigur doriți să ștergeți acest eveniment?");
        if (result == JOptionPane.YES_OPTION) {
            int eventId = (int) tableModel.getValueAt(selectedRow, 0);
            eventDAO.delete(eventId);
            ModernUI.showSuccessMessage(this, "Eveniment șters cu succes!");
            loadEvents();
        }
    }
}