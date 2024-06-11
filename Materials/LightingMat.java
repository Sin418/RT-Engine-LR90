package Materials;

import java.awt.*;

public class LightingMat extends Material{

    private double intensity;

    public LightingMat(Color color) {
        super(color);
    }

    public void setIntensity(double intensity){
        this.intensity = intensity;
    }

    public double getIntensity(){
        return intensity;
    }



}
