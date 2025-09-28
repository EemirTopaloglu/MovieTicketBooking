package gui;

import database.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MainFrame extends JFrame {
    private JTable movieTable;
    private DefaultTableModel model;
    private JTextField seatField, searchField;
    private JComboBox<String> sortComboBox;
    private int currentUserId;
    private String currentUsername;

    public MainFrame(int userId, String username) {
        this.currentUserId = userId;
        this.currentUsername = username;
        initUI();
    }

    private void initUI() {
        setTitle("Movie List & Booking");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 🧱 Tablo
        model = new DefaultTableModel(new Object[]{"ID", "Title", "Duration", "Date", "Available Seats"}, 0);
        movieTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(movieTable);
        add(scrollPane, BorderLayout.CENTER);

        // 🔍 Alt Panel: Arama, sıralama, koltuk sayısı, butonlar
        JPanel bottomPanel = new JPanel(new FlowLayout());

        bottomPanel.add(new JLabel("Search title:"));
        searchField = new JTextField(10);
        bottomPanel.add(searchField);

        sortComboBox = new JComboBox<>(new String[]{"Oldest First", "Newest First"});
        bottomPanel.add(sortComboBox);

        JButton filterButton = new JButton("Apply Filter");
        bottomPanel.add(filterButton);

        bottomPanel.add(new JLabel("Seats to reserve:"));
        seatField = new JTextField(5);
        bottomPanel.add(seatField);

        JButton reserveButton = new JButton("Reserve");
        bottomPanel.add(reserveButton);

        filterButton.addActionListener(e -> loadMovies());
        reserveButton.addActionListener(e -> makeReservation());

        add(bottomPanel, BorderLayout.SOUTH);

        loadMovies();
        setVisible(true);
    }

    private void loadMovies() {
        model.setRowCount(0);
        String searchText = searchField.getText().trim();
        String sortOrder = (sortComboBox.getSelectedIndex() == 0) ? "ASC" : "DESC";

        String query = "SELECT * FROM movies WHERE title LIKE ? ORDER BY show_date " + sortOrder;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + searchText + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("duration"),
                        rs.getDate("show_date"),
                        rs.getInt("available_seats")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void makeReservation() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a movie.");
            return;
        }

        int movieId = (int) model.getValueAt(selectedRow, 0);
        String movieTitle = (String) model.getValueAt(selectedRow, 1);
        String movieDate = model.getValueAt(selectedRow, 3).toString();
        int availableSeats = (int) model.getValueAt(selectedRow, 4);

        int requestedSeats;
        try {
            requestedSeats = Integer.parseInt(seatField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of seats.");
            return;
        }

        if (requestedSeats <= 0 || requestedSeats > availableSeats) {
            JOptionPane.showMessageDialog(this, "Invalid seat count.");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            String insertBooking = "INSERT INTO bookings (user_id, movie_id, seats_booked) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertBooking)) {
                ps.setInt(1, currentUserId);
                ps.setInt(2, movieId);
                ps.setInt(3, requestedSeats);
                ps.executeUpdate();
            }

            String updateSeats = "UPDATE movies SET available_seats = available_seats - ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSeats)) {
                ps.setInt(1, requestedSeats);
                ps.setInt(2, movieId);
                ps.executeUpdate();
            }

            conn.commit();
            seatField.setText("");
            loadMovies();

            // 🎟 Ticket receipt popup
            String receipt = """
                    🎟 Ticket Receipt
                    
                    Name: %s
                    Movie: %s
                    Date: %s
                    Seats: %d
                    Time: %s
                    """.formatted(
                    currentUsername,
                    movieTitle,
                    movieDate,
                    requestedSeats,
                    new java.util.Date().toString()
            );

            JOptionPane.showMessageDialog(this, receipt, "Your Ticket", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Reservation failed.");
        }
    }
}
