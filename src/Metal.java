public class Metal extends Material {
    private Vec3 reflectance;
    private double fuzz;    // fuzziness of reflection

    public Metal(Vec3 reflectance, double fuzz) {
        this.reflectance = reflectance;
        if (fuzz < 1) {
            this.fuzz = fuzz;
        } else {
            this.fuzz = 1;
        }
    }

    @Override
    public ScatterRecord scatter(Ray rayIn, HitRecord rec) {
        Vec3 reflected = Vec3.reflect(Vec3.unitVector(rayIn.getDirection()), rec.normal);
        reflected = Vec3.unitVector(reflected).add(Vec3.randomUnitVector().multiply(fuzz));

        Ray scattered = new Ray(rec.p, reflected);
        if (Vec3.dot(scattered.getDirection(), rec.normal) > 0) return new ScatterRecord(this.reflectance, scattered);
        return null;
    }
}
