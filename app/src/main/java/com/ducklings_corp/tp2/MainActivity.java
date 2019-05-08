package com.ducklings_corp.tp2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private ImageButton[] images = new ImageButton[9];
    private int movements = 0;
    private int firstRandom = 0;
    private int secondRandom = 0;
    private HashMap<String, Integer> scoreboard;
    private String username = null;
    private int lastPlayMovements = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        {
            int i = 0;
            this.images[i++] = findViewById(R.id.tile0);
            this.images[i++] = findViewById(R.id.tile1);
            this.images[i++] = findViewById(R.id.tile2);
            this.images[i++] = findViewById(R.id.tile3);
            this.images[i++] = findViewById(R.id.tile4);
            this.images[i++] = findViewById(R.id.tile5);
            this.images[i++] = findViewById(R.id.tile6);
            this.images[i++] = findViewById(R.id.tile7);
            this.images[i++] = findViewById(R.id.tile8);
        }

        Random rng;
        int flipped;
        boolean side;

        rng = new Random();

        do {
            flipped = 0;
            for (int i = 0; i < 9; i++) {
                side = rng.nextBoolean();
                flipTile(i, side);
                flipped += side ? 0 : 1;
            }
        } while (flipped == 0 || flipped == 9);

        TextView txtrandom;
        Random Rgn;
        Rgn = new Random();

        firstRandom = Rgn.nextInt(10);
        secondRandom = Rgn.nextInt(10);

        txtrandom = findViewById(R.id.random);
        txtrandom.setText(firstRandom + "+" + secondRandom + " = ");

        try {
            scoreboard = (HashMap<String, Integer>) this.getIntent().getExtras().getSerializable("scoreboard");
        } catch (Exception e) {
            Log.d("Scores", "No scores recorded");
            scoreboard = new HashMap<>();
        }

        for (String k : scoreboard.keySet()) {
            Log.d("Scores", String.format("%s: %d movimientos %n", k, scoreboard.get(k)));
        }

        TextView lastPlayMovementsView;

        lastPlayMovementsView = findViewById(R.id.lastPlayMovements);

        try {
            lastPlayMovements = (int) this.getIntent().getExtras().getInt("lastPlayMovements");
            lastPlayMovementsView.setText("Ultima partida: " + lastPlayMovements);
        } catch (Exception e) {
            Log.d("Scores", "No other play recorded");
            lastPlayMovements = -1;
        }

    }

    public void switchTiles(View view) {
        ImageButton button;
        button = (ImageButton) view;

        /*
        0 1 2
        3 4 5
        6 7 8
        */
        int idx = btnIdToIdx(button.getId()); // Transform ID to this.images's index

        switchSurroundingTiles(idx);

        if (won()) {
            endGame(true);
        } else if (lastPlayMovements != -1 && lastPlayMovements < movements) {
            endGame(false);
        }
    }

    private void switchSurroundingTiles(int idx) {
        TextView thisPlayMovements;

        thisPlayMovements = findViewById(R.id.thisPlayMovements);

        Log.d("Flipping","Tile: "+idx);

        /*
         * This arrays declares which tiles should be flipped depending
         *  on the one clicked by the user
         *
         *  ! The 4 is not defined 'cause it should always be flipped, so it's hardcoded bellow
         *  ! Due to the hardcoded flipTile(4), clicking the center flips the 4th tile 2 times.
         *   To prevent this, when clicking it, it is declared in flipActions to flip itself again,
         *   making it a triple flip, so it lands on the opposite side it started
         * */
        int[][] flipActions = {
                {1, 3},    // 0
                {0, 2},    // 1
                {1, 5},    // 2
                {0, 6},    // 3
                {1, 3, 5, 7, 4},    // 4
                {8, 2},    // 5
                {7, 3},    // 6
                {8, 6},    // 7
                {7, 5},    // 8
        };

        flipTile(idx);
        for (int i = 0; i < flipActions[idx].length; i++) {
            flipTile(flipActions[idx][i]);
        }
        flipTile(4);

        movements++;
        thisPlayMovements.setText("Esta partida: " + movements);
    }

    private void flipTile(int idx) {
        Drawable.ConstantState buttonState, vimState;

        buttonState = this.images[idx].getDrawable().getConstantState();
        vimState = ContextCompat.getDrawable(this, R.drawable.vim).getConstantState();

        if (buttonState == vimState) {
            flipTile(idx, false);
        } else {
            flipTile(idx, true);
        }
    }

    private void flipTile(int idx, boolean side) {
        if (side) {
            this.images[idx].setImageResource(R.drawable.vim);
        } else {
            this.images[idx].setImageResource(R.drawable.emacs);
        }
    }

    private int btnIdToIdx(int id) {
        for (int i = 0; i < 9; i++) {
            if (this.images[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private boolean won() {
        Drawable.ConstantState buttonState, vimState;

        vimState = ContextCompat.getDrawable(this, R.drawable.vim).getConstantState();

        int trues = 0;
        for (int i = 0; i < 9; i++) {
            buttonState = this.images[i].getDrawable().getConstantState();
            if (buttonState == vimState) {
                trues++;
            }
        }
        return trues == 0 || trues == 9;
    }

    private void endGame(boolean won) {
        if(won) {
            Toast.makeText(this, "Ganaste!", Toast.LENGTH_LONG).show();

            Bundle data;
            Intent intent;

            this.scoreboard.put(this.username, this.movements);

            data = new Bundle();
            data.putSerializable("scoreboard", this.scoreboard);
            data.putInt("lastPlayMovements", this.movements);
            intent = new Intent(MainActivity.this, scoreboard.class);

            intent.putExtras(data);
            startActivity(intent);
        } else {
            findViewById(R.id.buttoninvisible).setVisibility(View.GONE);
            findViewById(R.id.endGameScreen).setVisibility(View.VISIBLE);
        }
    }

    public void acceptUsername(View view) {
        EditText usernameInput;
        LinearLayout captcha, usernameInputLayout;

        usernameInput = findViewById(R.id.username);

        username = usernameInput.getText().toString();

        captcha = findViewById(R.id.captchaLayout);
        captcha.setVisibility(View.VISIBLE);

        usernameInputLayout = findViewById(R.id.usernameInputLayout);
        usernameInputLayout.setVisibility(View.GONE);

    }

    public void checkChaptcha(View view) {
        EditText editRandom;
        int convertedRandom;
        LinearLayout tiles, captcha;

        editRandom = findViewById(R.id.captcha);

        convertedRandom = Integer.parseInt(editRandom.getText().toString());
        if (convertedRandom == firstRandom + secondRandom) {
            tiles = findViewById(R.id.buttoninvisible);
            captcha = findViewById(R.id.captchaLayout);

            tiles.setVisibility(View.VISIBLE);
            captcha.setVisibility(View.INVISIBLE);
        }
    }

    public void smartWin(View view) {
        final Drawable.ConstantState vimState;
        Drawable.ConstantState buttonState;
        int vims, emacs;
        final boolean swapVims;

        vimState = ContextCompat.getDrawable(this, R.drawable.vim).getConstantState();

        vims = 0;
        emacs = 0;

        for (int i = 0; i < 9; i++) {
            buttonState = images[i].getDrawable().getConstantState();
            if (buttonState == vimState) {
                vims++;
            } else {
                emacs++;
            }
        }
        swapVims = emacs > vims;


        final Timer timer;
        TimerTask timedTask;

         timer = new Timer();
         timedTask = new TimerTask() {
             @Override
             public void run() {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         Drawable.ConstantState buttonState;
                         Boolean[] vimTiles;

                         vimTiles = new Boolean[9];
                         for (int i = 0; i < 9; i++) {
                             buttonState = images[i].getDrawable().getConstantState();
                             vimTiles[i] = buttonState == vimState;
                         }
                         for (int i = 0; i < 9; i++) {
                             if (vimTiles[i] && swapVims) {
                                 switchSurroundingTiles(i);
                             } else if (!vimTiles[i] && !swapVims) {
                                 switchSurroundingTiles(i);
                             }
                         }
                         if (won()) {
                             timer.cancel();
                             endGame(true);
                         } else if (lastPlayMovements != -1 && lastPlayMovements < movements) {
                             endGame(false);
                         }
                     }
                 });
             }
         };
        timer.schedule(timedTask, 0, 500);
    }

    public void randomWin(View view) {
        final Timer timer;
        final TimerTask timedTask;

        timer = new Timer();
        timedTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Random rng;
                        rng = new Random();
                        switchSurroundingTiles(rng.nextInt(9));
                        if (won()) {
                            timer.cancel();
                            endGame(true);
                        } else if (lastPlayMovements != -1 && lastPlayMovements < movements) {
                            endGame(false);
                        }
                    }
                });
            }
        };
        timer.schedule(timedTask, 0, 500);
    }

    public void restart(View view) {
        Bundle data;
        Intent intent;

        data = new Bundle();
        data.putSerializable("scoreboard", this.scoreboard);
        data.putInt("lastPlayMovements", this.lastPlayMovements);

        intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtras(data);
        startActivity(intent);
    }
}
