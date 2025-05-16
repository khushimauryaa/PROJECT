import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegistrationFrame extends JFrame {
    private JTextField usernameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton registerButton, cancelButton;

    public RegistrationFrame() {
        setTitle("Movie Booking - Registration");
        setSize(450, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // Absolute positioning

        getContentPane().setBackground(new Color(240, 240, 240));

        // Title
        JLabel title = new JLabel("Movie Booking Registration", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(50, 20, 350, 30); // Fixed width to show full text
        add(title);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(80, 80, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(200, 80, 150, 25);
        add(usernameField);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(80, 120, 100, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(200, 120, 150, 25);
        add(emailField);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(80, 160, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 160, 150, 25);
        add(passwordField);

        // Confirm Password
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassLabel.setBounds(80, 200, 130, 25);
        add(confirmPassLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(200, 200, 150, 25); // Aligned with label
        add(confirmPasswordField);

        // Buttons
        registerButton = new JButton("Register");
        registerButton.setBounds(120, 260, 90, 30);
        registerButton.setBackground(new Color(0, 150, 0));
        registerButton.setForeground(Color.WHITE);
        add(registerButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(230, 260, 90, 30);
        cancelButton.setBackground(new Color(200, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        add(cancelButton);

        // Register Action
        registerButton.addActionListener(e -> {
            try {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    throw new Exception("Fields cannot be empty!");
                }

                if (!password.equals(confirmPassword)) {
                    throw new Exception("Passwords do not match!");
                }

                Connection conn = DatabaseConnection.getConnection();
                if (conn == null)
                    throw new Exception("Database connection failed!");

                String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Registration Successful!");
                conn.close();

                dispose();
                new LoginFrame();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel Action
        cancelButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new RegistrationFrame();
    }
}

