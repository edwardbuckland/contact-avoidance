package graph.bipartite;

import static gui.View.*;

import graph.Graph.*;
import graphics.*;

public class Activity extends Node {
  private String name;

  public double startTime;
  public double endTime;

  public Activity(String name, double start_time, double end_time) {
    super(new Vector(Math.random()*10, (start_time + end_time)/2, Math.random()*3 + 3));

    this.name = name;
    startTime = start_time;
    endTime = end_time;
  }

  @Override
  public void draw() {
    super.draw();

    drawText(name, location);
  }
}
