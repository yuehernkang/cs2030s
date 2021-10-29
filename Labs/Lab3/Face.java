class Face implements Cloneable{
    private final int[][] grid;

    Face(int[][] grid) {
        this.grid = grid;
    }

    Face right() {
        int [][] newGrid = new int[3][3];
        newGrid[0][0] = this.grid[0][1];
        newGrid[0][1] = this.grid[1][1];
        newGrid[0][2] = this.grid[2][1];
        newGrid[1][0] = this.grid[1][0];
        newGrid[1][1] = this.grid[1][1];
        newGrid[1][2] = this.grid[1][2];
        newGrid[0][2] = this.grid[0][0];
        newGrid[1][2] = this.grid[1][0];
        newGrid[2][2] = this.grid[2][0];
        return new Face(newGrid);
    }


    @Override
    public Face clone() {
        int[][] newGrid = new int[3][3];
        for(int i = 0; i < this.grid.length; i++) {
            for(int j = 0; j < this.grid[i].length; j++) {
                newGrid[i][j] = this.grid[i][j];
            }
        }
        return Face(newGrid);
    }

        
    public String toString() {
        String printedCube = "\n";
        for(int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                printedCube += String.format("%02d", this.grid[i][j]);
            }
            printedCube += "\n";
        }
        return printedCube;
    }
}