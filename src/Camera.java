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
    private double verticalFov = 90;
    private Vec3 lookFrom;
    private Vec3 lookAt;
    private Vec3 vUp;

    // orthogonal vectors for camera frame
    private Vec3 u, v, w;
    private double deFocusAngle = 0;
    private double focusDist = 10;

    private Vec3 deFocusDiskU;
    private Vec3 deFocusDiskV;

    public double aspectRatio = 1.0;
    public int imageWidth = 100;
    public int maxDepth = 50;

    public Vec3 getvUp() {
        return vUp;
    }

    public void setvUp(Vec3 vUp) {
        this.vUp = vUp;
    }

    public Vec3 getLookAt() {
        return lookAt;
    }

    public void setLookAt(Vec3 lookAt) {
        this.lookAt = lookAt;
    }

    public Vec3 getLookFrom() {
        return lookFrom;
    }

    public void setLookFrom(Vec3 lookFrom) {
        this.lookFrom = lookFrom;
    }

    public double getVerticalFov() {
        return verticalFov;
    }

    public double getFocusDist() {
        return focusDist;
    }

    public void setFocusDist(double focusDist) {
        this.focusDist = focusDist;
    }

    public double getDeFocusAngle() {
        return deFocusAngle;
    }

    public void setDeFocusAngle(double deFocusAngle) {
        this.deFocusAngle = deFocusAngle;
    }

    public void setVerticalFov(double verticalFov) {
        this.verticalFov = verticalFov;
    }

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

        HitRecord rec = new HitRecord();

        if (world.hit(r, new Interval(0.001, Utils.INFINITY), rec)) {
            ScatterRecord scatterRec = rec.material.scatter(r, rec);

            if (scatterRec != null) {
                Vec3 scatteredColor = rayColor(scatterRec.getScattered(), depth - 1, world);
                return scatteredColor.multiply(scatterRec.getAttenuation());
            }

            return new Vec3(0, 0, 0);  // Absorbed
        }

        // Background gradient (sky)
        Vec3 unitDirection = Vec3.unitVector(r.getDirection());
        double a = 0.5 * (unitDirection.y() + 1.0);

        return new Vec3(1.0, 1.0, 1.0).multiply(1.0 - a)
                .add(new Vec3(0.5, 0.7, 1.0).multiply(a));
    }

    private void initialize() {
        this.imageHeight = (int)(imageWidth / aspectRatio);
        imageHeight = (imageHeight < 1) ? 1 : imageHeight;

        pixelSamplesScale = 1.0 / samplesPerPixel;

        cameraCenter = this.getLookFrom();

        // camera config
        double theta = Utils.degreesToRadians(verticalFov);
        double h = Math.tan(theta / 2);
        double viewportHeight = 2 * h * this.getFocusDist();
        double viewportWidth = viewportHeight * ((double)(imageWidth) / imageHeight);
        // u,v,w unit basis vectors for the camera coordinate frame.
        w = Vec3.unitVector(this.getLookFrom().subtract(this.getLookAt()));
        u = Vec3.unitVector(Vec3.cross(this.getvUp(), w));
        v = Vec3.cross(w, u);

        // vectors over horizontal and vertical edges of viewport
        Vec3 viewportU = u.multiply(viewportWidth);
        Vec3 viewportV = v.negate().multiply(viewportHeight);

        // delta vectors
        this.pixelDeltaU = viewportU.divide(imageWidth);
        this.pixelDeltaV = viewportV.divide(imageHeight);

        Vec3 viewportUpperLeft = cameraCenter
                .subtract(w.multiply(this.getFocusDist()))
                .subtract(viewportU.divide(2))
                .subtract(viewportV.divide(2));

        this.pixel00Location = viewportUpperLeft.add(pixelDeltaU.add(pixelDeltaV).multiply(0.5));

        double deFocusRadius = this.getFocusDist() * Math.tan(Utils.degreesToRadians(this.getDeFocusAngle() / 2));
        this.deFocusDiskU = u.multiply(deFocusRadius);
        this.deFocusDiskV = v.multiply(deFocusRadius);
    }

    private Ray getRay(int column, int row) {
        Vec3 offset = sampleSquare();
        Vec3 pixelSample = pixel00Location
                .add(pixelDeltaU.multiply(column + offset.x()))
                .add(pixelDeltaV.multiply(row + offset.y()));

        Vec3 rayOrigin = (this.getDeFocusAngle() <= 0) ? cameraCenter : deFocusDiskSample();
        Vec3 rayDirection = pixelSample.subtract(rayOrigin);

        return new Ray(rayOrigin, rayDirection);
    }

    private Vec3 deFocusDiskSample() {
        // Returns a random point in the camera defocus disk
        Vec3 p = Vec3.randomInUnitDisk();
        return cameraCenter.add(deFocusDiskU.multiply(p.x())).add(deFocusDiskV.multiply(p.y()));
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