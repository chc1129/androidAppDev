package com.example.shooting;

import android.graphics.Canvas;

public abstract class BaseObject {
    static final int STATE_NORMAL = -1;
    static final int STATE_DESTROYED = 0;

    int state = STATE_NORMAL;

    enum Type {
        Droid,
        Bullet,
        Missile,
        City,
    }

    public abstract Type getType();

    float yPosition;
    float xPosition;

    public abstract void draw(Canvas canvas);

    public boolean isAvailable(int width, int height) {
        if (yPosition < 0 || xPosition < 0 || yPosition > height || xPosition > width) {
            return false;
        }

        if (state == STATE_DESTROYED) {
            return false;
        }

        return true;
    }

    public abstract void move();

    public abstract boolean isHit(BaseObject object);

    public void hit() {
        state = STATE_DESTROYED;
    }

    static double calcDistance(BaseObject obj1, BaseObject obj2) {
        float distX = obj1.xPosition - obj2.xPosition;
        float distY = obj1.yPosition - obj2.yPosition;
        return Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
    }
}
