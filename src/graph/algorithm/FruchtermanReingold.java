package graph.algorithm;

import static graph.Graph.*;
import static graph.bipartite.Activity.*;
import static java.lang.Math.*;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import javax.swing.Timer;

import graph.Graph.*;
import graphics.Vector;
import gui.admin.*;

public class FruchtermanReingold {
  public static AtomicInteger   attraction  = new AtomicInteger(50);

  public static Timer           timer       = new Timer(20, event ->
  {
    Map<Node, Vector> displacement = new HashMap<>();
    BiConsumer<Node, Vector> displace = (node, delta) -> displacement.put(node, displacement.get(node).plus(delta));

    // repel pairs
    for (Node first: nodes) {
      displacement.put(first, new Vector(random()*1e-4, 0, random()*1e-4));

      nodes.stream()
           .filter(second -> !second.edges.isEmpty() && second.location.minus(first.location).norm() > 1e-4)
           .forEach(second -> {
             Vector delta = first.location.minus(second.location);
             displace.accept(first, delta.unit().times(min(1/delta.norm()/attractionRatio(), 0.1)));
           });
    }

    // attract along edges
    for (Node first: nodes)
      for (Node second: first.edges.keySet()) {
        Vector delta = first.location.minus(second.location);
        Vector scaled_delta = delta.times(min(delta.norm()*attractionRatio(), 0.1));
        displace.accept(first,  scaled_delta.times(-1));
        displace.accept(second, scaled_delta);
      }

    activities.stream()
              .filter(node -> !node.selected && !node.edges.isEmpty())
              .forEach(node -> {
                Vector delta = displacement.get(node);
                delta.y = 0;
                node.location = node.location.plus(delta.times(2));
              });

    View.view.repaint();
  });

  private static double attractionRatio() {
    return pow(attraction.get()/50.0, 10)/80*sqrt(nodes.size()) + 1e-5;
  }
}
