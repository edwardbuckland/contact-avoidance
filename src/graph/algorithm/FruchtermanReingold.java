package graph.algorithm;

import static graph.Graph.*;
import static java.lang.Math.*;

import java.util.*;
import java.util.concurrent.atomic.*;

import javax.swing.Timer;

import graph.Graph.*;
import graph.bipartite.*;
import graphics.Vector;
import gui.admin.*;

public class FruchtermanReingold {
  public static AtomicInteger attraction = new AtomicInteger(50);

  public static Timer timer = new Timer(20, event ->
  {
    Map<Node, Vector> displacement = new HashMap<>();
    double k = sqrt(5.0/nodes.size());

    // repel pairs
    for (Node first: nodes) {
      displacement.put(first, new Vector(0, 0, 0));

      for (Node second: nodes) {
        if (first != second) {
          Vector delta = first.location.minus(second.location);
          displacement.put(first, displacement.get(first).plus(delta.unit().times(k*k/(delta.norm() + 1e-4))));
        }
      }
    }

    // attract along edges
    for (Node first: nodes)
      for (Node second: first.edges.keySet()) {
        Vector delta = first.location.minus(second.location);
        Vector scaled_delta = delta.times(delta.norm()/k*pow(attraction.get()/100.0, 3)/100.0);
        displacement.put(first,  displacement.get(first ).minus(scaled_delta));
        displacement.put(second, displacement.get(second).plus (scaled_delta));
      }

    nodes.stream()
         .filter(Activity.class::isInstance)
         .filter(node -> !node.selected)
         .forEach(node -> {
           Vector delta = displacement.get(node);
           delta.y = 0;
           node.location = node.location.plus(delta);
         });

    View.view.repaint();
  });
}
