package graph.bipartite;

import static gui.admin.View.*;
import static java.awt.Color.*;
import static java.lang.Math.*;

import java.util.*;
import java.util.stream.*;

import graph.Graph.Node;
import graphics.Vector;
import gui.admin.*;

public class Person extends ArrayList<Node> implements Drawable {
  private static final long serialVersionUID = -3796609626176481425L;

  public static List<Person> people = new ArrayList<>();
  private static int uniqueIndex;

  public int index = uniqueIndex++;
  public String name;

  private List<Activity> activities = new ArrayList<>();

  static {
    Activity maths = new Activity("maths", 9, 11);
    Activity cafe = new Activity("cafe", 12, 13);
    Activity coffee = new Activity("coffee", 12, 13);
    Activity cake = new Activity("cake", 15, 23);

    new Person("John Smith").addActivities(maths, cafe, cake);
    new Person("Frank Furter").addActivities(maths, coffee);
  }

  public Person(String name) {
    this.name = name;

    people.add(this);
  }

  private Vector timePoint(double t) {
    return new Vector(index*6, t, 0);
  }

  @Override
  public void draw() {
    drawLine(timePoint(0), timePoint(24));
    drawText(name, timePoint(24));
  }

  @Override
  public String toString() {
    return name;
  }

  public void addActivities(Activity... activities_list) {
    activities.addAll(List.of(activities_list));

    if (!activities.isEmpty()) {
      activities.sort((first, second) -> (int)signum(first.endTime - second.endTime));

    Node node = new Node(timePoint(0), green);
    node.edges.put(activities.get(0), 1.0);
    add(node);

    for (int i = 0; i < activities.size() - 1; i++) {
      node = new Node(timePoint((activities.get(i).endTime + activities.get(i + 1).startTime)/2), green);
      activities.get(i).edges.put(node, 1.0);
      node.edges.put(activities.get(i + 1), 1.0);
    }

    node = new Node(timePoint(24), green);
    activities.get(activities.size() - 1).edges.put(node, 1.0);
    }
  }

  public void removeActivity(Activity activity) {
    activities.remove(activity);
  }

  public Stream<Activity> activities() {
    return activities.stream();
  }
}
