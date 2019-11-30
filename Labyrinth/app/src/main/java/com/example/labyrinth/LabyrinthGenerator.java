package com.example.labyrinth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LabyrinthGenerator {

    public static final int FLOOR = 0;
    public static final int WALL = 1;
    public static final int START = 2;
    public static final int GOAL = 3;
    public static final int HOLE = 4;
    public static final int INNER_WALL = -1;

    public static class MapResult {
        final int[][] map;
        final int startX;
        final int startY;

        MapResult(int[][] map, int startX, int startY) {
            this.map = map;
            this.startX = startX;
            this.startY = startY;
        }
    }

    public enum Direction {
        TOP,
        LEFT,
        RIGHT,
        BOTTOM,
    }

    public static MapResult getMap(int horizontalBlockCount, int verticalBlockCount, int seed) {

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

        result = generateLabyrinth(horizontalBlockCount, verticalBlockCount, result, seed);

        int startY = -1;
        int startX = -1;

        for (int y = verticalBlockCount - 1; y >= 0; y--) {
            for (int x = horizontalBlockCount - 1; x >= 0; x--) {
                if (result[y][x] == FLOOR) {
                    startX = x;
                    startY = y;
                    result[startY][startX] = START;
                    break;
                }
            }
            if (startX != -1 && startY != -1) {
                break;
            }
        }

        int[][] exam = new int[verticalBlockCount][horizontalBlockCount];

        calcStep(result, startX, startY, exam, 0);

        int maxScore = 0;
        int maxScoreXPosition = 0;
        int maxScoreYPosition = 0;

        for (int y = 0; y < verticalBlockCount; y++) {
            for (int x = 0; x < horizontalBlockCount; x++) {
                if (exam[y][x] > maxScore) {
                    maxScore = exam[y][x];
                    maxScoreXPosition = x;
                    maxScoreYPosition = y;
                }
            }
        }
        result[maxScoreYPosition][maxScoreXPosition] = GOAL;

        return new MapResult(result, startX, startY);
    }

    private static int[][] calcStep(int[][] map, int x, int y, int[][] result, int score) {
        score++;

        if (y < 0 || x < 0 || y >= map.length || x >= map[0].length) {
            return result;
        }

        if (map[y][x] == WALL) {
            result[y][x] = -1;
            return result;
        }

        if (map[y][x] == HOLE) {
            result[y][x] = -1;
            return result;
        }

        if (result[y][x] == 0 || result[y][x] > score) {
            result[y][x] = score;

            calcStep(map, x, y - 1, result, score);
            calcStep(map, x, y + 1, result, score);
            calcStep(map, x - 1, y, result, score);
            calcStep(map, x + 1, y, result, score);
        }

        return result;
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

        int holeCount = seed + 1;
        if (holeCount > (verticalBlockCount + horizontalBlockCount)) {
            holeCount = verticalBlockCount + horizontalBlockCount;
        }
        setHoles(holeCount, rand, verticalBlockCount, horizontalBlockCount, map);

        return map;
    }

    private static void setHoles(int holeCount, Random rand,
                                 int verticalBlockCount, int horizontalBlockCount, int[][] map) {
        do {
            int y = rand.nextInt(verticalBlockCount - 2) + 1;
            int x = rand.nextInt(horizontalBlockCount - 2) + 1;

            if (map[y][x] == WALL) {
                map[y][x] = HOLE;
                holeCount--;
            }
        } while (holeCount > 0);
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
