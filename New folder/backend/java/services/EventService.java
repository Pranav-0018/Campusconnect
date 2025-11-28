package services;

import database.DBConnection;
import models.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    
    public boolean createEvent(Event event) {
        String sql = "INSERT INTO events (title, description, event_date, created_by, created_at) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, event.getTitle());
            pstmt.setString(2, event.getDescription());
            pstmt.setDate(3, Date.valueOf(event.getEventDate()));
            pstmt.setInt(4, event.getCreatedBy());
            pstmt.setTimestamp(5, Timestamp.valueOf(event.getCreatedAt()));
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = """
            SELECT e.*, u.name as created_by_name 
            FROM events e 
            JOIN users u ON e.created_by = u.id 
            ORDER BY e.event_date DESC
        """;
        
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
                event.setCreatedByName(rs.getString("created_by_name"));
                event.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                events.add(event);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return events;
    }
    
    public List<Event> getUpcomingEvents() {
        List<Event> events = new ArrayList<>();
        String sql = """
            SELECT e.*, u.name as created_by_name 
            FROM events e 
            JOIN users u ON e.created_by = u.id 
            WHERE e.event_date >= CURDATE() 
            ORDER BY e.event_date ASC
        """;
        
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
                event.setCreatedByName(rs.getString("created_by_name"));
                event.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                events.add(event);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return events;
    }
}
