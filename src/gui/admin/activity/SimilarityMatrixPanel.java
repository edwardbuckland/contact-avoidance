package gui.admin.activity;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

public class SimilarityMatrixPanel extends JPanel {
  private static final long               serialVersionUID        = -312545537565221736L;

  private double[][]                      similarityMatrix;

  SimilarityMatrixPanel(double[][] similarity_matrix) {
    similarityMatrix = similarity_matrix;
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    Graphics2D graphics_2d = (Graphics2D)graphics;

    if (getWidth() > getHeight())
      graphics_2d.translate((getWidth() - getHeight())/2, 0);
    else
      graphics_2d.translate(0, (getHeight() - getWidth())/2);

    int n = similarityMatrix.length;

    double unit_size = Math.min(getWidth(), getHeight())/n;

    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++) {
        double similarity = similarityMatrix[i][j];
        double red_value =   (similarity > 0.5? 2 - 2*similarity: 1)*255;
        double green_value = (similarity < 0.5?     2*similarity: 1)*255;

        graphics_2d.setColor(new Color((int)red_value, (int)green_value, 0));
        graphics_2d.fill(new Rectangle2D.Double(i*unit_size + 2, j*unit_size + 2,
                                                  unit_size - 4,   unit_size - 4));
      }
  }
}