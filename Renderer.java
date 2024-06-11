import Objects.Shapes.Sphere;

import java.awt.*;
import java.awt.image.BufferedImage;

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

        for (Sphere sphere : scene.getObjects()) {
            Vector oc = VectorCalculations.subtractVectors(ray.getOrigin(), sphere.getCenter());
            double A = VectorCalculations.calculateDotProduct(ray.getDirection(), ray.getDirection());
            double B = 2.0 * VectorCalculations.calculateDotProduct(oc, ray.getDirection());
            double C = VectorCalculations.calculateDotProduct(oc, oc) - sphere.getRadius() * sphere.getRadius();
            double discriminant = B * B - 4 * A * C;

            if (discriminant > 0) {
                double t1 = (-B - Math.sqrt(discriminant)) / (2.0 * A);
                double t2 = (-B + Math.sqrt(discriminant)) / (2.0 * A);
                double t = Math.min(t1, t2);

                if (t > 0 && t < closestIntersectionDistance) {
                    closestIntersectionDistance = t;
                    Vector intersectionPoint = VectorCalculations.addVectors(ray.getOrigin(), VectorCalculations.multiplyVectorTo(ray.getDirection(), t));

                    if (isInShadow(intersectionPoint, sphere)) {
                        closestObjectColor = Color.black; // Shadow color
                        System.out.println("is In shadow at: " + intersectionPoint);
                    } else {
                        closestObjectColor = sphere.getMaterial().getColor();
                        System.out.println("Not in shadow at: " + intersectionPoint);
                    }
                }
            }
        }
        return closestObjectColor;
    }




    private boolean isInShadow(Vector intersectionPoint, Sphere sphere) {
        Vector lightSourcePos = scene.getLight().getPosition();
        Vector shadowRayDirection = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint);
        Ray shadowRay = new Ray(intersectionPoint, shadowRayDirection);

        for (Sphere obj : scene.getObjects()) {
            if (obj != sphere) {
                Vector oc = VectorCalculations.subtractVectors(shadowRay.getOrigin(), obj.getCenter());
                double A = VectorCalculations.calculateDotProduct(shadowRay.getDirection(), shadowRay.getDirection());
                double B = 2.0 * VectorCalculations.calculateDotProduct(oc, shadowRay.getDirection());
                double C = VectorCalculations.calculateDotProduct(oc, oc) - obj.getRadius() * obj.getRadius();
                double discriminant = B * B - 4 * A * C;

                if (discriminant > 0) {
                    double t1 = (-B - Math.sqrt(discriminant)) / (2.0 * A);
                    double t2 = (-B + Math.sqrt(discriminant)) / (2.0 * A);
                    double t = Math.min(t1, t2);

                    // Check if the intersection is between the intersection point and the light source
                    double lightDistance = VectorCalculations.subtractVectors(lightSourcePos, intersectionPoint).getMagnuitude();
                    if (t > 0 && t < lightDistance) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



}
