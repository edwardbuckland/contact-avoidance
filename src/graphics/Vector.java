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

import static java.lang.Double.*;
import static java.lang.Math.*;

public class Vector {
  public double     x;
  public double     y;
  public double     z;

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
