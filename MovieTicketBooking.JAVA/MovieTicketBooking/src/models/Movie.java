package models;

import java.sql.Date;

public class Movie {
    private int id;
    private String title;
    private String duration;
    private Date showDate;
    private int availableSeats;

    public Movie() {}

    public Movie(int id, String title, String duration, Date showDate, int availableSeats) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.showDate = showDate;
        this.availableSeats = availableSeats;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public Date getShowDate() { return showDate; }
    public void setShowDate(Date showDate) { this.showDate = showDate; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}

