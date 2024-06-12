package Vectors;

import java.util.ArrayList;

public class VectorCalculations {
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

    public static Vector addVectors(Vector vector1, Vector vector2) {
        if (vector1.getLength() != vector2.getLength()) {
            throw new IllegalArgumentException("Vectors must be of the same size.");
        }

        ArrayList<Double> newComponents = new ArrayList<>();
        for (int i = 0; i < vector1.getLength(); i++) {
            newComponents.add(vector1.getDirection().get(i) + vector2.getDirection().get(i));
        }
        return new Vector(newComponents);
    }

    public static Vector subtractVectors(Vector vector1, Vector vector2) {
        if (vector1.getLength() != vector2.getLength()) {
            throw new IllegalArgumentException("Vectors must be of the same size.");
        }

        ArrayList<Double> newComponents = new ArrayList<>();
        for (int i = 0; i < vector1.getLength(); i++) {
            newComponents.add(vector1.getDirection().get(i) - vector2.getDirection().get(i));
        }
        return new Vector(newComponents);
    }

    public static Vector multiplyVectorTo(Vector vector, double scalar) {
        ArrayList<Double> newComponents = new ArrayList<>();
        for (double component : vector.getDirection()) {
            newComponents.add(component * scalar);
        }
        return new Vector(newComponents);
    }

    public static Vector calculateCrossProduct(Vector vector1, Vector vector2) {
        if (vector1.getLength() != 3 || vector2.getLength() != 3) {
            throw new IllegalArgumentException("Cross product is defined only for 3-dimensional vectors.");
        }

        ArrayList<Double> newComponents = new ArrayList<>();
        double x1 = vector1.getDirection().get(0);
        double y1 = vector1.getDirection().get(1);
        double z1 = vector1.getDirection().get(2);

        double x2 = vector2.getDirection().get(0);
        double y2 = vector2.getDirection().get(1);
        double z2 = vector2.getDirection().get(2);

        double crossProductX = y1 * z2 - z1 * y2;
        double crossProductY = z1 * x2 - x1 * z2;
        double crossProductZ = x1 * y2 - y1 * x2;

        newComponents.add(crossProductX);
        newComponents.add(crossProductY);
        newComponents.add(crossProductZ);

        return new Vector(newComponents);
    }
}
