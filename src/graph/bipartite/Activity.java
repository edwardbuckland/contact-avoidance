package graph.bipartite;

import static gui.admin.View.*;
import static java.awt.Color.*;

import graph.Graph.*;
import graphics.*;

public class Activity extends Node {
  private String name;

  public double startTime;
  public double endTime;

  public Activity(String name, double start_time, double end_time) {
    super(new Vector(Math.random()*10, (start_time + end_time)/2, Math.random()*3 + 3), red);

    this.name = name;
    startTime = start_time;
    endTime = end_time;
  }

  @Override
  public void draw() {
    super.draw();

    drawText(toString(), location);
  }

  @Override
  public String toString() {
    return name + " (" + timeString(startTime, true) + " - " + timeString(endTime, true) + ")";
  }

  public static String timeString(double time, boolean minutes) {
    return (int)((time + 11)%12 + 1) + (minutes? String.format(":%02d", (int)(time%1*60)): "")
              + " " + (time%24 > 11? "pm": "am");
  }
}
