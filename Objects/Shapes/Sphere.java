package Objects.Shapes;
import Materials.Material;
import Vectors.Vector;
public class Sphere {
    private double radius;
    private Vector center;
    private Material material;

    public double getRadius(){
        return radius;
    }
    public Vector getCenter(){
        return center;
    }

    public Material getMaterial(){
        return material;
    }
}
