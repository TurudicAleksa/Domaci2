package org.example.dungeonrunner;

import java.util.Random;

public class RandomPosition {

    private static final Random rng = new Random();


    public static double[] findRandomOpenPosition(DungeonMap map) {
        int cols = map.getCols();
        int rows = map.getRows();

        for (int attempt = 0; attempt < 1000; attempt++) {
            int col = rng.nextInt(cols);
            int row = rng.nextInt(rows);

            if (isWalkable(map, col, row)) {
                double worldX = col * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0;
                double worldZ = row * Constants.CELL_SIZE + Constants.CELL_SIZE / 2.0;
                return new double[]{worldX, worldZ};
            }
        }

        return null;
    }
    public static double[] findRandomLeverPos(DungeonMap map, double[] dx, double[] dy){
        int cols = map.getCols();
        int rows = map.getRows();
        boolean valid = true;

        for (int attempt = 0; attempt < 1000; attempt++) {
            int col = rng.nextInt(cols);
            int row = rng.nextInt(rows);
            valid = true;

            if (isWalkable(map, col, row)) {
                for(int i=0;i<dx.length;i++){
                    if((int)dx[i]==col && (int)dy[i]==row) valid=false;
                }
                if(valid){
                    return new double[]{col, row};
                }

            }
        }

        return null;
    }

    private static boolean isWalkable(DungeonMap map, int col, int row) {
        int tile = map.get(col, row);
        return tile == Constants.EMPTY;
    }
}