public class Metal extends Material {
    private Vec3 reflectance;

    public Metal(Vec3 reflectance) {
        this.reflectance = reflectance;
    }

    @Override
    public ScatterRecord scatter(Ray rayIn, HitRecord rec) {
        Vec3 reflected = Vec3.reflect(Vec3.unitVector(rayIn.getDirection()), rec.normal);
        Ray scattered = new Ray(rec.p, reflected);
        return new ScatterRecord(reflectance, scattered);
    }
}
