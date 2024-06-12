import Vectors.Vector;
import Vectors.VectorCalculations;
import java.awt.Color;

public class Plane {
    private Vector normal;
    private Vector point;
    private Color color;

    public Plane(Vector normal, Vector point, Color color) {
        this.normal = normal.normalize();
        this.point = point;
        this.color = color;
    }

    public Vector getNormal() {
        return normal;
    }

    public Vector getPoint() {
        return point;
    }

    public Color getColor() {
        return color;
    }

    public double intersect(Ray ray) {
        double denominator = VectorCalculations.calculateDotProduct(normal, ray.getDirection());
        if (Math.abs(denominator) > 1e-6) { 
            Vector p0l0 = VectorCalculations.subtractVectors(point, ray.getOrigin());
            double t = VectorCalculations.calculateDotProduct(p0l0, normal) / denominator;
            return t >= 0 ? t : Double.MAX_VALUE; 
        }
        return Double.MAX_VALUE;
    }
}
