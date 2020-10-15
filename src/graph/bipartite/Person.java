package graph.bipartite;

import static graph.Node.*;
import static gui.admin.GraphView.*;
import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import graph.Node;
import graphics.Vector;
import gui.admin.*;

public class Person extends ArrayList<Node> implements Drawable {
  private static final long     serialVersionUID    = -3796609626176481425L;

  public static List<Person>    people              = new ArrayList<>();
  private static int            uniqueIndex;

  public int                    index               = uniqueIndex++;
  public String                 name;

  private List<Activity>        activities          = new ArrayList<>();

  static {
    Activity coffee1 = new Activity("coffee", 8, 9);
    Activity coffee2 = new Activity("coffee", 9, 10);
    Activity maths = new Activity("maths", 9, 11);
    Activity computing = new Activity("computing", 10, 12);
    Activity cafe = new Activity("cafe", 12, 13);
    Activity sushi = new Activity("sushi", 12, 13);
    Activity economics = new Activity("economics", 14.25, 16.25);
    Activity physics = new Activity("physics", 13.25, 16.25);
    Activity bar = new Activity("bar", 18, 19.5);

    new Person("John Citizen").addActivities(coffee1, computing, physics, bar);
    new Person("Frank Furter").addActivities(coffee2, computing, cafe);
  }

  public Person(String name) {
    this.name = name;

    people.add(this);
  }

  private Vector timePoint(double t) {
    return new Vector((index + 1)/2*6*signum(index%2 - 0.5), t, 0);
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

  public Stream<Activity> activities() {
    return activities.stream();
  }

  public void addActivities(Activity... activities_list) {
    activities.addAll(List.of(activities_list));

    updateActivities();
  }

  public void removeActivity(Activity activity) {
    activities.remove(activity);

    updateActivities();
  }

  private void updateActivities() {
    stream().map(node -> node.edges.keySet())
            .flatMap(Set::stream)
            .forEach(node -> forEach(node.edges::remove));

    forEach(nodes::remove);

    clear();

    if (!activities.isEmpty()) {
      activities.sort((first, second) -> (int)signum(first.endTime - second.endTime));

      Node node = new PersonNode(timePoint(0), this);
      node.edges.put(activities.get(0), 1.0);
      add(node);

      for (int i = 0; i < activities.size() - 1; i++) {
        node = new PersonNode(timePoint((activities.get(i).endTime + activities.get(i + 1).startTime)/2), this);
        activities.get(i).edges.put(node, 1.0);
        node.edges.put(activities.get(i + 1), 1.0);
        add(node);
      }

      node = new PersonNode(timePoint(24), this);
      activities.get(activities.size() - 1).edges.put(node, 1.0);
      add(node);
    }
  }

  public double similarity(Person person) {
    Function<Stream<Activity>, Double> duration = stream ->
      stream.mapToDouble(activity -> activity.endTime - activity.startTime)
                                                                .sum();

    double shared = duration.apply(activities.stream()
                                             .filter(person.activities::contains));

    return shared/(duration.apply(Stream.concat(activities.stream(), person.activities.stream())) - shared);
  }
}
