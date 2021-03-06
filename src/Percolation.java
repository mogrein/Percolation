/**
 * Created by mogrein on 02.07.14.
 */

public class Percolation {
    private int gridSize;
    private boolean[] grid;
    private WeightedQuickUnionUF gridGraph;
    private WeightedQuickUnionUF gridGraphFullness;

    // create gridSize-by-gridSize grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Argument must be positive");
        }
        gridSize = N;
        grid = new boolean[N * N];
        gridGraph = new WeightedQuickUnionUF(grid.length + 2);
        gridGraphFullness = new WeightedQuickUnionUF(grid.length+1);
    }

    // open site (row i, column j) if it is not already
    public void open(int i, int j) {
        if (i < 1 || i > gridSize || j < 1 || j > gridSize) {
            throw new IndexOutOfBoundsException("No element in the grid");
        }
        int elemNum = (i - 1) * gridSize + j; //element number in the gridGraph

        //if not already opened, then open and connect with opened neighbours.
        if (!grid[elemNum - 1]) {
            grid[elemNum - 1] = true;
            if (j != 1 && grid[elemNum - 2]) {
                gridGraph.union(elemNum, elemNum - 1);
                gridGraphFullness.union(elemNum, elemNum - 1);
            }
            if (j != gridSize && grid[elemNum]) {
                gridGraph.union(elemNum, elemNum + 1);
                gridGraphFullness.union(elemNum, elemNum + 1);
            }
            if (i != 1) {
                if (grid[elemNum - gridSize - 1]) {
                    gridGraph.union(elemNum, elemNum - gridSize);
                    gridGraphFullness.union(elemNum, elemNum - gridSize);
                }
            } else {
                gridGraph.union(0, elemNum);
                gridGraphFullness.union(0, elemNum);
            }
            if (i != gridSize) {
                if (grid[elemNum + gridSize - 1]) {
                    gridGraph.union(elemNum, elemNum + gridSize);
                    gridGraphFullness.union(elemNum, elemNum + gridSize);
                }
            } else {
                gridGraph.union(elemNum, grid.length + 1);
            }
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        if (i < 1 || i > gridSize || j < 1 || j > gridSize) {
            throw new IndexOutOfBoundsException("No element in the grid");
        }
        return grid[(i - 1) * gridSize + j - 1];
    }

    public boolean isFull(int i, int j) {   // is site (row i, column j) full?
        if (i < 1 || i > gridSize || j < 1 || j > gridSize) {
            throw new IndexOutOfBoundsException("No element in the grid");
        }
        return grid[(i - 1) * gridSize + j - 1]
                && gridGraphFullness.connected(0, (i - 1) * gridSize + j);
    }

    // does the system percolate?
    public boolean percolates() {
        return gridGraph.connected(0, grid.length + 1);
    }

//    private void printGrid() {
//        for (int i = 0; i < gridSize; ++i) {
//            int tmp = i * gridSize;
//            char c;
//            for (int j = 0; j < gridSize; ++j) {
//                c = grid[tmp + j] ? '0' : ' ';
//                System.out.print(c);
//            }
//            System.out.print(" | ");
//            for (int j = 0; j < gridSize; ++j) {
//                c = isFull(i + 1, j + 1) ? '0' : ' ';
//                System.out.print(c);
//            }
//            System.out.println();
//        }
//    }
}