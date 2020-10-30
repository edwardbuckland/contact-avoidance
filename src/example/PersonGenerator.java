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

package example;

import static graph.bipartite.Activity.*;
import static java.util.concurrent.ThreadLocalRandom.*;

import java.io.*;
import java.util.*;

import graph.bipartite.*;

public class PersonGenerator {
  private static List<String>       firstNames      = new ArrayList<>();
  private static List<String>       lastNames       = new ArrayList<>();
  static {
    InputStream first_names_stream = PersonGenerator.class.getResourceAsStream("/data/first-names.txt");
    InputStream  last_names_stream = PersonGenerator.class.getResourceAsStream("/data/last-names.txt");

    try (BufferedReader first_names_reader = new BufferedReader(new InputStreamReader(first_names_stream));
         BufferedReader  last_names_reader = new BufferedReader(new InputStreamReader( last_names_stream))) {
      for (String first_name; (first_name = first_names_reader.readLine()) != null; firstNames.add(first_name));
      for (String  last_name; ( last_name =  last_names_reader.readLine()) != null;  lastNames.add( last_name));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static <T> T randomElement(List<T> list) {
    return list.get(current().nextInt(list.size()));
  }

  public static void generatePeople(int n, int max_activities) {
    for (int i = 0; i < n; i++) {
      Person person = new Person(i == 0? "Edward Buckland": randomElement(firstNames) + " " +
                                 randomElement(lastNames));

      for (int j = 0; j < max_activities; j++) {
        Activity activity = randomElement(j < 3? activities.subList(55, 65):
                                          j < 5? activities.subList(20, 80):
                                                 activities);

        if (person.canAttend(activity))
        {
          person.addActivities(activity);
        }
      }
    }
  }
}
