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
