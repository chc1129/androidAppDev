package com.example.labyrinth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LabyrinthView.EventCallback {

    private LabyrinthView labyrinthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        labyrinthView = new LabyrinthView(this);
        labyrinthView.setCallback(this);
        labyrinthView.startSensor();

        setContentView(labyrinthView);
    }

    @Override
    public void onGoal() {
        Toast.makeText(this, "Goal!!", Toast.LENGTH_SHORT).show();

        labyrinthView.stopSensor();
    }
}
