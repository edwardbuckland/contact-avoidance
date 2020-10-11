package graph;

import static gui.admin.View.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import graphics.Vector;
import gui.admin.*;

public class Graph {
  public static List<Node>      nodes       = new ArrayList<>();

  public static class Node implements Drawable {
    public Vector               location;
    public boolean              selected;
    private Color               color;

    public Map<Node, Double>    edges       = new HashMap<>();

    public Node(Vector location, Color color) {
      this.location = location;
      this.color = color;

      nodes.add(this);
    }

    @Override
    public void draw() {
      edges.entrySet()
           .forEach(entry -> drawArrow(location, entry.getKey().location));
      drawPoint(location, selected? 400: 200, selected? color.brighter(): color);
    }
  }
}
