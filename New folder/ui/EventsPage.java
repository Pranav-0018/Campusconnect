package ui;

import models.Event;
import models.User;
import services.EventService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventsPage extends JPanel {
    private User currentUser;
    private EventService eventService;
    private JPanel eventsPanel;
    private JScrollPane scrollPane;
    
    public EventsPage(User user) {
        this.currentUser = user;
        this.eventService = new EventService();
        initializeComponents();
        setupLayout();
        loadEvents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        eventsPanel.setBackground(Color.WHITE);
        
        scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    private void setupLayout() {
        // Top panel
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel for events
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("📅 Campus Events & Hackathons");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(138, 43, 226));
        
        panel.add(titleLabel, BorderLayout.WEST);
        
        // Add event button for teachers only
        if (currentUser.isTeacher()) {
            JButton addEventButton = createStyledButton("➕ Add Event", new Color(46, 204, 113));
            addEventButton.addActionListener(new AddEventActionListener());
            panel.add(addEventButton, BorderLayout.EAST);
        }
        
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
        button.setPreferredSize(new Dimension(130, 35));
        
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
    
    private void loadEvents() {
        eventsPanel.removeAll();
        
        List<Event> events = eventService.getAllEvents();
        
        if (events.isEmpty()) {
            JLabel noEventsLabel = new JLabel("No events scheduled yet.");
            noEventsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noEventsLabel.setForeground(Color.GRAY);
            noEventsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            eventsPanel.add(noEventsLabel);
        } else {
            for (Event event : events) {
                JPanel eventPanel = createEventPanel(event);
                eventsPanel.add(eventPanel);
                eventsPanel.add(Box.createVerticalStrut(15));
            }
        }
        
        eventsPanel.revalidate();
        eventsPanel.repaint();
    }
    
    private JPanel createEventPanel(Event event) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(138, 43, 226), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(event.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(138, 43, 226));
        
        JLabel dateLabel = new JLabel("📅 " + event.getEventDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setForeground(new Color(231, 76, 60));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        // Content
        JTextArea descriptionArea = new JTextArea(event.getDescription());
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Footer
        JLabel creatorLabel = new JLabel("Organized by: " + event.getCreatedByName() + " ⭐");
        creatorLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        creatorLabel.setForeground(Color.GRAY);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(descriptionArea, BorderLayout.CENTER);
        panel.add(creatorLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private class AddEventActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAddEventDialog();
        }
    }
    
    private void showAddEventDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Event", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title field
        JTextField titleField = new JTextField(30);
        styleTextField(titleField);
        
        // Description field
        JTextArea descriptionArea = new JTextArea(5, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        
        // Date field
        JTextField dateField = new JTextField(30);
        dateField.setText(LocalDate.now().plusDays(7).toString());
        styleTextField(dateField);
        
        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Event Title:"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descScrollPane, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Event Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = createStyledButton("💾 Save Event", new Color(46, 204, 113));
        JButton cancelButton = createStyledButton("❌ Cancel", new Color(231, 76, 60));
        
        saveButton.addActionListener(ev -> {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String dateStr = dateField.getText().trim();
            
            if (title.isEmpty() || description.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                LocalDate eventDate = LocalDate.parse(dateStr);
                boolean success = eventService.createEvent(title, description, eventDate, currentUser.getId());
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Event created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadEvents();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to create event. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format! Use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(ev -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
}
