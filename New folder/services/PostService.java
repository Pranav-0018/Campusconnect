package services;

import database.DBConnection;
import models.Post;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostService {
    
    public boolean createPost(int userId, String content) {
        String sql = "INSERT INTO posts (user_id, content, created_at) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, content);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.*, u.name, u.role FROM posts p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "ORDER BY p.created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setUserName(rs.getString("name"));
                post.setUserRole(rs.getString("role"));
                post.setContent(rs.getString("content"));
                post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                posts.add(post);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return posts;
    }
}
