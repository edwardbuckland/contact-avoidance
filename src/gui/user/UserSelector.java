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

package gui.user;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import graph.bipartite.*;

public class UserSelector extends JPanel {
  private static final long     serialVersionUID    = -1812097228826180716L;

  public static Person          currentUser;

  private JLabel                label               = new JLabel();
  private JButton               button              = new JButton();

  public UserSelector() {
    super(new BorderLayout());

    label.setHorizontalAlignment(JLabel.CENTER);
    add(label);

    button.addActionListener(event -> {
      String name = JOptionPane.showInputDialog(this, "Name: ", "Select User", JOptionPane.PLAIN_MESSAGE);
      if (name != null) {
        Optional<Person> new_user = Person.people.stream()
                                          .filter(person -> person.name.equals(name))
                                          .findFirst();

        if (new_user.isPresent())
          currentUser = new_user.get();
        else
          currentUser = new Person(name);
      }
    });
    add(button, BorderLayout.EAST);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (currentUser == null)
      button.setText("Log in...");
    else {
      button.setText("Switch Users...");
      if (label.getText() != currentUser.name)
        label.setText(currentUser.name);
    }
  }
}
