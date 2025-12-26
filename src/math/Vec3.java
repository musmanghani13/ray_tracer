package math;

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

    public static Vec3 randomInUnitDisk() {
        while (true) {
            Vec3 p = new Vec3(Utils.randomDouble(-1, 1), Utils.randomDouble(-1, 1), 0);
            if (p.lengthSquared() < 1) {
                return p;
            }
        }
    }

    /*
    Explanation from documentation
        The reflected ray direction in red is just v+2b. In our design, n is a unit vector (length one), but v may not be.
        To get the vector b, we scale the normal vector by the length of the projection of v onto n, which is given by the
        dot product v⋅n. (If n were not a unit vector, we would also need to divide this dot product by the length of n.).
        Finally, because v points into the surface, and we want b to point out of the surface, we need to negate this projection length.
        The dot product calculates the projection of v onto n.
     */
    public static Vec3 reflect(Vec3 v, Vec3 n) {
        return v.subtract(n.multiply(2 * Vec3.dot(v, n)));
    }

    /**
     * Refract a vector using Snell's law
     * @param uv The incident vector (unit length)
     * @param n The surface normal (unit length)
     * @param etaiOverEtat The ratio of refractive indices (eta_incident / eta_transmitted)
     * @return The refracted vector
     */
    public static Vec3 refract(Vec3 uv, Vec3 n, double etaiOverEtat) {
        double cosTheta = Math.min(Vec3.dot(uv.negate(), n), 1.0);
        Vec3 rOutPerp = uv.add(n.multiply(cosTheta)).multiply(etaiOverEtat);
        Vec3 rOutParallel = n.multiply(-Math.sqrt(Math.abs(1.0 - rOutPerp.lengthSquared())));
        return rOutPerp.add(rOutParallel);
    }

    public boolean nearZero() {
        double s = 1e-8;
        return (Math.abs(e[0]) < s) && (Math.abs(e[1]) < s) && (Math.abs(e[2]) < s);
    }

    @Override
    public String toString() {
        return e[0] + " " + e[1] + " " + e[2];
    }
}
