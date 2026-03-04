package gui;

import dao.UserDAO;
import model.User;
import utils.ModernUI;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private UserDAO userDAO;

    public LoginFrame() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("SpațiuCultural - Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ModernUI.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(500, 120));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("SpațiuCultural");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Platformă Culturală Interactive");
        subtitleLabel.setFont(ModernUI.BODY_FONT);
        subtitleLabel.setForeground(new Color(230, 230, 255));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalGlue());

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel loginLabel = ModernUI.createHeadingLabel("Autentificare");
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = ModernUI.createBodyLabel("Email");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = ModernUI.createTextField("");
        emailField.setMaximumSize(new Dimension(350, 40));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordLabel = ModernUI.createBodyLabel("Parolă");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = ModernUI.createPasswordField();
        passwordField.setMaximumSize(new Dimension(350, 40));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = ModernUI.createPrimaryButton("Autentificare");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(350, 45));
        loginButton.addActionListener(e -> login());

        JButton registerButton = ModernUI.createSecondaryButton("Creează Cont Nou");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(350, 45));
        registerButton.addActionListener(e -> openRegisterFrame());

        formPanel.add(loginLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        formPanel.add(emailLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        formPanel.add(loginButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(registerButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            ModernUI.showErrorMessage(this, "Vă rugăm completați toate câmpurile!");
            return;
        }

        User user = userDAO.authenticate(email, password);

        if (user != null) {
            ModernUI.showSuccessMessage(this, "Bun venit, " + user.getNume() + "!");
            openMainFrame(user);
        } else {
            ModernUI.showErrorMessage(this, "Email sau parolă incorectă!");
        }
    }

    private void openRegisterFrame() {
        new RegisterFrame().setVisible(true);
        dispose();
    }

    private void openMainFrame(User user) {
        new MainFrame(user).setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
