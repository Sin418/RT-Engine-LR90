import Vectors.Vector;
import Vectors.VectorCalculations;

public class Ray {
    private Vector origin;
    private  Vector direction;

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector pointAtParameter(double t){
        Vector scaledDirection = VectorCalculations.multiplyVectorTo(direction,t);
        return VectorCalculations.addVectors(origin,scaledDirection);
    }

}
