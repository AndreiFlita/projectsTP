package gui;

import dao.UserDAO;
import model.User;
import utils.ModernUI;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    private User currentUser;
    private UserDAO userDAO;
    private JTextField nameField;
    private JTextField emailField;
    private JTextArea bioArea;
    private JTextArea interesseArea;

    public ProfilePanel(User user) {
        this.currentUser = user;
        this.userDAO = new UserDAO();
        initComponents();
        loadUserData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(ModernUI.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JLabel titleLabel = ModernUI.createTitleLabel("👤 Profilul Meu");
        headerPanel.add(titleLabel);

        // Main Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Profile Card
        JPanel profileCard = ModernUI.createCardPanel();
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setPreferredSize(new Dimension(700, 600));

        // Avatar Section
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        avatarPanel.setBackground(ModernUI.CARD_COLOR);

        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        avatarLabel.setForeground(ModernUI.PRIMARY_COLOR);

        JPanel avatarCircle = new JPanel();
        avatarCircle.setLayout(new BorderLayout());
        avatarCircle.setBackground(new Color(240, 240, 250));
        avatarCircle.setPreferredSize(new Dimension(150, 150));
        avatarCircle.setBorder(BorderFactory.createLineBorder(ModernUI.PRIMARY_COLOR, 3));
        avatarCircle.add(avatarLabel, BorderLayout.CENTER);

        avatarPanel.add(avatarCircle);

        profileCard.add(avatarPanel);
        profileCard.add(Box.createRigidArea(new Dimension(0, 20)));

        // Name Field
        JLabel nameLabel = ModernUI.createBodyLabel("Nume Complet:");
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameField = ModernUI.createTextField("");
        nameField.setMaximumSize(new Dimension(600, 40));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        profileCard.add(nameLabel);
        profileCard.add(Box.createRigidArea(new Dimension(0, 5)));
        profileCard.add(nameField);
        profileCard.add(Box.createRigidArea(new Dimension(0, 15)));

        // Email Field
        JLabel emailLabel = ModernUI.createBodyLabel("Email:");
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField = ModernUI.createTextField("");
        emailField.setMaximumSize(new Dimension(600, 40));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        profileCard.add(emailLabel);
        profileCard.add(Box.createRigidArea(new Dimension(0, 5)));
        profileCard.add(emailField);
        profileCard.add(Box.createRigidArea(new Dimension(0, 15)));

        // Role Display
        JLabel roleLabel = ModernUI.createBodyLabel("Rol:");
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleValue = new JLabel(currentUser.getRol().toUpperCase());
        roleValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleValue.setForeground(ModernUI.PRIMARY_COLOR);
        roleValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        profileCard.add(roleLabel);
        profileCard.add(Box.createRigidArea(new Dimension(0, 5)));
        profileCard.add(roleValue);
        profileCard.add(Box.createRigidArea(new Dimension(0, 15)));

        // Bio Area
        JLabel bioLabel = ModernUI.createBodyLabel("Biografie:");
        bioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        bioArea = ModernUI.createTextArea();
        bioArea.setRows(4);
        JScrollPane bioScroll = new JScrollPane(bioArea);
        bioScroll.setPreferredSize(new Dimension(600, 100));
        bioScroll.setMaximumSize(new Dimension(600, 100));
        bioScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        profileCard.add(bioLabel);
        profileCard.add(Box.createRigidArea(new Dimension(0, 5)));
        profileCard.add(bioScroll);
        profileCard.add(Box.createRigidArea(new Dimension(0, 15)));

        // Interese Area
        JLabel interesseLabel = ModernUI.createBodyLabel("Interese:");
        interesseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        interesseArea = ModernUI.createTextArea();
        interesseArea.setRows(3);
        JScrollPane interesseScroll = new JScrollPane(interesseArea);
        interesseScroll.setPreferredSize(new Dimension(600, 80));
        interesseScroll.setMaximumSize(new Dimension(600, 80));
        interesseScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        profileCard.add(interesseLabel);
        profileCard.add(Box.createRigidArea(new Dimension(0, 5)));
        profileCard.add(interesseScroll);
        profileCard.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(ModernUI.CARD_COLOR);
        buttonPanel.setMaximumSize(new Dimension(600, 60));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveBtn = ModernUI.createPrimaryButton("💾 Salvează Modificări");
        saveBtn.addActionListener(e -> saveProfile());

        JButton changePasswordBtn = ModernUI.createSecondaryButton("🔒 Schimbă Parola");
        changePasswordBtn.addActionListener(e -> changePassword());

        buttonPanel.add(saveBtn);
        buttonPanel.add(changePasswordBtn);

        profileCard.add(buttonPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(profileCard, gbc);

        // Statistics Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        statsPanel.add(createStatCard("🗓️", "Membru din", "Decembrie 2024"));
        statsPanel.add(createStatCard("📊", "Nivel Activitate", "Activ"));
        statsPanel.add(createStatCard("⭐", "Puncte", "0"));

        gbc.gridy = 1;
        contentPanel.add(statsPanel, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
    }

    private JPanel createStatCard(String icon, String label, String value) {
        JPanel card = ModernUI.createCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(220, 120));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(ModernUI.SMALL_FONT);
        labelLabel.setForeground(ModernUI.TEXT_SECONDARY);
        labelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(ModernUI.PRIMARY_COLOR);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(labelLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private void loadUserData() {
        nameField.setText(currentUser.getNume());
        emailField.setText(currentUser.getEmail());
        bioArea.setText(currentUser.getBio() != null ? currentUser.getBio() : "");
        interesseArea.setText(currentUser.getInterese() != null ? currentUser.getInterese() : "");
    }

    private void saveProfile() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String bio = bioArea.getText().trim();
        String interese = interesseArea.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            ModernUI.showErrorMessage(this, "Numele și email-ul sunt obligatorii!");
            return;
        }

        currentUser.setNume(name);
        currentUser.setEmail(email);
        currentUser.setBio(bio);
        currentUser.setInterese(interese);

        userDAO.update(currentUser);
        ModernUI.showSuccessMessage(this, "Profil actualizat cu succes!");
    }

    private void changePassword() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Schimbă Parola", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        panel.setBackground(ModernUI.BACKGROUND_COLOR);

        JLabel headerLabel = ModernUI.createHeadingLabel("🔒 Schimbă Parola");
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField currentPasswordField = ModernUI.createPasswordField();
        currentPasswordField.setMaximumSize(new Dimension(350, 40));
        currentPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField newPasswordField = ModernUI.createPasswordField();
        newPasswordField.setMaximumSize(new Dimension(350, 40));
        newPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField confirmPasswordField = ModernUI.createPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(350, 40));
        confirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(ModernUI.BACKGROUND_COLOR);
        buttonPanel.setMaximumSize(new Dimension(350, 60));

        JButton changeBtn = ModernUI.createPrimaryButton("Schimbă");
        JButton cancelBtn = ModernUI.createSecondaryButton("Anulează");

        changeBtn.addActionListener(e -> {
            String currentPwd = new String(currentPasswordField.getPassword());
            String newPwd = new String(newPasswordField.getPassword());
            String confirmPwd = new String(confirmPasswordField.getPassword());

            if (currentPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
                ModernUI.showErrorMessage(dialog, "Completați toate câmpurile!");
                return;
            }

            if (!currentPwd.equals(currentUser.getParola())) {
                ModernUI.showErrorMessage(dialog, "Parola curentă este incorectă!");
                return;
            }

            if (!newPwd.equals(confirmPwd)) {
                ModernUI.showErrorMessage(dialog, "Noile parole nu coincid!");
                return;
            }

            if (newPwd.length() < 6) {
                ModernUI.showErrorMessage(dialog, "Parola trebuie să aibă minim 6 caractere!");
                return;
            }

            currentUser.setParola(newPwd);
            userDAO.update(currentUser);
            ModernUI.showSuccessMessage(dialog, "Parola a fost schimbată cu succes!");
            dialog.dispose();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(changeBtn);
        buttonPanel.add(cancelBtn);

        panel.add(headerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(ModernUI.createBodyLabel("Parola Curentă:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(currentPasswordField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ModernUI.createBodyLabel("Parola Nouă:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(newPasswordField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ModernUI.createBodyLabel("Confirmă Parola Nouă:"));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(confirmPasswordField);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(buttonPanel);

        dialog.add(panel);
        dialog.setVisible(true);
    }
}
