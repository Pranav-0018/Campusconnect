package services;

import database.DBConnection;
import models.Query;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueryService {
    
    public boolean createQuery(Query query) {
        String sql = "INSERT INTO queries (student_id, question, created_at) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, query.getStudentId());
            pstmt.setString(2, query.getQuestion());
            pstmt.setTimestamp(3, Timestamp.valueOf(query.getCreatedAt()));
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean answerQuery(int queryId, String answer, int teacherId) {
        String sql = "UPDATE queries SET answer = ?, answered_by = ?, answered_at = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, answer);
            pstmt.setInt(2, teacherId);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(4, queryId);
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Query> getAllQueries() {
        List<Query> queries = new ArrayList<>();
        String sql = """
            SELECT q.*, 
                   s.name as student_name, 
                   t.name as teacher_name 
            FROM queries q 
            JOIN users s ON q.student_id = s.id 
            LEFT JOIN users t ON q.answered_by = t.id 
            ORDER BY q.created_at DESC
        """;
        
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
                
                Integer answeredBy = rs.getObject("answered_by", Integer.class);
                query.setAnsweredBy(answeredBy);
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
    
    public List<Query> getUnansweredQueries() {
        List<Query> queries = new ArrayList<>();
        String sql = """
            SELECT q.*, s.name as student_name 
            FROM queries q 
            JOIN users s ON q.student_id = s.id 
            WHERE q.answer IS NULL 
            ORDER BY q.created_at ASC
        """;
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Query query = new Query();
                query.setId(rs.getInt("id"));
                query.setStudentId(rs.getInt("student_id"));
                query.setStudentName(rs.getString("student_name"));
                query.setQuestion(rs.getString("question"));
                query.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                queries.add(query);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return queries;
    }
}
