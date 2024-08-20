package Vectors;

import java.util.ArrayList;

/**
 * A utility class for performing various vector operations.
 * This class provides methods to perform dot products, cross products, and vector arithmetic.
 */
public class VectorCalculations {

    /**
     * Calculates the dot product of two vectors.
     * @param vector1 The first vector.
     * @param vector2 The second vector.
     * @return The dot product of the two vectors.
     * @throws IllegalArgumentException if the vectors are not of the same size.
     */
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

    /**
     * Adds two vectors component-wise.
     * @param vector1 The first vector.
     * @param vector2 The second vector.
     * @return A new vector representing the sum of the two vectors.
     * @throws IllegalArgumentException if the vectors are not of the same size.
     */
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

    /**
     * Subtracts the second vector from the first vector component-wise.
     * @param vector1 The first vector.
     * @param vector2 The second vector to subtract.
     * @return A new vector representing the difference between the two vectors.
     * @throws IllegalArgumentException if the vectors are not of the same size.
     */
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

    /**
     * Multiplies each component of the vector by a scalar.
     * @param vector The vector to scale.
     * @param scalar The scalar to multiply with.
     * @return A new vector representing the scaled vector.
     */
    public static Vector multiplyVectorByScalar(Vector vector, double scalar) {
        ArrayList<Double> newComponents = new ArrayList<>();
        for (double component : vector.getDirection()) {
            newComponents.add(component * scalar);
        }
        return new Vector(newComponents);
    }

    /**
     * Calculates the cross product of two 3-dimensional vectors.
     * @param vector1 The first vector.
     * @param vector2 The second vector.
     * @return A new vector representing the cross product.
     * @throws IllegalArgumentException if either vector is not 3-dimensional.
     */
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

    /**
     * Normalizes a vector to a unit vector.
     * @param vector The vector to normalize.
     * @return A new vector representing the normalized vector.
     */
    public static Vector normalize(Vector vector) {
        double magnitude = calculateMagnitude(vector);
        if (magnitude == 0) {
            throw new IllegalArgumentException("Cannot normalize a zero vector.");
        }

        return multiplyVectorByScalar(vector, 1.0 / magnitude);
    }

    /**
     * Calculates the magnitude of a vector.
     * @param vector The vector to calculate the magnitude of.
     * @return The magnitude of the vector.
     */
    public static double calculateMagnitude(Vector vector) {
        double sumOfSquares = 0.0;
        for (double component : vector.getDirection()) {
            sumOfSquares += component * component;
        }
        return Math.sqrt(sumOfSquares);
    }

    /**
     * Computes the angle between two vectors.
     * @param vector1 The first vector.
     * @param vector2 The second vector.
     * @return The angle in radians between the two vectors.
     */
    public static double calculateAngleBetweenVectors(Vector vector1, Vector vector2) {
        double dotProduct = calculateDotProduct(vector1, vector2);
        double magnitude1 = calculateMagnitude(vector1);
        double magnitude2 = calculateMagnitude(vector2);

        if (magnitude1 == 0 || magnitude2 == 0) {
            throw new IllegalArgumentException("Cannot calculate angle with a zero vector.");
        }

        return Math.acos(dotProduct / (magnitude1 * magnitude2));
    }

    /**
     * Projects one vector onto another vector.
     * @param vector1 The vector to project.
     * @param vector2 The vector to project onto.
     * @return A new vector representing the projection of vector1 onto vector2.
     */
    public static Vector projectVectorOnto(Vector vector1, Vector vector2) {
        double dotProduct = calculateDotProduct(vector1, vector2);
        double magnitudeSquared = calculateMagnitude(vector2) * calculateMagnitude(vector2);

        if (magnitudeSquared == 0) {
            throw new IllegalArgumentException("Cannot project onto a zero vector.");
        }

        double scalar = dotProduct / magnitudeSquared;
        return multiplyVectorByScalar(vector2, scalar);
    }

    /**
     * Reflects a vector around a normal vector.
     * @param vector The vector to reflect.
     * @param normal The normal vector.
     * @return A new vector representing the reflection of the original vector around the normal vector.
     */
    public static Vector reflectVector(Vector vector, Vector normal) {
        Vector normalizedNormal = normalize(normal);
        double dotProduct = calculateDotProduct(vector, normalizedNormal);
        return subtractVectors(vector, multiplyVectorByScalar(normalizedNormal, 2 * dotProduct));
    }
}
