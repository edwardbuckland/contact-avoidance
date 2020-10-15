package graph;

import static gui.admin.GraphView.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import graphics.Vector;
import gui.admin.*;

public abstract class Node implements Drawable {
  public static List<Node>    nodes       = new ArrayList<>();

  public Vector               location;
  public boolean              selected;

  public Map<Node, Double>    edges       = new HashMap<>();

  public Node(Vector location) {
    this.location = location;

    nodes.add(this);
  }

  @Override
  public void draw() {
    edges.entrySet()
         .forEach(entry -> drawArrow(location, entry.getKey().location));
    drawPoint(location, selected? 400: 200, selected? color().brighter(): color());
  }

  public abstract Color color();
}
