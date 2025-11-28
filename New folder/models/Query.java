package models;

import java.time.LocalDateTime;

public class Query {
    private int id;
    private int studentId;
    private String studentName;
    private String question;
    private String answer;
    private int answeredBy;
    private String teacherName;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;
    
    public Query() {}
    
    public Query(int studentId, String question) {
        this.studentId = studentId;
        this.question = question;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    
    public int getAnsweredBy() { return answeredBy; }
    public void setAnsweredBy(int answeredBy) { this.answeredBy = answeredBy; }
    
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(LocalDateTime answeredAt) { this.answeredAt = answeredAt; }
    
    public boolean isAnswered() {
        return answer != null && !answer.trim().isEmpty();
    }
}
