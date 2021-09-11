class Face implements Cloneable {
    private final int[][] grid;
    
    Face(int[][] grid) {
        this.grid = grid;
    }

    Face right() {
        this.grid[0][0] = this.grid[0][1];
        this.grid[0][1] = this.grid[1][1];
        this.grid[0][2] = this.grid[2][1];
        this.grid[1][0] = this.grid[1][0];
        this.grid[1][1] = this.grid[1][1];
        this.grid[1][2] = this.grid[1][2];
        this.grid[0][2] = this.grid[0][0];
        this.grid[1][2] = this.grid[1][0];
        this.grid[2][2] = this.grid[2][0];
        return new Face(new int[][] {this.grid[0], this.grid[1], this.grid[2]});
    }


    @Override
    public Face clone() {
        int[][] newGrid = new int[2][2];
        for(int i = 0; i < this.grid.length; i++) {
            for(int j = 0; j < this.grid[i].length; i++) {
                this.grid[i][j] = newGrid[i][j];
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

interface Cloneable {
    public Face clone();
}
