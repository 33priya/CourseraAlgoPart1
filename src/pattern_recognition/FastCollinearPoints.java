package pattern_recognition;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();
    private final ArrayList<FCLineSegment> fcLineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("argument is null");
        }

        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("argument has null point");
            }
        }

        Arrays.sort(points);
        int size = points.length;
        for (int i = 0; i < size - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("argument has duplicate points: " + points[i]);
            }
        }

        for (int i = 0; i < size; i++) {
            Point origin = points[i];
            Point[] aux = new Point[size - (i + 1)];
            int newIndex = 0;
            for (int index = i + 1; index < size; index++) {
                aux[newIndex++] = points[index];
            }
            Arrays.sort(aux, origin.slopeOrder());
            int newSize = aux.length;
            for (int j = 0; j < newSize; j++) {
                double ojSlope = origin.slopeTo(aux[j]);
                int count = 1;

                for (int k = j + 1; k < newSize; k++) {
                    double okSlope = origin.slopeTo(aux[k]);
                    if (Double.compare(ojSlope, okSlope) == 0) {
                        count++;
                    } else {
                        break;
                    }
                }

                if (count >= 3) {
                    addLineSegment(aux, origin, j, count);
                }
            }
        }

        for (FCLineSegment fcLineSegment : fcLineSegments) {
            lineSegments.add(new LineSegment(fcLineSegment.p, fcLineSegment.q));
        }
    }

    private void addLineSegment(Point[] points, Point origin, int index, int size) {
        Point min = origin;
        Point max = origin;

        for (int i = index; i < (index + size); i++) {
            if (min.compareTo(points[i]) > 0) {
                min = points[i];
            }

            if (max.compareTo(points[i]) < 0) {
                max = points[i];
            }
        }

        boolean isLSExist = false;
        for (FCLineSegment fcLineSegment : fcLineSegments) {
            if (fcLineSegment.isSegmentContains(min, max)) {
                isLSExist = true;
                break;
            }
        }

        if (!isLSExist) {
            fcLineSegments.add(new FCLineSegment(min, max));
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]);
    }

    private class FCLineSegment {
        final Point p, q;

        FCLineSegment(Point p, Point q) {
            if (p == null || q == null) {
                throw new NullPointerException("argument is null");
            }
            this.p = p;
            this.q = q;
        }

        private double lineSegmentSlope() {
            return p.slopeTo(q);
        }

        private boolean isSegmentContains(Point min, Point max) {
            if (min == null || max == null) {
                throw new NullPointerException("argument is null");
            }

            if (Double.compare(this.lineSegmentSlope(), min.slopeTo(max)) == 0) {
                if (this.p.compareTo(min) == 0 || this.p.compareTo(max) == 0 || this.q.compareTo(min) == 0 || this.q.compareTo(max) == 0) {
                    return true;
                } else {
                    return (Double.compare(this.p.slopeTo(min), this.q.slopeTo(min)) == 0);
                }
            }

            return false;
        }
    }
}
