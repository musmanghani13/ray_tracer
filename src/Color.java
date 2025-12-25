import java.io.BufferedWriter;
import java.io.IOException;

public class Color {

    private static double linearToGama(double linearComponent) {
        if (linearComponent > 0) return Math.sqrt(linearComponent);
        return 0;
    }

    public static void writeColor(BufferedWriter w, Vec3 pixelColor) throws IOException {
        double r = pixelColor.x();
        double g = pixelColor.y();
        double b = pixelColor.z();

        r = linearToGama(r);
        g = linearToGama(g);
        b = linearToGama(b);

        Interval intensity = new Interval(0.000, 0.999);
        int rByte = (int)(256 * intensity.clamp(r));
        int gByte = (int)(256 * intensity.clamp(g));
        int bByte = (int)(256 * intensity.clamp(b));

        w.write(rByte + " " + gByte + " " + bByte + "\n");
    }
}
