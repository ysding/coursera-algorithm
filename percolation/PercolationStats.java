/* *****************************************************************************
 *  Name: Yusheng
 *  Date: 12/18/2018
 *  Description: PercolationsStats
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double stddev;
    private final double lo;
    private final double hi;

    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException();
        }
        double[] x = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                p.open(row, col);
            }
            x[i] = ((double) p.numberOfOpenSites()) / n / n;
        }
        this.mean = StdStats.mean(x);
        this.stddev = StdStats.stddev(x);
        this.lo = this.mean - CONFIDENCE_95 * this.stddev / Math.sqrt(trials);
        this.hi = this.mean + CONFIDENCE_95 * this.stddev / Math.sqrt(trials);
    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.stddev;
    }

    public double confidenceLo() {
        return this.lo;
    }

    public double confidenceHi() {
        return this.hi;
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(2, 10000);
        System.out.println(ps.mean());
        System.out.println(ps.stddev());
        System.out.println(ps.confidenceLo());
        System.out.println(ps.confidenceHi());
    }
}
