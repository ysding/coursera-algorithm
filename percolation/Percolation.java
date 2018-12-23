/* *****************************************************************************
 *  Name: Yusheng Ding
 *  Date: 12/18/2018
 *  Description: Percolation. Union Find.
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private byte[] openState;
    // private boolean[] full;
    private boolean percolated;
    private int openNum = 0;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf2;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.percolated = false;
        this.n = n;
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.uf2 = new WeightedQuickUnionUF(n * n + 2);
        this.openState = new byte[n * n + 2];
        // this.full = new boolean[n * n + 2];
        this.openState[0] |= 1;
        this.openState[1] |= 1;
    }

    private int getId(int row, int col) {
        return (row - 1) * this.n + (col - 1) + 2;
    }

    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
        int id = getId(row, col);
        if ((this.openState[id] & 1) == 1) {
            return;
        }
        this.openNum += 1;
        this.openState[id] |= 1;
        this.union(row, col, row, col + 1);
        this.union(row, col, row, col - 1);
        this.union(row, col, row + 1, col);
        this.union(row, col, row - 1, col);
        if (row == 1) {
            this.uf.union(id, 0);
            this.uf2.union(id, 0);
        }
        if (row == n) this.uf.union(id, 1);
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
        int id = this.getId(row, col);
        return (this.openState[id] & 1) == 1;
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
        int id = this.getId(row, col);
        if (!((this.openState[id] & 2) == 2))
            if (this.uf2.find(0) == this.uf2.find(id))
                this.openState[id] |= 2;
        return (this.openState[id] & 2) == 2;
    }

    public int numberOfOpenSites() {
        return this.openNum;
    }

    public boolean percolates() {
        if (!this.percolated)
            this.percolated = (this.uf.find(0) == this.uf.find(1));
        return this.percolated;
    }

    private void union(int x0, int y0, int x1, int y1) {
        if (x1 < 1 || y1 < 1 || x1 > this.n || y1 > this.n) {
            return;
        }
        int id0 = this.getId(x0, y0);
        int id1 = this.getId(x1, y1);
        if ((this.openState[id1] & 1) == 1 && (this.openState[id0] & 1) == 1) {
            this.uf.union(id1, id0);
            this.uf2.union(id0, id1);
        }
    }

    public static void main(String[] args) {

    }
}
