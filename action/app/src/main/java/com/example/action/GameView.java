package com.example.action;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    protected woid onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(droidBitmap, 0, 0, PAINT);
    }
}
