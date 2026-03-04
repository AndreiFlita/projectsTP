package gui;

import model.User;
import utils.ModernUI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("SpațiuCultural - Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        // Top Navigation Bar
        JPanel navBar = createNavigationBar();

        // Content Area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        // Add different panels based on role
        contentPanel.add(createDashboardPanel(), "dashboard");
        contentPanel.add(new MarketplacePanel(currentUser), "marketplace");
        contentPanel.add(new EventsPanel(currentUser), "events");
        contentPanel.add(new ForumPanel(currentUser), "forum");
        contentPanel.add(new ProfilePanel(currentUser), "profile");

        // Journal panel - only for users and artists (not admin)
        if (!currentUser.getRol().equals("admin")) {
            contentPanel.add(new JournalPanel(currentUser), "journal");
        }

        // Event organizer panel for artists only
        if (currentUser.getRol().equals("artist")) {
            contentPanel.add(new EventOrganizerPanel(currentUser), "myevents");
        }

        // Admin panel if user is admin
        if (currentUser.getRol().equals("admin")) {
            contentPanel.add(new AdminPanel(currentUser), "admin");
        }

        mainPanel.add(navBar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createNavigationBar() {
        JPanel navBar = new JPanel();
        navBar.setLayout(new BorderLayout());
        navBar.setBackground(ModernUI.PRIMARY_COLOR);
        navBar.setPreferredSize(new Dimension(1200, 70));
        navBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left side - Logo and Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);

        JLabel logoLabel = new JLabel("🎨 SpațiuCultural");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        leftPanel.add(logoLabel);

        // Center - Navigation Buttons
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        centerPanel.setOpaque(false);

        JButton dashboardBtn = createNavButton("📊 Dashboard", "dashboard");
        centerPanel.add(dashboardBtn);

        // Marketplace and Events for all users
        JButton marketplaceBtn = createNavButton("🛍️ Marketplace", "marketplace");
        JButton eventsBtn = createNavButton("🎭 Evenimente", "events");
        centerPanel.add(marketplaceBtn);
        centerPanel.add(eventsBtn);

        // My Events button only for artists
        if (currentUser.getRol().equals("artist")) {
            JButton myEventsBtn = createNavButton("🎪 Evenimentele Mele", "myevents");
            centerPanel.add(myEventsBtn);
        }

        // Journal button only for users and artists (not admin)
        if (!currentUser.getRol().equals("admin")) {
            JButton journalBtn = createNavButton("📓 Jurnal", "journal");
            centerPanel.add(journalBtn);
        }

        // Forum for all users
        JButton forumBtn = createNavButton("💬 Forum", "forum");
        centerPanel.add(forumBtn);

        // Add admin button if user is admin
        if (currentUser.getRol().equals("admin")) {
            JButton adminBtn = createNavButton("👨‍💼 Admin", "admin");
            centerPanel.add(adminBtn);
        }

        // Right side - User Menu
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        JLabel userLabel = new JLabel("👤 " + currentUser.getNume());
        userLabel.setFont(ModernUI.BODY_FONT);
        userLabel.setForeground(Color.WHITE);

        JButton profileBtn = createSmallNavButton("Profil");
        profileBtn.addActionListener(e -> cardLayout.show(contentPanel, "profile"));

        JButton logoutBtn = createSmallNavButton("Logout");
        logoutBtn.addActionListener(e -> logout());

        rightPanel.add(userLabel);
        rightPanel.add(profileBtn);
        rightPanel.add(logoutBtn);

        navBar.add(leftPanel, BorderLayout.WEST);
        navBar.add(centerPanel, BorderLayout.CENTER);
        navBar.add(rightPanel, BorderLayout.EAST);

        return navBar;
    }

    private JButton createNavButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setFont(ModernUI.BODY_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(ModernUI.PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));

        button.addActionListener(e -> cardLayout.show(contentPanel, panelName));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ModernUI.SECONDARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ModernUI.PRIMARY_COLOR);
            }
        });

        return button;
    }

    private JButton createSmallNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(ModernUI.SMALL_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(90, 75, 180));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(80, 32));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ModernUI.SECONDARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(90, 75, 180));
            }
        });

        return button;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(ModernUI.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        JLabel titleLabel = ModernUI.createTitleLabel("Bun venit, " + currentUser.getNume() + "! 👋");
        headerPanel.add(titleLabel);

        // Stats Cards - different for admin
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        if (currentUser.getRol().equals("admin")) {
            statsPanel.add(createStatCard("👥 Utilizatori", "Gestionează utilizatorii platformei", ModernUI.PRIMARY_COLOR));
            statsPanel.add(createStatCard("💬 Forum", "Moderează discuțiile din forum", ModernUI.ACCENT_COLOR));
            statsPanel.add(createStatCard("⚙️ Administrare", "Controlează activitatea platformei", ModernUI.SUCCESS_COLOR));
        } else {
            statsPanel.add(createStatCard("🛍️ Produse", "Explorează marketplace-ul cultural", ModernUI.PRIMARY_COLOR));
            statsPanel.add(createStatCard("🎭 Evenimente", "Descoperă evenimente culturale", ModernUI.ACCENT_COLOR));
            statsPanel.add(createStatCard("💬 Forum", "Discută cu comunitatea", ModernUI.SUCCESS_COLOR));
        }

        // Welcome Message
        JPanel welcomePanel = ModernUI.createCardPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));

        JLabel welcomeTitle = ModernUI.createHeadingLabel("🎨 Despre SpațiuCultural");

        String welcomeTextContent;
        if (currentUser.getRol().equals("admin")) {
            welcomeTextContent = "Bine ai venit în panoul de administrator! Aici poți:\n\n" +
                    "• Modera utilizatorii platformei\n" +
                    "• Bana/Debana utilizatori care încalcă regulile\n" +
                    "• Schimba rolurile utilizatorilor (user, artist, admin)\n" +
                    "• Șterge conturi problematice\n" +
                    "• Monitoriza și modera discuțiile din forum\n" +
                    "• Șterge topicuri și postări inadecvate\n" +
                    "• Asigura un mediu sigur pentru toți utilizatorii";
        } else if (currentUser.getRol().equals("artist")) {
            welcomeTextContent = "SpațiuCultural este o platformă care îmbină comunitatea digitală cu promovarea " +
                    "artei și a creatorilor locali. Aici poți:\n\n" +
                    "• Vinde opere de artă prin Marketplace\n" +
                    "• Organiza și gestiona evenimente culturale\n" +
                    "• Crea un jurnal cultural personalizat\n" +
                    "• Participa în forumuri de discuții despre artă și cultură\n" +
                    "• Conecta cu alți pasionați de cultură";
        } else {
            welcomeTextContent = "SpațiuCultural este o platformă care îmbină comunitatea digitală cu promovarea " +
                    "artei și a creatorilor locali. Aici poți:\n\n" +
                    "• Explora și achiziționa opere de artă de la artiști locali\n" +
                    "• Descoperi și rezerva bilete la evenimente culturale\n" +
                    "• Crea un jurnal cultural personalizat\n" +
                    "• Participa în forumuri de discuții despre artă și cultură\n" +
                    "• Conecta cu alți pasionați de cultură";
        }

        JTextArea welcomeText = new JTextArea(welcomeTextContent);
        welcomeText.setFont(ModernUI.BODY_FONT);
        welcomeText.setForeground(ModernUI.TEXT_SECONDARY);
        welcomeText.setEditable(false);
        welcomeText.setOpaque(false);
        welcomeText.setLineWrap(true);
        welcomeText.setWrapStyleWord(true);

        welcomePanel.add(welcomeTitle);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        welcomePanel.add(welcomeText);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(welcomePanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatCard(String title, String description, Color accentColor) {
        JPanel card = ModernUI.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(300, 150));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(ModernUI.BODY_FONT);
        descLabel.setForeground(ModernUI.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(descLabel);

        return card;
    }

    private void logout() {
        int result = ModernUI.showConfirmDialog(this, "Sigur doriți să vă deconectați?");
        if (result == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}