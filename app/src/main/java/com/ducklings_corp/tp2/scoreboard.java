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
    private HashMap<String,Integer> scoreboard;
    private int lastPlayMovements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoreboard = (HashMap<String, Integer>) this.getIntent().getExtras().getSerializable("scoreboard");
        lastPlayMovements = (int) this.getIntent().getExtras().getInt("lastPlayMovements");

        TextView scoreboardView;
        scoreboardView = findViewById(R.id.scoreboard);
        for(String k: scoreboard.keySet()) {
            scoreboardView.setText(
                    String.format("%s\n%s: %d movimientos %n", scoreboardView.getText(), k, scoreboard.get(k))
            );
        }
    }

    public void back(View view) {
        Bundle data;
        Intent intent;

        data = new Bundle();
        data.putSerializable("scoreboard",this.scoreboard);
        data.putInt("lastPlayMovements",this.lastPlayMovements);

        intent = new Intent(scoreboard.this,MainActivity.class);
        intent.putExtras(data);
        startActivity(intent);
    }
}
