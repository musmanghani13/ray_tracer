public class Sphere implements Hittable {
    private Vec3 center;
    private double radius;
    private Material material;

    public Sphere(Vec3 center, double radius, Material m) {
        this.center = center;
        this.radius = Math.max(0, radius);
        this.material = m;
    }

    @Override
    public boolean hit(Ray r, Interval rayT, HitRecord record) {
        Vec3 oc = center.subtract(r.getOrigin());
        double a = r.getDirection().lengthSquared();
        double h = Vec3.dot(r.getDirection(), oc);
        double c = oc.lengthSquared() - radius*radius;

        double discriminant = h*h - a*c;

        if (discriminant < 0) {
            return false;
        }

        double sqrtDiscriminant = Math.sqrt(discriminant);
        double root = (h - sqrtDiscriminant) / a;
        if (!rayT.surrounds(root)) {
            root = (h + sqrtDiscriminant) / a;
            if (!rayT.surrounds(root)) {
                return false;  // Both roots outside valid range
            }
        }

        record.t = root;
        record.p = r.at(record.t);
        Vec3 outwardNormal = (record.p.subtract(center)).divide(radius);
        record.setFaceNormal(r, outwardNormal);
        record.material = this.material;

        return true;
    }

    public Vec3 getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}
