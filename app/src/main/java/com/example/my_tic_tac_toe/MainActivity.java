package com.example.my_tic_tac_toe;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points = 0;
    private int player2Points = 0;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    String player1Name,player2Name;
    TextView currentChanceTextView;

    private MyDbHelper dbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize your database helper
        dbHelper = new MyDbHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        // Create and show an AlertDialog to get player names
        showNameInputDialog();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        currentChanceTextView = findViewById(R.id.current_chance);

    }

    private void showNameInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Player Names");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_player_names, null);
        builder.setView(dialogView);

        final EditText editTextPlayer1 = dialogView.findViewById(R.id.edit_text_player1);
        final EditText editTextPlayer2 = dialogView.findViewById(R.id.edit_text_player2);

        builder.setPositiveButton("Start Game", (dialog, which) -> {
            player1Name = editTextPlayer1.getText().toString();
            player2Name = editTextPlayer2.getText().toString();

            if (!player1Name.isEmpty() && !player2Name.isEmpty()) {
                textViewPlayer1.setText(player1Name + " 0");
                textViewPlayer2.setText(player2Name + " 0");

                currentChanceTextView.setText(player1Name + "'s Turn");

                if (!dbHelper.doesUserExist(player1Name)) {
                    dbHelper.addUser(player1Name);
                }

                if (!dbHelper.doesUserExist(player2Name)) {
                    dbHelper.addUser(player2Name);
                }
            } else {
                Toast.makeText(MainActivity.this, "Please enter both player names", Toast.LENGTH_SHORT).show();
                showNameInputDialog();
            }
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setTextSize(16);
            button.setTextColor(getResources().getColor(R.color.black));
        });

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText("0");
            currentChanceTextView.setText(player2Name + "'s Turn");
        } else {
            ((Button) v).setText("X");
            currentChanceTextView.setText(player1Name + "'s Turn");
        }

        roundCount++;
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
                currentChanceTextView.setText(player1Name + "'s Turn");
                dbHelper.updateUserData(player1Name, player1Points); // Update player 1's data in the database
                dbHelper.updateUserTotalMatch(player1Name,player2Name);
            } else {
                player2Wins();
                currentChanceTextView.setText(player1Name + "'s Turn");
                dbHelper.updateUserData(player2Name, player2Points); // Update player 2's data in the database
                dbHelper.updateUserTotalMatch(player1Name,player2Name);
            }
        } else if (roundCount == 9) {
            draw();
            currentChanceTextView.setText(player1Name + "'s Turn");
            dbHelper.updateUserDrawMatch(player1Name,player2Name);
            dbHelper.updateUserTotalMatch(player1Name,player2Name);
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }
    private void player1Wins() {
        player1Points++;
        Toast.makeText(this, "Hurrahh..!!" + player1Name +" wins the match!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }
    private void player2Wins() {
        player2Points++;
        Toast.makeText(this, "Hurrahh..!!" + player2Name +" wins the match!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }
    private void draw() {
        Toast.makeText(this, "Oops! It's a Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText() {
        textViewPlayer1.setText(player1Name + " " + player1Points);
        textViewPlayer2.setText(player2Name + " " + player2Points);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        player1Turn = true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }

    //App Bar With Options Code
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.leaderboard) {
            Toast.makeText(this, "About Us clicked", Toast.LENGTH_SHORT).show();
            Intent iLeaderboard = new Intent(MainActivity.this, LeaderboardActivity.class);
            startActivity(iLeaderboard);

            return true;
        }
        else if (id == R.id.logout) {
//            SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putBoolean("flag", false);
//            editor.putString("name", "");
//            editor.apply();
//            Toast.makeText(this, "Log out Successful", Toast.LENGTH_SHORT).show();
//            Intent iMusic = new Intent(Dashboard.this, MainActivity.class);
//            startActivity(iMusic);
//            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}