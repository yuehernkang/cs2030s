public class RubikFront extends Rubik {
    private static final int ARRAY_SIZE = 3;
    private static final int ARRAY_SIZE_6 = 6;
    private static final int TOP = 0;
    private static final int LEFT = 1;
    private static final int FRONT = 2;
    private static final int RIGHT = 3;
    private static final int DOWN = 4;
    private static final int BACK = 5;

    private final Face[] cubeFaces;

    RubikFront(int[][][] grid) {
        this.cubeFaces = new Face[ARRAY_SIZE_6];
        for (int i = 0; i < ARRAY_SIZE_6; i++) {
            this.cubeFaces[i] = new Face(grid[i]);
        }
    }

        
    @Override
    public RubikFront clone() {
        int[][][] grid = new int[ARRAY_SIZE_6][][];
        for (int i = 0; i < ARRAY_SIZE_6; i++) {
            grid[i] = cubeFaces[i].toIntArray();
        }
        return new RubikFront(grid);
    }

    RubikFront right() {
        /*ROTATE FRONT
        MOVE LEFT TO TOP
        MOVE DOWN TO LEFT
        MOVE RIGHT TO DOWN
        MOVE TOP TO RIGHT
        */

        RubikFront result = this.clone();
        result.cubeFaces[FRONT] = result.cubeFaces[FRONT].right();

        int[][] leftGrid = result.cubeFaces[LEFT].left().toIntArray();
        int[][] topGrid = result.cubeFaces[TOP].half().toIntArray();
        int[][] rightGrid = result.cubeFaces[RIGHT].right().toIntArray();
        int[][] bottomGrid = result.cubeFaces[DOWN].toIntArray();

        int[] dummy;
        dummy = rightGrid[0].clone();
        rightGrid[0] = topGrid[0].clone();
        topGrid [0] = leftGrid[0].clone();
        leftGrid[0] = bottomGrid[0].clone();
        bottomGrid[0] = dummy;

        result.cubeFaces[LEFT] = new Face(leftGrid).right();
        result.cubeFaces[TOP] = new Face(topGrid).half();
        result.cubeFaces[RIGHT] = new Face(rightGrid).left();
        result.cubeFaces[DOWN] = new Face(bottomGrid);
        return result;
        }


    @Override
    RubikFront half() {
        return this.right().right();
    }

    @Override
    RubikFront left() {
        return this.half().right();
    }

    @Override
    RubikFront rightView() {
        RubikFront result = this.clone();
        Face d = this.cubeFaces[LEFT].half();

        result.cubeFaces[TOP] = this.cubeFaces[TOP].right();
        result.cubeFaces[LEFT] = this.cubeFaces[FRONT].clone();
        result.cubeFaces[FRONT] = this.cubeFaces[RIGHT].clone();
        result.cubeFaces[RIGHT] = this.cubeFaces[BACK].half();
        result.cubeFaces[DOWN] = this.cubeFaces[DOWN].left();
        result.cubeFaces[BACK] = d;

        return result;
    }

    @Override
    RubikFront leftView() {
        return backView().rightView();
    }

    @Override
    RubikFront downView() {
        /*ROTATE LEFT AND RIGHT FACE
        COPY TOP FACE
        top = front
        front = down
        down = back
        back = top
        */
        RubikFront result = this.clone();
        Face d = this.cubeFaces[TOP].clone();

        result.cubeFaces[TOP] = this.cubeFaces[FRONT].clone();
        result.cubeFaces[LEFT] = this.cubeFaces[LEFT].left();
        result.cubeFaces[FRONT] = this.cubeFaces[DOWN].clone();
        result.cubeFaces[RIGHT] = this.cubeFaces[RIGHT].right();
        result.cubeFaces[DOWN] = this.cubeFaces[BACK].clone();
        result.cubeFaces[BACK] = d;

        return result;
    }

    @Override
    RubikFront upView() {
        return this.downView().downView().downView();
    }

    @Override
    RubikFront backView() {
        return this.rightView().rightView();
    }

    @Override
    RubikFront frontView() {
        return this;
    }

    int[][][] toGrid() {
        int[][][] answer = new int[ARRAY_SIZE_6][][];
        for (int i = 0; i < ARRAY_SIZE_6; i++) {
            answer[i] = cubeFaces[i].toIntArray();
        }
        return answer;
    }

    private String fromIntArray(int[] grid) {
        String answer = "";
        for (int i = 0; i < grid.length; i++) {
            answer += String.format("%02d", grid[i]);
        }
        return answer;
    }

    @Override
    public String toString() {
        String dots = "......";
        String finalString = "\n";

        /* TOP
        ......010203......
        ......040506......
        ......283134......
        */

        for (int i = 0; i < ARRAY_SIZE; i++) {
            finalString += dots;
            finalString += fromIntArray(cubeFaces[TOP].toIntArray()[i]);
            finalString += dots;
            finalString += '\n';
        }

        /* LEFT FRONT RIGHT
        101109212427392930
        131408202326383233
        161707192225373536
        */
        for (int i = 0; i < ARRAY_SIZE; i++) {
            finalString += fromIntArray(cubeFaces[LEFT].toIntArray()[i]);
            finalString += fromIntArray(cubeFaces[FRONT].toIntArray()[i]);
            finalString += fromIntArray(cubeFaces[RIGHT].toIntArray()[i]);
            finalString += '\n';
        }

        /* DOWN
        ......121518......
        ......404142......
        ......434445......
        */
        for (int i = 0; i < ARRAY_SIZE; i++) {
            finalString += dots;
            finalString += fromIntArray(cubeFaces[DOWN].toIntArray()[i]);
            finalString += dots;
            finalString += '\n';
        }

        /*
        ......464748......
        ......495051......
        ......525354......
        */
        for (int i = 0; i < ARRAY_SIZE; i++) {
            finalString += dots;
            finalString += fromIntArray(cubeFaces[BACK].toIntArray()[i]);
            finalString += dots;
            finalString += '\n';
        }

        return finalString;
    }    
}
