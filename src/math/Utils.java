package math;

public class Utils {
    public static final double INFINITY = Double.POSITIVE_INFINITY;
    public static final double PI = 3.1415926535897932385;

    public static double degreesToRadians(double degrees) {
        return degrees * PI / 180.0;
    }

    public static double randomDouble() {
        return Math.random();
    }

    public static double randomDouble(double min, double max) {
        return min + (max - min) * randomDouble();
    }
}
