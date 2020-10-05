package graphics;

import static java.lang.Math.*;

public class Transform {
  private static double theta = 0;
  private static double phi = 0;
  private static Vector position = new Vector(0, 0, 100);

  public static Vector transform(Vector vector) {
    vector = vector.plus(position);
    Vector camera = new Vector(new Vector(            cos(phi), 0,                      sin(phi)).dot(vector),
                               new Vector( sin(theta)*sin(phi), cos(theta), -sin(theta)*cos(phi)).dot(vector),
                               new Vector(-cos(theta)*sin(phi), sin(theta),  cos(theta)*cos(phi)).dot(vector));
    return camera.z > 0? camera.divide(camera.z/1000): new Vector();
  }

  public static void translate(Vector vector) {
    position = position.plus(new Vector(new Vector(           cos(phi), 0,                     -sin(phi)).dot(vector),
                                        new Vector(sin(theta)*sin(phi),  cos(theta), sin(theta)*cos(phi)).dot(vector),
                                        new Vector(cos(theta)*sin(phi), -sin(theta), cos(theta)*cos(phi)).dot(vector)));
  }

  public static void rotate(double d_theta, double d_phi) {
    theta += d_theta;
    theta = max(-PI/2, min(PI/2, theta));

    phi += d_phi;
  }
}
