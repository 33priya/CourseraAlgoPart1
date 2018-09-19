package pattern_recognition;

import java.util.ArrayList;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("argument is null");
        }

        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("argument has null point");
            }
        }

        int size = points.length;
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if ((points[i].compareTo(points[j]) == 0) && (i != j)) {
                    throw new IllegalArgumentException("argument has duplicate points: " + points[j]);
                }
            }
        }

        for (int p = 0; p < size; p++) {
            for (int q = p + 1; q < size; q++) {
                for (int r = q + 1; r < size; r++) {
                    for (int s = r + 1; s < size; s++) {
                        double pqSlope = points[p].slopeTo(points[q]);
                        double prSlope = points[p].slopeTo(points[r]);

                        if (Double.compare(pqSlope, prSlope) == 0) {
                            double psSlope = points[p].slopeTo(points[s]);
                            if (Double.compare(pqSlope, psSlope) == 0) {
                                lineSegments.add(getLineSegment(points[p], points[q], points[r], points[s]));
                            }
                        }
                    }
                }
            }
        }
    }

    private LineSegment getLineSegment(Point p, Point q, Point r, Point s) {
        Point min = p;
        Point max = p;
        // Find Min
        if (min.compareTo(q) > 0) {
            min = q;
        }
        if (min.compareTo(r) > 0) {
            min = r;
        }
        if (min.compareTo(s) > 0) {
            min = s;
        }

        // Find Max
        if (max.compareTo(q) < 0) {
            max = q;
        }
        if (max.compareTo(r) < 0) {
            max = r;
        }
        if (max.compareTo(s) < 0) {
            max = s;
        }

        return new LineSegment(min, max);
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }
}
