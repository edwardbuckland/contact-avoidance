package graph.bipartite;

import static graph.bipartite.Activity.ActivityStatus.*;
import static gui.admin.GraphView.*;
import static java.awt.Color.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;

import graph.Node;
import graph.algorithm.*;
import graphics.Vector;
import gui.admin.*;
import gui.user.tab.map.*;

public class Activity extends Node {
  public static List<Activity>  activities          = new ArrayList<>();

  static {
    for (double time: new double[] {9.0, 10.0, 11.0, 12.0, 13.0, 14.25, 15.25, 16.25, 17.25}) {
      new Activity("Coffee",    time,   time + 1);
      new Activity("Sushi",     time,   time + 1);

      new Activity("Gym",       time,   time + 1);
      new Activity("Swim",      time,   time + 1);
      new Activity("Tennis",    time,   time + 1);
      new Activity("Squash",    time,   time + 1);

      new Activity("Library",   time,   time + 1);
    }

    new Activity("maths", 9, 11);
    new Activity("computing", 10, 12);
    new Activity("cafe", 12, 13);
    new Activity("sushi", 12, 13);
    new Activity("economics", 14.25, 16.25);
    new Activity("physics", 13.25, 16.25);
    new Activity("bar", 18, 19.5);
  }

  public static Activity selectedActivity() {
    return selectedNode instanceof Activity? (Activity)selectedNode: null;
  }

  public static String timeString(double time, boolean minutes) {
    return (int)((time + 11)%12 + 1) + (minutes? String.format(":%02d", (int)(time%1*60)): "")
              + " " + (time%24 > 11? "pm": "am");
  }

  protected enum ActivityStatus {
    PENDING_APPROVAL            (orange),
    APPROVED                    (green ),
    REJECTED                    (red   );

    public Color                color;

    private ActivityStatus(Color color) {
      this.color = color;
    }

    @Override
    public String toString() {
      String string = super.toString().replace("_", " ");
      return string.substring(0, 1) + string.substring(1).toLowerCase();
    }
  }

  private ActivityStatus        status              = PENDING_APPROVAL;
  public String                 name;

  public double                 startTime;
  public double                 endTime;

  public List<Building>         locations           = new ArrayList<>();

  public Activity(String name, double start_time, double end_time) {
    super(new Vector(0, (start_time + end_time)/2, 0));

    this.name = name;
    startTime = start_time;
    endTime = end_time;

    activities.add(this);
  }

  public void approve() {
    status = APPROVED;
  }

  public void reject() {
    status = REJECTED;
  }

  public String status() {
    return status.toString();
  }

  public boolean approved() {
    return status == APPROVED;
  }

  public boolean pending() {
    return status == PENDING_APPROVAL;
  }

  public boolean selected() {
    return selected;
  }

  @Override
  public Color color() {
    return status.color;
  }

  @Override
  public void draw() {
    super.draw();

    if (GraphView.drawAccessories)
      drawText(toString(), location);
  }

  @Override
  public String toString() {
    return name + " (" + timeString(startTime, true) + " - " + timeString(endTime, true) + ")";
  }

  private List<Person> people() {
    return edges.keySet()
                .stream()
                .map(PersonNode.class::cast)
                .map(node -> node.person)
                .collect(Collectors.toList());
  }

  public double[][] similarityMatrix() {
    int n = edges.size();
    double[][] matrix = new double[n][n];
    Person[] people = people().toArray(Person[]::new);

    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        matrix[i][j] = people[i].similarity(people[j]);

    return matrix;
  }

  private void dispose() {
    people().forEach(person -> person.removeActivity(this));
    activities.remove(this);
    nodes.remove(this);
  }

  public List<Activity> deriveActivities() {
    return locations.stream()
                    .map(location -> {
                      Activity activity = new Activity(name + " " + location, startTime, endTime);
                      activity.locations.add(location);
                      activity.approve();
                      return activity;
                    })
                    .collect(Collectors.toList());
  }

  public void randomSplit() {
    List<Activity> derived_activities = deriveActivities();
    List<Person> people = people();

    Collections.shuffle(people);

    for (int i = 0; i < people.size(); i++) {
      people.get(i).addActivities(derived_activities.get(i%derived_activities.size()));
    }

    dispose();
  }

  public void clusteredSplit() {
    List<Activity> derived_activities = deriveActivities();
    List<Person> people = people();

    int[] clusters = SpectralCluster.spectralCluster(similarityMatrix(), locations.size());

    for (int i = 0; i < people().size(); i++)
      people.get(i).addActivities(derived_activities.get(clusters[i]));

    dispose();
  }
}
