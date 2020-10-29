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

import static graph.Node.*;
import static gui.admin.tab.bipartite.BipartiteView.*;
import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import graph.Node;
import graphics.Vector;
import gui.admin.*;

public class Person extends ArrayList<Node> implements Drawable {
  private static final long     serialVersionUID    = -3796609626176481425L;

  private static final double   PROBABILITY         = 0.5;

  public static List<Person>    people              = new ArrayList<>();

  public int                    index               = Person.people.size();
  public String                 name;

  public List<Activity>         activities          = new ArrayList<>();

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
      node.edges.put(activities.get(0), PROBABILITY);
      add(node);

      for (int i = 0; i < activities.size() - 1; i++) {
        node = new PersonNode(timePoint((activities.get(i).endTime + activities.get(i + 1).startTime)/2), this);
        activities.get(i).edges.put(node, PROBABILITY);
        node.edges.put(activities.get(i + 1), PROBABILITY);
        add(node);
      }

      node = new PersonNode(timePoint(24), this);
      activities.get(activities.size() - 1).edges.put(node, PROBABILITY);
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

  public boolean canAttend(Activity activity) {
    return activities().noneMatch(registered -> registered.startTime < activity.endTime &&
                                                registered.endTime > activity.startTime);
  }
}
