package services;

import database.DBConnection;
import models.Query;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QueryService {
    
    public boolean createQuery(int studentId, String question) {
        String sql = "INSERT INTO queries (student_id, question, created_at) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            stmt.setString(2, question);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean answerQuery(int queryId, String answer, int teacherId) {
        String sql = "UPDATE queries SET answer = ?, answered_by = ?, answered_at = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, answer);
            stmt.setInt(2, teacherId);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(4, queryId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Query> getAllQueries() {
        List<Query> queries = new ArrayList<>();
        String sql = "SELECT q.*, s.name as student_name, t.name as teacher_name " +
                    "FROM queries q " +
                    "JOIN users s ON q.student_id = s.id " +
                    "LEFT JOIN users t ON q.answered_by = t.id " +
                    "ORDER BY q.created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Query query = new Query();
                query.setId(rs.getInt("id"));
                query.setStudentId(rs.getInt("student_id"));
                query.setStudentName(rs.getString("student_name"));
                query.setQuestion(rs.getString("question"));
                query.setAnswer(rs.getString("answer"));
                query.setAnsweredBy(rs.getInt("answered_by"));
                query.setTeacherName(rs.getString("teacher_name"));
                query.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                Timestamp answeredAt = rs.getTimestamp("answered_at");
                if (answeredAt != null) {
                    query.setAnsweredAt(answeredAt.toLocalDateTime());
                }
                
                queries.add(query);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return queries;
    }
}
