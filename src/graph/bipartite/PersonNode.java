package graph.bipartite;

import java.awt.*;

import graph.*;
import graphics.*;

public class PersonNode extends Node {
  public Person     person;

  public PersonNode(Vector location, Person person) {
    super(location);

    this.person = person;
  }

  @Override
  public Color color() {
    return Color.blue;
  }
}
