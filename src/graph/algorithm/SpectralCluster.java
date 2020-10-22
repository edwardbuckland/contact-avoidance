package graph.algorithm;

import static java.lang.Math.*;
import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.AbstractMap.*;
import java.util.Map.*;
import java.util.stream.*;

public class SpectralCluster {
  public static int[] spectralCluster(double[][] similarity_matrix, int k) {
    int n = similarity_matrix.length;

    double[] degree = new double[n];

    for (int i = 0; i < n; i++) {
      similarity_matrix[i][i] = 0;
      degree[i] = DoubleStream.of(similarity_matrix[i]).sum();
    }

    for (int i = 0; i < n; i ++)
      for (int j = 0; j < n; j++)
        similarity_matrix[i][j] = i == j? degree[i]: - similarity_matrix[i][j];

    double[] eigenvalues = QRSpectralDecomposition.decompose(similarity_matrix);

    List<Integer> indexes = IntStream.range(0, n)
                                     .mapToObj(i ->  new SimpleEntry<>(eigenvalues[i], i))
                                     .sorted((first, second) -> (int)signum(first.getKey() - second.getKey()))
                                     .map(Entry::getValue)
                                     .limit(k)
                                     .collect(toList());

    for (int i = 0; i < n; i++) {
      final int final_i = i;
      similarity_matrix[i] = indexes.stream()
                                    .mapToDouble(index -> similarity_matrix[final_i][index])
                                    .toArray();
    }

    return KMeans.cluster(similarity_matrix, k);
  }
}
