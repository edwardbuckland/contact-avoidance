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

package gui.admin.tab;

import javax.swing.*;

import graph.algorithm.*;

public class Footer extends JPanel {
  private static final long     serialVersionUID    = 3279546830043780324L;

  private static final String   PLAY                = "\u25b6";
  private static final String   STOP                = "\u25fc";

  public Footer() {
    JButton play_pause_button = new JButton(PLAY);
    play_pause_button.addActionListener(event -> {
      if (play_pause_button.getText() == PLAY) {
        FruchtermanReingold.timer.start();
        play_pause_button.setText(STOP);
      }
      else {
        FruchtermanReingold.timer.stop();
        play_pause_button.setText(PLAY);
      }
    });
    add(play_pause_button);

    add(new JLabel("Attraction"));
    JSlider attraction_slider = new JSlider();
    attraction_slider.addChangeListener(event -> FruchtermanReingold.attraction.set(attraction_slider.getValue()));
    add(attraction_slider);

    JCheckBox draw_labels = new JCheckBox("Draw accessories", true);
    draw_labels.addActionListener(event -> {
      BipartiteView.drawAccessories = draw_labels.isSelected();
      repaint();
    });
    add(draw_labels);
  }
}
