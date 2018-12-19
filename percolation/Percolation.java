/* *****************************************************************************
 *  Name: Yusheng Ding
 *  Date: 12/18/2018
 *  Description: Percolation. Union Find.
 **************************************************************************** */

public class Percolation {
    private int n;
    private int[] intArray;
    private int[] openState;
    private int openNum = 0;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        this.intArray = new int[n * n + 2];
        this.openState = new int[n * n + 2];
        for (int row = 0; row < n; row += 1) {
            for (int col = 0; col < n; col += 1) {
                int id = this.getId(row, col);
                this.intArray[id] = id;
                this.openState[id] = 0;
            }
        }
        this.intArray[0] = 0;
        this.intArray[1] = 1;
        for (int col = 0; col < n; col += 1) {
            this.intArray[this.getId(0, col)] = 0;
            this.intArray[this.getId(this.n - 1, col)] = 1;
        }
    }

    public int getId(int row, int col) {
        return row * this.n + col + 2;
    }

    public void open(int row, int col) {
        int id = getId(row, col);
        if (this.openState[id] == 1) {
            return;
        }
        this.openNum += 1;
        this.openState[id] = 1;
        this.union(row, col, row, col + 1);
        this.union(row, col, row, col - 1);
        this.union(row, col, row + 1, col);
        this.union(row, col, row - 1, col);
    }

    public boolean isOpen(int row, int col) {
        return this.openState[this.getId(row, col)] == 1;
    }

    public boolean isFull(int row, int col) {
        return this.connected(0, this.getId(row, col));
    }

    public int numberOfOpenSites() {
        return this.openNum;
    }

    public boolean percolates() {
        return this.connected(0, 1);
    }

    public void union(int x0, int y0, int x1, int y1) {
        if (x1 < 0 || y1 < 0 || x1 >= this.n || y1 >= this.n) {
            return;
        }
        int id0 = this.getId(x0, y0);
        int id1 = this.getId(x1, y1);
        if (this.openState[id1] == 1 && this.openState[id0] == 1) {
            this.intArray[this.root(id0)] = this.intArray[this.root(id1)];
        }
    }

    public int root(int id) {
        if (this.intArray[id] != id) {
            this.intArray[id] = this.root(this.intArray[id]);
        }
        return this.intArray[id];
    }

    public boolean connected(int id0, int id1) {
        return this.root(id0) == this.root(id1);
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(2);
        System.out.println(p.percolates());
        // System.out.println(p.isOpen(0, 0));
        // System.out.println(p.isOpen(0, 1));
        // System.out.println(p.isOpen(1, 0));
        // System.out.println(p.isOpen(1, 1));
        p.open(0, 0);
        System.out.println(p.numberOfOpenSites());
        System.out.println(p.percolates());
        // System.out.println(p.isOpen(0, 0));
        // System.out.println(p.isFull(0, 0));
        p.open(1, 1);
        System.out.println(p.percolates());
        p.open(0, 1);
        System.out.println(p.percolates());
    }
}
