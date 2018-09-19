package kd_tree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;
    private int count;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        if (isEmpty()) {
            return 0;
        }
        return count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("arg is null.");
        }

        root = insert(p, root, new RectHV(0, 0, 1, 1), true);
    }

    private Node insert(Point2D point, Node node, RectHV rectHV, boolean lineVertical) {
        if (node == null) {
            count++;
            return new Node(point, rectHV, lineVertical);
        }

        int comp = ((-1) * node.compareTo(point));
        if (comp < 0) {
            node.leftNode = insert(point, node.leftNode, node.rectHVLeft, !node.lineVertical);
        } else if (comp > 0) {
            node.rightNode = insert(point, node.rightNode, node.rectHVRight, !node.lineVertical);
        } else {
            node.point = point;
        }

        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return containsPoint(p, root);
    }

    private boolean containsPoint(Point2D point, Node node) {
        if (node == null) {
            return false;
        }

        int comp = ((-1) * node.compareTo(point));
        if (comp < 0) {
            return containsPoint(point, node.leftNode);
        } else if (comp > 0) {
            return containsPoint(point, node.rightNode);
        } else {
            return true;
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        node.point.draw();

        double xmin = node.rectHV.xmin();
        double xmax = node.rectHV.xmax();
        double ymin = node.rectHV.ymin();
        double ymax = node.rectHV.ymax();

        StdDraw.setPenRadius(0.01);
        if (node.lineVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            xmin = xmax = node.pointX;
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            ymin = ymax = node.pointY;
        }
        StdDraw.line(xmin, ymin, xmax, ymax);
        draw(node.leftNode);
        draw(node.rightNode);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> resultList = new ArrayList<Point2D>();
        range(root, rect, resultList);
        return resultList;
    }

    private void range(Node node, RectHV rect, List<Point2D> resultList) {
        if (node == null) {
            return;
        }
        if (node.rectHV.intersects(rect)) {
            if (rect.contains(node.point)) {
                resultList.add(node.point);
            }
            range(node.leftNode, rect, resultList);
            range(node.rightNode, rect, resultList);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("arg is null.");
        }

        if (isEmpty()) {
            return null;
        }

        Point2D nearest = root.point; // Assign root point as nearest
        return nearest(root, nearest, p);
    }

    private Point2D nearest(Node node, Point2D nearest, Point2D queryPoint) {
        if (node == null) { // Node is null return nearest
            return nearest;
        }

        // Calculate distance between nearest and query point
        double distancePointToQueryPoint = nearest.distanceTo(queryPoint);
        // Search in this node rect
        if (distancePointToQueryPoint <= node.rectHV.distanceTo(queryPoint)) {
            return nearest;
        }

        double distanceNodePointToQueryPoint = node.point.distanceTo(queryPoint);
        nearest = ((distanceNodePointToQueryPoint < distancePointToQueryPoint) ? node.point : nearest);

        // return nearest if there is no child
        if ((node.leftNode == null) && (node.rightNode == null)) {
            return nearest;
        }

        // Search in right node
        if (node.rightNode != null) {
            nearest = nearest(node.rightNode, nearest, queryPoint);
        }

        // Search in left node
        if (node.leftNode != null) {
            nearest = nearest(node.leftNode, nearest, queryPoint);
        }

        return nearest;
    }

    private class Node implements Comparable<Point2D> {
        private Point2D point;
        private Node leftNode, rightNode;
        private final RectHV rectHV;
        private RectHV rectHVLeft;
        private RectHV rectHVRight;
        private final boolean lineVertical;
        private final double pointX;
        private final double pointY;

        public Node(Point2D point, RectHV rectHV, boolean lineVertical) {
            this.point = point;
            this.rectHV = rectHV;
            this.lineVertical = lineVertical;
            pointX = point.x();
            pointY = point.y();

            if (lineVertical) {
                rectHVLeft = new RectHV(rectHV.xmin(), rectHV.ymin(), pointX, rectHV.ymax());
                rectHVRight = new RectHV(pointX, rectHV.ymin(), rectHV.xmax(), rectHV.ymax());
            } else {
                rectHVLeft = new RectHV(rectHV.xmin(), rectHV.ymin(), rectHV.xmax(), pointY);
                rectHVRight = new RectHV(rectHV.xmin(), pointY, rectHV.xmax(), rectHV.ymax());
            }
        }

        @Override
        public int compareTo(Point2D that) {
            if (lineVertical) {
                if (this.pointX < that.x()) {
                    return -1;
                } else if (this.pointX > that.x()) {
                    return +1;
                } else {
                    return Double.compare(this.pointY, that.y());
                }
            } else {
                if (this.pointY < that.y()) {
                    return -1;
                } else if (this.pointY > that.y()) {
                    return +1;
                } else {
                    return Double.compare(this.pointX, that.x());
                }
            }
        }
    }
}
