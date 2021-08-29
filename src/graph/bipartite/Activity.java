/*
 *  Copyright (C) 2020 Edward Buckland. Some rights reserved.
 *
 *  This file is part of "Contact Avoidance".
 *
 *  "Contact Avoidance" is distributed under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the License, or (at your option)
 *  any later version.
 *
 *  "Contact Avoidance" is a demonstration application only and is therefore not intended for
 *  general use. "Contact Avoidance" is distributed WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 *  You should have received a copy of the GNU Affero General Public License along with "Contact
 *  Avoidance". If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html>.
 */

package graph.bipartite;

import static graph.bipartite.Activity.ActivityStatus.*;
import static gui.admin.tab.bipartite.BipartiteView.*;
import static java.awt.Color.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;

import graph.Node;
import graph.algorithm.*;
import graphics.Vector;
import gui.admin.tab.*;
import gui.admin.tab.bipartite.*;
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
    PENDING_APPROVAL            (yellow),
    SCHEDULED                   (orange),
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

  public Activity(String name, double start_time, double duration) {
    super(new Vector(0, start_time + duration/2, 0));

    this.name = name;
    startTime = start_time;
    endTime = start_time + duration;

    activities.add(this);
  }

  public void schedule() {
    status = SCHEDULED;
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

  public boolean pending() {
    return status == PENDING_APPROVAL;
  }

  public boolean scheduled() {
    return status == SCHEDULED;
  }

  public boolean approved() {
    return status == APPROVED;
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

    if (BipartiteView.drawAccessories)
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
    ActivitiesTable.update();
  }

  public List<Activity> deriveActivities() {
    return locations.stream()
                    .map(location -> {
                      Activity activity = new Activity(name + " " + location, startTime, endTime - startTime);
                      activity.locations.add(location);
                      activity.schedule();
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

//    int[] clusters = SpectralCluster.spectralCluster(similarityMatrix(), locations.size());
    int[] clusters = SpectralCluster.spectralClusterApacheCommons(similarityMatrix(), locations.size());

    for (int i = 0; i < people().size(); i++)
      people.get(i).addActivities(derived_activities.get(clusters[i]));

    dispose();
  }
}
