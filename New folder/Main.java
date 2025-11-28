import ui.LoginPage;
import javax.swing.*;

/**
 * Campus Connect - University Platform
 * 
 * A desktop application connecting students and teachers with features like:
 * - Social feed for sharing posts
 * - Event management (teachers can create, students can view)
 * - Query system (students ask, teachers answer)
 * - User profiles
 * 
 * @author Campus Connect Team
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Enable anti-aliasing for better text rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Launch the application
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginPage().setVisible(true);
                System.out.println("Campus Connect application started successfully!");
                System.out.println("Demo Accounts:");
                System.out.println("Teacher: teacher@demo.com / teacher123");
                System.out.println("Student: student@demo.com / student123");
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error starting Campus Connect: " + e.getMessage(), 
                    "Startup Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
