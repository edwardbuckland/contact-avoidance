package gui.user;

import static java.awt.RenderingHints.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

public class MapView extends JPanel {
  private static final long serialVersionUID = 5304720443805548092L;

  private double scale = 0.2;

  BufferedImage mapImage;
  Image scaledMapImage;

  public MapView() {
    try {
      mapImage = ImageIO.read(getClass().getResource("/parkville-campus-map.png"));
      scaledMapImage = mapImage.getScaledInstance((int)(scale*mapImage.getWidth()),
                         (int)(scale*mapImage.getHeight()), BufferedImage.SCALE_SMOOTH);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  @Override
  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension((int)(scale*mapImage.getWidth()), (int)(scale*mapImage.getHeight()));
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    ((Graphics2D)graphics).setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
    ((Graphics2D)graphics).setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);

    graphics.drawImage(scaledMapImage, 0, 0, getWidth(), getHeight(), null);
  }
}
