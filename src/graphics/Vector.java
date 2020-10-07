package graphics;

import static java.lang.Double.*;
import static java.lang.Math.*;

public class Vector {
  public double x;
  public double y;
  public double z;

  public Vector() {
    this(POSITIVE_INFINITY, POSITIVE_INFINITY);
  }

  public Vector(double x, double y) {
    this(x, y, NaN);
  }

  public Vector(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector plus(Vector vector) {
    return new Vector(x + vector.x, y + vector.y, z + vector.z);
  }

  public Vector minus(Vector vector) {
    return new Vector(x - vector.x, y - vector.y, z - vector.z);
  }

  public Vector times(double factor) {
    return new Vector(x*factor, y*factor, z*factor);
  }

  public Vector divide(double factor) {
    return new Vector(x/factor, y/factor, z/factor);
  }

  public double dot(Vector vector) {
    return x*vector.x + y*vector.y + z*vector.z;
  }

  public double norm() {
    return hypot(hypot(x, y), z == NaN? 0: z);
  }

  public Vector unit() {
    return norm() == 0? this: divide(norm());
  }

  public Vector copy() {
    return new Vector(x, y, z);
  }

  public Vector mirrorY() {
    return new Vector(x, -y, z);
  }
}
