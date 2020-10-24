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

package graph.algorithm;

import static java.lang.Math.*;

public class GramSchmidt {
  public static double[][] orthonormalise(double[][] matrix) {
    int n = matrix.length;
    double[][] orthonormal = new double[n][n];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++)
        orthonormal[j][i] = matrix[j][i];

      for (int k = 0; k < i; k++) {
        double dot = 0;

        for (int j = 0; j < n; j++)
          dot += matrix[j][i]*orthonormal[j][k];

        for (int j = 0; j < n; j++)
          orthonormal[j][i] -= dot*orthonormal[j][k];
      }

      double norm = 0;

      for (int j = 0; j < n; j++)
        norm += pow(orthonormal[j][i], 2);

      if (norm > 1e-6) {
        norm = sqrt(norm);

        for (int j = 0; j < n; j++)
          orthonormal[j][i] /= norm;
      }
    }

    return orthonormal;
  }
}
