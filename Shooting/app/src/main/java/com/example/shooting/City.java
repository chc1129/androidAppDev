package com.example.shooting;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import javax.security.auth.callback.Callback;

public class City extends BaseObject {

    private static final int CITY_HEIGHT = 80;

    private final Paint paint = new Paint();

    public final Rect rect;

    public City(int width, int height) {

        int left = 0;
        int top = height - CITY_HEIGHT;
        int right = width;
        int bottom = height;
        rect = new Rect(left, top, right, bottom);

        yPosition = rect.centerY();
        xPosition = rect.centerX();

        paint.setColor(Color.LTGRAY);
    }

    @Override
    public boolean isHit(BaseObject object) {
        if (object.getType() != Type.Missile) {
            return false;
        }

        int x = Math.round(object.xPosition);
        int y = Math.round(object.yPosition);
        return rect.contains(x, y);
    }

    @Override
    public Type getType() {
        return Type.City;
    }

    @Override
    public void draw(Canvas canvas) {
        if (state != STATE_NORMAL) {
            return;
        }
        canvas.drawRect(rect, paint);
    }

    @Override
    public void move() {
    }
}
