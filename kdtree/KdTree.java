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

        public Node(Point2D p) {
            this.p = p;
            leftTree = null;
            rightTree = null;
            rect = new RectHV(p.x(), p.y(), p.x(), p.y());
        }

        public void updateRect(RectHV that) {
            this.rect = new RectHV(Math.min(rect.xmin(), that.xmin()),
                                   Math.min(rect.ymin(), that.ymin()),
                                   Math.max(rect.xmax(), that.xmax()),
                                   Math.max(rect.ymax(), that.ymax()));
        }

        public boolean less(Node that, boolean horizontal) {
            if (horizontal) {
                return this.p.y() - that.p.y() < 0;
            }
            else {
                return this.p.x() - that.p.x() < 0;
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
        root = insert(root, new Node(p), false);
    }

    private Node insert(Node tmpRoot, Node node, boolean horizontal) {
        if (tmpRoot == null) {
            size += 1;
            return node;
        }
        if (tmpRoot.p.equals(node.p)) return tmpRoot;
        if (node.less(tmpRoot, horizontal)) {
            tmpRoot.leftTree = insert(tmpRoot.leftTree, node, !horizontal);
            tmpRoot.updateRect(tmpRoot.leftTree.rect);
        }
        else {
            tmpRoot.rightTree = insert(tmpRoot.rightTree, node, !horizontal);
            tmpRoot.updateRect(tmpRoot.rightTree.rect);
        }
        return tmpRoot;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, new Node(p), false);
    }

    private boolean contains(Node tmpRoot, Node node, boolean horizontal) {
        if (tmpRoot == null) return false;
        if (tmpRoot.p.equals(node.p)) return true;
        if (node.less(tmpRoot, horizontal)) return contains(tmpRoot.leftTree, node, !horizontal);
        else return contains(tmpRoot.rightTree, node, !horizontal);
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
        return nearest(root, new Node(p), Double.POSITIVE_INFINITY, false);
    }

    private Point2D nearest(Node tmpRoot, Node node, double minDis, boolean horizontal) {
        if (tmpRoot == null || tmpRoot.rect.distanceTo(node.p) >= minDis) return null;
        Point2D res = null;
        double tmpDis = tmpRoot.p.distanceTo(node.p);
        if (tmpDis < minDis) {
            res = tmpRoot.p;
            minDis = tmpDis;
        }
        if (node.less(tmpRoot, horizontal)) {
            Point2D candidate = nearest(tmpRoot.leftTree, node, minDis, !horizontal);
            if (candidate != null) {
                res = candidate;
                minDis = res.distanceTo(node.p);
            }
            if ((horizontal && Math.abs(node.p.y() - tmpRoot.p.y()) > minDis) ||
                    (!horizontal && Math.abs(node.p.x() - tmpRoot.p.x()) > minDis))
                return res;
            else {
                candidate = nearest(tmpRoot.rightTree, node, minDis, !horizontal);
                if (candidate != null) {
                    res = candidate;
                }
                return res;
            }
        }
        else {
            Point2D candidate = nearest(tmpRoot.rightTree, node, minDis, !horizontal);
            if (candidate != null) {
                res = candidate;
                minDis = res.distanceTo(node.p);
            }
            if ((horizontal && Math.abs(node.p.y() - tmpRoot.p.y()) > minDis) ||
                    (!horizontal && Math.abs(node.p.x() - tmpRoot.p.x()) > minDis))
                return res;
            else {
                candidate = nearest(tmpRoot.leftTree, node, minDis, !horizontal);
                if (candidate != null) {
                    res = candidate;
                }
                return res;
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
