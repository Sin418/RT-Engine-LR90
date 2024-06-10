package Objects.Shapes;
import Materials.Material;
import Vectors.Vector;
public class Sphere {
    private double radius;
    private Vector center;
    private Material material;

    public Sphere(Vector center, double radius, Material material){
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

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
