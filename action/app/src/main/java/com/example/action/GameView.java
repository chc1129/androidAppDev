package com.example.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class GameView extends View {

    private static final int GROUND_HEIGHT = 50;
    private Ground ground;

    private Bitmap droidBitmap;
    private Droid droid;

    private final Droid.Callback droidCallback = new Droid.Callback() {
        @Override
        public int getDistanceFromGround(Droid droid) {
            return ground.rect.top - droid.rect.bottom;
        }
    };

    public GameView(Context context) {
        super(context);

        droidBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.droid);
        droid = new Droid(droidBitmap, 0, 0, droidCallback);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        if (ground == null) {
            int top = height - GROUND_HEIGHT;
            ground = new Ground(0, top, width, height);
        }

        droid.move();
        droid.draw(canvas);
        ground.draw(canvas);

        invalidate();
    }
}
