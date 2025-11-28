package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Event {
    private int id;
    private String title;
    private String description;
    private LocalDate eventDate;
    private int createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
    
    public Event() {}
    
    public Event(String title, String description, LocalDate eventDate, int createdBy) {
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    
    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
    
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
