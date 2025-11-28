package services;

import database.DBConnection;
import models.Event;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    
    public boolean createEvent(String title, String description, LocalDate eventDate, int createdBy) {
        String sql = "INSERT INTO events (title, description, event_date, created_by) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setDate(3, Date.valueOf(eventDate));
            stmt.setInt(4, createdBy);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT e.*, u.name as creator_name FROM events e " +
                    "JOIN users u ON e.created_by = u.id " +
                    "ORDER BY e.event_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setTitle(rs.getString("title"));
                event.setDescription(rs.getString("description"));
                event.setEventDate(rs.getDate("event_date").toLocalDate());
                event.setCreatedBy(rs.getInt("created_by"));
                event.setCreatedByName(rs.getString("creator_name"));
                events.add(event);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return events;
    }
}
