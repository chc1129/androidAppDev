package com.example.action;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.telecom.Call;

public class Droid {

    private final Paint paint = new Paint();

    private Bitmap bitmap;

    final Rect rect;

    public interface Callback {
        int getDistanceFromGround(Droid droid);
    }

    private final Callback callback;

    public Droid(Bitmap bitmap, int left, int top, Callback callback) {
        this.bitmap = bitmap;
        int right = left + bitmap.getWidth();
        int bottom = top + bitmap.getHeight();
        this.rect = new Rect(left, top, right, bottom);
        this.callback = callback;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, rect.left, rect.top, paint);
    }

    public void move() {
        int distanceFromGround = callback.getDistanceFromGround(this);
        if (distanceFromGround == 0) {
            return;
        } else if (distanceFromGround < 0) {
            rect.offset(0, distanceFromGround);
            return;
        }

        rect.offset(0, 5); // 下へ
    }
}