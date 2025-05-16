import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, exitButton, signUpButton;

    public LoginFrame() {
        setTitle("Movie Booking - Login");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // Using absolute positioning for better control

        // Background Color
        getContentPane().setBackground(new Color(240, 240, 240));

        // Title Label
        JLabel title = new JLabel("Movie Booking Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(100, 20, 250, 30);
        add(title);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(100, 80, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(200, 80, 150, 25);
        add(usernameField);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(100, 120, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 120, 150, 25);
        add(passwordField);

        // Buttons
        loginButton = new JButton("Login");
        loginButton.setBounds(120, 180, 90, 30);
        loginButton.setBackground(new Color(0, 150, 0)); // Green
        loginButton.setForeground(Color.WHITE);
        add(loginButton);

        exitButton = new JButton("Exit");
        exitButton.setBounds(230, 180, 90, 30);
        exitButton.setBackground(new Color(200, 0, 0)); // Red
        exitButton.setForeground(Color.WHITE);
        add(exitButton);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(160, 220, 120, 30);
        signUpButton.setBackground(new Color(0, 100, 200)); // Blue
        signUpButton.setForeground(Color.WHITE);
        add(signUpButton);

        // Login Action
        loginButton.addActionListener(e -> {
            try {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    throw new Exception("Fields cannot be empty!");
                }

                // Validate with Database
                Connection conn = DatabaseConnection.getConnection();
                if (conn == null)
                    throw new Exception("Database connection failed!");

                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    conn.close();
                    dispose(); // Close Login Frame
                    
                    // Pass the username to BookingFrame
                    new BookingFrame(username); // Open Booking Page with username
                } else {
                    throw new Exception("Invalid username or password!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Exit Action
        exitButton.addActionListener(e -> System.exit(0));

        // Sign Up Action
        signUpButton.addActionListener(e -> {
            new RegistrationFrame(); // Open Registration Frame
            dispose(); // Close Login Frame
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
