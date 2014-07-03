/**
 * Created by mogrein on 02.07.14.
 */

public class Percolation {
    private int gridSize;
    private boolean grid[];
    private WeightedQuickUnionUF gridGraph;

    public Percolation(int N) {             // create gridSize-by-gridSize grid, with all sites blocked
        if (N <= 0) {
            throw new IndexOutOfBoundsException();
        }
        int sqrN = N * N;
        gridSize = N;
        grid = new boolean[sqrN];
        gridGraph = new WeightedQuickUnionUF(sqrN + 2);
    }

    public void open(int i, int j) throws IndexOutOfBoundsException {        // open site (row i, column j) if it is not already
        if (i < 1 || i > gridSize || j < 1 || j > gridSize) {
            throw new IndexOutOfBoundsException("No element in the grid");
        }
        int elemNum = i * gridSize + j;
        if (!grid[elemNum - gridSize - 1]) {
            grid[elemNum - gridSize - 1] = true;
            if (j != 1) gridGraph.union(elemNum, elemNum + 1);
            if (j != gridSize) gridGraph.union(elemNum, elemNum - 1);
            if (i != 1) {
                gridGraph.union(elemNum, elemNum - gridSize);
            } else {
                gridGraph.union(0, elemNum);
            }
            if (i != gridSize) {
                gridGraph.union(elemNum, elemNum + gridSize);
            } else {
                gridGraph.union(elemNum, gridGraph.count() - 1);
            }
        }
    }

    public boolean isOpen(int i, int j) {   // is site (row i, column j) open?
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

    public boolean percolates() {           // does the system percolate?
        return gridGraph.connected(0, gridGraph.count() - 1);
    }
}