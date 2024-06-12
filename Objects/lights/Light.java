package Objects.lights;
import Vectors.Vector;

public class Light {
    private Vector position;

    public Light(Vector position) {
        this.position = position;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }
}
