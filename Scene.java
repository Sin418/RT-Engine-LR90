import Objects.Shapes.Sphere;
import Objects.lights.Light;

import java.util.ArrayList;

public class Scene {
    private ArrayList<Sphere> objects = new ArrayList<>();
    private Camera camera;
    private Light light;

    public Scene(Camera camera, Light light){
        this.camera = camera;
        this.light= light;
    }



    public void addObject(Sphere item){
        objects.add(item);
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }
    public void setLight(Light light){
        this.light = light;
    }

    public Light getLight(){
        return this.light;
    }

    public Camera getCamera(){
        return this.camera;
    }

    public ArrayList<Sphere> getObjects(){
        return objects;
    }
}
