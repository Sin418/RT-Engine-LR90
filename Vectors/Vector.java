package Vectors;
import java.util.List;
import java.util.ArrayList;

public class Vector {
    private List<Double> direction;

    public Vector(List<Double> direction) {
        this.direction = new ArrayList<>(direction);
    }

    public int getLength(){
        return direction.size();
    }

    public List<Double> getDirection() {
        return direction;
    }

    public void setDirection(List<Double> direction) {
        this.direction = direction;
    }

    public double getMagnitude() {
        double sum = 0.0;
        for (double component : direction) {
            sum += component * component;
        }
        return Math.sqrt(sum);
    }

    public Vector normalize() {
        double magnitude = getMagnitude();
        List<Double> normalizedComponents = new ArrayList<>();
        for (double component : direction) {
            normalizedComponents.add(component / magnitude);
        }
        return new Vector(normalizedComponents);
    }

    public String print() {
        return direction.toString();
    }
}
