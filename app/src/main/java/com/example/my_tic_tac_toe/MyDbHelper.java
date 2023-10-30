package com.example.my_tic_tac_toe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "leaderboard.db";
    private static final int DATABASE_VERSION = 1;

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "users" table with the specified schema
        String createTableQuery = "CREATE TABLE users (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "matches_won INTEGER, " + // Matches won column
                "matches_played INTEGER," +
                "matches_draw INTEGER)"; // Matches played column
        db.execSQL(createTableQuery);
    }

    public void updateUserData(String username, int matchesWon) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get the current matches won value from the database
        String query = "SELECT matches_won FROM users WHERE name=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        int currentMatchesWon = 0;
        if (cursor.moveToFirst()) {
            currentMatchesWon = cursor.getInt(0);
        }

        // Calculate the new total matches won
        int newMatchesWon = currentMatchesWon + matchesWon;

        // Update the database with the new total matches won
        ContentValues values = new ContentValues();
        values.put("matches_won", newMatchesWon);
        db.update("users", values, "name=?", new String[]{username});

        cursor.close();
        db.close();
    }

    public void updateUserTotalMatch(String username, String username2) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Username 1
        String query = "SELECT matches_played FROM users WHERE name=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        int currentMatchesPlayed = 0;
        if (cursor.moveToFirst()) {
            currentMatchesPlayed = cursor.getInt(0);
        }
        int newMatchesPlayed = currentMatchesPlayed + 1;

        // Update the database with the new total matches played
        ContentValues values = new ContentValues();
        values.put("matches_played", newMatchesPlayed);
        db.update("users", values, "name=?", new String[]{username});

        //Username 2
        String query2 = "SELECT matches_played FROM users WHERE name=?";
        Cursor cursor2 = db.rawQuery(query2, new String[]{username2});

        currentMatchesPlayed = 0;
        if (cursor2.moveToFirst()) {
            currentMatchesPlayed = cursor2.getInt(0);
        }
        newMatchesPlayed = currentMatchesPlayed + 1;

        // Update the database with the new total matches played
        ContentValues values2 = new ContentValues();
        values2.put("matches_played", newMatchesPlayed);
        db.update("users", values2, "name=?", new String[]{username2});

        cursor.close();
        db.close();
    }

    public void updateUserDrawMatch(String username, String username2) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Username 1
        String query = "SELECT matches_draw FROM users WHERE name=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        int currentDrawPlayed = 0;
        if (cursor.moveToFirst()) {
            currentDrawPlayed = cursor.getInt(0);
        }
        int newMatchesPlayed = currentDrawPlayed + 1;

        // Update the database with the new total matches played
        ContentValues values = new ContentValues();
        values.put("matches_draw", newMatchesPlayed);
        db.update("users", values, "name=?", new String[]{username});

        //Username 2
        String query2 = "SELECT matches_draw FROM users WHERE name=?";
        Cursor cursor2 = db.rawQuery(query2, new String[]{username2});

        currentDrawPlayed = 0;
        if (cursor2.moveToFirst()) {
            currentDrawPlayed = cursor2.getInt(0);
        }
        newMatchesPlayed = currentDrawPlayed + 1;

        // Update the database with the new total matches played
        ContentValues values2 = new ContentValues();
        values2.put("matches_draw", newMatchesPlayed);
        db.update("users", values2, "name=?", new String[]{username2});

        cursor.close();
        db.close();
    }



    public boolean doesUserExist(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE name=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean userExists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return userExists;
    }

    public void addUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", username);
        values.put("matches_won", 0); // Default value
        values.put("matches_played", 0);
        values.put("matches_draw",0);// Default value
        db.insert("users", null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades, if needed
    }

    public List<User> getTopUsers(int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<User> topUsers = new ArrayList<>();

        // Query the database to get the top users
        String query = "SELECT * FROM users ORDER BY matches_won DESC LIMIT " + limit;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUsername(cursor.getString(1));
                user.setMatchesPlayed(cursor.getInt(3)); // Corrected index
                user.setMatchesWon(cursor.getInt(2)); // Corrected index
                user.setMatchesDraw(cursor.getInt(4)); // Corrected index
                topUsers.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return topUsers;
    }
}
