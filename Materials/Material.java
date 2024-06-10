package Materials;
import java.awt.*;

public class Material {
    // hex code
    private Color color;
    private double reflectiveStrength;

    public Material(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }
}
