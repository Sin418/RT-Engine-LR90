import Objects.Shapes.Sphere;
import Objects.lights.Light;
import java.util.ArrayList;

public class Scene {
    private ArrayList<Sphere> objects = new ArrayList<>();
    private Camera camera;
    private Light light;
    private static Plane defaultPlane;

    public Scene(Camera camera, Light light, Plane defaultPlane) {
        this.camera = camera;
        this.light = light;
        this.defaultPlane = defaultPlane;
    }

    public void addObject(Sphere item) {
        objects.add(item);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public Light getLight() {
        return light;
    }

    public Camera getCamera() {
        return camera;
    }

    public ArrayList<Sphere> getObjects() {
        return objects;
    }

    public static Plane getDefaultPlane() {
        return defaultPlane;
    }
}
