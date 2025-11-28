package ui;

import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfilePage extends JPanel {
    private User currentUser;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField classField;
    private JTextField sectionField;
    private JTextArea bioArea;
    private boolean isEditing = false;
    
    public ProfilePage(User user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        loadUserData();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        nameField = new JTextField(30);
        emailField = new JTextField(30);
        classField = new JTextField(30);
        sectionField = new JTextField(30);
        bioArea = new JTextArea(5, 30);
        
        // Style components
        styleTextField(nameField);
        styleTextField(emailField);
        styleTextField(classField);
        styleTextField(sectionField);
        
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Initially disable editing
        setFieldsEditable(false);
    }
    
    private void setupLayout() {
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Profile icon and title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(248, 249, 250));
        
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 48));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(new Color(248, 249, 250));
        
        String titleText = "My Profile";
        if (currentUser.isTeacher()) {
            titleText += " ⭐";
        }
        
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(138, 43, 226));
        
        JLabel roleLabel = new JLabel(currentUser.isTeacher() ? "Teacher Account" : "Student Account");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roleLabel.setForeground(Color.GRAY);
        
        textPanel.add(titleLabel);
        textPanel.add(roleLabel);
        
        titlePanel.add(iconLabel);
        titlePanel.add(textPanel);
        
        panel.add(titlePanel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        
        // Class (only for students)
        if (currentUser.isStudent()) {
            gbc.gridx = 0; gbc.gridy = 2;
            panel.add(createLabel("Class:"), gbc);
            gbc.gridx = 1;
            panel.add(classField, gbc);
            
            // Section
            gbc.gridx = 0; gbc.gridy = 3;
            panel.add(createLabel("Section:"), gbc);
            gbc.gridx = 1;
            panel.add(sectionField, gbc);
        }
        
        // Bio
        int bioRow = currentUser.isStudent() ? 4 : 2;
        gbc.gridx = 0; gbc.gridy = bioRow;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(createLabel("Bio:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(bioArea), gbc);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(138, 43, 226));
        return label;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        
        JButton editButton = createStyledButton("✏️ Edit Profile", new Color(52, 152, 219));
        JButton saveButton = createStyledButton("💾 Save Changes", new Color(46, 204, 113));
        JButton cancelButton = createStyledButton("❌ Cancel", new Color(231, 76, 60));
        
        editButton.addActionListener(new EditActionListener());
        saveButton.addActionListener(new SaveActionListener());
        cancelButton.addActionListener(new CancelActionListener());
        
        // Initially show only edit button
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        
        panel.add(editButton);
        panel.add(saveButton);
        panel.add(cancelButton);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        
        // Add hover effect
        Color originalColor = backgroundColor;
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
        
        return button;
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }
    
    private void loadUserData() {
        nameField.setText(currentUser.getName());
        emailField.setText(currentUser.getEmail());
        
        // Load additional profile data (placeholder)
        if (currentUser.isStudent()) {
            classField.setText("Computer Science");
            sectionField.setText("A");
        }
        bioArea.setText("Hello! I'm " + currentUser.getName() + ", a " + 
            (currentUser.isTeacher() ? "teacher" : "student") + " at this university.");
    }
    
    private void setFieldsEditable(boolean editable) {
        nameField.setEditable(editable);
        // Email should not be editable
        emailField.setEditable(false);
        classField.setEditable(editable && currentUser.isStudent());
        sectionField.setEditable(editable && currentUser.isStudent());
        bioArea.setEditable(editable);
        
        // Change appearance based on editability
        Color bgColor = editable ? Color.WHITE : new Color(245, 245, 245);
        nameField.setBackground(bgColor);
        classField.setBackground(bgColor);
        sectionField.setBackground(bgColor);
        bioArea.setBackground(bgColor);
        
        // Email always disabled
        emailField.setBackground(new Color(245, 245, 245));
    }
    
    private class EditActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            isEditing = true;
            setFieldsEditable(true);
            
            // Toggle button visibility
            Component parent = ((JButton) e.getSource()).getParent();
            for (Component comp : parent.getComponents()) {
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    if (btn.getText().contains("Edit")) {
                        btn.setVisible(false);
                    } else {
                        btn.setVisible(true);
                    }
                }
            }
            parent.revalidate();
        }
    }
    
    private class SaveActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Here you would typically save to database
            // For now, just update the current user object
            currentUser.setName(nameField.getText().trim());
            
            JOptionPane.showMessageDialog(ProfilePage.this, 
                "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            isEditing = false;
            setFieldsEditable(false);
            
            // Toggle button visibility
            Component parent = ((JButton) e.getSource()).getParent();
            for (Component comp : parent.getComponents()) {
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    if (btn.getText().contains("Edit")) {
                        btn.setVisible(true);
                    } else {
                        btn.setVisible(false);
                    }
                }
            }
            parent.revalidate();
        }
    }
    
    private class CancelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Reload original data
            loadUserData();
            
            isEditing = false;
            setFieldsEditable(false);
            
            // Toggle button visibility
            Component parent = ((JButton) e.getSource()).getParent();
            for (Component comp : parent.getComponents()) {
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    if (btn.getText().contains("Edit")) {
                        btn.setVisible(true);
                    } else {
                        btn.setVisible(false);
                    }
                }
            }
            parent.revalidate();
        }
    }
}
