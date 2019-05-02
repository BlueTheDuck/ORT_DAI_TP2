package com.ducklings_corp.tp2;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageButton[] images = new ImageButton[9];
    private int movements = 0;
    private int firstRandom=0;
    private int secondRandom=0;

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
        Rgn=new Random();

        firstRandom= Rgn.nextInt(10);
        secondRandom= Rgn.nextInt(10);

        txtrandom= findViewById(R.id.random);
        txtrandom.setText(firstRandom+"+"+secondRandom);
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
        /*
        * This arrays declares which tiles should be flipped depending
        *  on the one clicked by the users
        *
        *  ! The 4 is not defined 'cause it should always be flipped, so it's hardcoded bellow
        *  ! Due to the hardcoded flipTile(4), clicking the center flips the 4th tile 2 times.
        *   To prevent this, when clicking it, it is declared in flipActions to flip itself again,
        *   making it a triple flip, so it lands on the opposite side it started
        * */
        int[][] flipActions = {
                {1,3},    // 0
                {0,2},    // 1
                {1,5},    // 2
                {0,6},    // 3
                {1,3,5,7,4},    // 4
                {8,2},    // 5
                {7,3},    // 6
                {8,6},    // 7
                {7,5},    // 8
        };

        flipTile(idx);
        for (int i=0;i<flipActions[idx].length;i++) {
            flipTile(flipActions[idx][i]);
        }
        flipTile(4);

        movements++;

        if(won()) {
            Toast.makeText(this,"Ganaste!",Toast.LENGTH_LONG).show();
        }
    }

    private void flipTile(int idx) {
        Drawable.ConstantState buttonState, vimState;

        buttonState = this.images[idx].getDrawable().getConstantState();
        vimState = ContextCompat.getDrawable(this,R.drawable.vim).getConstantState();

        if(buttonState==vimState) {
            flipTile(idx,false);
        } else {
            flipTile(idx,true);
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
        for(int i=0;i<9;i++) {
            if(this.images[i].getId()==id) {
                return i;
            }
        }
        return -1;
    }

    private boolean won() {
        Drawable.ConstantState buttonState, vimState;

        vimState = ContextCompat.getDrawable(this,R.drawable.vim).getConstantState();

        int trues = 0;
        for(int i=0;i<9;i++) {
            buttonState = this.images[i].getDrawable().getConstantState();
            if(buttonState==vimState) {
                trues++;
            }
        }
        return trues==0 || trues==9;
    }

    private void Checkchaptcha(View view){
        EditText editRandom;
        int convertedRandom;

    editRandom=findViewById(R.id.captcha);

        convertedRandom=Integer.parseInt(editRandom.getText().toString());
        if(convertedRandom==firstRandom+secondRandom){

        }
    }
}
