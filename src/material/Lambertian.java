package material;

import core.Ray;
import geometry.HitRecord;
import math.Vec3;

public class Lambertian extends Material {
    private Vec3 reflectance;

    public Lambertian(Vec3 reflectance) {
        this.reflectance = reflectance;
    }

    @Override
    public ScatterRecord scatter(Ray rayIn, HitRecord rec) {
        Vec3 scatterDirection = Vec3.randomOnHemisphere(rec.normal);
        if (scatterDirection.nearZero()) {
            scatterDirection = rec.normal;
        }
        Ray scattered = new Ray(rec.p, scatterDirection);
        return new ScatterRecord(this.reflectance, scattered);
    }
}
