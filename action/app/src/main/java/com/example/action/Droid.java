package com.example.action;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Droid {

    private final Paint paint  = new Paint();

    private Bitmap bitmap;

    final Rect rect;

    public Droid(Bitmap bitmap, int left, int top) {
        int right = left + bitmap.getWidth();
        int bottom = top + bitmap.getHeight();
        this.rect = new Rect(left, top, right, bottom);
        this.bitmap = bitmap;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, rect.left, rect.top, paint);
    }

    public void move() {
        rect.offset(0, 5); // 下へ
    }
}