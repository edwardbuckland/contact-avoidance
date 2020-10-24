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

public class QRSpectralDecomposition {
  public static double[] decompose(double[][] matrix) {
    int n = matrix.length;
    double[][] P = new double[n][n];
    double[][] A = new double[n][n];

    for (int i = 0; i < n; i++) {
      P[i][i] = 1;

      for (int j = 0; j < n; j++)
        A[i][j] = matrix[i][j];
    }

    for (int k = 0; k < 20; k++) {
      double[][] Q = GramSchmidt.orthonormalise(A);

      A = product(product(Q, A, true), Q, false);
      P = product(P, Q, false);
    }

    double[] eigenvalues = new double[n];

    for (int i = 0; i < n; i++) {
      eigenvalues[i] = A[i][i];

      for (int j = 0; j < n; j++)
        matrix[i][j] = P[i][j];
    }

    return eigenvalues;
  }

  private static double[][] product(double[][] first, double[][] second, boolean transpose) {
    int n = first.length;
    double[][] product = new double[n][n];

    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        for (int k = 0; k < n; k++)
          product[i][j] += (transpose? first[k][i]: first[i][k])*second[k][j];

    return product;
  }
}
