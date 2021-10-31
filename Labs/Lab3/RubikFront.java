public class RubikFront extends Rubik {
    private static final int ARRAY_SIZE = 3;
    private static final int ARRAY_SIZE_6 = 6;
    RubikFront(int[][][] grid) {
        super(grid);
    }

        
    @Override
    public Rubik clone() {
        int[][][] newGrid = new int[ARRAY_SIZE_6][ARRAY_SIZE][ARRAY_SIZE];
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                newGrid[i][j] = this.grid[i][j];
            }
        }
        return new RubikFront(newGrid);
    }
    
}
