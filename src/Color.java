import java.io.BufferedWriter;
import java.io.IOException;

public class Color {

    public static void writeColor(BufferedWriter w, Vec3 pixelColor) throws IOException {
        double r = pixelColor.x();
        double g = pixelColor.y();
        double b = pixelColor.z();

        Interval intensity = new Interval(0.000, 0.999);
        int rByte = (int)(256 * intensity.clamp(r));
        int gByte = (int)(255.999 * intensity.clamp(g));
        int bByte = (int)(255.999 * intensity.clamp(b));

        w.write(rByte + " " + gByte + " " + bByte + "\n");
    }
}
