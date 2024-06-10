import Objects.Shapes.Sphere;

import java.awt.*;
import java.awt.image.BufferedImage;
import Vectors.Vector;
import Vectors.VectorCalculations;

public class Renderer {
    private Scene scene;

    public Renderer(Scene scene){
        this.scene = scene;
    }

    public void render(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        for(int y=0;y<height;y++){
            for(int x=0; x<width;x++){
                double u = (x+0.5)/width;
                double v = (y+0.5)/height;
                Ray ray = scene.getCamera().getRay(u,v);
                Color color = traceRay(ray);
                image.setRGB(x,y,color.getRGB());
            }
        }
    }

    public Color traceRay(Ray ray){
        double closestIntersectionDistance = Double.MAX_VALUE;
        Color closestObjectColor = Color.black;

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
                    closestObjectColor = sphere.getMaterial().getColor();
                }
            }
        }

        return closestObjectColor;

    }


}
