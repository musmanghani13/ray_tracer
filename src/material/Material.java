package material;

import core.Ray;
import geometry.HitRecord;

public abstract class Material {
    public abstract ScatterRecord scatter(
            Ray rayIn,
            HitRecord rec
    );
}
