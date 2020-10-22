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

  private double[][]                    similarityMatrix;

  SimilarityMatrixPanel(double[][] similarity_matrix) {
    similarityMatrix = similarity_matrix;
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    int n = similarityMatrix.length;

    if (n > 0) {
      Graphics2D graphics_2d = (Graphics2D)graphics;

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
          double red_value =   (similarity > 0.5? 2 - 2*similarity: 1)*255;
          double green_value = (similarity < 0.5?     2*similarity: 1)*255;

          graphics_2d.setColor(new Color((int)red_value, (int)green_value, 0));
          graphics_2d.fill(new Rectangle2D.Double(i*unit_size +   PADDING, j*unit_size +   PADDING,
                                                    unit_size - 2*PADDING,   unit_size - 2*PADDING));
        }

      graphics_2d.translate(min(width, height) + 8*PADDING, 0);
      graphics_2d.rotate(-PI/2);

      graphics_2d.setPaint(new GradientPaint(new Point2D.Double(-height - PADDING, 0), red,
                                             new Point2D.Double(        - PADDING, 0), green));

      graphics_2d.fillRect(-height - PADDING, 0, height + 2*PADDING, LEGEND_WIDTH/2);

      graphics_2d.setColor(black);

      drawAxisLabel(graphics_2d, "Similarity", -PADDING - height/2, CENTER_ALIGNMENT);
      drawAxisLabel(graphics_2d, " 0.0 ",      -PADDING - height,    RIGHT_ALIGNMENT);
      drawAxisLabel(graphics_2d, "1.0 ",       -PADDING,              LEFT_ALIGNMENT);
    }
  }

  private void drawAxisLabel(Graphics2D graphics_2d, String text, int x, double alignment) {
    graphics_2d.drawString(text, (int)(x - graphics_2d.getFontMetrics().stringWidth(text)*(1 - alignment)),
                           LEGEND_WIDTH - PADDING);
  }
}