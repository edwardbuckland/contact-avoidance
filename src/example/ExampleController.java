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

import java.util.*;

import graph.*;
import graph.bipartite.*;
import gui.admin.tab.*;

public class ExampleController {
  public static Map<String, Class<? extends Example>>   examples    = new HashMap<>();

  static {
    examples.put("blah", null);
  }

  public static void loadExample(Example example) {
    Node.nodes.clear();
    Activity.activities.clear();

    example.load();

    ActivitiesTable.update();
  }

  public interface Example {
    void load();
  }
}
