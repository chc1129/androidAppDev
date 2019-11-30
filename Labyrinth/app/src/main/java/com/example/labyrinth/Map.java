package com.example.labyrinth;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;
import java.util.concurrent.BlockingDeque;

public class Map implements Ball.OnMoveListener {
    private final int blockSize;
    private int horizontalBlockCount;
    private int verticalBlockCount;

    private final Block[][] blockArray;
    private final Block[][] targetBlock = new Block[3][3];

    private Block startBlock;

    public Block getStartBlock() {
        return startBlock;
    }

    private final LabyrinthView.EventCallback eventCallback;

    public Map(int width, int height, int blockSize, int stageSeed,
               LabyrinthView.EventCallback eventCallback) {
        this.blockSize = blockSize;
        this.horizontalBlockCount = width / blockSize;
        this.verticalBlockCount = height / blockSize;
        this.eventCallback = eventCallback;

        blockArray = createMap(stageSeed);
    }

    private Block[][] createMap(int seed) {

        if (horizontalBlockCount % 2 == 0) {
            horizontalBlockCount--;
        }
        if (verticalBlockCount % 2 == 0) {
            verticalBlockCount--;
        }

        Block[][] array = new Block[verticalBlockCount][horizontalBlockCount];

        LabyrinthGenerator.MapResult mapResult = LabyrinthGenerator.getMap(horizontalBlockCount, verticalBlockCount, seed);

        int[][] map = mapResult.map;

        for (int y = 0; y < verticalBlockCount; y++) {
            for (int x = 0; x < horizontalBlockCount; x++) {
                int type = map[y][x];
                int left = x * blockSize + 1;
                int top = y * blockSize + 1;
                int right = left + blockSize - 2;
                int bottom = top + blockSize - 2;
                array[y][x] = new Block(type, left, top, right, bottom);
            }
        }
        startBlock = array[mapResult.startY][mapResult.startX];

        return array;
    }

    void draw(Canvas canvas) {
        int yLength = blockArray.length;
        for (int y = 0; y < yLength; y++) {

            int xLength = blockArray[y].length;
            for (int x = 0; x < xLength; x++) {
                blockArray[y][x].draw(canvas);
            }
        }
    }

    private void setTargetBlock(int verticalBlock, int horizontalBlock) {
        targetBlock[1][1] = getBlock(verticalBlock, horizontalBlock);

        targetBlock[0][0] = getBlock(verticalBlock - 1, horizontalBlock - 1);
        targetBlock[0][1] = getBlock(verticalBlock - 1, horizontalBlock);
        targetBlock[0][2] = getBlock(verticalBlock - 1, horizontalBlock + 1);

        targetBlock[1][0] = getBlock(verticalBlock, horizontalBlock - 1);
        targetBlock[1][2] = getBlock(verticalBlock, horizontalBlock + 1);

        targetBlock[2][0] = getBlock(verticalBlock + 1, horizontalBlock - 1);
        targetBlock[2][1] = getBlock(verticalBlock + 1, horizontalBlock);
        targetBlock[2][2] = getBlock(verticalBlock + 1, horizontalBlock + 1);
    }

    private Block getBlock(int y, int x) {
        if (y < 0 || x < 0 || y >= verticalBlockCount || x >= horizontalBlockCount) {
            return null;
        }
        return blockArray[y][x];
    }

    private int placeVerticalBlock = -1;
    private int placeHorizontalBlock = -1;

    private boolean canMove(Rect movedRect) {
        int horizontalBlock = movedRect.centerX() / blockSize;
        int verticalBlock = movedRect.centerY() / blockSize;

        if (placeHorizontalBlock != horizontalBlock
                || placeVerticalBlock != verticalBlock) {
            setTargetBlock(verticalBlock, horizontalBlock);
            placeHorizontalBlock = horizontalBlock;
            placeVerticalBlock = verticalBlock;
        }

        int yLength = targetBlock.length;
        for (int y = 0; y < yLength; y++) {
            int xLength = targetBlock[0].length;
            for (int x = 0; x < xLength; x++) {
                Block block = targetBlock[y][x];
                if (block == null) {
                    continue;
                }
                if (block.type == Block.TYPE_WALL && Rect.intersects(block.rect, movedRect)) {
                    return false;
                } else if (block.type == Block.TYPE_GOAL
                        && block.rect.contains(movedRect.centerX(), movedRect.centerY())) {
                    eventCallback.onGoal();
                    return true;
                }
            }
        }
        return true;
    }

    private final Rect tempBallRect = new Rect();

    @Override
    public int getCanMoveHorizontalDistance(RectF ballRect, int xOffset) {
        int result = xOffset;

        ballRect.round(tempBallRect);
        tempBallRect.offset(xOffset, 0);

        int align = 1;
        if (xOffset < 0) {
            align = -1;
        }

        while (!canMove(tempBallRect)) {
            tempBallRect.offset(-align, 0);
            result -= align;
        }

        return result;
    }

    @Override
    public int getCanMoveVerticalDistance(RectF ballRect, int yOffset) {
        int result = yOffset;

        ballRect.round(tempBallRect);
        tempBallRect.offset(0, yOffset);

        int align = 1;
        if (yOffset < 0) {
            align = -1;
        }

        while (!canMove(tempBallRect)) {
            tempBallRect.offset(0, -align);
            result -= align;
        }

        return result;
    }

    static class Block {
        private static final int TYPE_FLOOR = 0;
        private static final int TYPE_WALL = 1;
        private static final int TYPE_START = 2;
        private static final int TYPE_GOAL = 3;

        private static final int COLOR_FLOOR = Color.GRAY;
        private static final int COLOR_WALL = Color.BLACK;
        private static final int COLOR_START = Color.YELLOW;
        private static final int COLOR_GOAL = Color.GREEN;

        private final int type;
        private final Paint paint;

        final Rect rect;

        private Block(int type, int left, int top, int right, int bottom) {
            this.type = type;
            paint = new Paint();

            switch (type) {
                case TYPE_FLOOR:
                    paint.setColor(COLOR_FLOOR);
                    break;
                case TYPE_WALL:
                    paint.setColor(COLOR_WALL);
                    break;
                case TYPE_START:
                    paint.setColor(COLOR_START);
                    break;
                case TYPE_GOAL:
                    paint.setColor(COLOR_GOAL);
                    break;
            }
            rect = new Rect(left, top, right, bottom);
        }

        private void draw(Canvas canvas) {
            canvas.drawRect(rect, paint);
        }
    }
}
