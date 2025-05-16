import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class BookingFrame extends JFrame {
    private String selectedMovie, selectedSeats, selectedScreen, username;
    private ArrayList<String> selectedSeatList = new ArrayList<>();

    public BookingFrame(String username) {
        this.username = username;
        setTitle("Movie Booking");
        setSize(600, 700); // Increased size to accommodate images and seat buttons
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Title
        JLabel title = new JLabel("Select Movie and Seats", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(150, 20, 300, 30);
        add(title);

        // Movie selection (Dropdown)
        JLabel movieLabel = new JLabel("Select Movie:");
        movieLabel.setBounds(50, 80, 150, 25);
        add(movieLabel);

        String[] movies = {"Avatar", "Avengers", "Inception"};
        JComboBox<String> movieComboBox = new JComboBox<>(movies);
        movieComboBox.setBounds(200, 80, 250, 25);
        add(movieComboBox);

        // Movie Image Display
        JLabel movieImageLabel = new JLabel();
        movieImageLabel.setBounds(50, 120, 200, 300);
        add(movieImageLabel);

        // Set image based on movie selection
        movieComboBox.addActionListener(e -> {
            String selectedMovie = (String) movieComboBox.getSelectedItem();
            ImageIcon movieImageIcon = getMovieImageIcon(selectedMovie);
            movieImageLabel.setIcon(movieImageIcon);
        });

        // Initial movie image (Avatar)
        ImageIcon initialImage = getMovieImageIcon("Avatar");
        movieImageLabel.setIcon(initialImage);

        // Screen selection (Dropdown)
        JLabel screenLabel = new JLabel("Select Screen:");
        screenLabel.setBounds(50, 440, 150, 25);
        add(screenLabel);

        String[] screens = {"Screen 1", "Screen 2", "Screen 3"};
        JComboBox<String> screenComboBox = new JComboBox<>(screens);
        screenComboBox.setBounds(200, 440, 250, 25);
        add(screenComboBox);

        // Seat selection (Grid of Buttons)
        JPanel seatPanel = new JPanel();
        seatPanel.setBounds(50, 480, 500, 150);
        seatPanel.setLayout(new GridLayout(4, 6, 10, 10)); // 4 rows and 6 columns
        add(seatPanel);

        String[] rows = {"A", "B", "C", "D"};
        JButton[][] seatButtons = new JButton[4][6]; // Create a 4x6 grid for seats
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                String seatLabel = rows[i] + (j + 1); // Seat names like A1, A2, etc.
                JButton seatButton = new JButton(seatLabel);
                seatButton.setBackground(Color.LIGHT_GRAY); // Default color
                seatButton.setFont(new Font("Arial", Font.PLAIN, 12));
                seatButton.setFocusPainted(false);
                seatButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (seatButton.getBackground() == Color.LIGHT_GRAY) {
                            seatButton.setBackground(Color.GREEN); // Selected
                            selectedSeatList.add(seatLabel); // Add to selected list
                        } else {
                            seatButton.setBackground(Color.LIGHT_GRAY); // Unselected
                            selectedSeatList.remove(seatLabel); // Remove from selected list
                        }
                    }
                });
                seatButtons[i][j] = seatButton;
                seatPanel.add(seatButton);
            }
        }

        // Book button
        JButton bookButton = new JButton("Book Now");
        bookButton.setBounds(200, 650, 150, 40);
        bookButton.setBackground(new Color(0, 150, 0)); // Green
        bookButton.setForeground(Color.WHITE);
        add(bookButton);

        // Action for booking
        bookButton.addActionListener(e -> {
            try {
                selectedMovie = (String) movieComboBox.getSelectedItem();
                selectedScreen = (String) screenComboBox.getSelectedItem();
                selectedSeats = String.join(",", selectedSeatList); // Convert list to a string

                if (selectedSeats.isEmpty()) {
                    throw new Exception("Please select at least one seat!");
                }

                // Insert booking into the database
                Connection conn = DatabaseConnection.getConnection();
                if (conn == null)
                    throw new Exception("Database connection failed!");

                String insertBookingQuery = "INSERT INTO bookings (username, movie, screen, seats, booking_time) VALUES (?, ?, ?, ?, NOW())";
                PreparedStatement stmt = conn.prepareStatement(insertBookingQuery);
                stmt.setString(1, username);
                stmt.setString(2, selectedMovie);
                stmt.setString(3, selectedScreen);
                stmt.setString(4, selectedSeats);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Booking Successful!");

                // Redirect to PaymentPage
                new PaymentFrame(selectedMovie, selectedScreen, selectedSeats, username); // Pass booking details to PaymentFrame
                dispose(); // Close Booking Frame

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private ImageIcon getMovieImageIcon(String movieName) {
        String imagePath = "";
        switch (movieName) {
            case "Avatar":
                imagePath = "avatar.jpg";
                break;
            case "Avengers":
                imagePath = "avengers.jpg";
                break;
            case "Inception":
                imagePath = "inception.jpg";
                break;
            default:
                break;
        }
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(200, 300, Image.SCALE_SMOOTH); // Resize image
        return new ImageIcon(resizedImage);
    }

    public static void main(String[] args) {
        new BookingFrame("User123"); // Test with a username
    }
}
