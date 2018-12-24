/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> list = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] input) {
        Point[] points;
        if (input == null) throw new IllegalArgumentException();
        for (int i = 0; i < input.length; i++) {
            if (input[i] == null)
                throw new IllegalArgumentException();
        }

        points = Arrays.copyOf(input, input.length);
        // throw Exception when repeated.
        Arrays.sort(points);
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < points.length; i++) {
            Arrays.sort(points);
            Point p = input[i];
            Arrays.sort(points, 0, points.length, p.slopeOrder());
            int count = 1;
            for (int j = 0; j < points.length; j++) {
                if (j != points.length - 1 &&
                        Double.compare(points[j].slopeTo(p), points[j + 1].slopeTo(p)) == 0) {
                    count += 1;
                }
                else {
                    if (count >= 3 && p.compareTo(points[j - count + 1]) < 0) {
                        list.add(new LineSegment(p, points[j]));
                    }
                    count = 1;
                }
            }
        }
    }

    // the number of line segments
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
