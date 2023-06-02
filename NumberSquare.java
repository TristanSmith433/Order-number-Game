public class NumberSquare {

    public static final int START_SCORE = 50; // do 50 swaps, start at 50 score (5 recommended for testing)

    public int size;
    public int[] grid;
    public int score;
    public double startTime = -1;
    public double endTime = -1;

    public NumberSquare(int size) {
        this.size = size;
        this.grid = new int[size * size];

        for (int i = 0, y = 0; y < size; y++) {
            for (int x = 0; x < size; x++, i++) {
                grid[i] = i - 1;
            }
        } // fill grid
    }

    public void randomize() {
        score = START_SCORE;
        startTime = (double) System.currentTimeMillis() / 1000F;
        endTime = -1; // set values

        int blankIndex = -1;
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] < 0) {
                blankIndex = i;
                break;
            }
        } // cache position of blank tile to reduce processing time

        for (int pass = 0; pass < score; pass++) {
            int[] blankCoords = getCoords(blankIndex); // get coords of blank tile
            int[] swapCoords = getCoords(blankIndex); // start at blank tile position
            int axis = (int) Math.round(Math.random()); // get random axis (0 or 1)
            if (blankCoords[axis] > 0 && blankCoords[axis] < size - 1) { // can we move in both directions on this axis
                swapCoords[axis] += Math.random() < 0.5 ? 1 : -1; // move randomly
            } else if (blankCoords[axis] > 0) { // can we move in the negative on this axis
                swapCoords[axis] += -1; // move in negative
            } else {
                swapCoords[axis] += 1; // move in positive
            }
            int swapIndex = getIndex(swapCoords[0], swapCoords[1]); // get index of swap coordinates
            action(blankIndex, swapIndex, 0); // perform swap at no cost
            blankIndex = swapIndex; // update cached position of blank tile
        }
    }

    private boolean action(int index1, int index2, int cost) {
        if (!(index1 >= 0 && index2 >= 0 && index1 < grid.length && index2 < grid.length)) return false; // check that index is on grid
        int[]
                coords1 = getCoords(index1),
                coords2 = getCoords(index2); // get coordinates of indexes
        if (coords1[0] != coords2[0]) { // do coordinates match on x
            if (coords1[1] != coords2[1]) { // do coordinates match on y
                return false; // coordinates must mach on one axis
            } else if (Math.abs(coords1[0] - coords2[0]) != 1) { // do x coordinates differ by 1
                return false; // x coords must differ by 1 exactly
            }
        } else if (Math.abs(coords1[1] - coords2[1]) != 1) { // do y coordinates differ by 1
            return false; // y coords must differ by 1 exactly
        }

        int temp = grid[index1];
        grid[index1] = grid[index2];
        grid[index2] = temp; // swap tiles

        score -= cost; // remove cost form score
        return true; // indicate successful swap
    }

    public boolean action(int index1, int index2) { // all external swaps cost 1 point
        return action(index1, index2, 1);
    }

    public double isWin() {
        for (int i = 1; i < grid.length; i++) {
            if (grid[i - 1] > grid[i]) {
                return -1; // return -1 if grid is not "sorted"
            }
        }
        if (endTime == -1) endTime = ((double) System.currentTimeMillis() / 1000F); // if this is the first time win is checked after winning, record time
        return endTime - startTime; // return time between start and finish
    }

    public int[] getCoords(int i) { // helper function to convert grid index to coords
        return new int[]{ i % size, i / size };
    }

    public int getIndex(int x, int y) { // inverse getCoords
        return x + (y * size);
    }

}
