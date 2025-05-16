import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class PaymentFrame extends JFrame {
    private String selectedMovie, selectedSeats, selectedPaymentMethod, username;
    private JTextField cardNumberField, upiField;
    private JButton confirmPaymentButton;
    private JPanel paymentDetailsPanel;

    public PaymentFrame(String movie, String screen, String seats, String username) {
        this.selectedMovie = movie;
        this.selectedSeats = seats;
        this.username = username;

        setTitle("Payment - Movie Booking");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(230, 230, 230));

        JLabel title = new JLabel("Payment Gateway", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(150, 20, 300, 30);
        add(title);

        JLabel paymentLabel = new JLabel("Select Payment Method:");
        paymentLabel.setBounds(50, 80, 200, 25);
        add(paymentLabel);

        JRadioButton cardButton = new JRadioButton("Credit/Debit Card");
        JRadioButton upiButton = new JRadioButton("UPI Payment");
        cardButton.setBackground(new Color(230, 230, 230));
        upiButton.setBackground(new Color(230, 230, 230));

        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(cardButton);
        paymentGroup.add(upiButton);

        cardButton.setBounds(50, 110, 150, 25);
        upiButton.setBounds(50, 140, 150, 25);

        add(cardButton);
        add(upiButton);

        paymentDetailsPanel = new JPanel();
        paymentDetailsPanel.setLayout(null);
        paymentDetailsPanel.setBounds(50, 180, 500, 150);
        paymentDetailsPanel.setBackground(new Color(220, 220, 220));
        add(paymentDetailsPanel);

        confirmPaymentButton = new JButton("Confirm Payment");
        confirmPaymentButton.setBounds(200, 360, 180, 40);
        confirmPaymentButton.setBackground(new Color(0, 120, 215));
        confirmPaymentButton.setForeground(Color.WHITE);
        confirmPaymentButton.setEnabled(false);
        add(confirmPaymentButton);

        cardButton.addActionListener(e -> showCardPaymentFields());
        upiButton.addActionListener(e -> showUPIPaymentFields());

        confirmPaymentButton.addActionListener(e -> processPayment());

        setVisible(true);
    }

    private void showCardPaymentFields() {
        paymentDetailsPanel.removeAll();
        selectedPaymentMethod = "Credit/Debit Card";

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberField = new JTextField();

        JLabel ifscLabel = new JLabel("IFSC Code:");
        JTextField ifscField = new JTextField();

        cardNumberLabel.setBounds(20, 20, 150, 25);
        cardNumberField.setBounds(180, 20, 250, 25);
        ifscLabel.setBounds(20, 60, 150, 25);
        ifscField.setBounds(180, 60, 250, 25);

        paymentDetailsPanel.add(cardNumberLabel);
        paymentDetailsPanel.add(cardNumberField);
        paymentDetailsPanel.add(ifscLabel);
        paymentDetailsPanel.add(ifscField);
        
        paymentDetailsPanel.revalidate();
        paymentDetailsPanel.repaint();
        confirmPaymentButton.setEnabled(true);
    }

    private void showUPIPaymentFields() {
        paymentDetailsPanel.removeAll();
        selectedPaymentMethod = "UPI";

        JLabel upiLabel = new JLabel("UPI ID:");
        upiField = new JTextField();

        upiLabel.setBounds(20, 20, 150, 25);
        upiField.setBounds(180, 20, 250, 25);

        paymentDetailsPanel.add(upiLabel);
        paymentDetailsPanel.add(upiField);

        paymentDetailsPanel.revalidate();
        paymentDetailsPanel.repaint();
        confirmPaymentButton.setEnabled(true);
    }

    private void processPayment() {
        if (selectedPaymentMethod.equals("Credit/Debit Card") && cardNumberField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your Card Number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (selectedPaymentMethod.equals("UPI") && upiField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your UPI ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            Connection conn = DatabaseConnection.getConnection(); // Ensure you have a working DatabaseConnection class
            if (conn == null) {
                throw new Exception("Database connection failed!");
            }
    
            // Get user_id from username
            String userQuery = "SELECT user_id FROM users WHERE username = ?";
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();
    
            if (!userRs.next()) {
                throw new Exception("User not found!");
            }
    
            int userId = userRs.getInt("user_id");
    
            // Insert into payment table
            String insertPaymentQuery = "INSERT INTO payment (user_id, payment_method, payment_status) VALUES (?, ?, 'Completed')";
            PreparedStatement paymentStmt = conn.prepareStatement(insertPaymentQuery);
            paymentStmt.setInt(1, userId);
            paymentStmt.setString(2, selectedPaymentMethod);
            paymentStmt.executeUpdate();
    
            JOptionPane.showMessageDialog(this, "Booking Confirmed!\nUser: " + username + 
                    "\nMovie: " + selectedMovie + "\nSeats: " + selectedSeats + 
                    "\nPayment: " + selectedPaymentMethod, "Booking Success", JOptionPane.INFORMATION_MESSAGE);
    
            conn.close();
            this.dispose();
    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public static void main(String[] args) {
        new PaymentFrame("Avatar", "Screen 1", "A1, A2", "User123");
    }
}
