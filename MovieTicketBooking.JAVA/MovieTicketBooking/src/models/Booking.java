package models;

import java.sql.Timestamp;

public class Booking {
    private int id;
    private int userId;
    private int movieId;
    private int seatsBooked;
    private Timestamp reservationTime;

    public Booking() {}

    public Booking(int id, int userId, int movieId, int seatsBooked, Timestamp reservationTime) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.seatsBooked = seatsBooked;
        this.reservationTime = reservationTime;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public int getSeatsBooked() { return seatsBooked; }
    public void setSeatsBooked(int seatsBooked) { this.seatsBooked = seatsBooked; }

    public Timestamp getReservationTime() { return reservationTime; }
    public void setReservationTime(Timestamp reservationTime) { this.reservationTime = reservationTime; }
}
