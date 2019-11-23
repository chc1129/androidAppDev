package com.example.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

    private static final int GROUND_MOVE_TO_LEFT = 10;
    private static final int GROUND_HEIGHT = 50;
    private Ground ground;

    private Bitmap droidBitmap;
    private Droid droid;

    private final Droid.Callback droidCallback = new Droid.Callback() {
        @Override
        public int getDistanceFromGround(Droid droid) {
            boolean horizontal = !(droid.rect.left >= ground.rect.right
                    || droid.rect.right <= ground.rect.left);

            if (!horizontal) {
                return Integer.MAX_VALUE;
            }

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
        ground.move(GROUND_MOVE_TO_LEFT);
        droid.draw(canvas);
        ground.draw(canvas);

        invalidate();
    }

    private static final long MAX_TOUCH_TIME = 500;     // ミリ秒
    private long touchDownStartTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownStartTime = System.currentTimeMillis();
                return true;
            case MotionEvent.ACTION_UP:
                float time = System.currentTimeMillis() - touchDownStartTime;
                jumpDroid(time);
                touchDownStartTime = 0;
                break;
        }
        return super.onTouchEvent(event);
    }

    private void jumpDroid(float time) {
        if (droidCallback.getDistanceFromGround(droid) > 0) {
            return;
        }

        droid.jump(Math.min(time, MAX_TOUCH_TIME) / MAX_TOUCH_TIME);
    }
}
