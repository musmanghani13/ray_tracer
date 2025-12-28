package geometry;

import core.Interval;
import core.Ray;

import java.util.ArrayList;
import java.util.List;

public class HittableList implements Hittable {
    private List<Hittable> objects;

    public HittableList() {
        objects = new ArrayList<>();
    }

    public HittableList(Hittable object) {
        objects = new ArrayList<>();
        objects.add(object);
    }

    public void clear() {
        objects.clear();
    }

    public void add(Hittable object) {
        objects.add(object);
    }

    @Override
    public boolean hit(Ray r, Interval rayT, HitRecord record) {
        HitRecord tempRecord = new HitRecord();
        boolean hitAnything = false;

        double closestSoFar = rayT.getMax();

        for (Hittable o : objects) {
            if (o.hit(r, new Interval(rayT.getMin(), closestSoFar), tempRecord)) {
                hitAnything = true;
                closestSoFar = tempRecord.t;
                record.t = tempRecord.t;
                record.p = tempRecord.p;
                record.normal = tempRecord.normal;
                record .frontFacing = tempRecord.frontFacing;
                record.material = tempRecord.material;
            }
        }
        return hitAnything;
    }

    public int size() {
        return objects.size();
    }
}
