package pattern_recognition;

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        if (that == null) {
            throw new NullPointerException("argument is null");
        }
        StdDraw.line(x, y, that.x, that.y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    @Override
    public int compareTo(Point that) {
        if (that == null) {
            throw new NullPointerException("argument is null");
        }

        if (this.y == that.y) {
            return Integer.compare(this.x, that.x);
        } else if (this.y > that.y) {
            return 1;
        } else {
            return -1;
        }
    }

    public double slopeTo(Point that) {
        if (that == null) {
            throw new NullPointerException("argument is null");
        }

        if ((this.x == that.x) && (this.y == that.y)) {
            return Double.NEGATIVE_INFINITY;
        } else if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        } else if (this.y == that.y) {
            return 0.0;
        } else {
            return ((that.y - this.y)/((double) (that.x - this.x)));
        }
    }

    public Comparator<Point> slopeOrder() {
        return new SlopeComparator();
    }

    private class SlopeComparator implements Comparator<Point> {
        @Override
        public int compare(Point p1, Point p2) {
            double slopeP1 = slopeTo(p1);
            double slopeP2 = slopeTo(p2);
            return Double.compare(slopeP1, slopeP2);
        }
    }
}
