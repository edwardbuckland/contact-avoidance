package graph;

import static gui.View.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;

import graph.algorithm.*;
import graphics.Vector;
import gui.*;

public class Graph {
  public static List<Node> nodes = new ArrayList<>();

  public Node selectedNode;

  public DoubleSummaryStatistics statistics;

  public void calculate() {
    statistics = Arrays.stream(FloydWarshall.floydWarshall())
                .flatMapToDouble(Arrays::stream)
                .filter(Double::isFinite)
                .boxed()
                .collect(Collectors.summarizingDouble(Double::doubleValue));
  }

  public double coefficient() {
    return statistics.getAverage()/statistics.getCount()*nodes.size()*(nodes.size() - 1);
  }

  public static class Node implements Drawable {
    public Vector location;
    public Map<Node, Double> edges = new HashMap<>();

    public Node(Vector location) {
      this.location = location;

      nodes.add(this);
    }

    @Override
    public void draw() {
      edges.entrySet()
           .forEach(entry -> drawArrow(location, entry.getKey().location));
      drawPoint(location, 200, Color.green);
    }
  }
}
