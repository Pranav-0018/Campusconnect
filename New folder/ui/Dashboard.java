package ui;

import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;
    private FeedPage feedPage;
    private EventsPage eventsPage;
    private QueryPage queryPage;
    private ProfilePage profilePage;
    
    public Dashboard(User user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        setFrameProperties();
    }
    
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        
        // Initialize pages
        feedPage = new FeedPage(currentUser);
        eventsPage = new EventsPage(currentUser);
        queryPage = new QueryPage(currentUser);
        profilePage = new ProfilePage(currentUser);
        
        // Add tabs with icons
        tabbedPane.addTab("🏠 Feed", feedPage);
        tabbedPane.addTab("📅 Events", eventsPage);
        tabbedPane.addTab("💬 Queries", queryPage);
        tabbedPane.addTab("👤 Profile", profilePage);
        
        // Style the tabbed pane
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(138, 43, 226));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + 
            (currentUser.isTeacher() ? " ⭐" : ""));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        
        // Sign out button
        JButton signOutButton = new JButton("🚪 Sign Out");
        signOutButton.setFont(new Font("Arial", Font.BOLD, 12));
        signOutButton.setBackground(Color.WHITE);
        signOutButton.setForeground(new Color(138, 43, 226));
        signOutButton.setBorderPainted(false);
        signOutButton.setFocusPainted(false);
        signOutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        signOutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to sign out?", "Confirm Sign Out", 
                JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new LoginPage().setVisible(true);
            }
        });
        
        panel.add(welcomeLabel, BorderLayout.WEST);
        panel.add(signOutButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private void setFrameProperties() {
        setTitle("Campus Connect - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
