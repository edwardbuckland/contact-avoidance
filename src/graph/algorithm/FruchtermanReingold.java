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

package graph.algorithm;

import static graph.Node.*;
import static graph.bipartite.Activity.*;
import static java.lang.Math.*;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import javax.swing.Timer;

import graph.*;
import graphics.Vector;
import gui.admin.tab.*;

public class FruchtermanReingold {
  public static AtomicInteger   attraction  = new AtomicInteger(50);

  public static Timer           timer       = new Timer(20, event ->
  {
    Map<Node, Vector> displacement = new HashMap<>();
    BiConsumer<Node, Vector> displace = (node, delta) -> displacement.put(node, displacement.get(node).plus(delta));

    // repel pairs
    for (Node first: nodes) {
      displacement.put(first, new Vector(random()*1e-4, 0, random()*1e-4));

      nodes.stream()
           .filter(second -> !second.edges.isEmpty() && second.location.minus(first.location).norm() > 1e-4)
           .forEach(second -> {
             Vector delta = first.location.minus(second.location);
             displace.accept(first, delta.unit().times(1/delta.norm()/attractionRatio()));
           });
    }

    // attract along edges
    for (Node first: nodes)
      for (Node second: first.edges.keySet()) {
        Vector delta = first.location.minus(second.location);
        Vector scaled_delta = delta.times(delta.norm()*attractionRatio());
        displace.accept(first,  scaled_delta.times(-1));
        displace.accept(second, scaled_delta);
      }

    activities.stream()
              .filter(node -> !node.selected() && !node.edges.isEmpty())
              .forEach(node -> {
                Vector delta = displacement.get(node);
                delta.y = 0;
                delta = delta.unit().times(atan(pow(delta.norm()/nodes.size(), 2)));
                node.location = node.location.plus(delta.times(2));
              });

    BipartiteView.view.repaint();
  });

  private static double attractionRatio() {
    return pow(attraction.get()/50.0, 10)/800*sqrt(nodes.size()) + 1e-5;
  }
}
