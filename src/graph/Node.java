package graph;

import java.util.*;

import graphics.Vector;

public class Node {
  public Vector location;
  public Map<Node, Double> edges = new HashMap<>();
}