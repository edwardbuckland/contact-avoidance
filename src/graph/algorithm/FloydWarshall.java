package graph.algorithm;

import java.util.Map.*;

import graph.*;

public class FloydWarshall {
  public static double[][] floydWarshall(Graph graph) {
    int n = graph.size();

    double[][] distances = new double[n][n];

    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        distances[i][j] = i == j? 0: Double.POSITIVE_INFINITY;

    for (int i = 0; i < n; i++)
      for (Entry<Node, Double> edge: graph.get(i).edges.entrySet())
        distances[i][graph.indexOf(edge.getKey())] = edge.getValue();

    for (int k = 0; k < n; k++)
      for (int i = 0; i < n; i++)
        for (int j = 0; j <n; j++)
          distances[i][j] = Math.min(distances[i][j], distances[i][k] + distances[k][j]);

    return distances;
  }
}
