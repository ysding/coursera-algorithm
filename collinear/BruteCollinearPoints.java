/* *****************************************************************************
 *  Name: Yusheng Ding
 *  Date: 12/23/2018
 *  Description: check if all 4 points in one line;
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    /*
     *  examines 4 points at a time
     *  and checks whether they all lie on the same line segment
     * */
    private List<LineSegment> list = new ArrayList<>();


    public BruteCollinearPoints(Point[] input) {
        Point[] points;
        if (input == null) throw new IllegalArgumentException();
        for (int i = 0; i < input.length; i++) {
            if (input[i] == null)
                throw new IllegalArgumentException();
        }
        points = Arrays.copyOf(input, input.length);
        Arrays.sort(points);
        // throw Exception when repeated.
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            for (int j = i + 1; j < points.length; j++) {
                Point q = points[j];
                for (int k = j + 1; k < points.length; k++) {
                    Point r = points[k];
                    if (p.slopeOrder().compare(q, r) != 0)
                        continue;
                    for (int m = k + 1; m < points.length; m++) {
                        Point s = points[m];
                        if (p.slopeOrder().compare(q, s) == 0) {
                            // Only record the Line with 4 points in it.
                            list.add(new LineSegment(p, s));
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return list.size();
    }

    public LineSegment[] segments() {
        LineSegment[] res = new LineSegment[list.size()];
        list.toArray(res);
        return res;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
