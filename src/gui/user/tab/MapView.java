package gui.user.tab;

import static java.awt.RenderingHints.*;
import static java.lang.Math.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

public class MapView extends UserTab {
  private static final long serialVersionUID = 5304720443805548092L;

  private double scale = 0.2;

  BufferedImage mapImage;
  Image scaledMapImage;

  public MapView() {
    try {
      mapImage = ImageIO.read(getClass().getResource("/parkville-campus-map.png"));
      scaleImage();
    } catch (IOException e) {
      e.printStackTrace();
    }

    header.add(new ZoomButton(true));
    header.add(new ZoomButton(false));
  }

  @Override
  protected int panelWidth() {
    return (int)(scale*mapImage.getWidth());
  }

  @Override
  protected int panelHeight() {
    return (int)(scale*mapImage.getHeight());
  }

  private void scaleImage() {
    scaledMapImage = mapImage.getScaledInstance(panelWidth(), panelHeight(), BufferedImage.SCALE_SMOOTH);
    repaint();
  }

  @Override
  protected void paintPanel(Graphics2D graphics_2d) {
    graphics_2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

    graphics_2d.drawImage(scaledMapImage, 0, 0, panelWidth(), panelHeight(), null);
  }

  private class ZoomButton extends JButton {
    private static final long serialVersionUID = 677908755413283564L;

    private ZoomButton(boolean zoom_in) {
      super(zoom_in? "+": "-");

      addActionListener(event -> {
        scale = min(1, max(0.1, scale + (zoom_in? 1: -1)*0.1));
        scaleImage();
      });
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(super.getPreferredSize().height, super.getPreferredSize().height);
    }
  }
}
