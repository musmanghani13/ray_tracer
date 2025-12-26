package geometry;

import core.Interval;
import core.Ray;

public interface Hittable {
    /**
     * Check if a ray hits this object within the given t range
     * @return true if hit occurs, false otherwise
     */
    boolean hit(
            Ray r,
            Interval rayT,
            HitRecord record
    );
}
