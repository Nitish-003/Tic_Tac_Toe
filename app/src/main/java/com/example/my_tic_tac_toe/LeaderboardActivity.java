package com.example.my_tic_tac_toe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeaderboardAdapter leaderboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add a divider with 10dp height between items
        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.item_divider)); // Define the divider drawable

        recyclerView.addItemDecoration(itemDecoration);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leaderboard");


        // Get the top users from the database
        MyDbHelper dbHelper = new MyDbHelper(this);
        List<User> topUsers = dbHelper.getTopUsers(10);

        // Set up the RecyclerView adapter
        leaderboardAdapter = new LeaderboardAdapter(topUsers);
        recyclerView.setAdapter(leaderboardAdapter);
    }
}

class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private List<User> userList;

    LeaderboardAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.usernameTextView.setText(user.getUsername());
        holder.matchesWonTextView.setText(String.valueOf(user.getMatchesWon()));
        holder.matchesTotalTextView.setText(String.valueOf(user.getMatchesPlayed()));
        holder.matchesDrawTextView.setText(String.valueOf(user.getMatchesDraw()));

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView matchesWonTextView;
        TextView matchesTotalTextView;
        TextView matchesDrawTextView;

        ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            matchesWonTextView = itemView.findViewById(R.id.matchesWonTextView);
            matchesTotalTextView = itemView.findViewById(R.id.matchesPlayedTextView);
            matchesDrawTextView = itemView.findViewById(R.id.matchesDrawTextView);

        }
    }

}
