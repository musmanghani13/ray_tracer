package material;

import core.Ray;
import math.Vec3;

public class ScatterRecord {
    private Vec3 attenuation; // signals how much a ray color should be enhanced (multiplied)
    private Ray scattered;

    public ScatterRecord(Vec3 attenuation, Ray scattered) {
        this.attenuation = attenuation;
        this.scattered = scattered;
    }

    public Ray getScattered() {
        return scattered;
    }

    public Vec3 getAttenuation() {
        return attenuation;
    }
}
