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
import static graph.bipartite.Person.*;
import static java.lang.Math.*;

import java.util.*;

import graph.*;
import graph.bipartite.*;

public class GraphIntegral {
  public static double[][] integrate() {
    int n = people.size();

    Map<Node, double[]> probabilities = new HashMap<>();

    for (Node node: nodes)
      probabilities.put(node, new double[n]);

    for (int i = 0; i < n; i++)
      if (!people.get(i).isEmpty())
        probabilities.get(people.get(i).get(0))[i] = 1;

    nodes.stream()
         .sorted((first, second) -> (int)signum(first.location.y - second.location.y))
         .forEach(node -> {
           node.edges.forEach((next, weight) -> updateProbability(probabilities, node, next, weight));

           if (node instanceof PersonNode) {
             Person person = ((PersonNode)node).person;
             int index = person.indexOf(node) + 1;

             if (index < person.size())
               updateProbability(probabilities, node, person.get(index), 1);
           }
         });

    return people.stream()
                 .filter(person -> !person.isEmpty())
                 .map(person -> probabilities.get(person.get(person.size() - 1)))
                 .toArray(double[][]::new);
  }

  private static void updateProbability(Map<Node, double[]> probabilities, Node source,
                                        Node destination, double probability) {
    for (int i = 0; i < people.size(); i++)
      probabilities.get(destination)[i] = 1 - (1 - probabilities.get(destination)[i])*
                                          (1 - probabilities.get(source)[i]*probability);
  }
}
