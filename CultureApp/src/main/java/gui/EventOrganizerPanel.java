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

public class EventOrganizerPanel extends JPanel {
    private User currentUser;
    private EventDAO eventDAO;
    private JTable myEventsTable;
    private DefaultTableModel tableModel;

    public EventOrganizerPanel(User user) {
        this.currentUser = user;
        this.eventDAO = new EventDAO();
        initComponents();
        loadMyEvents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(ModernUI.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = ModernUI.createTitleLabel("🎭 Evenimentele Mele");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton addEventBtn = ModernUI.createPrimaryButton("➕ Creează Eveniment Nou");
        addEventBtn.addActionListener(e -> openAddEventDialog());

        JButton refreshBtn = ModernUI.createSecondaryButton("🔄 Reîncarcă");
        refreshBtn.addActionListener(e -> loadMyEvents());

        buttonPanel.add(refreshBtn);
        buttonPanel.add(addEventBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Statistics Panel
        JPanel statsPanel = createStatsPanel();

        // Table
        String[] columns = {"ID", "Titlu", "Locație", "Data Start", "Locuri Ocupate", "Preț", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        myEventsTable = new JTable(tableModel);
        myEventsTable.setFont(ModernUI.BODY_FONT);
        myEventsTable.setRowHeight(45);
        myEventsTable.getTableHeader().setFont(ModernUI.HEADING_FONT);
        myEventsTable.getTableHeader().setBackground(ModernUI.ACCENT_COLOR);
        myEventsTable.getTableHeader().setForeground(Color.WHITE);
        myEventsTable.setSelectionBackground(new Color(255, 230, 220));

        JScrollPane scrollPane = new JScrollPane(myEventsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton viewBtn = ModernUI.createPrimaryButton("👁️ Vezi Detalii");
        viewBtn.addActionListener(e -> viewEventDetails());

        JButton viewBookingsBtn = ModernUI.createSecondaryButton("🎫 Vezi Rezervări");
        viewBookingsBtn.addActionListener(e -> viewBookings());

        JButton editBtn = ModernUI.createSecondaryButton("✏️ Editează");
        editBtn.addActionListener(e -> editEvent());

        JButton deleteBtn = ModernUI.createDangerButton("🗑️ Șterge");
        deleteBtn.addActionListener(e -> deleteEvent());

        actionPanel.add(viewBtn);
        actionPanel.add(viewBookingsBtn);
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

        List<Event> myEvents = eventDAO.findAll().stream()
                .filter(e -> e.getOrganizerId() == currentUser.getUserId())
                .collect(java.util.stream.Collectors.toList());

        int totalEvents = myEvents.size();
        int totalCapacity = myEvents.stream().mapToInt(Event::getNumarLocuri).sum();
        int totalBooked = myEvents.stream().mapToInt(e -> e.getNumarLocuri() - e.getLocuriRamase()).sum();

        statsPanel.add(createStatCard("📊 Total Evenimente", String.valueOf(totalEvents), ModernUI.PRIMARY_COLOR));
        statsPanel.add(createStatCard("👥 Capacitate Totală", String.valueOf(totalCapacity), ModernUI.ACCENT_COLOR));
        statsPanel.add(createStatCard("🎫 Bilete Rezervate", String.valueOf(totalBooked), ModernUI.SUCCESS_COLOR));

        return statsPanel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = ModernUI.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(300, 120));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ModernUI.BODY_FONT);
        titleLabel.setForeground(ModernUI.TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private void loadMyEvents() {
        tableModel.setRowCount(0);
        List<Event> allEvents = eventDAO.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (Event event : allEvents) {
            // Show only events organized by current user
            if (event.getOrganizerId() == currentUser.getUserId()) {
                String status = event.getDataStart().isAfter(LocalDateTime.now()) ? "Viitor" : "Trecut";
                int occupied = event.getNumarLocuri() - event.getLocuriRamase();

                Object[] row = {
                        event.getEventId(),
                        event.getTitlu(),
                        event.getLocatie(),
                        event.getDataStart().format(formatter),
                        occupied + "/" + event.getNumarLocuri(),
                        event.getPretBilet() + " RON",
                        status
                };
                tableModel.addRow(row);
            }
        }
    }

    private void openAddEventDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Creează Eveniment Nou", true);
        dialog.setSize(550, 700);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(ModernUI.BACKGROUND_COLOR);

        JLabel headerLabel = ModernUI.createHeadingLabel("✨ Creează un eveniment nou");
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField titleField = ModernUI.createTextField("");
        titleField.setMaximumSize(new Dimension(450, 40));

        JTextArea descArea = ModernUI.createTextArea();
        descArea.setRows(4);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(450, 100));
        descScroll.setMaximumSize(new Dimension(450, 100));

        JTextField locationField = ModernUI.createTextField("");
        locationField.setMaximumSize(new Dimension(450, 40));

        // Date and Time fields
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        datePanel.setBackground(ModernUI.BACKGROUND_COLOR);
        datePanel.setMaximumSize(new Dimension(450, 40));

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
        seatsField.setMaximumSize(new Dimension(450, 40));

        JTextField priceField = ModernUI.createTextField("0");
        priceField.setMaximumSize(new Dimension(450, 40));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        buttonPanel.setMaximumSize(new Dimension(450, 60));

        JButton saveBtn = ModernUI.createPrimaryButton("💾 Creează Eveniment");
        JButton cancelBtn = ModernUI.createSecondaryButton("✖️ Anulează");

        saveBtn.addActionListener(e -> {
            try {
                if (titleField.getText().trim().isEmpty() || locationField.getText().trim().isEmpty()) {
                    ModernUI.showErrorMessage(dialog, "Titlul și locația sunt obligatorii!");
                    return;
                }

                int day = Integer.parseInt(dayField.getText());
                int month = Integer.parseInt(monthField.getText());
                int year = Integer.parseInt(yearField.getText());
                int hour = Integer.parseInt(hourField.getText());
                int minute = Integer.parseInt(minuteField.getText());

                LocalDateTime startDate = LocalDateTime.of(year, month, day, hour, minute);

                Event event = new Event();
                event.setOrganizerId(currentUser.getUserId());
                event.setTitlu(titleField.getText().trim());
                event.setDescriere(descArea.getText().trim());
                event.setLocatie(locationField.getText().trim());
                event.setDataStart(startDate);
                event.setNumarLocuri(Integer.parseInt(seatsField.getText()));
                event.setPretBilet(new BigDecimal(priceField.getText()));

                eventDAO.insert(event);
                ModernUI.showSuccessMessage(dialog, "Eveniment creat cu succes! 🎉");
                loadMyEvents();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                ModernUI.showErrorMessage(dialog, "Date invalide! Verifică câmpurile numerice.");
            } catch (Exception ex) {
                ModernUI.showErrorMessage(dialog, "Eroare la crearea evenimentului: " + ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        panel.add(headerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(ModernUI.createBodyLabel("Titlu Eveniment:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(titleField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ModernUI.createBodyLabel("Descriere:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(descScroll);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ModernUI.createBodyLabel("Locație:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(locationField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ModernUI.createBodyLabel("Data și Ora Start (zi/lună/an oră:min):"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(datePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ModernUI.createBodyLabel("Număr Total Locuri:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(seatsField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ModernUI.createBodyLabel("Preț Bilet (RON) - 0 pentru gratuit:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(priceField);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(buttonPanel);

        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }

    private void viewEventDetails() {
        int selectedRow = myEventsTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un eveniment!");
            return;
        }

        int eventId = (int) tableModel.getValueAt(selectedRow, 0);
        Event event = eventDAO.findById(eventId);

        if (event != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
            int occupied = event.getNumarLocuri() - event.getLocuriRamase();
            double occupancyRate = (occupied * 100.0) / event.getNumarLocuri();

            String details = String.format(
                    "📋 DETALII EVENIMENT\n\n" +
                            "Titlu: %s\n\n" +
                            "📍 Locație: %s\n\n" +
                            "📅 Data Start: %s\n\n" +
                            "👥 Locuri:\n" +
                            "  • Total: %d locuri\n" +
                            "  • Ocupate: %d (%.1f%%)\n" +
                            "  • Disponibile: %d\n\n" +
                            "💰 Preț Bilet: %.2f RON\n\n" +
                            "📝 Descriere:\n%s",
                    event.getTitlu(),
                    event.getLocatie(),
                    event.getDataStart().format(formatter),
                    event.getNumarLocuri(),
                    occupied,
                    occupancyRate,
                    event.getLocuriRamase(),
                    event.getPretBilet(),
                    event.getDescriere()
            );

            JTextArea textArea = new JTextArea(details);
            textArea.setFont(ModernUI.BODY_FONT);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(this, scrollPane, "Detalii Eveniment", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewBookings() {
        int selectedRow = myEventsTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un eveniment!");
            return;
        }

        int eventId = (int) tableModel.getValueAt(selectedRow, 0);
        Event event = eventDAO.findById(eventId);

        if (event != null) {
            int occupied = event.getNumarLocuri() - event.getLocuriRamase();
            String message = String.format(
                    "🎫 Rezervări pentru: %s\n\n" +
                            "Total rezervări: %d\n" +
                            "Locuri disponibile: %d\n\n" +
                            "Funcționalitatea completă de vizualizare a listei de participanți\n" +
                            "este în dezvoltare și va include:\n" +
                            "• Lista completă a participanților\n" +
                            "• Datele de contact\n" +
                            "• Export în CSV/Excel\n" +
                            "• Statistici detaliate",
                    event.getTitlu(),
                    occupied,
                    event.getLocuriRamase()
            );

            ModernUI.showSuccessMessage(this, message);
        }
    }

    private void editEvent() {
        int selectedRow = myEventsTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un eveniment!");
            return;
        }

        ModernUI.showWarningMessage(this, "Funcționalitate în dezvoltare!\n\nÎn curând vei putea edita:\n• Titlul și descrierea\n• Locația\n• Data și ora\n• Prețul biletului");
    }

    private void deleteEvent() {
        int selectedRow = myEventsTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un eveniment!");
            return;
        }

        int eventId = (int) tableModel.getValueAt(selectedRow, 0);
        String eventTitle = (String) tableModel.getValueAt(selectedRow, 1);

        int result = ModernUI.showConfirmDialog(this,
                "Sigur doriți să ștergeți evenimentul \"" + eventTitle + "\"?\n\n" +
                        "⚠️ Această acțiune va anula toate rezervările existente!");

        if (result == JOptionPane.YES_OPTION) {
            eventDAO.delete(eventId);
            ModernUI.showSuccessMessage(this, "Eveniment șters cu succes!");
            loadMyEvents();
        }
    }
}
