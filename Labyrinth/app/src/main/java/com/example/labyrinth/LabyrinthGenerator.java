package com.example.labyrinth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LabyrinthGenerator {

    public static final int FLOOR = 0;
    public static final int WALL = 1;
    public static final int INNER_WALL = -1;

    public enum Direction {
        TOP,
        LEFT,
        RIGHT,
        BOTTOM,
    }

    public static int[][] getMap(int horizontalBlockCount, int verticalBlockCount, int seed) {

        int[][] result = new int[verticalBlockCount][horizontalBlockCount];

        for (int y = 0; y < verticalBlockCount; y++) {
            for (int x = 0; x < horizontalBlockCount; x++) {
                if (y == 0 || y == verticalBlockCount - 1) {
                    result[y][x] = WALL;
                } else if (x == 0 || x == horizontalBlockCount - 1) {
                    result[y][x] = WALL;
                } else if (x > 1 && x % 2 == 0 && y > 1 && y % 2 == 0) {
                    result[y][x] = INNER_WALL;
                } else {
                    result[y][x] = FLOOR;
                }
            }
        }

        // 迷路を生成
        return generateLabyrinth(horizontalBlockCount, verticalBlockCount, result, seed);
    }

    private static int[][] generateLabyrinth(int horizontalBlockCount, int verticalBlockCount, int[][] map, int seed) {
        Random rand = new Random(seed);

        for (int y = 0; y < verticalBlockCount; y++) {
            for (int x = 0; x < horizontalBlockCount; x++) {
                if (map[y][x] == INNER_WALL) {
                    List<Direction> directionList = new ArrayList<>(Arrays.asList(
                            Direction.LEFT,
                            Direction.RIGHT,
                            Direction.BOTTOM));

                    if (y == 1) {
                        directionList = new ArrayList<>(Arrays.asList(
                                Direction.TOP,
                                Direction.LEFT,
                                Direction.RIGHT,
                                Direction.BOTTOM));
                    }

                    do {
                        Direction direction = directionList.get(rand.nextInt(directionList.size()));
                        if (setDirection(y, x, direction, map)) {
                            break;
                        } else {
                            directionList.remove(direction);
                        }
                    } while (directionList.size() > 0);
                }
            }
        }
        return map;
    }

    private static boolean setDirection(int y, int x, Direction direction, int[][] map) {
        map[y][x] = WALL;

        switch (direction) {
            case LEFT:
                x -= 1;
                break;
            case RIGHT:
                x += 1;
                break;
            case BOTTOM:
                y += 1;
                break;
            case TOP:
                y -= 1;
                break;
        }

        if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) {
            return false;
        }

        if (map[y][x] == WALL) {
            return false;
        }

        map[y][x] = WALL;

        return true;
    }
}
