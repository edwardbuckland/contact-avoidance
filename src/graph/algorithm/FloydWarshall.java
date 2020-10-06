package graph.algorithm;

import static graph.Graph.*;

import java.util.Map.*;

public class FloydWarshall {
  public static double[][] floydWarshall() {
    int n = nodes.size();

    double[][] distances = new double[n][n];

    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        distances[i][j] = i == j? 0: Double.POSITIVE_INFINITY;

    for (int i = 0; i < n; i++)
      for (Entry<Node, Double> edge: nodes.get(i).edges.entrySet())
        distances[i][nodes.indexOf(edge.getKey())] = edge.getValue();

    for (int k = 0; k < n; k++)
      for (int i = 0; i < n; i++)
        for (int j = 0; j <n; j++)
          distances[i][j] = Math.min(distances[i][j], distances[i][k] + distances[k][j]);

    return distances;
  }
}
