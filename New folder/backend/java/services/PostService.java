package services;

import database.DBConnection;
import models.Post;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService {
    
    public boolean createPost(Post post) {
        String sql = "INSERT INTO posts (user_id, content, created_at) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, post.getUserId());
            pstmt.setString(2, post.getContent());
            pstmt.setTimestamp(3, Timestamp.valueOf(post.getCreatedAt()));
            
            int result = pstmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = """
            SELECT p.*, u.name as user_name, u.role as user_role 
            FROM posts p 
            JOIN users u ON p.user_id = u.id 
            ORDER BY p.created_at DESC
        """;
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setUserName(rs.getString("user_name"));
                post.setUserRole(rs.getString("user_role"));
                post.setContent(rs.getString("content"));
                post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                posts.add(post);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return posts;
    }
    
    public List<Post> getPostsByUser(int userId) {
        List<Post> posts = new ArrayList<>();
        String sql = """
            SELECT p.*, u.name as user_name, u.role as user_role 
            FROM posts p 
            JOIN users u ON p.user_id = u.id 
            WHERE p.user_id = ? 
            ORDER BY p.created_at DESC
        """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setUserId(rs.getInt("user_id"));
                post.setUserName(rs.getString("user_name"));
                post.setUserRole(rs.getString("user_role"));
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
