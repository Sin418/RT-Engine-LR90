import Objects.lights.Light;
import Vectors.Vector;
import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import Materials.Material;
import Objects.Shapes.Sphere;

public class Main {
    public static void main(String[] args) {
        int width = 1920;
        int height = 1080;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Camera camera = new Camera(
                new Vector(List.of(0.0, 0.0, 0.0)),
                new Vector(List.of(-2.0, -1.5, -1.0)),
                new Vector(List.of(4.0, 0.0, 0.0)),
                new Vector(List.of(0.0, 3.0, 0.0))
        );

        Light light = new Light(new Vector(List.of(-3.0, -1.0, -2.0)),1.0);
        Plane groundPlane = new Plane(new Vector(List.of(0.0, 1.0, 0.0)), new Vector(List.of(0.0, -1.0, 0.0)), Color.gray);
        Scene scene = new Scene(camera, light, groundPlane);

        Material sphereMaterial = new Material(Color.green);
        Material sphereMaterial2 = new Material(Color.red);

        Sphere sphere = new Sphere(new Vector(List.of(1.0, -1.0, -2.0)), 1, sphereMaterial);
        Sphere sphere2 = new Sphere(new Vector(List.of(2.0, -1.5, -1.0)), 1, sphereMaterial2);

        scene.addObject(sphere);
        scene.addObject(sphere2);


        Renderer renderer = new Renderer(scene);
        renderer.render(image);

        JFrame frame = new JFrame("Ray Tracer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }
}
