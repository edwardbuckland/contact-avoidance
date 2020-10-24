/*
 *  Copyright (C) 2020 Edward Buckland. Some rights reserved.
 *
 *  This file is part of "Contact Avoidance".
 *
 *  "Contact Avoidance" is distributed under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the License, or (at your option)
 *  any later version.
 *
 *  "Contact Avoidance" is a demonstration application only and is therefore not intended for
 *  general use. "Contact Avoidance" is distributed WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 *  You should have received a copy of the GNU Affero General Public License along with "Contact
 *  Avoidance". If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html>.
 */

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
