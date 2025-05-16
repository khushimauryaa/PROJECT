# 🎟️ Movie Ticket Booking System (Java + MySQL)

This project is a **Java-based Movie Ticket Booking System** that allows users to select a movie, choose available seats, and book tickets through an interactive graphical user interface (GUI). The application is connected to a MySQL database to store booking information, making it a real-world simulation of a theater reservation system

## 🧩 Overview
🔹 What the Project Does:

- Provides a clean and responsive GUI for booking movie tickets.
- Users can:
  - Select a movie from a dropdown list.
  - View the available seats dynamically.
  - Select multiple seats.
  - Book tickets which are stored in a MySQL database.

This project showcases the integration of **Java Swing for GUI** and **MySQL for backend** using **JDBC (Java Database Connectivity)**.

---

## 🧑‍💻 My Contribution (Frontend Development)

I have **designed and implemented the entire frontend** of the application using **Java Swing** along with my team member . Here's exactly what I built:

### ✅ GUI Design – `BookingFrame.java`

- Created the **main booking window** with:
  - Movie selection dropdown.
  - Seat layout using buttons (toggle style).
  - "Book Now" button and confirmation dialogs.
- Applied proper layout managers to organize components visually.

### 🎬 Movie Selection – `MovieSelectionListener`

- Implemented dynamic behavior:
  - When a movie is selected, it loads the relevant seating layout.
  - Integrates with backend to fetch real-time seat availability.

### 🪑 Seat Selection – `SeatSelectionListener`

- Developed seat selection mechanism:
  - Users can select multiple seats.
  - Handles logic for already booked/unavailable seats.
  - Visual feedback (color change or disabling) on seat state.

### 🧠 Event Handling

- Attached `ActionListener` for user interactions.
- Encapsulated logic to update the GUI based on selections and bookings.
- Created modular listeners to keep code organized and readable.

### 🖼️ User Experience

- Ensured clean, modern, and intuitive interface.
- Added validations to prevent invalid bookings.
- Used confirmation popups to improve usability.


## ⚙️ Technologies Used

| Purpose             | Technology |
|---------------------|------------|
| Frontend GUI        | Java Swing |
| Backend Database    | MySQL      |
| Connectivity Layer  | JDBC       |
| IDE (Recommended)   | IntelliJ / Eclipse / NetBeans |

