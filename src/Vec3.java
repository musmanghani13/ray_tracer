public class Vec3 {
    public double[] e;

    public Vec3() {
        e = new double[] {0, 0, 0};
    }

    public Vec3(double e0, double e1, double e2) {
        e = new double[]{e0, e1, e2};
    }

    public double x() {
        return e[0];
    }

    public double y() {
        return e[1];
    }

    public double z() {
        return e[2];
    }

    public Vec3 negate() {
        return new Vec3(-e[0], -e[1], -e[2]);
    }

    // Array access
    public double get(int i) {
        return e[i];
    }

    public void set(int i, double value) {
        e[i] = value;
    }

    // Addition (modifies this vector)
    public Vec3 addAssign(Vec3 v) {
        e[0] += v.e[0];
        e[1] += v.e[1];
        e[2] += v.e[2];
        return this;
    }

    // Multiplication by scalar (modifies this vector)
    public Vec3 multiplyAssign(double t) {
        e[0] *= t;
        e[1] *= t;
        e[2] *= t;
        return this;
    }

    // Division by scalar (modifies this vector)
    public Vec3 divideAssign(double t) {
        return multiplyAssign(1 / t);
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return e[0] * e[0] + e[1] * e[1] + e[2] * e[2];
    }

    // Vector addition (creates new vector)
    public Vec3 add(Vec3 v) {
        return new Vec3(e[0] + v.e[0], e[1] + v.e[1], e[2] + v.e[2]);
    }

    // Vector subtraction (creates new vector)
    public Vec3 subtract(Vec3 v) {
        return new Vec3(e[0] - v.e[0], e[1] - v.e[1], e[2] - v.e[2]);
    }

    // Vector multiplication (element-wise, creates new vector)
    public Vec3 multiply(Vec3 v) {
        return new Vec3(e[0] * v.e[0], e[1] * v.e[1], e[2] * v.e[2]);
    }

    // Scalar multiplication (creates new vector)
    public Vec3 multiply(double t) {
        return new Vec3(t * e[0], t * e[1], t * e[2]);
    }

    // Scalar division (creates new vector)
    public Vec3 divide(double t) {
        return multiply(1 / t);
    }

    // Dot product
    public double dot(Vec3 v) {
        return e[0] * v.e[0] + e[1] * v.e[1] + e[2] * v.e[2];
    }

    // Cross product
    public Vec3 cross(Vec3 v) {
        return new Vec3(
                e[1] * v.e[2] - e[2] * v.e[1],
                e[2] * v.e[0] - e[0] * v.e[2],
                e[0] * v.e[1] - e[1] * v.e[0]
        );
    }

    // Unit vector
    public Vec3 unitVector() {
        return divide(length());
    }

    // Static utility methods for convenience
    public static Vec3 add(Vec3 u, Vec3 v) {
        return u.add(v);
    }

    public static Vec3 subtract(Vec3 u, Vec3 v) {
        return u.subtract(v);
    }

    public static Vec3 multiply(Vec3 u, Vec3 v) {
        return u.multiply(v);
    }

    public static Vec3 multiply(double t, Vec3 v) {
        return v.multiply(t);
    }

    public static Vec3 multiply(Vec3 v, double t) {
        return v.multiply(t);
    }

    public static Vec3 divide(Vec3 v, double t) {
        return v.divide(t);
    }

    public static double dot(Vec3 u, Vec3 v) {
        return u.dot(v);
    }

    public static Vec3 cross(Vec3 u, Vec3 v) {
        return u.cross(v);
    }

    public static Vec3 unitVector(Vec3 v) {
        return v.unitVector();
    }

    // bounce the ray in random direction
    public static Vec3 random() {
        return new Vec3(
                Utils.randomDouble(),
                Utils.randomDouble(),
                Utils.randomDouble()
        );
    }

    public static Vec3 random(double min, double max) {
        return new Vec3(
                Utils.randomDouble(min, max),
                Utils.randomDouble(min, max),
                Utils.randomDouble(min, max)
        );
    }

    public static Vec3 randomUnitVector() {
        while (true) {
            Vec3 p = Vec3.random(-1, 1);
            double lenSq = p.lengthSquared();
            if (1e-160 < lenSq && lenSq <= 1) return p.divide(Math.sqrt(lenSq));
        }
    }

    public static Vec3 randomOnHemisphere(Vec3 normal) {
        Vec3 onUnitHemisphere = randomUnitVector();

        if (Vec3.dot(onUnitHemisphere, normal) > 0.0) {
            return onUnitHemisphere;
        } else {
            return onUnitHemisphere.negate();
        }
    }

    @Override
    public String toString() {
        return e[0] + " " + e[1] + " " + e[2];
    }
}
