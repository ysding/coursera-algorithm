/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private int move = -1;
    private Stack<Board> solution = null;
    private boolean solvable = false;

    private class Node implements Comparable<Node> {
        private final int priority;
        private final Board board;
        private final int move;
        private final boolean twin;
        private final Node pre;

        public Node(Board board, Node pre, int move, boolean twin) {
            this.move = move;
            this.pre = pre;
            this.board = board;
            this.priority = this.board.manhattan() + move;
            this.twin = twin;
        }

        public int compareTo(Node that) {
            return this.priority - that.priority;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new java.lang.IllegalArgumentException();
        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> twinPq = new MinPQ<>();

        pq.insert(new Node(initial, null, 0, false));
        twinPq.insert(new Node(initial.twin(), null, 0, true));
        Node solveNode = null;
        while (!pq.isEmpty()) {
            solveNode = solveOneStep(pq);
            if (solveNode != null || solveOneStep(twinPq) != null) break;
        }
        if (solveNode != null) {
            this.solvable = true;
            this.move = solveNode.move;
            this.solution = new Stack<Board>();
            while (solveNode != null) {
                this.solution.push(solveNode.board);
                solveNode = solveNode.pre;
            }
        }

    }

    // if find the Goal Board, return it, else update pq && return null.
    private Node solveOneStep(MinPQ<Node> pq) {
        Node now = pq.delMin();
        if (now.board.isGoal()) {
            return now;
        }
        // add all neighbors except the one equal to pre.
        for (Board p : now.board.neighbors()) {
            if (now.pre == null || !now.pre.board.equals(p)) {
                pq.insert(new Node(p, now, now.move + 1, now.twin));
            }
        }
        return null;
    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return this.solvable;
    }

    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        return this.move;
    }

    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        return this.solution;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            int i = 0;
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
            StdOut.println(i);
        }

    }
}
