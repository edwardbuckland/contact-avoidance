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

import static java.util.concurrent.ThreadLocalRandom.*;

import java.io.*;
import java.util.*;

import graph.bipartite.*;

public class ActivityGenerator {
  private static final double       CLOSE_TIME      = 22;
  private static final double[]     UNI_TIMES       = new double[] {9, 10, 11, 12, 13, 14.25, 15.25,
                                                                    16.25, 17.25, 18.25};

  private static List<String>       subjects        = new ArrayList<>();
  static {
    InputStream stream = ActivityGenerator.class.getResourceAsStream("/data/subjects.txt");

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      for (String subject; (subject = reader.readLine()) != null; subjects.add(subject));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void generateActivities() {
    // subjects
    subjects.forEach(activity -> {
      double start_time = UNI_TIMES[current().nextInt(UNI_TIMES.length)];
      double duration;

      for (duration = 1; start_time + duration + 1 <= CLOSE_TIME && current().nextBoolean(); duration++);

      new Activity(activity, start_time, duration);
    });

    // facilities
    for (double start_time: UNI_TIMES)
      for (String activity: new String[] {"Library", "Study Room", "Computer Lab", "Design Lab",
                                          "Gym", "Tennis", "Squash", "Swim", "Basketball"})
        new Activity(activity, start_time, 1);

    // food and drink
    for (int start_time = 8; start_time < CLOSE_TIME; start_time++)
      for (String activity: new String[] {"Coffee", "Juice", "Bubble Tea"})
        new Activity(activity, start_time, 1);

    for (int start_time = 11; start_time < 15; start_time++)
      for (String activity: new String[] {"Sushi", "Sandwhich", "Pizza"})
        new Activity(activity, start_time, 1);

    for (int start_time = 18; start_time < CLOSE_TIME; start_time++)
      new Activity("Bar", start_time, 1);
  }
}
