package geometry;

import core.Ray;
import material.Material;
import math.Vec3;

/*

 */
public class HitRecord {
    public Vec3 p;
    public Vec3 normal;
    public double t;
    public boolean frontFacing;
    public Material material;   // geometry.HitRecord class needs to know which material was hit

    public HitRecord() {
        this.p = new Vec3();
        this.normal = new Vec3();
        this.t = 0.0;
    }

    void setFaceNormal(Ray ray, Vec3 outwardNormal) {
        frontFacing = Vec3.dot(ray.getDirection(), outwardNormal) < 0; // if this is true it means ray is coming from outside
        normal = frontFacing ? outwardNormal : outwardNormal.negate();
    }

    public void copy(HitRecord r) {
        this.t = r.t;
        this.p = r.p;
        this.normal = r.normal;
        this.frontFacing = r.frontFacing;
        this.material = r.material;
    }
}
