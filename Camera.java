import Vectors.Vector;
import Vectors.VectorCalculations;
public class Camera {
    private Vector origin;
    private Vector lowerLeftCorner;
    private Vector horizontal;
    private Vector vertical;

    public  Camera(Vector origin, Vector lowerLeftCorner, Vector horizontal, Vector vertical){
        this.origin = origin;
        this.lowerLeftCorner = lowerLeftCorner;
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public Ray getRay(double u, double v){
        Vector horizontalOffset = VectorCalculations.multiplyVectorTo(horizontal, u);
        Vector verticalOffset = VectorCalculations.multiplyVectorTo(vertical, v);
        Vector pointOnViewport = VectorCalculations.addVectors(lowerLeftCorner, horizontalOffset);
        pointOnViewport = VectorCalculations.addVectors(pointOnViewport, verticalOffset);

        Vector direction = VectorCalculations.subtractVectors(pointOnViewport, origin);

        return new Ray(origin, direction);
    }
}
