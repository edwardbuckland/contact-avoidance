package gui.user;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import graph.bipartite.*;

public class Timetable extends JPanel {
  private static final long serialVersionUID = 1432849598520383947L;

  private static final int HEIGHT = 1500;
  private static final int MARGIN = 10;
  private static final int LEFT_MARGIN = 40;
  private static final int SPACING = 5;

  private Person currentUser;

  public Timetable() {
    super(null);
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    graphics.setColor(Color.gray);

    for (int i = 0; i <= 24; i++) {
      String time = Activity.timeString(i, false);

      graphics.drawString(time, LEFT_MARGIN - graphics.getFontMetrics().stringWidth(time),
                          timeToY(i) + graphics.getFontMetrics().getAscent()/2);
      graphics.drawLine(LEFT_MARGIN + SPACING, timeToY(i), getWidth() - SPACING, timeToY(i));
    }

    if (currentUser != UserSelector.currentUser) {
      removeAll();

      (currentUser = UserSelector.currentUser).activities()
                                              .forEach(activity -> add(new ActivityButton(activity)));
    }
  }

  private int timeToY(double time) {
    return (int)(MARGIN + (getHeight() - 2*MARGIN)*time/24.0);
  }

  @Override
  public Dimension getMinimumSize() {
    return new Dimension(super.getMinimumSize().width, HEIGHT);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(super.getPreferredSize().width, HEIGHT);
  }

  private class ActivityButton extends JButton implements FocusListener {
    private static final long serialVersionUID = 6495591952070127440L;

    private Activity activity;

    private ActivityButton(Activity activity) {
      super(activity.toString());

      this.activity = activity;

      setBounds(LEFT_MARGIN + SPACING, timeToY(activity.startTime), Timetable.this.getWidth() -
                LEFT_MARGIN - 2*SPACING, timeToY(activity.endTime) - timeToY(activity.startTime) + 2);

      addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
      activity.selected = true;
    }

    @Override
    public void focusLost(FocusEvent e) {
      activity.selected = false;
    }
  }
}
