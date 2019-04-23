package com.ducklings_corp.tp2;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageButton[] images = new ImageButton[9];

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
    }

    public void switchTile(View view) {
        ImageButton button;

        button = (ImageButton) view;

        switch (button.getId()) {
            case R.id.tile0:
                flipTile(0);
                break;

            default:
                break;
        }
    }

    private void fliptTile(int idx) {
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
}
