package com.ducklings_corp.tp2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.HashMap;

public class scoreboard extends Activity {
    private boolean submited = false;
    private int score = 0;
    private HashMap<String,Integer> scoreboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        submited = false;
        score = this.getIntent().getExtras().getInt("score");

        try {
            scoreboard = (HashMap<String, Integer>) this.getIntent().getExtras().getSerializable("scoreboard");
        } catch (Exception e) {
            scoreboard = new HashMap<>();
        }
    }

    public void submitScore(View view) {
        if (submited) {
            return;
        }

        EditText usernameInput;
        String username;
        TextView scoreboardView;

        usernameInput = findViewById(R.id.username);
        username = usernameInput.getText().toString();

        scoreboardView = findViewById(R.id.scoreboard);

        scoreboard.put(username,score);

        for(String k: scoreboard.keySet()) {
            scoreboardView.setText(
                    String.format("%s\n%s: %d movimientos %n", scoreboardView.getText(), username, score)
            );
        }


        submited = true;
    }

    public void back(View view) {
        Intent intent;
        intent = new Intent(scoreboard.this,MainActivity.class);
        startActivity(intent);
    }
}
