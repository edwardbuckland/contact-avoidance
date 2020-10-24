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

import javax.swing.*;

public class ExampleMenu extends JMenu {
  private static final long         serialVersionUID    = -3782680998637307965L;

  public static JMenuBar createMenuBar() {
    JMenuBar menu_bar = new JMenuBar();
    menu_bar.add(new ExampleMenu());
    return menu_bar;
  }

  private ExampleMenu() {
    super("Examples");

    ExampleController.examples.forEach((name, example) -> add(new JMenuItem(new AbstractAction(name) {
      private static final long     serialVersionUID    = 2398512433134647385L;
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          ExampleController.loadExample(example.getDeclaredConstructor().newInstance());
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    })));
  }
}
