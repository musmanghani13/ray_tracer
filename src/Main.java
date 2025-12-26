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

        Material groundMaterial = new Lambertian(new Vec3(0.5, 0.5, 0.5));
        world.add(new Sphere(new Vec3(0, -1000, 0), 1000, groundMaterial));

        for (int a = -11; a < 11; a++) {
            for (int b = -11; b < 11; b++) {
                double chooseMat = Utils.randomDouble();
                Vec3 center = new Vec3(
                        a + 0.9 * Utils.randomDouble(),
                        0.2,
                        b + 0.9 * Utils.randomDouble()
                );

                if (center.subtract(new Vec3(4, 0.2, 0)).length() > 0.9) {
                    Material sphereMaterial;

                    if (chooseMat < 0.8) {
                        // Diffuse
                        Vec3 albedo = Vec3.random().multiply(Vec3.random());
                        sphereMaterial = new Lambertian(albedo);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    } else if (chooseMat < 0.95) {
                        // Metal
                        Vec3 albedo = Vec3.random(0.5, 1);
                        double fuzz = Utils.randomDouble(0, 0.5);
                        sphereMaterial = new Metal(albedo, fuzz);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    } else {
                        // Glass
                        sphereMaterial = new Dielectric(1.5);
                        world.add(new Sphere(center, 0.2, sphereMaterial));
                    }
                }
            }
        }

        // Three large spheres
        Material material1 = new Dielectric(1.5);
        world.add(new Sphere(new Vec3(0, 1, 0), 1.0, material1));

        Material material2 = new Lambertian(new Vec3(0.4, 0.2, 0.1));
        world.add(new Sphere(new Vec3(-4, 1, 0), 1.0, material2));

        Material material3 = new Metal(new Vec3(0.7, 0.6, 0.5), 0.0);
        world.add(new Sphere(new Vec3(4, 1, 0), 1.0, material3));

        // Camera
        Camera cam = new Camera();

        cam.aspectRatio = 16.0 / 9.0;
        cam.imageWidth = 400;
        cam.setSamplesPerPixel(50);
        cam.maxDepth = 50;

        cam.setVerticalFov(20);
        cam.setLookFrom(new Vec3(13, 2, 3));
        cam.setLookAt(new Vec3(0, 0, 0));
        cam.setvUp(new Vec3(0, 1, 0));

        cam.setDeFocusAngle(0.6);
        cam.setFocusDist(10.0);

        cam.render(world);
    }
}
