import Vectors.Vector;
import Vectors.VectorCalculations;

public class Ray {
    private final Vector origin;
    private final Vector direction;

    private  Vector changedVector;
    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;

    }

    public Vector pointAtParameter(double t){
        Vector scaledDirection = VectorCalculations.multiplyVectorTo(direction,t);
        return VectorCalculations.addVectors(origin,scaledDirection);
    }

}
