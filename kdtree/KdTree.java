/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

public class KdTree {

    private class Node {
        private Point2D p;
        private Node leftTree;
        private Node rightTree;
        private RectHV rect;

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            leftTree = null;
            rightTree = null;
            this.rect = rect;
        }

        public void updateRect(RectHV that) {
            this.rect = new RectHV(Math.min(rect.xmin(), that.xmin()),
                                   Math.min(rect.ymin(), that.ymin()),
                                   Math.max(rect.xmax(), that.xmax()),
                                   Math.max(rect.ymax(), that.ymax()));
        }

        public boolean lessOrEqual(Point2D that, boolean horizontal) {
            if (horizontal) {
                return this.p.y() - that.y() <= 0;
            }
            else {
                return this.p.x() - that.x() <= 0;
            }
        }
    }

    private Node root;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, 0, 0, 1, 1, false);
    }

    private Node insert(Node tmpRoot, Point2D p, double xmin, double ymin, double xmax, double ymax,
                        boolean horizontal) {
        if (tmpRoot == null) {
            size += 1;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }
        if (tmpRoot.p.equals(p)) return tmpRoot;
        if (tmpRoot.lessOrEqual(p, horizontal)) {
            if (horizontal) {
                tmpRoot.leftTree = insert(tmpRoot.leftTree, p, xmin, tmpRoot.p.y(), xmax, ymax,
                                          false);
            }
            else {
                tmpRoot.leftTree = insert(tmpRoot.leftTree, p, tmpRoot.p.x(), ymin, xmax, ymax,
                                          true);
            }
        }
        else {
            if (horizontal) {
                tmpRoot.rightTree = insert(tmpRoot.rightTree, p, xmin, ymin, xmax, tmpRoot.p.y(),
                                           false);
            }
            else {
                tmpRoot.rightTree = insert(tmpRoot.rightTree, p, xmin, ymin, tmpRoot.p.x(), ymax,
                                           true);
            }
        }
        return tmpRoot;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p, false);
    }

    private boolean contains(Node tmpRoot, Point2D p, boolean horizontal) {
        if (tmpRoot == null) return false;
        if (tmpRoot.p.equals(p)) return true;
        if (tmpRoot.lessOrEqual(p, horizontal)) return contains(tmpRoot.leftTree, p, !horizontal);
        else return contains(tmpRoot.rightTree, p, !horizontal);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node tmpRoot) {
        if (tmpRoot == null) return;
        tmpRoot.p.draw();
        draw(tmpRoot.leftTree);
        draw(tmpRoot.rightTree);
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> res = new Stack<>();
        range(rect, root, res);
        return res;
    }

    private void range(RectHV rect, Node tmpRoot, Stack<Point2D> res) {
        if (tmpRoot == null || !tmpRoot.rect.intersects(rect)) return;
        if (rect.contains(tmpRoot.p)) res.push(tmpRoot.p);
        range(rect, tmpRoot.rightTree, res);
        range(rect, tmpRoot.leftTree, res);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return nearest(root, p, null);
    }

    private Point2D nearest(Node tmpRoot, Point2D p, Point2D min) {
        if (tmpRoot == null) return min;
        if (min == null) {
            min = tmpRoot.p;
        }
        if (min.distanceSquaredTo(p) >= tmpRoot.rect.distanceSquaredTo(p)) {
            if (tmpRoot.p.distanceSquaredTo(p) < min.distanceSquaredTo(p))
                min = tmpRoot.p;
            if (tmpRoot.rightTree != null && tmpRoot.leftTree != null &&
                    tmpRoot.rightTree.rect.distanceSquaredTo(p) <
                            tmpRoot.leftTree.rect.distanceSquaredTo(p)) {
                min = nearest(tmpRoot.rightTree, p, min);
                min = nearest(tmpRoot.leftTree, p, min);
            }
            else {
                min = nearest(tmpRoot.leftTree, p, min);
                min = nearest(tmpRoot.rightTree, p, min);
            }
        }
        return min;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
