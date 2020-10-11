package graphics;

import static java.lang.Math.*;

public class Transform {
  private static final double   VIEWPORT_SIZE   = 950;

  private static double         theta;
  private static double         phi             = -PI/4;
  private static Vector         position        = new Vector(50, 12, 50);

  public static Vector transform(Vector vector) {
    Vector camera = rotate(vector.mirrorY().plus(position), phi, theta);
    return camera.z > 0? camera.unit().times(VIEWPORT_SIZE): new Vector();
  }

  public static double distance(Vector vector) {
    return rotate(vector.mirrorY().plus(position), phi, theta).norm();
  }

  public static void translate(Vector vector) {
    position = position.plus(rotate(vector, -phi, 0));
  }

  public static void rotate(double d_theta, double d_phi) {
    theta += d_theta;
    theta = max(-PI/2, min(PI/2, theta));

    phi += d_phi;
  }

  private static Vector rotate(Vector vector, double phi, double theta) {
    return new Vector(new Vector(            cos(phi),          0,             sin(phi)).dot(vector),
                      new Vector( sin(theta)*sin(phi), cos(theta), -sin(theta)*cos(phi)).dot(vector),
                      new Vector(-cos(theta)*sin(phi), sin(theta),  cos(theta)*cos(phi)).dot(vector));
  }
}
