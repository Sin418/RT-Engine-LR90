import Objects.Shapes.Sphere;
import java.util.ArrayList;

public class Scene {
    private ArrayList<Sphere> objects = new ArrayList<>();
    private Camera camera;

    public Scene(Camera camera){
        this.camera = camera;
    }

    public void addObject(Sphere item){
        objects.add(item);
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }

    public Camera getCamera(){
        return this.camera;
    }

    public ArrayList<Sphere> getObjects(){
        return objects;
    }
}
