package ui;

import database.DBConnection;
import models.User;
import services.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private UserService userService;
    
    public LoginPage() {
        userService = new UserService();
        initializeComponents();
        setupLayout();
        setFrameProperties();
    }
    
    private void initializeComponents() {
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        roleComboBox = new JComboBox<>(new String[]{"student", "teacher"});
        
        // Style components
        styleTextField(emailField);
        styleTextField(passwordField);
        styleComboBox(roleComboBox);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Create gradient background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(138, 43, 226),  // Purple
                    getWidth(), getHeight(), new Color(30, 144, 255)  // Blue
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        
        // Create login panel
        JPanel loginPanel = createLoginPanel();
        backgroundPanel.add(loginPanel);
        
        add(backgroundPanel, BorderLayout.CENTER);
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("Campus Connect");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(138, 43, 226));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Connect. Learn. Grow.");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        panel.add(subtitleLabel, gbc);
        
        // Role selection
        gbc.gridwidth = 1;
        gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Login as:"), gbc);
        gbc.gridx = 1;
        panel.add(roleComboBox, gbc);
        
        // Email
        gbc.gridy = 3; gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        // Password
        gbc.gridy = 4; gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        // Login button
        JButton loginButton = createStyledButton("Login");
        loginButton.addActionListener(new LoginActionListener());
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(loginButton, gbc);
        
        return panel;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(138, 43, 226));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(120, 30, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(138, 43, 226));
            }
        });
        
        return button;
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
    }
    
    private void setFrameProperties() {
        setTitle("Campus Connect - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginPage.this, 
                    "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Test database connection first
            if (!DBConnection.testConnection()) {
                JOptionPane.showMessageDialog(LoginPage.this, 
                    "Database connection failed! Using demo mode.", "Warning", JOptionPane.WARNING_MESSAGE);
                // Demo login for testing
                if (email.equals("teacher@demo.com") && password.equals("teacher123")) {
                    User demoTeacher = new User(1, "Demo Teacher", "teacher@demo.com", "teacher");
                    openDashboard(demoTeacher);
                    return;
                } else if (email.equals("student@demo.com") && password.equals("student123")) {
                    User demoStudent = new User(2, "Demo Student", "student@demo.com", "student");
                    openDashboard(demoStudent);
                    return;
                }
            }
            
            User user = userService.authenticate(email, password, role);
            
            if (user != null) {
                JOptionPane.showMessageDialog(LoginPage.this, 
                    "Login successful! Welcome " + user.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
                openDashboard(user);
            } else {
                JOptionPane.showMessageDialog(LoginPage.this, 
                    "Invalid credentials! Try demo accounts:\nTeacher: teacher@demo.com / teacher123\nStudent: student@demo.com / student123", 
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openDashboard(User user) {
        dispose();
        SwingUtilities.invokeLater(() -> {
            new Dashboard(user).setVisible(true);
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginPage().setVisible(true);
        });
    }
}
