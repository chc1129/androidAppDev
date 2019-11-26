package com.example.labyrinth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;

public class LabyrinthView extends SurfaceView implements SurfaceHolder.Callback {

    private static final int DRAW_INTERVAL = 1000 / 60;
    private static final float TEXT_SIZE = 40f;

    private final Paint paint = new Paint();
    private final Paint textPaint = new Paint();

    private final Bitmap ballBitmap;

    public LabyrinthView(Context context) {
        super(context);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(TEXT_SIZE);
        ballBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);

        getHolder().addCallback(this);
    }

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

                drawLabyrinth(canvas);

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

    private static final float ALPHA = 0.8f;
    private float[] sensorValues;

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (sensorValues == null) {
                sensorValues = new float[3];
                sensorValues[0] = event.values[0];
                sensorValues[1] = event.values[1];
                sensorValues[2] = event.values[2];
                return;
            }
            sensorValues[0] = sensorValues[0] * ALPHA + event.values[0] * (1f - ALPHA);
            sensorValues[1] = sensorValues[1] * ALPHA + event.values[1] * (1f - ALPHA);
            sensorValues[2] = sensorValues[2] * ALPHA + event.values[2] * (1f - ALPHA);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void startSensor() {
        sensorValues = null;

        SensorManager sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopSensor() {
        SensorManager sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(sensorEventListener);
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

    public void drawLabyrinth(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(ballBitmap, 50, 50, paint);

        if (sensorValues != null) {
            canvas.drawText("sensor[0] = " + sensorValues[0], 10, 150, textPaint);
            canvas.drawText("sensor[1] = " + sensorValues[1], 10, 200, textPaint);
            canvas.drawText("sensor[2] = " + sensorValues[2], 10, 250, textPaint);
        }
    }
}
