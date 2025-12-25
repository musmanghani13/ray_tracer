public class Interval {
    private double min;
    private double max;

    public static final Interval EMPTY = new Interval(Utils.INFINITY, -Utils.INFINITY);
    public static final Interval UNIVERSE = new Interval(-Utils.INFINITY, Utils.INFINITY);

    public Interval() {
        this.min = Utils.INFINITY;
        this.max = -Utils.INFINITY;
    }

    public Interval(double mn, double mx) {
        this.min = mn;
        this.max = mx;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double size() {
        return max - min;
    }

    public boolean contains(double x) {
        return this.min <= x && x <= this.max;
    }

    public boolean surrounds(double x) {
        return this.min < x && x < this.max;
    }

    double clamp(double x) {
        if (x < this.getMin()) return this.getMin();
        if (x > this.getMax()) return this.getMax();
        return x;
    }
}
