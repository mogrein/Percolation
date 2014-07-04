/**
 * Created by mogrein on 02.07.14.
 */

public class Percolation {
    private int gridSize;
    private boolean[] grid;
    private WeightedQuickUnionUF gridGraph;

    // create gridSize-by-gridSize grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IndexOutOfBoundsException("Argument must be positive");
        }
        gridSize = N;
        grid = new boolean[N * N];
        gridGraph = new WeightedQuickUnionUF(grid.length + 2);
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
            if (j != 1) {
                if (grid[elemNum - 2]) gridGraph.union(elemNum, elemNum - 1);
            }
            if (j != gridSize) {
                if (grid[elemNum]) gridGraph.union(elemNum, elemNum + 1);
            }
            if (i != 1) {
                if (grid[elemNum - gridSize - 1])
                    gridGraph.union(elemNum, elemNum - gridSize);
            } else {
                gridGraph.union(0, elemNum);
            }
            if (i != gridSize) {
                if (grid[elemNum + gridSize - 1])
                    gridGraph.union(elemNum, elemNum + gridSize);
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
        return gridGraph.connected(0, i * gridSize + j);
    }

    // does the system percolate?
    public boolean percolates() {
        return gridGraph.connected(0, grid.length + 1);
    }
}