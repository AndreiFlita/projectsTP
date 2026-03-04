package gui;

import dao.ForumTopicDAO;
import dao.ForumPostDAO;
import model.ForumTopic;
import model.ForumPost;
import model.User;
import utils.ModernUI;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ForumPanel extends JPanel {
    private User currentUser;
    private ForumTopicDAO topicDAO;
    private ForumPostDAO postDAO;

    public ForumPanel(User user) {
        this.currentUser = user;
        this.topicDAO = new ForumTopicDAO();
        this.postDAO = new ForumPostDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(ModernUI.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Check if user is banned
        if ("banned".equals(currentUser.getState())) {
            showBannedMessage();
            return;
        }

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = ModernUI.createTitleLabel("💬 Forum Cultural");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JButton newTopicBtn = ModernUI.createPrimaryButton("➕ Topic Nou");
        newTopicBtn.addActionListener(e -> createNewTopic());

        JButton refreshBtn = ModernUI.createSecondaryButton("🔄 Reîncarcă");
        refreshBtn.addActionListener(e -> {
            removeAll();
            initComponents();
            revalidate();
            repaint();
        });

        buttonPanel.add(refreshBtn);
        buttonPanel.add(newTopicBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Categories Panel
        JPanel categoriesPanel = new JPanel();
        categoriesPanel.setLayout(new GridLayout(2, 3, 15, 15));
        categoriesPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        categoriesPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        categoriesPanel.add(createCategoryCard("📚 Literatură", "Discuții despre cărți și autori"));
        categoriesPanel.add(createCategoryCard("🎬 Film", "Recenzii și recomandări de filme"));
        categoriesPanel.add(createCategoryCard("🎨 Arte Vizuale", "Pictură, sculptură, fotografie"));
        categoriesPanel.add(createCategoryCard("🎭 Teatru", "Spectacole și piese de teatru"));
        categoriesPanel.add(createCategoryCard("🎵 Muzică", "Concerte și artiști"));
        categoriesPanel.add(createCategoryCard("🏛️ Cultură Generală", "Discuții generale despre artă"));

        // Recent Topics Panel
        JPanel recentPanel = ModernUI.createCardPanel();
        recentPanel.setLayout(new BoxLayout(recentPanel, BoxLayout.Y_AXIS));

        JLabel recentLabel = ModernUI.createHeadingLabel("📌 Discuții Recente");
        recentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        recentPanel.add(recentLabel);
        recentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Load recent topics from database
        List<ForumTopic> recentTopics = topicDAO.findAll();
        for (int i = 0; i < Math.min(10, recentTopics.size()); i++) {
            ForumTopic topic = recentTopics.get(i);
            recentPanel.add(createTopicRow(topic));
        }

        if (recentTopics.isEmpty()) {
            JLabel noTopicsLabel = new JLabel("Nu există discuții încă. Fii primul care creează un topic!");
            noTopicsLabel.setFont(ModernUI.BODY_FONT);
            noTopicsLabel.setForeground(ModernUI.TEXT_SECONDARY);
            noTopicsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            recentPanel.add(noTopicsLabel);
        }

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        centerPanel.add(categoriesPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(recentPanel), BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void showBannedMessage() {
        JPanel bannedPanel = new JPanel();
        bannedPanel.setLayout(new BoxLayout(bannedPanel, BoxLayout.Y_AXIS));
        bannedPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        bannedPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        JLabel iconLabel = new JLabel("🚫");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 72));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Acces Restricționat");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(ModernUI.WARNING_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea messageArea = new JTextArea(
                "Contul tău a fost suspendat temporar de către un administrator.\n\n" +
                        "Nu poți posta pe forum în acest moment.\n\n" +
                        "Pentru mai multe informații, te rugăm să contactezi echipa de administrare."
        );
        messageArea.setFont(ModernUI.BODY_FONT);
        messageArea.setForeground(ModernUI.TEXT_SECONDARY);
        messageArea.setEditable(false);
        messageArea.setOpaque(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageArea.setMaximumSize(new Dimension(600, 150));

        bannedPanel.add(iconLabel);
        bannedPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        bannedPanel.add(titleLabel);
        bannedPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        bannedPanel.add(messageArea);

        add(bannedPanel, BorderLayout.CENTER);
    }

    private JPanel createCategoryCard(String title, String description) {
        JPanel card = ModernUI.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(350, 130));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ModernUI.PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(ModernUI.BODY_FONT);
        descLabel.setForeground(ModernUI.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Count topics in this category
        String categoryName = title.substring(title.indexOf(" ") + 1).trim();
        List<ForumTopic> categoryTopics = topicDAO.findByCategory(categoryName);
        JLabel statsLabel = new JLabel(categoryTopics.size() + " topicuri");
        statsLabel.setFont(ModernUI.SMALL_FONT);
        statsLabel.setForeground(ModernUI.TEXT_SECONDARY);
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(descLabel);
        card.add(Box.createVerticalGlue());
        card.add(statsLabel);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 245, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(ModernUI.CARD_COLOR);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openCategory(categoryName);
            }
        });

        return card;
    }

    private JPanel createTopicRow(ForumTopic topic) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(ModernUI.CARD_COLOR);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(12, 0, 12, 0)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(ModernUI.CARD_COLOR);

        JLabel titleLabel = new JLabel(topic.getTitlu());
        titleLabel.setFont(ModernUI.BODY_FONT);
        titleLabel.setForeground(ModernUI.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String timeAgo = topic.getDataCrearii() != null ? topic.getDataCrearii().format(formatter) : "Recent";

        JLabel infoLabel = new JLabel(String.format("%s • de %s • %s • %d răspunsuri",
                topic.getCategorie(),
                topic.getNumeCreator() != null ? topic.getNumeCreator() : "Anonim",
                timeAgo,
                topic.getNumarPostari()));
        infoLabel.setFont(ModernUI.SMALL_FONT);
        infoLabel.setForeground(ModernUI.TEXT_SECONDARY);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(infoLabel);

        row.add(leftPanel, BorderLayout.CENTER);

        row.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                row.setBackground(new Color(248, 248, 252));
                leftPanel.setBackground(new Color(248, 248, 252));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                row.setBackground(ModernUI.CARD_COLOR);
                leftPanel.setBackground(ModernUI.CARD_COLOR);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openTopic(topic);
            }
        });

        return row;
    }

    private void createNewTopic() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Topic Nou", true);
        dialog.setSize(550, 550);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(ModernUI.BACKGROUND_COLOR);

        JLabel headerLabel = ModernUI.createHeadingLabel("Creează un topic nou");
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] categories = {"Literatură", "Film", "Arte Vizuale", "Teatru", "Muzică", "Cultură Generală"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setFont(ModernUI.BODY_FONT);
        categoryBox.setMaximumSize(new Dimension(450, 40));

        JTextField titleField = ModernUI.createTextField("");
        titleField.setMaximumSize(new Dimension(450, 40));

        JTextArea contentArea = ModernUI.createTextArea();
        contentArea.setRows(8);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setPreferredSize(new Dimension(450, 200));
        contentScroll.setMaximumSize(new Dimension(450, 200));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        buttonPanel.setMaximumSize(new Dimension(450, 60));

        JButton postBtn = ModernUI.createPrimaryButton("Postează");
        JButton cancelBtn = ModernUI.createSecondaryButton("Anulează");

        postBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();

            if (title.isEmpty() || content.isEmpty()) {
                ModernUI.showErrorMessage(dialog, "Completați toate câmpurile!");
                return;
            }

            // Create topic
            ForumTopic topic = new ForumTopic();
            topic.setTitlu(title);
            topic.setCategorie(categoryBox.getSelectedItem().toString());
            topic.setCreatDe(currentUser.getUserId());

            ForumTopic insertedTopic = topicDAO.insert(topic);

            if (insertedTopic != null) {
                // Create first post with content
                ForumPost post = new ForumPost();
                post.setTopicId(insertedTopic.getTopicId());
                post.setUserId(currentUser.getUserId());
                post.setContinut(content);

                postDAO.insert(post);

                ModernUI.showSuccessMessage(dialog, "Topic creat cu succes!");
                dialog.dispose();

                // Refresh panel
                removeAll();
                initComponents();
                revalidate();
                repaint();
            } else {
                ModernUI.showErrorMessage(dialog, "Eroare la crearea topicului!");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(postBtn);
        buttonPanel.add(cancelBtn);

        panel.add(headerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(ModernUI.createBodyLabel("Categorie:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(categoryBox);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ModernUI.createBodyLabel("Titlu:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(titleField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ModernUI.createBodyLabel("Conținut:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(contentScroll);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(buttonPanel);

        dialog.add(new JScrollPane(panel));
        dialog.setVisible(true);
    }

    private void openCategory(String category) {
        // Create dialog to show topics in category
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), category, true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ModernUI.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = ModernUI.createTitleLabel("📂 " + category);

        JPanel topicsPanel = new JPanel();
        topicsPanel.setLayout(new BoxLayout(topicsPanel, BoxLayout.Y_AXIS));
        topicsPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        List<ForumTopic> topics = topicDAO.findByCategory(category);

        for (ForumTopic topic : topics) {
            topicsPanel.add(createTopicRow(topic));
        }

        if (topics.isEmpty()) {
            JLabel noTopicsLabel = new JLabel("Nu există discuții în această categorie încă.");
            noTopicsLabel.setFont(ModernUI.BODY_FONT);
            noTopicsLabel.setForeground(ModernUI.TEXT_SECONDARY);
            topicsPanel.add(noTopicsLabel);
        }

        JButton closeBtn = ModernUI.createSecondaryButton("Închide");
        closeBtn.addActionListener(e -> dialog.dispose());

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(topicsPanel), BorderLayout.CENTER);
        panel.add(closeBtn, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void openTopic(ForumTopic topic) {
        // Create dialog to show topic and posts
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), topic.getTitlu(), true);
        dialog.setSize(900, 700);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = ModernUI.createTitleLabel(topic.getTitlu());
        JLabel categoryLabel = new JLabel("Categorie: " + topic.getCategorie());
        categoryLabel.setFont(ModernUI.BODY_FONT);
        categoryLabel.setForeground(ModernUI.TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(ModernUI.BACKGROUND_COLOR);
        titlePanel.add(titleLabel);
        titlePanel.add(categoryLabel);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // Posts panel
        JPanel postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        List<ForumPost> posts = postDAO.findByTopicId(topic.getTopicId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (ForumPost post : posts) {
            JPanel postCard = ModernUI.createCardPanel();
            postCard.setLayout(new BoxLayout(postCard, BoxLayout.Y_AXIS));
            postCard.setMaximumSize(new Dimension(850, Integer.MAX_VALUE));
            postCard.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel postHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
            postHeader.setBackground(ModernUI.CARD_COLOR);

            JLabel authorLabel = new JLabel("👤 " + (post.getNumeAutor() != null ? post.getNumeAutor() : "Anonim"));
            authorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            authorLabel.setForeground(ModernUI.PRIMARY_COLOR);

            JLabel dateLabel = new JLabel(" • " + (post.getDataPostarii() != null ? post.getDataPostarii().format(formatter) : ""));
            dateLabel.setFont(ModernUI.SMALL_FONT);
            dateLabel.setForeground(ModernUI.TEXT_SECONDARY);

            postHeader.add(authorLabel);
            postHeader.add(dateLabel);

            JTextArea contentArea = new JTextArea(post.getContinut());
            contentArea.setFont(ModernUI.BODY_FONT);
            contentArea.setForeground(ModernUI.TEXT_PRIMARY);
            contentArea.setEditable(false);
            contentArea.setOpaque(false);
            contentArea.setLineWrap(true);
            contentArea.setWrapStyleWord(true);
            contentArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            postCard.add(postHeader);
            postCard.add(contentArea);

            postsPanel.add(postCard);
            postsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(postsPanel);
        scrollPane.setBorder(null);

        // Reply section
        JPanel replyPanel = new JPanel(new BorderLayout());
        replyPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        replyPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JTextArea replyArea = ModernUI.createTextArea();
        replyArea.setRows(3);
        JScrollPane replyScroll = new JScrollPane(replyArea);

        JButton replyBtn = ModernUI.createPrimaryButton("💬 Răspunde");
        replyBtn.addActionListener(e -> {
            String content = replyArea.getText().trim();
            if (content.isEmpty()) {
                ModernUI.showWarningMessage(dialog, "Scrie un răspuns!");
                return;
            }

            ForumPost post = new ForumPost();
            post.setTopicId(topic.getTopicId());
            post.setUserId(currentUser.getUserId());
            post.setContinut(content);

            if (postDAO.insert(post) != null) {
                ModernUI.showSuccessMessage(dialog, "Răspuns adăugat!");
                dialog.dispose();
                openTopic(topic); // Reopen to show new post
            }
        });

        JButton closeBtn = ModernUI.createSecondaryButton("Închide");
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel replyButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        replyButtons.setBackground(ModernUI.BACKGROUND_COLOR);
        replyButtons.add(closeBtn);
        replyButtons.add(replyBtn);

        replyPanel.add(new JLabel("Răspunde:"), BorderLayout.NORTH);
        replyPanel.add(replyScroll, BorderLayout.CENTER);
        replyPanel.add(replyButtons, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(replyPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
}