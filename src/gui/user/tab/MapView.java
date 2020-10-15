package gui.user.tab;

import static java.awt.RenderingHints.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

public class MapView extends UserTab {
  private static final long             serialVersionUID        = 5304720443805548092L;

  private static final int              PERCENTAGE_INCREMENT    = 10;

  private int                           percentage;

  private Map<Integer, BufferedImage>   images                  = new HashMap<>();

  public MapView() {
    super(new FlowLayout());

    for (percentage = 100;; percentage -= PERCENTAGE_INCREMENT)
      try {
        images.put(percentage, ImageIO.read(getClass().getResource("/parkville-campus-map" + percentage + ".png")));
      } catch (Exception e) {
        percentage += 10;
        break;
      }

    header.add(new ZoomButton(true));
    header.add(new ZoomButton(false));
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
  }

  private class ZoomButton extends JButton {
    private static final long serialVersionUID = 677908755413283564L;

    private ZoomButton(boolean zoom_in) {
      super(zoom_in? "+": "-");

      addActionListener(event -> {
        int new_percentage = percentage + (zoom_in? 1: -1)*PERCENTAGE_INCREMENT;

        if (images.containsKey(new_percentage))
          SwingUtilities.invokeLater(() -> {
          for (int i = 0; i < 2; i++)
            scrollBars()[i].setValue(scrollBars()[i].getValue()*new_percentage/percentage);

          percentage = new_percentage;
          MapView.this.revalidate();
          });
      });
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(super.getPreferredSize().height, super.getPreferredSize().height);
    }
  }
}
