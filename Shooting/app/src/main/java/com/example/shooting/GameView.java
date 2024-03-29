package com.example.shooting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final long DRAW_INTERVAL = 1000 / 60;

    private static final int MISSILE_LAUNCH_WEIGHT = 50;

    private static final float SCORE_TEXT_SIZE = 60.0f;

    private static final long VIBRATION_LENGTH_HIT_MISSILE = 100;
    private static final long VIBRATION_LENGTH_HIT_DROID = 1000;

    private static final int SCORE_LEVEL = 100;

    private Droid droid;
    private City city;
    private final List<BaseObject> missileList = new ArrayList<>();
    private final List<BaseObject> bulletList = new ArrayList<>();

    private final Random rand = new Random(System.currentTimeMillis());

    private long score;
    private final Paint paintScore = new Paint();

    private DrawThread drawThread;

    private class DrawThread extends Thread {
        private final AtomicBoolean isFinished = new AtomicBoolean();

        public void finish() {
            isFinished.set(true);
        }

        @Override
        public void run() {
            SurfaceHolder holder = getHolder();

            while (!isFinished.get()) {
                if (holder.isCreating()) {
                    continue;
                }

                Canvas canvas = holder.lockCanvas();
                if (canvas == null) {
                    continue;
                }

                drawGame(canvas);
                holder.unlockCanvasAndPost(canvas);

                synchronized (this) {
                    try {
                        wait(DRAW_INTERVAL);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void startDrawThread() {
        stopDrawThread();

        drawThread = new DrawThread();
        drawThread.start();
    }

    public boolean stopDrawThread() {
        if (drawThread == null) {
            return false;
        }
        drawThread.finish();
        drawThread = null;
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startDrawThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopDrawThread();
    }

    public interface EventCallback {
        void onGameOver(long score);
    }

    private EventCallback eventCallback;

    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    private Handler handler = new Handler();

    private final Vibrator vibrator;

    public GameView(Context context) {
        super(context);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        paintScore.setColor(Color.BLACK);
        paintScore.setTextSize(SCORE_TEXT_SIZE);
        paintScore.setAntiAlias(true);

        getHolder().addCallback(this);
    }

    private void drawGame(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        if (droid == null) {
            Bitmap droidBitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.droid);
            droid = new Droid(droidBitmap, width, height);
            city = new City(width, height);
        }

        if (rand.nextInt(MISSILE_LAUNCH_WEIGHT) == 0) {
            long count = score / SCORE_LEVEL + 1;
            for (int i = 0; i < count; i++) {
                Missile missile = launchMissile(width, height);
                missileList.add(missile);
            }
        }

        drawObjectList(canvas, missileList, width, height);
        drawObjectList(canvas, bulletList, width, height);

        for (int i = 0; i < missileList.size(); i++) {
            BaseObject missile = missileList.get(i);

            if (droid.isHit(missile) || city.isHit(missile)) {
                missile.hit();
                droid.hit();
                city.hit();

                vibrator.vibrate(VIBRATION_LENGTH_HIT_DROID);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        eventCallback.onGameOver(score);
                    }
                });
                break;
            }

            for (int j = 0; j < bulletList.size(); j++) {
                BaseObject bullet = bulletList.get(j);

                if (bullet.isHit(missile)) {
                    missile.hit();
                    bullet.hit();

                    vibrator.vibrate(VIBRATION_LENGTH_HIT_MISSILE);

                    score += 10;
                }
            }
        }

        city.draw(canvas);
        droid.draw(canvas);

        canvas.drawText("Score: " + score, 0, SCORE_TEXT_SIZE, paintScore);
    }

    private static void drawObjectList(Canvas canvas, List<BaseObject> objectList, int width, int height) {
        for (int i = 0; i < objectList.size(); i++) {
            BaseObject object = objectList.get(i);
            if (object.isAvailable(width, height)) {
                object.move();
                object.draw(canvas);
            } else {
                objectList.remove(object);
                i--;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                fire(event.getX(), event.getY());
                break;
        }

        return super.onTouchEvent(event);
    }

    private void fire(float x, float y) {
        float alignX = (x - droid.rect.centerX()) / Math.abs(y - droid.rect.centerY());

        Bullet bullet = new Bullet(droid.rect, alignX);
        bulletList.add(0, bullet);
    }

    private Missile launchMissile(int width, int height) {
        int fromX = rand.nextInt(width);
        int toX = rand.nextInt(width);

        float alignX = (toX - fromX) / (float) height;
        return new Missile(fromX, alignX);
    }
}