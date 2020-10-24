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

import static java.lang.Math.*;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.stream.*;

public class KMeans {
  public static int[] cluster(double[][] data, int k) {
    int n = data.length;

    Map<double[], Set<Integer>> clusters = new HashMap<>();

    for (int i = 0; i < k; i++) {
      clusters.put(new double[data[0].length], new HashSet<>());
    }

    for (int i = 0; i < n; i++)
      clusters.get(clusters.keySet().toArray()[i%k]).add(i);

    for (AtomicBoolean reassigned = new AtomicBoolean(true); reassigned.get(); reassigned.set(false)) {
      clusters.entrySet().forEach(entry -> {
        for (int i = 0; i < k; i++) {
          final int final_i = i;
          entry.getKey()[i] = entry.getValue()
                                   .stream()
                                   .mapToDouble(index -> data[index][final_i])
                                   .average()
                                   .getAsDouble();
        }
      });

      BiFunction<double[], double[], Double> distance = (first, second) -> {
        double result = 0;

        for (int i = 0; i < k; i++)
          result += pow(second[i] - first[i], 2);

        return result;
      };

      clusters.entrySet().forEach(entry -> {
        Iterator<Integer> iterator = entry.getValue().iterator();

        while (iterator.hasNext()) {
          int index = iterator.next();

          double[] closest_centroid = clusters.keySet()
                                              .stream()
                                              .sorted((first, second) -> (int)signum(
                                                        distance.apply(data[index], first) -
                                                        distance.apply(data[index], second)))
                                              .findFirst()
                                              .get();

          if (closest_centroid != entry.getKey()) {
            iterator.remove();
            clusters.get(closest_centroid).add(index);
            reassigned.set(true);
          }
        }
      });
    }

    int[] cluster_labels = new int[data.length];

    List<Set<Integer>> cluster_sets = clusters.values().stream().collect(Collectors.toList());

    for (int i = 0; i < cluster_sets.size(); i++) {
      final int i_final = i;
      cluster_sets.get(i).forEach(index -> cluster_labels[index] = i_final);
    }

    return cluster_labels;
  }
}
