package com.example.action;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
    implements GameView.GameOverCallback {

    private GameView gameView;

    @Override
    public void onGameOver() {
        Toast.makeText(this, "Game Over", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        gameView.setCallback(this);
        setContentView(gameView);
    }
}
