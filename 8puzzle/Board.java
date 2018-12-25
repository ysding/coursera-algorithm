/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    private final int[][] blocks;
    private final int length;
    private int manhattanDis = -1;
    private int hamDis = -1;

    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    {
        length = blocks.length;
        this.blocks = new int[length][length];
        for (int i = 0; i < length; i++)
            this.blocks[i] = Arrays.copyOf(blocks[i], length);
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension n
    {
        return length;
    }

    public int hamming()                   // number of blocks out of place
    {
        if (hamDis != -1) return hamDis;
        hamDis = 0;
        for (int i = 0; i < length; i++)
            for (int j = 0; j < length; j++) {
                if (blocks[i][j] != 0 &&
                        blocks[i][j] != i * length + j + 1)
                    hamDis += 1;
            }
        return hamDis;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        if (manhattanDis != -1) return manhattanDis;
        manhattanDis = 0;
        for (int i = 0; i < length; i++)
            for (int j = 0; j < length; j++) {
                if (blocks[i][j] != 0 &&
                        blocks[i][j] != i * length + j + 1) {
                    manhattanDis += Math.abs(i - (blocks[i][j] - 1) / length);
                    manhattanDis += Math.abs(j - (blocks[i][j] - 1) % length);
                }
            }
        return manhattanDis;
    }

    public boolean isGoal()                // is this board the goal board?
    {
        return hamming() == 0;
    }

    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        if (blocks[0][0] != 0 && blocks[0][1] != 0) {
            return change(0, 0, 0, 1);
        }
        else {
            return change(1, 0, 1, 1);
        }
    }

    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == this) return true;
        if (y == null || this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.length != that.length) return false;
        int[][] arrThat = that.blocks;
        for (int i = 0; i < this.length; i++)
            for (int j = 0; j < this.length; j++) {
                if (this.blocks[i][j] != arrThat[i][j]) {
                    return false;
                }
            }
        return true;
    }

    private Board change(int x0, int y0, int x1, int y1) {
        int[][] newblocks = new int[length][length];
        for (int i = 0; i < length; i++)
            newblocks[i] = Arrays.copyOf(blocks[i], length);
        int i = newblocks[x0][y0];
        newblocks[x0][y0] = newblocks[x1][y1];
        newblocks[x1][y1] = i;
        return new Board(newblocks);
    }

    private boolean validate(int x, int y) {
        if (x < 0 || y < 0 || x >= length || y >= length)
            return false;
        return true;
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Stack<Board> boardit = new Stack<Board>();
        int i, j;
        for (i = 0; i < length; i++) {
            for (j = 0; j < length; j++) {
                if (this.blocks[i][j] == 0) {
                    if (validate(i + 1, j)) boardit.push(this.change(i, j, i + 1, j));
                    if (validate(i - 1, j)) boardit.push(this.change(i, j, i - 1, j));
                    if (validate(i, j + 1)) boardit.push(this.change(i, j, i, j + 1));
                    if (validate(i, j - 1)) boardit.push(this.change(i, j, i, j - 1));
                    return boardit;
                }
            }
        }
        return boardit;
    }

    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(length + "\n");
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                sb.append(String.format("%2d ", blocks[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    {
        int[][] a = new int[3][3];

        int[][] b = new int[3][3];
        a[0][0] = 0;
        a[0][1] = 1;
        a[0][2] = 3;
        a[1][0] = 4;
        a[1][1] = 2;
        a[1][2] = 5;
        a[2][0] = 7;
        a[2][1] = 6;
        a[2][2] = 8;
        b[0][0] = 0;
        b[0][1] = 1;
        b[0][2] = 3;
        b[1][0] = 4;
        b[1][1] = 2;
        b[1][2] = 5;
        b[2][0] = 7;
        b[2][1] = 8;
        b[2][2] = 6;
        boolean t = false;
        for (int i = 0; i < 20; i++) {
            t = new Board(a).equals(new Board(b));
            if (t) {
                System.out.println(t);
            }
        }
    }
}
