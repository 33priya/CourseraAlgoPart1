package pattern_recognition;

public class LineSegment {
    private Point p;
    private Point q;

    public LineSegment(Point p, Point q) {
        if (p == null || q == null) {
            throw new NullPointerException("argument is null");
        }
        this.p = p;
        this.q = q;
    }

    public void draw() {
        p.drawTo(q);
    }

    @Override
    public String toString() {
        return p + " -> " + q;
    }
}
