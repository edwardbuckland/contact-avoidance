package graph.algorithm;

import java.util.stream.*;

public class SpectralCluster {
  public static int[] spectralCluster(double[][] similarity_matrix) {
    int n = similarity_matrix.length;

    double[] degree = new double[n];

    for (int i = 0; i < n; i++) {
      similarity_matrix[i][i] = 0;
      degree[i] = DoubleStream.of(similarity_matrix[i]).sum();
    }

    for (int i = 0; i < n; i ++)
      for (int j = 0; j < n; j++)
        similarity_matrix[i][j] = i == j? degree[i]: - similarity_matrix[i][j];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++)
        System.out.print(similarity_matrix[i][j] + " ");
      System.out.println();
    }

    int[] partitions = new int[n];

    return partitions;
  }
}