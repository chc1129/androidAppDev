package com.example.shooting;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bullet extends BaseObject {

    private static final float MOVE_WEIGHT = 3.0f;

    private final Paint paint = new Paint();

    private static final float SIZE = 15f;

    public final float alignX;

    Bullet(Rect rect, float alignXValue) {
        yPosition = rect.centerY();
        xPosition = rect.centerX();
        alignX = alignXValue;

        paint.setColor(Color.RED);
    }

    @Override
    public void move() {
        yPosition -= 1 * MOVE_WEIGHT;
        xPosition += alignX * MOVE_WEIGHT;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(xPosition, yPosition, SIZE, paint);
    }
}
