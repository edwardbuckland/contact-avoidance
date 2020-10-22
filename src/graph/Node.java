package graph;

import static gui.admin.tab.BipartiteView.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import graphics.Vector;
import gui.admin.*;

public abstract class Node implements Drawable {
  public static List<Node>      nodes           = new ArrayList<>();
  protected static Node         selectedNode;

  public static void clearSelection() {
    if (selectedNode != null)
      selectedNode.selected = false;

    selectedNode = null;
  }

  public Vector                 location;
  protected boolean             selected;

  public Map<Node, Double>      edges           = new HashMap<>();

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

  public void select() {
    if (selectedNode != null)
      selectedNode.selected = false;

    selectedNode = this;
    selected = true;
  }

  public abstract Color color();
}
