package com.example.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class GameView extends View {

    private static final Paint PAINT = new Paint();

    private static final int GROUND_HEIGHT = 50;
    private Ground ground;

    private Bitmap droidBitmap;

    public GameView(Context context) {
        super(context);

        droidBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.droid);
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

        canvas.drawBitmap(droidBitmap, 0, 0, PAINT);
        ground.draw(canvas);
    }
}
