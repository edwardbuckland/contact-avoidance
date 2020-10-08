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

    drawText(name + " (" + timeString(startTime) + " - " + timeString(endTime) + ")", location);
  }

  private String timeString(double time) {
    return String.format("%d:%02d %s", (int)time - (time > 12? 12: 0), (int)(time%1*60), time%24 > 11? "pm": "am");

  }
}
