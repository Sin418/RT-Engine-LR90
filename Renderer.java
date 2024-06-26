import Objects.Shapes.Sphere;
import java.awt.*;
import java.awt.image.BufferedImage;

import Materials.Material;
import Objects.lights.Light;
import Vectors.Vector;
import Vectors.VectorCalculations;

public class Renderer {
    private Scene scene;

    public Renderer(Scene scene) {
        this.scene = scene;
    }

    public void render(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double u = (x + 0.5) / width;
                double v = (y + 0.5) / height;
                Ray ray = scene.getCamera().getRay(u, v);
                Color color = traceRay(ray);
                image.setRGB(x, y, color.getRGB());
            }
        }
    }

    public Color traceRay(Ray ray) {
    double closestIntersectionDistance = Double.MAX_VALUE;
    Color closestObjectColor = Color.white;

    // Check intersection with light source sphere
    Sphere lightSphere = new Sphere(scene.getLight().getPosition(), 0.1, new Material(Color.blue)); // Small sphere with blue color
    Vector oc = VectorCalculations.subtractVectors(ray.getOrigin(), lightSphere.getCenter());
    double A = VectorCalculations.calculateDotProduct(ray.getDirection(), ray.getDirection());
    double B = 2.0 * VectorCalculations.calculateDotProduct(oc, ray.getDirection());
    double C = VectorCalculations.calculateDotProduct(oc, oc) - lightSphere.getRadius() * lightSphere.getRadius();
    double discriminant = B * B - 4 * A * C;

    if (discriminant > 0) {
        double t1 = (-B - Math.sqrt(discriminant)) / (2.0 * A);
        double t2 = (-B + Math.sqrt(discriminant)) / (2.0 * A);
        double t = Math.min(t1, t2);

        if (t > 0 && t < closestIntersectionDistance) {
            closestIntersectionDistance = t;
            closestObjectColor = lightSphere.getMaterial().getColor();
        }
    }

    Plane plane = scene.getDefaultPlane();
    double planeIntersectionDistance = plane.intersect(ray);
    if (planeIntersectionDistance < closestIntersectionDistance) {
        closestIntersectionDistance = planeIntersectionDistance;
        Vector intersectionPoint = VectorCalculations.addVectors(ray.getOrigin(), VectorCalculations.multiplyVectorTo(ray.getDirection(), planeIntersectionDistance));

        if (isInShadow(intersectionPoint, plane)) {
            closestObjectColor = Color.black;
        } else {
            closestObjectColor = plane.getColor();
        }
    }

    for (Sphere sphere : scene.getObjects()) {
        oc = VectorCalculations.subtractVectors(ray.getOrigin(), sphere.getCenter());
        A = VectorCalculations.calculateDotProduct(ray.getDirection(), ray.getDirection());
        B = 2.0 * VectorCalculations.calculateDotProduct(oc, ray.getDirection());
        C = VectorCalculations.calculateDotProduct(oc, oc) - sphere.getRadius() * sphere.getRadius();
        discriminant = B * B - 4 * A * C;

        if (discriminant > 0) {
            double t1 = (-B - Math.sqrt(discriminant)) / (2.0 * A);
            double t2 = (-B + Math.sqrt(discriminant)) / (2.0 * A);
            double t = Math.min(t1, t2);

            if (t > 0 && t < closestIntersectionDistance) {
                closestIntersectionDistance = t;
                Vector intersectionPoint = VectorCalculations.addVectors(ray.getOrigin(), VectorCalculations.multiplyVectorTo(ray.getDirection(), t));

                if (isInShadow(intersectionPoint, sphere)) {
                    closestObjectColor = Color.black; 
                } else {
                    closestObjectColor = sphere.getMaterial().getColor();
                }
            }
        }
    }

    return closestObjectColor;
}

    
    private Color applyLuminanceDropOff(Color originalColor, Vector intersectionPoint) {
        Vector lightSourcePos = scene.getLight().getPosition();
        double distance = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).getMagnitude();
        double luminance = scene.getLight().getLuminance() / (distance * distance); 
        
        double luminanceFactor = 5.0; 
        luminance *= luminanceFactor;
        
        luminance = Math.max(0, Math.min(1, luminance)); 
        
        int red = (int) (originalColor.getRed() * luminance + 0.5);
        int green = (int) (originalColor.getGreen() * luminance + 0.5);
        int blue = (int) (originalColor.getBlue() * luminance + 0.5);
        
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));
        
        return new Color(red, green, blue);
    }
    
    

    private boolean isInShadow(Vector intersectionPoint, Plane plane) {
        Vector lightSourcePos = scene.getLight().getPosition();
        Vector shadowRayDirection = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).normalize();

        Vector shadowRayOrigin = VectorCalculations.addVectors(intersectionPoint, VectorCalculations.multiplyVectorTo(shadowRayDirection, 0.001));
        Ray shadowRay = new Ray(shadowRayOrigin, shadowRayDirection);

        for (Sphere obj : scene.getObjects()) {
            Vector oc = VectorCalculations.subtractVectors(shadowRay.getOrigin(), obj.getCenter());
            double A = VectorCalculations.calculateDotProduct(shadowRay.getDirection(), shadowRay.getDirection());
            double B = 2.0 * VectorCalculations.calculateDotProduct(oc, shadowRay.getDirection());
            double C = VectorCalculations.calculateDotProduct(oc, oc) - obj.getRadius() * obj.getRadius();
            double discriminant = B * B - 4 * A * C;

            if (discriminant > 0) {
                double t1 = (-B - Math.sqrt(discriminant)) / (2.0 * A);
                double t2 = (-B + Math.sqrt(discriminant)) / (2.0 * A);
                double t = Math.min(t1, t2);

                double lightDistance = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).getMagnitude();
                if (t > 0 && t < lightDistance) {
                    return true;
                }
            }
        }

        plane = Scene.getDefaultPlane();
        double t = plane.intersect(shadowRay);
        double lightDistance = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).getMagnitude();
        if (t > 0 && t < lightDistance) {
            return true;
        }
            
        

        return false;
    }

    private boolean isInShadow(Vector intersectionPoint, Sphere sphere) {
        Vector lightSourcePos = scene.getLight().getPosition();
        Vector shadowRayDirection = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).normalize();

        Vector shadowRayOrigin = VectorCalculations.addVectors(intersectionPoint, VectorCalculations.multiplyVectorTo(shadowRayDirection, 0.001));
        Ray shadowRay = new Ray(shadowRayOrigin, shadowRayDirection);

        for (Sphere obj : scene.getObjects()) {
            Vector oc = VectorCalculations.subtractVectors(shadowRay.getOrigin(), obj.getCenter());
            double A = VectorCalculations.calculateDotProduct(shadowRay.getDirection(), shadowRay.getDirection());
            double B = 2.0 * VectorCalculations.calculateDotProduct(oc, shadowRay.getDirection());
            double C = VectorCalculations.calculateDotProduct(oc, oc) - obj.getRadius() * obj.getRadius();
            double discriminant = B * B - 4 * A * C;

            if (discriminant > 0) {
                double t1 = (-B - Math.sqrt(discriminant)) / (2.0 * A);
                double t2 = (-B + Math.sqrt(discriminant)) / (2.0 * A);
                double t = Math.min(t1, t2);

                double lightDistance = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).getMagnitude();
                if (t > 0 && t < lightDistance) {
                    return true;
                }
            }
        }

        return false;
    }
}
