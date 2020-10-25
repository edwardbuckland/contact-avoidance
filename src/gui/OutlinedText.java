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

package gui;

import static java.awt.Color.*;

import java.awt.*;

public class OutlinedText {
  public static void drawText(Graphics2D graphics_2d, String text, double x, double y) {
    Color color = graphics_2d.getColor();

    graphics_2d.setColor(graphics_2d.getBackground());
    graphics_2d.translate(1, 1);
    for (int i = 0, j = 1; i < 4; i += j = 1 - j) {
      graphics_2d.drawString(text, (float)(x + 5), (float)(y - 5));
      graphics_2d.translate((i - 1)%2, (i - 2)%2);
    }
    graphics_2d.translate(-1, -1);

    graphics_2d.setColor(black);
    graphics_2d.drawString(text, (float)(x + 5), (float)(y - 5));

    graphics_2d.setColor(color);
  }
}
