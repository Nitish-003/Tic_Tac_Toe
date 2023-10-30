package com.example.my_tic_tac_toe;

public class User {
    private String username;
    private int matchesPlayed;
    private int matchesWon;
    private int matchesDraw;

    public User() {
        // Default constructor
    }

    public User(String username, int matchesPlayed, int matchesWon, int matchesDraw) {
        this.username = username;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.matchesDraw = matchesDraw;
    }

    // Getters and setters for the user data fields
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(int matchesWon) {
        this.matchesWon = matchesWon;
    }

    public int getMatchesDraw() {
        return matchesDraw;
    }

    public void setMatchesDraw(int matchesDraw) {
        this.matchesDraw = matchesDraw;
    }

}
