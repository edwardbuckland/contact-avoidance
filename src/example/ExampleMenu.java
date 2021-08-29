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

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import graph.*;
import graph.bipartite.*;
import gui.admin.tab.*;

public class ExampleMenu extends JMenu {
  private static final long         serialVersionUID    = -3782680998637307965L;

  public static Map<String, Runnable>   examples    = new HashMap<>();
  static {
    examples.put("Binary Split",  ExampleGenerator::twoWaySplit);
    examples.put("Ternary Split", ExampleGenerator::threeWaySplit);
    examples.put("Small",         ExampleGenerator::small);
    examples.put("Medium",        ExampleGenerator::medium);
    examples.put("Large",         ExampleGenerator::large);
    examples.put("Benchmarking",  ExampleGenerator::benchmarking);
  }

  public static JMenuBar createMenuBar() {
    JMenuBar menu_bar = new JMenuBar();
    menu_bar.add(new ExampleMenu());
    return menu_bar;
  }

  private ExampleMenu() {
    super("Examples");

    examples.forEach((name, example) -> add(new JMenuItem(new AbstractAction(name) {
      private static final long     serialVersionUID    = 2398512433134647385L;
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          Person.people.clear();
          Activity.activities.clear();
          Node.nodes.clear();

          example.run();

          ActivitiesTable.update();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    })));
  }
}
