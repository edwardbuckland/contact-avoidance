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

package gui.user.tab.map;

import static gui.user.UserSelector.*;
import static java.awt.RenderingHints.*;
import static java.awt.event.KeyEvent.*;
import static java.awt.geom.AffineTransform.*;
import static java.lang.Math.*;
import static java.util.Collections.min;
import static javax.swing.KeyStroke.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

import graph.*;
import graph.bipartite.*;
import gui.*;
import gui.user.tab.*;

public class MapTab extends UserTab {
  private static final long                     serialVersionUID        = 5304720443805548092L;

  public static final int                       PERCENTAGE_INCREMENT    = 10;
  private static final int                      MARKER_SIZE             = 40;

  public static Map<Integer, BufferedImage>     images                  = new HashMap<>();

  public static boolean loadNextImage() {
    int percentage = images.isEmpty()? 100: min(images.keySet()) - PERCENTAGE_INCREMENT;

    try {
      images.put(percentage, ImageIO.read(MapTab.class.getResource("/image/parkville-campus-map" +
                                          percentage + ".png")));
    }
    catch (Exception e) {
      return false;
    }

    return true;
  }

  private int                                   percentage              = min(images.keySet());

  public MapTab() {
    super(new FlowLayout());

    int command_mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

    ZoomButton zoom_out_button = new ZoomButton(false);
    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(getKeyStroke(VK_MINUS, command_mask), "zoom-out");
    getActionMap().put("zoom-out", zoom_out_button.getAction());
    header.add(zoom_out_button);

    ZoomButton zoom_in_button = new ZoomButton(true);
    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(getKeyStroke(VK_EQUALS, command_mask), "zoom-in");
    getActionMap().put("zoom-in", zoom_in_button.getAction());
    header.add(zoom_in_button);

    addMouseMotionListener(new MapListener());
  }

  @Override
  protected int panelWidth() {
    return images.get(percentage).getWidth();
  }

  @Override
  protected int panelHeight() {
    return images.get(percentage).getHeight();
  }

  @Override
  protected void paintPanel(Graphics2D graphics_2d) {
    graphics_2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

    graphics_2d.drawImage(images.get(percentage), 0, 0, panelWidth(), panelHeight(), null);

    if (currentUser != null) {
      currentUser.activities()
                 .filter(Activity::approved)
                 .forEach(activity -> {
                   Path2D marker = new Path2D.Double(Path2D.WIND_EVEN_ODD);
                   marker.append(new CubicCurve2D.Double(0, 0, 0.25, -0.25, 0.5, -1, 0, -1), false);
                   marker.append(new CubicCurve2D.Double(0, -1, -0.5, -1, -0.25, -0.25, 0, 0), true);
                   marker.append(new Ellipse2D.Double(-0.15, -0.85, 0.3, 0.3), false);

                   double scale = MARKER_SIZE*(activity.selected()? 2: 1);
                   marker.transform(getScaleInstance(scale, scale));
                   marker.transform(getTranslateInstance(getX(activity), getY(activity)));

                   graphics_2d.setColor(Color.red);
                   graphics_2d.fill(marker);
                   graphics_2d.setColor(Color.black);
                   graphics_2d.draw(marker);

                   Rectangle2D bounds = graphics_2d.getFontMetrics().getStringBounds(activity.toString(),
                                                                                     graphics_2d);

                   OutlinedText.drawText(graphics_2d, activity.toString(), getX(activity) -
                                         bounds.getWidth()/2, getY(activity) + bounds.getHeight());
                 });
    }
  }

  private double getX(Activity activity) {
    return activity.locations.get(0).longtitude*panelWidth();
  }

  private double getY(Activity activity) {
    return activity.locations.get(0).latitude*panelHeight();
  }

  private Point2D getMarkerCenter(Activity activity) {
    return new Point2D.Double(getX(activity), getY(activity) - MARKER_SIZE);
  }

  private class ZoomButton extends JButton {
    private static final long           serialVersionUID        = 677908755413283564L;

    private ZoomButton(boolean zoom_in) {
      super(new AbstractAction(zoom_in? "+": "-") {
        private static final long       serialVersionUID        = -3256774310195693202L;

        @Override
        public void actionPerformed(ActionEvent event) {
          int new_percentage = percentage + (zoom_in? 1: -1)*PERCENTAGE_INCREMENT;

          if (images.containsKey(new_percentage)) {
            for (int i = 0; i < 2; i++)
              scrollBars()[i].setValue(scrollBars()[i].getValue()*new_percentage/percentage);

            SwingUtilities.invokeLater(() -> {
              percentage = new_percentage;
            });
          }
        }
      });
      
      setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(super.getPreferredSize().height, super.getPreferredSize().height);
    }
  }

  private class MapListener extends MouseAdapter {
    @Override
    public void mouseMoved(MouseEvent event) {
      if (currentUser != null) {
        currentUser.activities()
                   .filter(Activity::approved)
                   .filter(activity -> event.getPoint().distance(getMarkerCenter(activity)) < MARKER_SIZE)
                   .sorted((first, second) -> (int)signum(event.getPoint().distance(getMarkerCenter(first)) -
                                                          event.getPoint().distance(getMarkerCenter(second))))
                   .findFirst()
                   .ifPresentOrElse(Node::select, Node::clearSelection);
      }
    }
  }
}
