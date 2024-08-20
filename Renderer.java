import Objects.Shapes.Sphere;
import java.awt.*;
import java.awt.image.BufferedImage;

import Materials.Material;
import Objects.lights.Light;
import Vectors.Vector;
import Vectors.VectorCalculations;
import Objects.Shapes.Plane;
import Objects.Shapes.Shape;

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
                Color color = traceRay(ray, 0); // The second parameter is the recursion depth for reflections/refractions
                // Set the pixel color in the BufferedImage
                image.setRGB(x, y, color.getRGB());
            }
        }
    }

    /**
     * Traces a ray to determine the color of the pixel it corresponds to.
     * @param ray The Ray object to trace.
     * @param depth The recursion depth for reflection/refraction.
     * @return The Color of the pixel corresponding to the ray.
     */
    public Color traceRay(Ray ray, int depth) {
        double closestIntersectionDistance = Double.MAX_VALUE;
        Color closestObjectColor = Color.black;
        Material closestMaterial = null;
        Vector closestIntersectionPoint = null;
        Shape closestShape = null;

        // Check intersections with all objects in the scene
        for (Shape shape : scene.getObjects()) {
            double t = shape.intersect(ray);
            if (t > 0 && t < closestIntersectionDistance) {
                closestIntersectionDistance = t;
                closestShape = shape;
                closestIntersectionPoint = VectorCalculations.addVectors(ray.getOrigin(), VectorCalculations.multiplyVectorTo(ray.getDirection(), t));
                closestMaterial = shape.getMaterial();
            }
        }

        if (closestShape == null) {
            return Color.black; // No intersection, return black
        }

        // Compute color at the intersection point
        Color baseColor = computeColorAtIntersection(ray, closestIntersectionPoint, closestMaterial);

        // Apply reflections and refractions if needed
        if (depth < scene.getMaxDepth()) {
            Color reflectionColor = computeReflection(ray, closestIntersectionPoint, closestMaterial, depth);
            Color refractionColor = computeRefraction(ray, closestIntersectionPoint, closestMaterial, depth);

            baseColor = blendColors(baseColor, reflectionColor, closestMaterial.getReflection());
            baseColor = blendColors(baseColor, refractionColor, closestMaterial.getRefraction());
        }

        return baseColor;
    }

    /**
     * Computes the color at the intersection point based on shading.
     * @param ray The Ray object used to trace.
     * @param intersectionPoint The point of intersection.
     * @param material The material of the intersected shape.
     * @return The base color computed at the intersection point.
     */
    private Color computeColorAtIntersection(Ray ray, Vector intersectionPoint, Material material) {
        Color color = Color.black;
        Vector normal = material.getNormal(intersectionPoint);
        Vector lightDir = VectorCalculations.subtractVectors(scene.getLight().getPosition(), intersectionPoint).normalize();
        Vector viewDir = VectorCalculations.subtractVectors(ray.getOrigin(), intersectionPoint).normalize();
        Vector reflectionDir = VectorCalculations.subtractVectors(VectorCalculations.multiplyVectorTo(normal, 2 * VectorCalculations.calculateDotProduct(normal, lightDir)), lightDir);

        double ambient = 0.1;
        double diffuse = Math.max(0, VectorCalculations.calculateDotProduct(normal, lightDir));
        double specular = Math.pow(Math.max(0, VectorCalculations.calculateDotProduct(viewDir, reflectionDir)), material.getShininess());

        Color ambientColor = new Color((int)(ambient * material.getColor().getRed()), (int)(ambient * material.getColor().getGreen()), (int)(ambient * material.getColor().getBlue()));
        Color diffuseColor = new Color((int)(diffuse * material.getColor().getRed()), (int)(diffuse * material.getColor().getGreen()), (int)(diffuse * material.getColor().getBlue()));
        Color specularColor = new Color((int)(specular * 255), (int)(specular * 255), (int)(specular * 255));

        color = blendColors(ambientColor, diffuseColor, 1.0);
        color = blendColors(color, specularColor, material.getSpecular());

        // Add ambient occlusion effect
        color = applyAmbientOcclusion(intersectionPoint, color);

        return color;
    }

    /**
     * Computes the reflection color of the scene.
     * @param ray The Ray object used to trace.
     * @param intersectionPoint The point of intersection.
     * @param material The material of the intersected shape.
     * @param depth The recursion depth.
     * @return The color resulting from reflection.
     */
    private Color computeReflection(Ray ray, Vector intersectionPoint, Material material, int depth) {
        Vector normal = material.getNormal(intersectionPoint);
        Vector reflectionDir = VectorCalculations.subtractVectors(VectorCalculations.multiplyVectorTo(normal, 2 * VectorCalculations.calculateDotProduct(normal, ray.getDirection())), ray.getDirection()).normalize();
        Ray reflectionRay = new Ray(intersectionPoint, reflectionDir);
        return traceRay(reflectionRay, depth + 1);
    }

    /**
     * Computes the refraction color of the scene.
     * @param ray The Ray object used to trace.
     * @param intersectionPoint The point of intersection.
     * @param material The material of the intersected shape.
     * @param depth The recursion depth.
     * @return The color resulting from refraction.
     */
    private Color computeRefraction(Ray ray, Vector intersectionPoint, Material material, int depth) {
        // Implement refraction calculation here
        // Placeholder: return a transparent color
        return new Color(255, 255, 255, 128); // Half-transparent color
    }

    /**
     * Blends two colors based on a weight factor.
     * @param baseColor The base color.
     * @param blendColor The color to blend.
     * @param factor The blend factor (0 to 1).
     * @return The blended color.
     */
    private Color blendColors(Color baseColor, Color blendColor, double factor) {
        int red = (int)(baseColor.getRed() * (1 - factor) + blendColor.getRed() * factor);
        int green = (int)(baseColor.getGreen() * (1 - factor) + blendColor.getGreen() * factor);
        int blue = (int)(baseColor.getBlue() * (1 - factor) + blendColor.getBlue() * factor);

        return new Color(Math.min(255, Math.max(0, red)), Math.min(255, Math.max(0, green)), Math.min(255, Math.max(0, blue)));
    }

    /**
     * Applies ambient occlusion effect to simulate soft shadows.
     * @param intersectionPoint The point of intersection.
     * @param baseColor The base color to adjust.
     * @return The color with ambient occlusion applied.
     */
    private Color applyAmbientOcclusion(Vector intersectionPoint, Color baseColor) {
        // Simple ambient occlusion simulation (e.g., darken the color)
        return new Color(
            (int)(baseColor.getRed() * 0.8),
            (int)(baseColor.getGreen() * 0.8),
            (int)(baseColor.getBlue() * 0.8)
        );
    }
}
