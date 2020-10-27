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

package gui.admin.activity;

import static java.awt.Color.*;
import static java.lang.Math.*;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

public class SimilarityMatrixPanel extends JPanel {
  private static final long             serialVersionUID        = -312545537565221736L;

  private static final int              LEGEND_WIDTH            = 30;
  private static final int              PADDING                 = 2;

  protected double[][]                  similarityMatrix;

  private String                        axisLabel;
  private boolean                       coloursReversed;

  public SimilarityMatrixPanel(double[][] similarity_matrix, String axis_label, boolean colours_reversed) {
    similarityMatrix = similarity_matrix;
    axisLabel = axis_label;
    coloursReversed = colours_reversed;
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    int n = similarityMatrix.length;

    if (n > 0) {
      Graphics2D graphics_2d = (Graphics2D)graphics;

      AffineTransform transform = graphics_2d.getTransform();

      int width = getWidth() - LEGEND_WIDTH - 10*PADDING;
      int height = getHeight();

      if (width > height)
        graphics_2d.translate((width - height)/2.0, 0);
      else
        graphics_2d.translate(0, (height - width)/2.0);

      double unit_size = (double)min(width, height)/n;

      for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++) {
          double similarity = similarityMatrix[i][j];

          graphics_2d.setColor(colourMap(similarity));
          graphics_2d.fill(new Rectangle2D.Double(i*unit_size +   PADDING, j*unit_size +   PADDING,
                                                    unit_size - 2*PADDING,   unit_size - 2*PADDING));
        }

      graphics_2d.translate(min(width, height) + 8*PADDING, 0);
      graphics_2d.rotate(-PI/2);

      float[] fractions = new float[100];
      Color[] colours = new Color[fractions.length];
      for (int i = 0; i < fractions.length; i++) {
        fractions[i] = i/(fractions.length - 1f);
        colours[i] = colourMap(fractions[i]);
      }

      graphics_2d.setPaint(new LinearGradientPaint(new Point2D.Double(-height - PADDING, 0),
                                                   new Point2D.Double(        - PADDING, 0),
                                                   fractions, colours));

      graphics_2d.fillRect(-height - PADDING, 0, height + 2*PADDING, LEGEND_WIDTH/2);

      graphics_2d.setColor(black);

      drawAxisLabel(graphics_2d, axisLabel, -PADDING - height/2, CENTER_ALIGNMENT);
      drawAxisLabel(graphics_2d, " 0.0",    -PADDING - height,    RIGHT_ALIGNMENT);
      drawAxisLabel(graphics_2d,  "1.0",    -PADDING,              LEFT_ALIGNMENT);

      graphics_2d.setTransform(transform);
    }
  }

  private Color colourMap(double similarity) {
    if (coloursReversed)
      similarity = 1 - similarity;

    double   red_value = (similarity > 0.5? 2 - 2*similarity: 1)*255;
    double green_value = (similarity < 0.5?     2*similarity: 1)*255;

    return new Color((int)red_value, (int)green_value, 0);
  }

  private void drawAxisLabel(Graphics2D graphics_2d, String text, int x, double alignment) {
    graphics_2d.drawString(text, (int)(x - graphics_2d.getFontMetrics().stringWidth(text)*(1 - alignment)),
                           LEGEND_WIDTH - PADDING);
  }
}