abstract class Rubik implements Cloneable {
    int[][][] grid = new int[ARRAY_SIZE_6][ARRAY_SIZE][ARRAY_SIZE];
    private static final int ARRAY_SIZE = 3;
    private static final int ARRAY_SIZE_6 = 6;
    
    Rubik(int[][][] newGrid) {
        this.grid = newGrid;
    }

    @Override
    public abstract Rubik clone();
    public abstract Rubik left();

    public String toString() {
        String output = "";
        String dots = "......";
        for(int i = 0; i < 6; i++) {
            if((i == 0 || i == 4 || i == 5)) {
                for(int j = 0; j < 3; j++) {
                    output += dots;    
                    for(int k = 0; k < 3; k++) {
                        output += String.format("%02d", grid[i][j][k]);
                    }
                    output = output + dots + "\n";
                }
            }
            else {
                if(i == 2 || i == 3) {
                    continue;
                } else {
                    for(int j = 0; j < 3; j++) {
                        for(int k = 0; k < 3; k++) {
                            output += String.format("%02d", grid[i][j][k]);
                        }

                        for(int k = 0; k < 3; k++) {
                            output += String.format("%02d", grid[i + 1][j][k]);
                        }

                        for(int k = 0; k < 3; k++) {
                            output += String.format("%02d", grid[i + 2][j][k]);
                        }

                        output += "\n";
                    }
                }
            }               

        }
        return output;
    }


}