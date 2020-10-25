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

import static graph.bipartite.Activity.*;
import static gui.user.tab.map.MapTab.*;
import static java.awt.GridBagConstraints.*;
import static java.lang.Math.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import javax.swing.*;

import graph.bipartite.*;
import gui.*;
import gui.user.tab.map.*;

public class AggregatedMapView extends JPanel {
  private static final long         serialVersionUID        = -2704078906802798985L;

  private JScrollPane               scrollPane              = new JScrollPane(new AggregatedMapPanel());
  private JSlider                   timeSlider              = new JSlider(JSlider.VERTICAL, 0, 24*60 - 1, 0);

  public AggregatedMapView() {
    super(new BorderLayout());

    JPanel slider_panel = new JPanel(new GridBagLayout());

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.anchor = EAST;
    constraints.fill = VERTICAL;
    constraints.weighty = 1;
    constraints.gridx = 0;

    slider_panel.add(new JLabel(), constraints);

    for (int i = 0; i < 25; i++) {
      JLabel label = new JLabel(timeString(24 - i, false));
      slider_panel.add(label, constraints);
    }

    slider_panel.add(new JLabel(), constraints);

    constraints.gridx = 1;
    constraints.gridheight = REMAINDER;

    timeSlider.addChangeListener(event -> repaint());
    slider_panel.add(timeSlider, constraints);

    add(slider_panel, BorderLayout.WEST);

    scrollPane.getViewport().addChangeListener(event -> repaint());
    add(scrollPane);
  }

  private double selectedTime() {
    return timeSlider.getValue()/60.0;
  }

  private class AggregatedMapPanel extends JPanel {
    private static final long       serialVersionUID        = 8066207952855416332L;

    private static final double     ATTENDANCE_FILLET_TIME  = 0.25;

    private BufferedImage           image;

    @Override
    protected void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);

      Graphics2D graphics_2d = (Graphics2D)graphics;

      if (image == null)
        invalidate();

      for (int percentage = 100; images.containsKey(percentage) && images.get(percentage).getWidth() >
          scrollPane.getViewport().getWidth() && images.get(percentage).getHeight() >
          scrollPane.getViewport().getHeight(); percentage -= PERCENTAGE_INCREMENT)
       image = images.get(percentage);

      graphics_2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);

      for (Building building: Building.values()) {
        double x = building.longtitude*getWidth ();
        double y = building.  latitude*getHeight();
        double size = 0;

        for (Activity activity: activities) {
          if (activity.locations.contains(building)) {
            double time_from_edge = min(selectedTime() - activity.startTime, activity.endTime - selectedTime());

            if (time_from_edge >= ATTENDANCE_FILLET_TIME)
              size += activity.edges.size();
            else if (time_from_edge > -ATTENDANCE_FILLET_TIME) {
              size += activity.edges.size()/(1 + exp(6*ATTENDANCE_FILLET_TIME*time_from_edge/
                        (pow(time_from_edge, 2) - pow(ATTENDANCE_FILLET_TIME, 2))));
            }
          }
        }

        if (size > 1e-6) {
          Ellipse2D bubble = new Ellipse2D.Double(x - size/2, y - size/2, size, size);

          graphics_2d.setColor(Color.red);
          graphics_2d.fill(bubble);
          graphics_2d.setColor(Color.black);
          graphics_2d.draw(bubble);
        }
      }

      graphics_2d.setFont(graphics_2d.getFont().deriveFont(20f));

      Rectangle2D bounds = graphics_2d.getFontMetrics().getStringBounds("0", graphics_2d);
      OutlinedText.drawText(graphics_2d, timeString(selectedTime(), true),
                            scrollPane.getHorizontalScrollBar().getValue() +   bounds.getWidth(),
                            scrollPane.getVerticalScrollBar().getValue()   + 2*bounds.getHeight());
    }

    @Override
    public Dimension getPreferredSize() {
      return image == null? super.getPreferredSize(): new Dimension(image.getWidth(), image.getHeight());
    }
  }
}
