public class Dielectric extends Material {
    private final double refractiveIndex;

    private static double reflectance(double cos, double refractiveIndex) {
        double r0 = (1 - refractiveIndex) / (1 + refractiveIndex);
        r0 = r0 * r0;
        return r0 + (1 - r0) * Math.pow((1 - cos), 5);
    }

    public Dielectric(double index) {
        this.refractiveIndex = index;
    }

    @Override
    public ScatterRecord scatter(Ray rayIn, HitRecord rec) {
        Vec3 reflectiveness = new Vec3(1.0, 1.0, 1.0);

        double ri = rec.frontFacing ? (1.0 / this.refractiveIndex) : this.refractiveIndex;

        Vec3 unitDirection = Vec3.unitVector(rayIn.getDirection());
        double cos = Math.min(Vec3.dot(unitDirection.negate(), rec.normal), 1.0);
        double sin = Math.sqrt(1.0 - cos*cos);
        boolean cannotRefract = ri * sin > 1.0;
        Vec3 direction;
        if (cannotRefract || reflectance(cos, ri) > Utils.randomDouble()) {
            direction = Vec3.reflect(unitDirection, rec.normal);
        } else {
            direction = Vec3.refract(unitDirection, rec.normal, ri);
        }
        Ray scattered = new Ray(rec.p, direction);
        return new ScatterRecord(reflectiveness, scattered);
    }
}
