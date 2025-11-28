package ui;

import models.User;
import services.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserService userService;
    
    public LoginPage() {
        userService = new UserService();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        setTitle("Campus Connect - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeComponents() {
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(102, 51, 153));
        JLabel titleLabel = new JLabel("Campus Connect");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationDialog();
            }
        });
    }
    
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = userService.loginUser(email, password);
        
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + user.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Open dashboard based on user role
            this.dispose();
            new Dashboard(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openRegistrationDialog() {
        JDialog registerDialog = new JDialog(this, "Register New User", true);
        registerDialog.setSize(350, 400);
        registerDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField nameField = new JTextField(20);
        JTextField regEmailField = new JTextField(20);
        JPasswordField regPasswordField = new JPasswordField(20);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"student", "teacher"});
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(regEmailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(regPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);
        
        JButton regButton = new JButton("Register");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(regButton, gbc);
        
        regButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = regEmailField.getText().trim();
            String password = new String(regPasswordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();
            
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(registerDialog, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (userService.emailExists(email)) {
                JOptionPane.showMessageDialog(registerDialog, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User newUser = new User(name, email, password, role);
            if (userService.registerUser(newUser)) {
                JOptionPane.showMessageDialog(registerDialog, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                registerDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(registerDialog, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        registerDialog.add(panel);
        registerDialog.setVisible(true);
    }
}
