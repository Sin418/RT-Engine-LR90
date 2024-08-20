import Objects.Shapes.Sphere;
import java.awt.*;
import java.awt.image.BufferedImage;

import Materials.Material;
import Objects.lights.Light;
import Vectors.Vector;
import Vectors.VectorCalculations;

/**
 * The Renderer class is responsible for rendering a scene by tracing rays
 * and computing colors based on intersections with objects and light sources.
 */
public class Renderer {
    private Scene scene;

    /**
     * Constructs a Renderer with the given scene.
     * @param scene The Scene object containing camera, light, and objects to render.
     */
    public Renderer(Scene scene) {
        this.scene = scene;
    }

    /**
     * Renders the scene onto the provided BufferedImage.
     * @param image The BufferedImage to draw the rendered scene onto.
     */
    public void render(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // Iterate over each pixel in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Convert pixel coordinates to normalized device coordinates
                double u = (x + 0.5) / width;
                double v = (y + 0.5) / height;
                // Generate the primary ray from the camera
                Ray ray = scene.getCamera().getRay(u, v);
                // Trace the ray to find the color
                Color color = traceRay(ray);
                // Set the pixel color in the BufferedImage
                image.setRGB(x, y, color.getRGB());
            }
        }
    }

    /**
     * Traces a ray to determine the color of the pixel it corresponds to.
     * @param ray The Ray object to trace.
     * @return The Color of the pixel corresponding to the ray.
     */
    public Color traceRay(Ray ray) {
        double closestIntersectionDistance = Double.MAX_VALUE;
        Color closestObjectColor = Color.white;

        // Check intersection with the light source sphere
        Sphere lightSphere = new Sphere(scene.getLight().getPosition(), 0.1, new Material(Color.blue)); // Small blue sphere as light source
        Vector oc = VectorCalculations.subtractVectors(ray.getOrigin(), lightSphere.getCenter());
        double A = VectorCalculations.calculateDotProduct(ray.getDirection(), ray.getDirection());
        double B = 2.0 * VectorCalculations.calculateDotProduct(oc, ray.getDirection());
        double C = VectorCalculations.calculateDotProduct(oc, oc) - lightSphere.getRadius() * lightSphere.getRadius();
        double discriminant = B * B - 4 * A * C;

        // If the discriminant is positive, there are real intersections
        if (discriminant > 0) {
            double sqrtDiscriminant = Math.sqrt(discriminant);
            double t1 = (-B - sqrtDiscriminant) / (2.0 * A);
            double t2 = (-B + sqrtDiscriminant) / (2.0 * A);
            double t = Math.min(t1, t2);

            if (t > 0 && t < closestIntersectionDistance) {
                closestIntersectionDistance = t;
                closestObjectColor = lightSphere.getMaterial().getColor();
            }
        }

        // Check intersection with the default plane
        Plane plane = scene.getDefaultPlane();
        double planeIntersectionDistance = plane.intersect(ray);
        if (planeIntersectionDistance < closestIntersectionDistance) {
            closestIntersectionDistance = planeIntersectionDistance;
            Vector intersectionPoint = VectorCalculations.addVectors(ray.getOrigin(), VectorCalculations.multiplyVectorTo(ray.getDirection(), planeIntersectionDistance));

            if (isInShadow(intersectionPoint, plane)) {
                closestObjectColor = Color.black; // Shadowed area
            } else {
                closestObjectColor = plane.getColor();
            }
        }

        // Check intersections with all spheres in the scene
        for (Sphere sphere : scene.getObjects()) {
            oc = VectorCalculations.subtractVectors(ray.getOrigin(), sphere.getCenter());
            A = VectorCalculations.calculateDotProduct(ray.getDirection(), ray.getDirection());
            B = 2.0 * VectorCalculations.calculateDotProduct(oc, ray.getDirection());
            C = VectorCalculations.calculateDotProduct(oc, oc) - sphere.getRadius() * sphere.getRadius();
            discriminant = B * B - 4 * A * C;

            if (discriminant > 0) {
                double sqrtDiscriminant = Math.sqrt(discriminant);
                double t1 = (-B - sqrtDiscriminant) / (2.0 * A);
                double t2 = (-B + sqrtDiscriminant) / (2.0 * A);
                double t = Math.min(t1, t2);

                if (t > 0 && t < closestIntersectionDistance) {
                    closestIntersectionDistance = t;
                    Vector intersectionPoint = VectorCalculations.addVectors(ray.getOrigin(), VectorCalculations.multiplyVectorTo(ray.getDirection(), t));

                    if (isInShadow(intersectionPoint, sphere)) {
                        closestObjectColor = Color.black; // Shadowed area
                    } else {
                        closestObjectColor = sphere.getMaterial().getColor();
                    }
                }
            }
        }

        // Apply luminance drop-off based on distance from the light source
        Color finalColor = applyLuminanceDropOff(closestObjectColor, ray.getOrigin());

        return finalColor;
    }

    /**
     * Adjusts the color based on the luminance drop-off effect due to distance from the light source.
     * @param originalColor The original color of the object.
     * @param intersectionPoint The point of intersection between the ray and the object.
     * @return The adjusted color considering the luminance drop-off.
     */
    private Color applyLuminanceDropOff(Color originalColor, Vector intersectionPoint) {
        Vector lightSourcePos = scene.getLight().getPosition();
        double distance = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).getMagnitude();
        double luminance = scene.getLight().getLuminance() / (distance * distance);

        // Adjust luminance factor as needed for better visual results
        double luminanceFactor = 5.0; 
        luminance *= luminanceFactor;

        // Clamp luminance value between 0 and 1
        luminance = Math.max(0, Math.min(1, luminance));

        // Compute the final color with adjusted luminance
        int red = (int) (originalColor.getRed() * luminance + 0.5);
        int green = (int) (originalColor.getGreen() * luminance + 0.5);
        int blue = (int) (originalColor.getBlue() * luminance + 0.5);

        // Clamp color values between 0 and 255
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        return new Color(red, green, blue);
    }

    /**
     * Determines if a point is in shadow relative to a plane.
     * @param intersectionPoint The point to check for shadow.
     * @param plane The plane to test for shadow.
     * @return True if the point is in shadow, false otherwise.
     */
    private boolean isInShadow(Vector intersectionPoint, Plane plane) {
        Vector lightSourcePos = scene.getLight().getPosition();
        Vector shadowRayDirection = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).normalize();

        // Slightly offset the origin to avoid self-shadowing
        Vector shadowRayOrigin = VectorCalculations.addVectors(intersectionPoint, VectorCalculations.multiplyVectorTo(shadowRayDirection, 0.001));
        Ray shadowRay = new Ray(shadowRayOrigin, shadowRayDirection);

        // Check if the shadow ray intersects with any spheres
        for (Sphere obj : scene.getObjects()) {
            Vector oc = VectorCalculations.subtractVectors(shadowRay.getOrigin(), obj.getCenter());
            double A = VectorCalculations.calculateDotProduct(shadowRay.getDirection(), shadowRay.getDirection());
            double B = 2.0 * VectorCalculations.calculateDotProduct(oc, shadowRay.getDirection());
            double C = VectorCalculations.calculateDotProduct(oc, oc) - obj.getRadius() * obj.getRadius();
            double discriminant = B * B - 4 * A * C;

            if (discriminant > 0) {
                double sqrtDiscriminant = Math.sqrt(discriminant);
                double t1 = (-B - sqrtDiscriminant) / (2.0 * A);
                double t2 = (-B + sqrtDiscriminant) / (2.0 * A);
                double t = Math.min(t1, t2);

                double lightDistance = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).getMagnitude();
                if (t > 0 && t < lightDistance) {
                    return true; // The point is in shadow
                }
            }
        }

        // Check if the shadow ray intersects with the default plane
        plane = scene.getDefaultPlane();
        double t = plane.intersect(shadowRay);
        double lightDistance = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).getMagnitude();
        if (t > 0 && t < lightDistance) {
            return true; // The point is in shadow
        }

        return false; // The point is not in shadow
    }

    /**
     * Determines if a point is in shadow relative to a sphere.
     * @param intersectionPoint The point to check for shadow.
     * @param sphere The sphere to test for shadow.
     * @return True if the point is in shadow, false otherwise.
     */
    private boolean isInShadow(Vector intersectionPoint, Sphere sphere) {
        Vector lightSourcePos = scene.getLight().getPosition();
        Vector shadowRayDirection = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).normalize();

        // Slightly offset the origin to avoid self-shadowing
        Vector shadowRayOrigin = VectorCalculations.addVectors(intersectionPoint, VectorCalculations.multiplyVectorTo(shadowRayDirection, 0.001));
        Ray shadowRay = new Ray(shadowRayOrigin, shadowRayDirection);

        // Check if the shadow ray intersects with any spheres
        for (Sphere obj : scene.getObjects()) {
            if (obj.equals(sphere)) {
                continue; // Skip the sphere itself
            }

            Vector oc = VectorCalculations.subtractVectors(shadowRay.getOrigin(), obj.getCenter());
            double A = VectorCalculations.calculateDotProduct(shadowRay.getDirection(), shadowRay.getDirection());
            double B = 2.0 * VectorCalculations.calculateDotProduct(oc, shadowRay.getDirection());
            double C = VectorCalculations.calculateDotProduct(oc, oc) - obj.getRadius() * obj.getRadius();
            double discriminant = B * B - 4 * A * C;

            if (discriminant > 0) {
                double sqrtDiscriminant = Math.sqrt(discriminant);
                double t1 = (-B - sqrtDiscriminant) / (2.0 * A);
                double t2 = (-B + sqrtDiscriminant) / (2.0 * A);
                double t = Math.min(t1, t2);

                double lightDistance = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).getMagnitude();
                if (t > 0 && t < lightDistance) {
                    return true; // The point is in shadow
                }
            }
        }

        return false; // The point is not in shadow
    }
}
