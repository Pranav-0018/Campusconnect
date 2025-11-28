package ui;

import models.Query;
import models.User;
import services.QueryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QueryPage extends JPanel {
    private User currentUser;
    private QueryService queryService;
    private JTextArea queryTextArea;
    private JPanel queriesPanel;
    private JScrollPane scrollPane;
    
    public QueryPage(User user) {
        this.currentUser = user;
        this.queryService = new QueryService();
        initializeComponents();
        setupLayout();
        loadQueries();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Query input area (only for students)
        if (currentUser.isStudent()) {
            queryTextArea = new JTextArea(3, 50);
            queryTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
            queryTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            queryTextArea.setLineWrap(true);
            queryTextArea.setWrapStyleWord(true);
        }
        
        // Queries display area
        queriesPanel = new JPanel();
        queriesPanel.setLayout(new BoxLayout(queriesPanel, BoxLayout.Y_AXIS));
        queriesPanel.setBackground(Color.WHITE);
        
        scrollPane = new JScrollPane(queriesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    private void setupLayout() {
        // Top panel
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel for queries
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        String titleText = currentUser.isStudent() ? "💬 Ask Your Questions" : "💬 Student Queries";
        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(138, 43, 226));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Query input section (only for students)
        if (currentUser.isStudent()) {
            JPanel inputPanel = createQueryInputPanel();
            panel.add(inputPanel, BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    private JPanel createQueryInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JLabel instructionLabel = new JLabel("Have a question? Ask your teachers!");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionLabel.setForeground(Color.GRAY);
        
        JScrollPane textScrollPane = new JScrollPane(queryTextArea);
        textScrollPane.setPreferredSize(new Dimension(0, 80));
        
        JButton askButton = createStyledButton("❓ Ask Question", new Color(138, 43, 226));
        askButton.addActionListener(new AskQuestionActionListener());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.add(askButton);
        
        panel.add(instructionLabel, BorderLayout.NORTH);
        panel.add(textScrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
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
        button.setPreferredSize(new Dimension(140, 35));
        
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
    
    private void loadQueries() {
        queriesPanel.removeAll();
        
        List<Query> queries = queryService.getAllQueries();
        
        if (queries.isEmpty()) {
            JLabel noQueriesLabel = new JLabel("No questions asked yet.");
            noQueriesLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noQueriesLabel.setForeground(Color.GRAY);
            noQueriesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            queriesPanel.add(noQueriesLabel);
        } else {
            for (Query query : queries) {
                JPanel queryPanel = createQueryPanel(query);
                queriesPanel.add(queryPanel);
                queriesPanel.add(Box.createVerticalStrut(15));
            }
        }
        
        queriesPanel.revalidate();
        queriesPanel.repaint();
    }
    
    private JPanel createQueryPanel(Query query) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        
        // Question section
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground(Color.WHITE);
        
        JLabel studentLabel = new JLabel("👤 " + query.getStudentName());
        studentLabel.setFont(new Font("Arial", Font.BOLD, 14));
        studentLabel.setForeground(new Color(52, 152, 219));
        
        JLabel timeLabel = new JLabel(query.getCreatedAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timeLabel.setForeground(Color.GRAY);
        
        JPanel questionHeaderPanel = new JPanel(new BorderLayout());
        questionHeaderPanel.setBackground(Color.WHITE);
        questionHeaderPanel.add(studentLabel, BorderLayout.WEST);
        questionHeaderPanel.add(timeLabel, BorderLayout.EAST);
        
        JTextArea questionArea = new JTextArea(query.getQuestion());
        questionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        questionArea.setBackground(Color.WHITE);
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        questionPanel.add(questionHeaderPanel, BorderLayout.NORTH);
        questionPanel.add(questionArea, BorderLayout.CENTER);
        
        panel.add(questionPanel, BorderLayout.NORTH);
        
        // Answer section
        if (query.isAnswered()) {
            JPanel answerPanel = createAnswerPanel(query);
            panel.add(answerPanel, BorderLayout.CENTER);
        } else if (currentUser.isTeacher()) {
            JPanel replyPanel = createReplyPanel(query);
            panel.add(replyPanel, BorderLayout.SOUTH);
        } else {
            JLabel waitingLabel = new JLabel("⏳ Waiting for teacher's response...");
            waitingLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            waitingLabel.setForeground(Color.GRAY);
            waitingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            panel.add(waitingLabel, BorderLayout.SOUTH);
        }
        
        return panel;
    }
    
    private JPanel createAnswerPanel(Query query) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 255, 248));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 204, 113)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel teacherLabel = new JLabel("👨‍🏫 " + query.getTeacherName() + " ⭐");
        teacherLabel.setFont(new Font("Arial", Font.BOLD, 14));
        teacherLabel.setForeground(new Color(46, 204, 113));
        
        JLabel answerTimeLabel = new JLabel(query.getAnsweredAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));
        answerTimeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        answerTimeLabel.setForeground(Color.GRAY);
        
        JPanel answerHeaderPanel = new JPanel(new BorderLayout());
        answerHeaderPanel.setBackground(new Color(248, 255, 248));
        answerHeaderPanel.add(teacherLabel, BorderLayout.WEST);
        answerHeaderPanel.add(answerTimeLabel, BorderLayout.EAST);
        
        JTextArea answerArea = new JTextArea(query.getAnswer());
        answerArea.setFont(new Font("Arial", Font.PLAIN, 14));
        answerArea.setBackground(new Color(248, 255, 248));
        answerArea.setEditable(false);
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        panel.add(answerHeaderPanel, BorderLayout.NORTH);
        panel.add(answerArea, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createReplyPanel(Query query) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 248, 248));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(231, 76, 60)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel replyLabel = new JLabel("💬 Reply to this question:");
        replyLabel.setFont(new Font("Arial", Font.BOLD, 12));
        replyLabel.setForeground(new Color(231, 76, 60));
        
        JTextArea replyArea = new JTextArea(2, 50);
        replyArea.setFont(new Font("Arial", Font.PLAIN, 12));
        replyArea.setLineWrap(true);
        replyArea.setWrapStyleWord(true);
        replyArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JButton replyButton = createStyledButton("📤 Reply", new Color(46, 204, 113));
        replyButton.setPreferredSize(new Dimension(100, 30));
        
        replyButton.addActionListener(e -> {
            String answer = replyArea.getText().trim();
            if (answer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please write a reply!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = queryService.answerQuery(query.getId(), answer, currentUser.getId());
            if (success) {
                JOptionPane.showMessageDialog(this, "Reply sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadQueries();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to send reply. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(255, 248, 248));
        buttonPanel.add(replyButton);
        
        panel.add(replyLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(replyArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private class AskQuestionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String question = queryTextArea.getText().trim();
            
            if (question.isEmpty()) {
                JOptionPane.showMessageDialog(QueryPage.this, 
                    "Please write your question!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = queryService.createQuery(currentUser.getId(), question);
            
            if (success) {
                queryTextArea.setText("");
                loadQueries();
                JOptionPane.showMessageDialog(QueryPage.this, 
                    "Question posted successfully! Teachers will respond soon.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(QueryPage.this, 
                    "Failed to post question. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
