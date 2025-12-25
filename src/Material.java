public abstract class Material {
    public abstract ScatterRecord scatter(
            Ray rayIn,
            HitRecord rec
    );
}
