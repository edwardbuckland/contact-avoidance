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

  public static Activity selectedActivity() {
    return selectedNode instanceof Activity? (Activity)selectedNode: null;
  }

  public static String timeString(double time, boolean minutes) {
    return (int)((time + 11)%12 + 1) + (minutes? String.format(":%02d", (int)(time%1*60)): "")
              + " " + (time%24 > 11? "pm": "am");
  }

  protected enum ActivityStatus {
    PENDING_APPROVAL    (orange),
    APPROVED            (green ),
    REJECTED            (red   );

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

  private Stream<Person> people() {
    return edges.keySet()
                .stream()
                .map(PersonNode.class::cast)
                .map(node -> node.person);
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

  public void randomSplit() {
    List<Activity> derived_activities = new ArrayList<>();

    for (int i = 0; i < locations.size(); i++) {
      Activity activity = new Activity(name + " " + i, startTime, endTime);
      activity.locations.add(locations.get(i));
      activity.approve();
      derived_activities.add(activity);
    }

    List<Person> people = people().collect(Collectors.toList());
    Collections.shuffle(people);

    for (int i = 0; i < people.size(); i++) {
      people.get(i).removeActivity(this);
      people.get(i).addActivities(derived_activities.get(i%derived_activities.size()));
    }

    activities.remove(this);
    nodes.remove(this);
  }

  public void clusteredSplit() {
    SpectralCluster.spectralCluster(similarityMatrix());
  }
}
