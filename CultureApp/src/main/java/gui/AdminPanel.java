package gui;

import dao.UserDAO;
import dao.ForumTopicDAO;
import dao.ForumPostDAO;
import model.User;
import model.ForumTopic;
import model.ForumPost;
import utils.ModernUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminPanel extends JPanel {
    private User currentUser;
    private UserDAO userDAO;
    private ForumTopicDAO topicDAO;
    private ForumPostDAO postDAO;

    private JTable usersTable;
    private DefaultTableModel usersTableModel;

    public AdminPanel(User user) {
        this.currentUser = user;
        this.userDAO = new UserDAO();
        this.topicDAO = new ForumTopicDAO();
        this.postDAO = new ForumPostDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(ModernUI.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = ModernUI.createTitleLabel("👨‍💼 Panou Administrator");

        JButton refreshBtn = ModernUI.createSecondaryButton("🔄 Actualizează");
        refreshBtn.addActionListener(e -> refreshCurrentTab());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        // Tabbed Pane with only 2 tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(ModernUI.BODY_FONT);
        tabbedPane.setBackground(ModernUI.BACKGROUND_COLOR);

        tabbedPane.addTab("👥 Utilizatori", createUsersPanel());
        tabbedPane.addTab("💬 Forum", createForumPanel());

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernUI.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Statistics Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        List<User> allUsers = userDAO.findAll();
        int totalUsers = allUsers.size();
        int totalArtists = (int) allUsers.stream().filter(u -> u.getRol().equals("artist")).count();
        int bannedUsers = (int) allUsers.stream().filter(u -> "banned".equals(u.getState())).count();

        statsPanel.add(createStatCard("👥 Total Utilizatori", String.valueOf(totalUsers), ModernUI.PRIMARY_COLOR));
        statsPanel.add(createStatCard("🎨 Total Artiști", String.valueOf(totalArtists), ModernUI.ACCENT_COLOR));
        statsPanel.add(createStatCard("🚫 Utilizatori Banați", String.valueOf(bannedUsers), ModernUI.WARNING_COLOR));

        // Table
        String[] columns = {"ID", "Nume", "Email", "Rol", "Status", "Data Înregistrării"};
        usersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        usersTable = new JTable(usersTableModel);
        usersTable.setFont(ModernUI.BODY_FONT);
        usersTable.setRowHeight(40);
        usersTable.getTableHeader().setFont(ModernUI.HEADING_FONT);
        usersTable.getTableHeader().setBackground(ModernUI.PRIMARY_COLOR);
        usersTable.getTableHeader().setForeground(Color.WHITE);
        usersTable.setSelectionBackground(new Color(220, 220, 240));

        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        loadUsers();

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton viewUserBtn = ModernUI.createPrimaryButton("👁️ Vezi Detalii");
        viewUserBtn.addActionListener(e -> viewUserDetails());

        JButton changeRoleBtn = ModernUI.createSecondaryButton("🔄 Schimbă Rol");
        changeRoleBtn.addActionListener(e -> changeUserRole());

        JButton banUserBtn = ModernUI.createDangerButton("🚫 Banează/Debanează");
        banUserBtn.addActionListener(e -> toggleBanUser());

        JButton deleteUserBtn = ModernUI.createDangerButton("🗑️ Șterge");
        deleteUserBtn.addActionListener(e -> deleteUser());

        actionPanel.add(viewUserBtn);
        actionPanel.add(changeRoleBtn);
        actionPanel.add(banUserBtn);
        actionPanel.add(deleteUserBtn);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createForumPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernUI.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = ModernUI.createHeadingLabel("📋 Toate Discuțiile din Forum");

        JPanel topicsPanel = new JPanel();
        topicsPanel.setLayout(new BoxLayout(topicsPanel, BoxLayout.Y_AXIS));
        topicsPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        List<ForumTopic> allTopics = topicDAO.findAll();

        for (ForumTopic topic : allTopics) {
            topicsPanel.add(createForumTopicCard(topic));
        }

        if (allTopics.isEmpty()) {
            JLabel noTopicsLabel = new JLabel("Nu există discuții în forum încă.");
            noTopicsLabel.setFont(ModernUI.BODY_FONT);
            noTopicsLabel.setForeground(ModernUI.TEXT_SECONDARY);
            topicsPanel.add(noTopicsLabel);
        }

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel statsLabel = new JLabel(String.format("Total: %d topicuri", allTopics.size()));
        statsLabel.setFont(ModernUI.BODY_FONT);
        statsLabel.setForeground(ModernUI.TEXT_SECONDARY);
        headerPanel.add(statsLabel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(topicsPanel), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createForumTopicCard(ForumTopic topic) {
        JPanel card = ModernUI.createCardPanel();
        card.setLayout(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createCompoundBorder(
                card.getBorder(),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(ModernUI.CARD_COLOR);

        JLabel titleLabel = new JLabel(topic.getTitlu());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(ModernUI.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String infoText = String.format("📂 %s • 👤 %s • 📅 %s • 💬 %d răspunsuri",
                topic.getCategorie(),
                topic.getNumeCreator() != null ? topic.getNumeCreator() : "Anonim",
                topic.getDataCrearii() != null ? topic.getDataCrearii().format(formatter) : "N/A",
                topic.getNumarPostari());

        JLabel infoLabel = new JLabel(infoText);
        infoLabel.setFont(ModernUI.SMALL_FONT);
        infoLabel.setForeground(ModernUI.TEXT_SECONDARY);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        leftPanel.add(infoLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(ModernUI.CARD_COLOR);

        JButton viewBtn = ModernUI.createSecondaryButton("👁️ Vezi");
        viewBtn.addActionListener(e -> viewTopicDetails(topic));

        JButton deleteBtn = ModernUI.createDangerButton("🗑️");
        deleteBtn.addActionListener(e -> deleteTopic(topic));

        rightPanel.add(viewBtn);
        rightPanel.add(deleteBtn);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 245, 250));
                leftPanel.setBackground(new Color(245, 245, 250));
                rightPanel.setBackground(new Color(245, 245, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(ModernUI.CARD_COLOR);
                leftPanel.setBackground(ModernUI.CARD_COLOR);
                rightPanel.setBackground(ModernUI.CARD_COLOR);
            }
        });

        return card;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = ModernUI.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(250, 120));

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

    private void loadUsers() {
        usersTableModel.setRowCount(0);
        List<User> users = userDAO.findAll();

        for (User user : users) {
            String status = "banned".equals(user.getState()) ? "BANAT" : "ACTIV";
            Object[] row = {
                    user.getUserId(),
                    user.getNume(),
                    user.getEmail(),
                    user.getRol().toUpperCase(),
                    status,
                    user.getDataInregistrarii() != null ?
                            user.getDataInregistrarii().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "N/A"
            };
            usersTableModel.addRow(row);
        }
    }

    private void viewUserDetails() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un utilizator!");
            return;
        }

        int userId = (int) usersTableModel.getValueAt(selectedRow, 0);
        User user = userDAO.findById(userId);

        if (user != null) {
            String status = "banned".equals(user.getState()) ? "BANAT" : "ACTIV";
            String details = String.format(
                    "ID: %d\n\nNume: %s\n\nEmail: %s\n\nRol: %s\n\nStatus: %s\n\nData Înregistrării: %s\n\nBio:\n%s\n\nInterese:\n%s",
                    user.getUserId(),
                    user.getNume(),
                    user.getEmail(),
                    user.getRol().toUpperCase(),
                    status,
                    user.getDataInregistrarii() != null ?
                            user.getDataInregistrarii().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm")) : "N/A",
                    user.getBio() != null ? user.getBio() : "Nu a completat",
                    user.getInterese() != null ? user.getInterese() : "Nu a completat"
            );

            JTextArea textArea = new JTextArea(details);
            textArea.setFont(ModernUI.BODY_FONT);
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 350));

            JOptionPane.showMessageDialog(this, scrollPane, "Detalii Utilizator", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void changeUserRole() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un utilizator!");
            return;
        }

        int userId = (int) usersTableModel.getValueAt(selectedRow, 0);
        User user = userDAO.findById(userId);

        if (user != null) {
            if ("banned".equals(user.getState())) {
                ModernUI.showWarningMessage(this, "Utilizatorul este banat! Debanează-l mai întâi.");
                return;
            }

            String[] roles = {"user", "artist", "admin"};
            String newRole = (String) JOptionPane.showInputDialog(
                    this,
                    "Selectați noul rol pentru " + user.getNume() + ":",
                    "Schimbă Rol",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    roles,
                    user.getRol()
            );

            if (newRole != null && !newRole.equals(user.getRol())) {
                user.setRol(newRole);
                userDAO.update(user);
                ModernUI.showSuccessMessage(this, "Rol schimbat cu succes!");
                loadUsers();
            }
        }
    }

    private void toggleBanUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un utilizator!");
            return;
        }

        int userId = (int) usersTableModel.getValueAt(selectedRow, 0);
        User user = userDAO.findById(userId);

        if (user != null) {
            if (userId == currentUser.getUserId()) {
                ModernUI.showErrorMessage(this, "Nu te poți bana pe tine însuți!");
                return;
            }

            if (user.getRol().equals("admin") && !"banned".equals(user.getState())) {
                ModernUI.showErrorMessage(this, "Nu poți bana alți administratori!");
                return;
            }

            String message;
            if ("banned".equals(user.getState())) {
                message = "Sigur doriți să debanați utilizatorul " + user.getNume() + "?";
            } else {
                message = "Sigur doriți să banați utilizatorul " + user.getNume() + "?\n" +
                        "Acesta nu va mai putea posta pe forumuri!";
            }

            int result = ModernUI.showConfirmDialog(this, message);

            if (result == JOptionPane.YES_OPTION) {
                if ("banned".equals(user.getState())) {
                    user.setState("active");
                    ModernUI.showSuccessMessage(this, "Utilizator debanat cu succes!");
                } else {
                    user.setState("banned");
                    ModernUI.showSuccessMessage(this, "Utilizator banat cu succes!");
                }

                userDAO.update(user);
                loadUsers();
            }
        }
    }

    private void deleteUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            ModernUI.showWarningMessage(this, "Selectați un utilizator!");
            return;
        }

        int userId = (int) usersTableModel.getValueAt(selectedRow, 0);
        String userName = (String) usersTableModel.getValueAt(selectedRow, 1);

        if (userId == currentUser.getUserId()) {
            ModernUI.showErrorMessage(this, "Nu te poți șterge pe tine însuți!");
            return;
        }

        int result = ModernUI.showConfirmDialog(this,
                "Sigur doriți să ștergeți utilizatorul " + userName + "?\nAceastă acțiune este ireversibilă!");

        if (result == JOptionPane.YES_OPTION) {
            userDAO.delete(userId);
            ModernUI.showSuccessMessage(this, "Utilizator șters cu succes!");
            loadUsers();
        }
    }

    private void viewTopicDetails(ForumTopic topic) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), topic.getTitlu(), true);
        dialog.setSize(900, 700);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = ModernUI.createTitleLabel(topic.getTitlu());
        JLabel infoLabel = new JLabel(String.format("Categorie: %s | Creat de: %s",
                topic.getCategorie(),
                topic.getNumeCreator() != null ? topic.getNumeCreator() : "Anonim"));
        infoLabel.setFont(ModernUI.BODY_FONT);
        infoLabel.setForeground(ModernUI.TEXT_SECONDARY);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        headerPanel.add(titleLabel);
        headerPanel.add(infoLabel);

        JPanel postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        List<ForumPost> posts = postDAO.findByTopicId(topic.getTopicId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (ForumPost post : posts) {
            JPanel postCard = ModernUI.createCardPanel();
            postCard.setLayout(new BoxLayout(postCard, BoxLayout.Y_AXIS));
            postCard.setMaximumSize(new Dimension(850, Integer.MAX_VALUE));

            JLabel authorLabel = new JLabel("👤 " + (post.getNumeAutor() != null ? post.getNumeAutor() : "Anonim") +
                    " • " + (post.getDataPostarii() != null ? post.getDataPostarii().format(formatter) : ""));
            authorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            authorLabel.setForeground(ModernUI.PRIMARY_COLOR);

            JTextArea contentArea = new JTextArea(post.getContinut());
            contentArea.setFont(ModernUI.BODY_FONT);
            contentArea.setEditable(false);
            contentArea.setOpaque(false);
            contentArea.setLineWrap(true);
            contentArea.setWrapStyleWord(true);
            contentArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

            postCard.add(authorLabel);
            postCard.add(contentArea);

            postsPanel.add(postCard);
            postsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JButton closeBtn = ModernUI.createSecondaryButton("Închide");
        closeBtn.addActionListener(e -> dialog.dispose());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(postsPanel), BorderLayout.CENTER);
        mainPanel.add(closeBtn, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void deleteTopic(ForumTopic topic) {
        int result = ModernUI.showConfirmDialog(this,
                "Sigur doriți să ștergeți topicul \"" + topic.getTitlu() + "\"?\n" +
                        "Toate răspunsurile vor fi șterse de asemenea!");

        if (result == JOptionPane.YES_OPTION) {
            topicDAO.delete(topic.getTopicId());
            ModernUI.showSuccessMessage(this, "Topic șters cu succes!");
            refreshCurrentTab();
        }
    }

    private void refreshCurrentTab() {
        removeAll();
        initComponents();
        revalidate();
        repaint();
    }
}