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

import static graph.bipartite.Person.*;

public class KAryContacts {
  // TODO: generalise for k-ary
  public static double averageContacts(int k) {
    int n = people.size();
    int total_pairs = 0;

    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        if (people.get(i).activities().anyMatch(people.get(j).activities::contains))
          total_pairs++;

    return total_pairs*1.0/n;
  }
}
