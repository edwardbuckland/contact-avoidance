package graph.bipartite;

import static gui.View.*;
import static java.awt.Color.*;

import java.util.*;

import graph.Graph.*;
import graphics.Vector;
import gui.*;

public class Person extends ArrayList<Node> implements Drawable {
  private static final long serialVersionUID = -3796609626176481425L;

  public static List<Person> people = new ArrayList<>();
  private static int uniqueIndex;

  public int index = uniqueIndex++;
  public String name;

  static {
    Activity maths = new Activity("maths", 9, 12);
    Activity cafe = new Activity("cafe", 12, 13);
    Activity coffee = new Activity("coffee", 12, 13);
    Activity cake = new Activity("cake", 15, 16);

    new Person("John", maths, cafe, cake);
    new Person("Smith", maths, coffee);
  }

  public Person(String name, Activity... activities) {
    this.name = name;

    people.add(this);

    Node node = new Node(timePoint(0), green);
    node.edges.put(activities[0], 1.0);
    add(node);

    for (int i = 0; i < activities.length - 1; i++) {
      node = new Node(timePoint((activities[i].endTime + activities[i + 1].startTime)/2), green);
      activities[i].edges.put(node, 1.0);
      node.edges.put(activities[i + 1], 1.0);
    }

    node = new Node(timePoint(24), green);
    activities[activities.length - 1].edges.put(node, 1.0);
  }

  private Vector timePoint(double t) {
    return new Vector(index*6, t, 0);
  }

  @Override
  public void draw() {
    drawLine(timePoint(0), timePoint(24));
    drawText(name, timePoint(24));
  }
}
