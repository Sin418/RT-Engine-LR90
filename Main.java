import Vectors.Vector;
import Vectors.VectorCalculations;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        ArrayList<Double> direction1 = new ArrayList<>();
        ArrayList<Double> direction2 = new ArrayList<>();

        direction1.add(1.0);
        direction1.add(2.0);
        direction1.add(3.0);

        direction2.add(4.0);
        direction2.add(5.0);
        direction2.add(6.0);

        Vector vector1 = new Vector(direction1);
        Vector vector2 = new Vector(direction2);

        double dotProduct = VectorCalculations.calculateDotProduct(vector1, vector2);

        Vector newVector = VectorCalculations.addVectors(vector1,vector2);

        System.out.println(dotProduct);
        System.out.println(newVector.toString() );
    }
}
