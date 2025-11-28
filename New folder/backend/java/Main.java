import ui.LoginPage;
import database.DBConnection;

public class Main {
    public static void main(String[] args) {
        // Test database connection
        if (DBConnection.testConnection()) {
            System.out.println("✅ Database connection successful!");
        } else {
            System.err.println("❌ Database connection failed!");
            System.err.println("Please check your MySQL server and credentials.");
            return;
        }
        
        // Launch the application
        System.out.println("🚀 Starting Campus Connect Application...");
        new LoginPage();
    }
}
