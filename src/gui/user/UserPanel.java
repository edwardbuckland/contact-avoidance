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

import static java.awt.Color.*;
import static java.awt.RenderingHints.*;
import static java.awt.geom.Arc2D.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.geom.Arc2D.Double;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

import graph.*;
import gui.user.tab.*;
import gui.user.tab.map.*;

public class UserPanel extends JPanel {
  private static final long     serialVersionUID    = 8753354135329406588L;

  public UserPanel() {
    super(new BorderLayout());

    add(new UserSelector(), BorderLayout.NORTH);

    JTabbedPane tabbed_pane = new JTabbedPane();
    tabbed_pane.addTab("Timetable", null, new TimetableTab(), "Timetable");
    tabbed_pane.addTab("Map", null, new MapTab(), "Map");
    try {
      tabbed_pane.addTab("QR Code", null, new JLabel(new ImageIcon(ImageIO.read(
                         getClass().getResource("/image/qr-code.png")))), "QR Code");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    tabbed_pane.addTab("Contact", new ContactPanel());

    tabbed_pane.addChangeListener(event -> Node.clearSelection());
    add(tabbed_pane);
    
    add(Box.createVerticalStrut(30), BorderLayout.SOUTH);
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    Graphics2D graphics_2d  = (Graphics2D)graphics;

    graphics_2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

    Point mouse_point = MouseInfo.getPointerInfo().getLocation();
    SwingUtilities.convertPointFromScreen(mouse_point, this);
    graphics_2d.setPaint(new RadialGradientPaint(mouse_point, getWidth(), new float[] {0f, 0.3f, 1f},
                         new Color[] {gray, darkGray, black}));

    // taken from iPhone X prototype technical drawings
    double glass_height    = 139.99;
    double screen_height   = 135.75;

    double scale           = getHeight()/glass_height;

    double bevel           = (glass_height - screen_height)/2*scale;
    double notch_edge      = (67.36 - 34)/2*scale;
    double notch_thickness = 4.99*scale;
    double notch_diameter  = 3.22*2*scale;
    double  tiny_diameter  = 1.30*2*scale;

    // circular approximations
    double inner_diameter  = getWidth()*0.18782;
    double outer_diameter  = inner_diameter + 2*bevel;

    int border = 2;

    setBorder(BorderFactory.createEmptyBorder((int)(bevel + notch_thickness) + border, (int)bevel + border,
                                              (int)bevel + border, (int)bevel + border));

    Path2D phone = new Path2D.Double(Path2D.WIND_EVEN_ODD);

    // outside
    phone.append(new Double(0, 0, outer_diameter, outer_diameter, 90, 90, OPEN), true);
    phone.append(new Double(0, getHeight() - outer_diameter, outer_diameter, outer_diameter, 180, 90, OPEN), true);
    phone.append(new Double(getWidth() - outer_diameter, getHeight() - outer_diameter, outer_diameter, outer_diameter,
                -90, 90, OPEN), true);
    phone.append(new Double(getWidth() - outer_diameter, 0, outer_diameter, outer_diameter, 0, 90, OPEN), true);
    phone.closePath();

    // inside
    phone.append(new Double(getWidth() - notch_edge, bevel, tiny_diameter, tiny_diameter, 90, 90, OPEN), false);
    phone.append(new Double(getWidth() - notch_edge - notch_diameter, bevel + notch_thickness - notch_diameter,
                            notch_diameter, notch_diameter, 0, -90, OPEN), true);
    phone.append(new Double(notch_edge, bevel + notch_thickness - notch_diameter, notch_diameter, notch_diameter,
                 -90, -90, OPEN), true);
    phone.append(new Double(notch_edge - tiny_diameter, bevel, tiny_diameter, tiny_diameter, 0, 90, OPEN), true);
    phone.append(new Double(bevel, bevel, inner_diameter, inner_diameter, 90, 90, OPEN), true);
    phone.append(new Double(bevel, getHeight() - bevel - inner_diameter, inner_diameter, inner_diameter, 180, 90,
                 OPEN), true);
    phone.append(new Double(getWidth() - bevel - inner_diameter, getHeight() - bevel - inner_diameter, inner_diameter,
                 inner_diameter, -90, 90, OPEN), true);
    phone.append(new Double(getWidth() - bevel - inner_diameter, bevel, inner_diameter, inner_diameter, 0, 90, OPEN), true);
    phone.closePath();

    graphics_2d.fill(phone);
  }
}
