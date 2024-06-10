package Vectors;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

public class Vector {
    private List<Double> direction;

    public Vector(List<Double> direction) {
        this.direction = new ArrayList<>(direction);
    }

    public int getLength(){
        int count = 0;

        for(Double i: this.direction){
            count++;
        }
        return count;
    }

    public List<Double> getDirection() {
        return direction;
    }

    public void setDirection(List<Double> direction) {
        this.direction = direction;
    }

    public double getMagnuitude(){
        double count = 0.0;

        for(double i:this.direction){
            count+=i;
        }

        return Math.sqrt(count);
    }

    @Override
    public String toString() {
        return this.getDirection().toString();
    }
}
