/*
    We're building a virtual camera that shoots rays through pixels to see what color they should be.
    Think of it like reverse photography - instead of light coming TO the camera, we shoot rays FROM the camera.

    CAMERA              VIEWPORT             IMAGE FILE
           ●                 ┌─────┐             ┌─────┐
           |                 │     │             │█████│
           |  Ray shoots →   │  ●  │  Maps to →  │█░░░█│
           |  through        │     │             │█████│
           |  viewport       └─────┘             └─────┘
           |
        (Point in 3D)    (Window in 3D)      (Pixel grid)

    Mapping Process
        1. For each pixel in the image (e.g., pixel [100, 50])
        2. Calculate corresponding point on the viewport
            Pixel (100, 50) → Viewport point (1.5, 0.8, -1.0) in 3D
        3. Shoot a ray from camera through that viewport point (1.5, 0.8, -1.0)
            Ray origin: Camera position (defined in Ray.java class)
            Ray direction: From camera to viewport point (defined in Ray.java class)
        4. Trace the ray to see what it hits in the 3D world
        5. Determine color and write it to that pixel in the image
*/
public class Main {
    public static void main(String[] args) {
        HittableList world = new HittableList();

        Material materialGround = new Lambertian(new Vec3(0.8, 0.8, 0.0));
        Material materialCenter = new Lambertian(new Vec3(0.7, 0.3, 0.3));
        Material leftMaterial = new Metal(new Vec3(0.8, 0.8, 0.8), 0.3);
        Material rightMaterial = new Metal(new Vec3(0.8, 0.6, 0.2), 0);

        world.add(new Sphere(new Vec3(0, 0, -1), 0.5, materialCenter));
        world.add(new Sphere(new Vec3(0, -100.5, -1), 100, materialGround)); // background
        world.add(new Sphere(new Vec3(-1, 0, -1), 0.3, leftMaterial));
        world.add(new Sphere(new Vec3(1, 0, -1), 0.5, rightMaterial));

        Camera camera = new Camera();

        camera.aspectRatio = 16.0 / 9.0;
        camera.imageWidth = 400;
        camera.setSamplesPerPixel(100);

        camera.render(world);
    }
}
