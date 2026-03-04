package gui;

import dao.UserDAO;
import model.User;
import utils.ModernUI;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox;
    private UserDAO userDAO;

    public RegisterFrame() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("SpațiuCultural - Înregistrare");
        setSize(550, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(ModernUI.BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ModernUI.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(550, 100));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Creează Cont Nou");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Alătură-te comunității culturale");
        subtitleLabel.setFont(ModernUI.BODY_FONT);
        subtitleLabel.setForeground(new Color(230, 230, 255));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalGlue());

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Name Field
        JLabel nameLabel = ModernUI.createBodyLabel("Nume Complet");
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField = ModernUI.createTextField("");
        nameField.setMaximumSize(new Dimension(400, 40));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Email Field
        JLabel emailLabel = ModernUI.createBodyLabel("Email");
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField = ModernUI.createTextField("");
        emailField.setMaximumSize(new Dimension(400, 40));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Password Field
        JLabel passwordLabel = ModernUI.createBodyLabel("Parolă");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField = ModernUI.createPasswordField();
        passwordField.setMaximumSize(new Dimension(400, 40));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Confirm Password Field
        JLabel confirmPasswordLabel = ModernUI.createBodyLabel("Confirmă Parola");
        confirmPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmPasswordField = ModernUI.createPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(400, 40));
        confirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Role Selection
        JLabel roleLabel = ModernUI.createBodyLabel("Tip Utilizator");
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        String[] roles = {"User", "Artist"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(ModernUI.BODY_FONT);
        roleComboBox.setMaximumSize(new Dimension(400, 40));
        roleComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons
        JButton registerButton = ModernUI.createPrimaryButton("Înregistrează-te");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(400, 45));
        registerButton.addActionListener(e -> register());

        JButton backButton = ModernUI.createSecondaryButton("Înapoi la Login");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(400, 45));
        backButton.addActionListener(e -> backToLogin());

        formPanel.add(nameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(nameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(emailLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(confirmPasswordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(roleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(roleComboBox);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        formPanel.add(registerButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        formPanel.add(backButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void register() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = roleComboBox.getSelectedItem().toString().toLowerCase();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            ModernUI.showErrorMessage(this, "Vă rugăm completați toate câmpurile!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            ModernUI.showErrorMessage(this, "Parolele nu coincid!");
            return;
        }

        if (password.length() < 6) {
            ModernUI.showErrorMessage(this, "Parola trebuie să aibă minim 6 caractere!");
            return;
        }

        if (userDAO.findByEmail(email) != null) {
            ModernUI.showErrorMessage(this, "Email-ul este deja înregistrat!");
            return;
        }

        User user = new User(name, email, password);
        user.setRol(role);

        User insertedUser = userDAO.insert(user);

        if (insertedUser != null) {
            ModernUI.showSuccessMessage(this, "Cont creat cu succes! Vă puteți autentifica acum.");
            backToLogin();
        } else {
            ModernUI.showErrorMessage(this, "Eroare la crearea contului. Încercați din nou!");
        }
    }

    private void backToLogin() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
