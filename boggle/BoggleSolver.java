/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private static final int R = 26;
    private final MyTrieST trie;
    private boolean[][] marked;

    private static class Node {
        private int val = 0;
        private Node[] next = new Node[R];
    }

    private class MyTrieST {

        private Node root;      // root of trie

        public MyTrieST() {
        }

        public Node getRoot() {
            return root;
        }


        public void put(String key, int val) {
            if (key == null) throw new IllegalArgumentException("first argument to put() is null");
            else root = put(root, key, val, 0);
        }

        private Node put(Node x, String key, int val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                x.val = val;
                return x;
            }
            int c = key.charAt(d) - 'A';
            x.next[c] = put(x.next[c], key, val, d + 1);
            return x;
        }

        public boolean contains(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            return get(key) != -1;
        }

        public int get(String key) {
            if (key == null) throw new IllegalArgumentException("argument to get() is null");
            Node x = get(root, key, 0);
            if (x == null) return -1;
            return x.val;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            int c = key.charAt(d) - 'A';
            return get(x.next[c], key, d + 1);
        }
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trie = new MyTrieST();
        for (String word : dictionary) {
            int value;
            if (word.length() < 3) continue;
            if (word.length() <= 4) value = 1;
            else if (word.length() == 5) value = 2;
            else if (word.length() == 6) value = 3;
            else if (word.length() == 7) value = 5;
            else value = 11;
            trie.put(word, value);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        marked = new boolean[board.rows()][board.cols()];
        Set<String> res = new HashSet<String>();
        Node root = trie.getRoot();
        if (root == null) return res;
        for (int i = 0; i < board.rows(); i++)
            for (int j = 0; j < board.cols(); j++) {
                marked[i][j] = true;
                if (board.getLetter(i, j) == 'Q') {
                    if (root.next['Q' - 'A'] != null)
                        dfs(root.next['Q' - 'A'].next['U' - 'A'], board,
                            "" + board.getLetter(i, j) + "U", i, j,
                            res);
                }
                else
                    dfs(root.next[board.getLetter(i, j) - 'A'], board, "" + board.getLetter(i, j),
                        i, j,
                        res);
                marked[i][j] = false;
            }
        return res;
    }

    private boolean validIndex(BoggleBoard board, int x, int y) {
        if (x < 0 || y < 0 || x >= board.rows() || y >= board.cols())
            return false;
        return true;
    }

    private void dfs(Node root, BoggleBoard board, String prefix, int x, int y, Set<String> res) {
        if (root == null) return;
        if (prefix.length() >= 3) {
            if (!res.contains(prefix) && trie.get(prefix) > 0) {
                res.add(prefix);
            }
        }

        for (int dx = -1; dx < 2; dx++)
            for (int dy = -1; dy < 2; dy++) {
                int nx = dx + x, ny = dy + y;
                if (validIndex(board, nx, ny) && !marked[nx][ny]) {
                    marked[nx][ny] = true;
                    if (board.getLetter(nx, ny) == 'Q') {
                        if (root.next['Q' - 'A'] != null)
                            dfs(root.next['Q' - 'A'].next['U' - 'A'], board,
                                prefix + board.getLetter(nx, ny) + 'U', nx, ny, res);
                    }
                    else {
                        dfs(root.next[board.getLetter(nx, ny) - 'A'], board,
                            prefix + board.getLetter(nx, ny), nx, ny, res);
                    }
                    marked[nx][ny] = false;
                }
            }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int res = trie.get(word);
        if (res == -1) return 0;
        return res;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
