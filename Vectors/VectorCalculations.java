package Vectors;
import java.util.ArrayList;


public class VectorCalculations {
    // Assuming that the 2 vectors are the same dimensions -> have to implement different stuff later
    public static double calculateDotProduct(Vector vector1, Vector vector2) {
        if (vector1.getLength() != vector2.getLength()) {
            throw new IllegalArgumentException("Vectors must be of the same size.");
        }

        double dotProduct = 0.0;
        for (int i = 0; i < vector1.getLength(); i++) {
            dotProduct += vector1.getDirection().get(i) * vector2.getDirection().get(i);
        }

        return dotProduct;
    }

    public static Vector addVectors(Vector vector1, Vector vector2){
        ArrayList<Double> newArray = new ArrayList<>();
        for (int i = 0; i < vector1.getLength(); i++) {
            double temp = 0.0;
            temp = vector1.getDirection().get(i) + vector2.getDirection().get(i);
            newArray.add(temp);
        }
        Vector newVector = new Vector(newArray);

        return newVector;
    }

    public static Vector subtractVectors(Vector vector1, Vector vector2){
        ArrayList<Double> newArray = new ArrayList<>();
        for (int i = 0; i < vector1.getLength(); i++) {
            double temp = 0.0;
            temp = vector1.getDirection().get(i) - vector2.getDirection().get(i);
            newArray.add(temp);
        }
        Vector newVector = new Vector(newArray);

        return newVector;
    }

    public static Vector multiplyVectorTo(Vector vector1, double scalar){
        ArrayList<Double> newArray = new ArrayList<>();
        for (int i = 0; i < vector1.getLength(); i++) {
            double temp = 0.0;
            temp = vector1.getDirection().get(i) * scalar;
            newArray.add(temp);
        }
        Vector newVector = new Vector(newArray);

        return newVector;
    }

}
