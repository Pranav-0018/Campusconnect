package ui;

import models.Post;
import models.User;
import services.PostService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FeedPage extends JPanel {
    private User currentUser;
    private PostService postService;
    private JTextArea postTextArea;
    private JPanel postsPanel;
    private JScrollPane scrollPane;
    
    public FeedPage(User user) {
        this.currentUser = user;
        this.postService = new PostService();
        initializeComponents();
        setupLayout();
        loadPosts();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Post creation area
        postTextArea = new JTextArea(3, 50);
        postTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        postTextArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        postTextArea.setLineWrap(true);
        postTextArea.setWrapStyleWord(true);
        
        // Posts display area
        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        postsPanel.setBackground(Color.WHITE);
        
        scrollPane = new JScrollPane(postsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    private void setupLayout() {
        // Top panel for creating posts
        JPanel topPanel = createPostCreationPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel for displaying posts
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createPostCreationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("What's on your mind?");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(138, 43, 226));
        
        // Text area with scroll
        JScrollPane textScrollPane = new JScrollPane(postTextArea);
        textScrollPane.setPreferredSize(new Dimension(0, 80));
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(new Color(248, 249, 250));
        
        JButton addPhotoButton = createStyledButton("📷 Add Photo", new Color(52, 152, 219));
        JButton postButton = createStyledButton("📝 Post", new Color(138, 43, 226));
        
        addPhotoButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Photo upload feature coming soon!", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        postButton.addActionListener(new PostActionListener());
        
        buttonsPanel.add(addPhotoButton);
        buttonsPanel.add(postButton);
        
        // Layout
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(248, 249, 250));
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(textScrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
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
        button.setPreferredSize(new Dimension(120, 35));
        
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
    
    private void loadPosts() {
        postsPanel.removeAll();
        
        List<Post> posts = postService.getAllPosts();
        
        if (posts.isEmpty()) {
            JLabel noPostsLabel = new JLabel("No posts yet. Be the first to share something!");
            noPostsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noPostsLabel.setForeground(Color.GRAY);
            noPostsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            postsPanel.add(noPostsLabel);
        } else {
            for (Post post : posts) {
                JPanel postPanel = createPostPanel(post);
                postsPanel.add(postPanel);
                postsPanel.add(Box.createVerticalStrut(10));
            }
        }
        
        postsPanel.revalidate();
        postsPanel.repaint();
    }
    
    private JPanel createPostPanel(Post post) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        
        // Header with user info
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        String userName = post.getUserName();
        if ("teacher".equals(post.getUserRole())) {
            userName += " ⭐";
        }
        
        JLabel userLabel = new JLabel(userName);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setForeground(new Color(138, 43, 226));
        
        JLabel timeLabel = new JLabel(post.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timeLabel.setForeground(Color.GRAY);
        
        headerPanel.add(userLabel, BorderLayout.WEST);
        headerPanel.add(timeLabel, BorderLayout.EAST);
        
        // Content
        JTextArea contentArea = new JTextArea(post.getContent());
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setBackground(Color.WHITE);
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    private class PostActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String content = postTextArea.getText().trim();
            
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(FeedPage.this, 
                    "Please write something before posting!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = postService.createPost(currentUser.getId(), content);
            
            if (success) {
                postTextArea.setText("");
                loadPosts();
                JOptionPane.showMessageDialog(FeedPage.this, 
                    "Post shared successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(FeedPage.this, 
                    "Failed to share post. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
