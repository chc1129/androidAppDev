package com.example.labyrinth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LabyrinthView.EventCallback {

    private static final String EXTRA_KEY_STAGE_SEED = "stage_seed";

    public static Intent newIntent(Context context, int stageSeed) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_KEY_STAGE_SEED, stageSeed);

        return intent;
    }

    private int stageSeed;
    private boolean isFinished;

    private LabyrinthView labyrinthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stageSeed = getIntent().getIntExtra(EXTRA_KEY_STAGE_SEED, 0);

        labyrinthView = new LabyrinthView(this);
        labyrinthView.setCallback(this);
        labyrinthView.setStageSeed(stageSeed);
        labyrinthView.startSensor();

        setContentView(labyrinthView);
    }

    @Override
    public void onGoal() {
        if (isFinished) {
            return;
        }
        isFinished = true;

        Toast.makeText(this, "Goal!!", Toast.LENGTH_SHORT).show();

        labyrinthView.stopSensor();

        Intent intent = newIntent(this, stageSeed + 1);
        startActivity(intent);
        finish();
    }

    @Override
    public void onHole() {
        if (isFinished) {
            return;
        }
        isFinished = true;

        Toast.makeText(this, "Hole!!", Toast.LENGTH_SHORT).show();

        labyrinthView.stopSensor();

        Intent intent = newIntent(this, stageSeed);
        startActivity(intent);
        finish();
    }
}
