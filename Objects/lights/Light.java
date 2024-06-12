package Objects.lights;
import Vectors.Vector;

public class Light {
    private Vector position;
    private double luminance;

    public Light(Vector position, double luminance) {
        this.position = position;
        this.luminance = luminance;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }
    
    public double getLuminance() {
        return luminance;
    }
}
