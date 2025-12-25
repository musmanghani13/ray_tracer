import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Camera {
    private int imageHeight;
    private Vec3 cameraCenter;
    private Vec3 pixel00Location;
    private Vec3 pixelDeltaU;
    private Vec3 pixelDeltaV;
    private int samplesPerPixel;
    private double pixelSamplesScale;

    public double aspectRatio = 1.0;
    public int imageWidth = 100;
    public int maxDepth = 50;  // NEW: Maximum number of ray bounces

    public void setSamplesPerPixel(int samplesPerPixel) {
        this.samplesPerPixel = samplesPerPixel;
    }

    public void setAspectRatio(double ratio) {
        this.aspectRatio = ratio;
    }

    private Vec3 rayColor(Ray r, int depth, Hittable world) {
        // If we've exceeded the ray bounce limit, no more light is gathered
        if (depth <= 0) {
            return new Vec3(0, 0, 0);
        }

        HitRecord record = new HitRecord();

        if (world.hit(r, new Interval(0.001, Utils.INFINITY), record)) {
            Vec3 direction = record.normal.add(Vec3.randomUnitVector());
            return rayColor(new Ray(record.p, direction), depth - 1, world).multiply(0.1);
        }

        // if we make it till here, nothing in the world was hit.
        Vec3 unitDirection = Vec3.unitVector(r.getDirection());
        double a = 0.5 * (unitDirection.y() + 1.0);

        return new Vec3(1.0, 1.0, 1.0).multiply(1.0 - a)
                .add(new Vec3(0.5, 0.7, 1.0).multiply(a));
    }

    private void initialize() {
        this.imageHeight = (int)(imageWidth / aspectRatio);
        imageHeight = (imageHeight < 1) ? 1 : imageHeight;

        pixelSamplesScale = 1.0 / samplesPerPixel;

        // camera config
        double focalLength = 1.0;
        double viewportHeight = 2.0;
        double viewportWidth = viewportHeight * ((double)(imageWidth) / imageHeight);
        this.cameraCenter = new Vec3(0, 0, 0);

        // vectors over horizontal and vertical edges of viewport
        Vec3 viewportU = new Vec3(viewportWidth, 0, 0);
        Vec3 viewportV = new Vec3(0, -viewportHeight, 0);

        // delta vectors
        this.pixelDeltaU = viewportU.divide(imageWidth);
        this.pixelDeltaV = viewportV.divide(imageHeight);

        Vec3 viewportUpperLeft = cameraCenter
                .subtract(new Vec3(0, 0, focalLength))
                .subtract(viewportU.divide(2))
                .subtract(viewportV.divide(2));

        this.pixel00Location = viewportUpperLeft.add(pixelDeltaU.add(pixelDeltaV).multiply(0.5));
    }

    private Ray getRay(int column, int row) {
        Vec3 offset = sampleSquare();
        Vec3 pixelSample = pixel00Location
                .add(pixelDeltaU.multiply(column + offset.x()))
                .add(pixelDeltaV.multiply(row + offset.y()));

        Vec3 rayDirection = pixelSample.subtract(cameraCenter);

        return new Ray(cameraCenter, rayDirection);
    }

    private Vec3 sampleSquare() {
        return new Vec3(
                Utils.randomDouble() - 0.5,
                Utils.randomDouble() - 0.5,
                0
        );
    }

    public void render(Hittable world) {
        initialize();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("image.ppm"))) {
            writer.write("P3\n");
            writer.write(imageWidth + " " + imageHeight + "\n");
            writer.write("255\n");

            for(int currentRow = 0; currentRow < imageHeight; currentRow++) {
                System.out.println("Scanlines remaining: " + (imageHeight - currentRow));

                for (int currentColumn = 0; currentColumn < imageWidth; currentColumn++) {
                    Vec3 pixelColor = new Vec3(0, 0, 0);

                    for (int currentSample = 0; currentSample < samplesPerPixel; currentSample++) {
                        Ray ray = getRay(currentColumn, currentRow);
                        pixelColor = pixelColor.add(rayColor(ray, maxDepth, world));
                    }

                    Color.writeColor(writer, pixelColor.multiply(pixelSamplesScale));
                }
            }

            System.out.println("Done.");
            System.out.println("Image created at: " + new java.io.File("image.ppm").getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}