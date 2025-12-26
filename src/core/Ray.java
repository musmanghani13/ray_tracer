package core;

import math.Vec3;

public class Ray {
    private Vec3 origin;
    private Vec3 direction;

    public Ray() {
        this.origin = new Vec3();
        this.direction = new Vec3();
    }

    public Ray(Vec3 o, Vec3 d) {
        this.origin = o;
        this.direction = d;
    }

    public Vec3 getOrigin() {
        return this.origin;
    }

    public Vec3 getDirection() {
        return this.direction;
    }

    // P(t)=A+tb
    public Vec3 at(double t) {
        return this.origin.add(this.direction.multiply(t));
    }
}
