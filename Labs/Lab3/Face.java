class Face implements Cloneable {
    private final int[][] grid;
    private static final int ARRAY_SIZE = 3;
    
    Face(int[][] grid) {
        this.grid = grid;
    }

    Face right() {
        int [][] newGrid = new int[ARRAY_SIZE][ARRAY_SIZE];
        newGrid[0][0] = this.grid[2][0];
        newGrid[0][1] = this.grid[1][0];
        newGrid[0][2] = this.grid[0][0];
        newGrid[1][0] = this.grid[2][1];
        newGrid[1][1] = this.grid[1][1];
        newGrid[1][2] = this.grid[0][1];
        newGrid[2][0] = this.grid[2][2];
        newGrid[2][1] = this.grid[1][2];
        newGrid[2][2] = this.grid[0][2];
        return new Face(newGrid);
    }

    Face left() {
        return this.right().right().right();
    }

    Face half() {
        return this.right().right();
    }

    int[][] toIntArray() {
        return this.grid;
    }


    @Override
    public Face clone() {
        int[][] newGrid = new int[ARRAY_SIZE][ARRAY_SIZE];
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                newGrid[i][j] = this.grid[i][j];
            }
        }
        return new Face(newGrid);
    }

        
    public String toString() {
        String printedCube = "\n";
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                printedCube += String.format("%02d", this.grid[i][j]);
            }
            printedCube += "\n";
        }
        return printedCube;
    }
}
