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
