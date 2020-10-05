package graph;

import java.util.*;
import java.util.stream.*;

import graph.algorithm.*;

public class Graph extends ArrayList<Node> {
  private static final long serialVersionUID = -6217573811647043491L;

  public static Graph graph = new Graph();

  public Node selectedNode;

  public DoubleSummaryStatistics statistics;

  public void calculate() {
    statistics = Arrays.stream(FloydWarshall.floydWarshall(this))
                .flatMapToDouble(Arrays::stream)
                .filter(Double::isFinite)
                .boxed()
                .collect(Collectors.summarizingDouble(Double::doubleValue));
  }

  public double coefficient() {
    return statistics.getAverage()/statistics.getCount()*size()*(size() - 1);
  }
}
