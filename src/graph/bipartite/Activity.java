package graph.bipartite;

import static graph.bipartite.Activity.ActivityStatus.*;
import static gui.admin.GraphView.*;
import static java.awt.Color.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import graph.*;
import graphics.Vector;
import gui.admin.*;
import gui.user.tab.map.*;

public class Activity extends Node {
  public static List<Activity>  activities      = new ArrayList<>();

  public static String timeString(double time, boolean minutes) {
    return (int)((time + 11)%12 + 1) + (minutes? String.format(":%02d", (int)(time%1*60)): "")
              + " " + (time%24 > 11? "pm": "am");
  }

  public enum ActivityStatus {
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

  public ActivityStatus         status          = PENDING_APPROVAL;
  public String                 name;

  public double                 startTime;
  public double                 endTime;

  public List<Building>         locations       = new ArrayList<>();

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
}
