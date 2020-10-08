package gui.user.tab;

import static graph.bipartite.Activity.*;
import static javax.swing.JOptionPane.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import graph.bipartite.*;
import gui.user.*;

public class Timetable extends UserTab {
  private static final long serialVersionUID = 1432849598520383947L;

  private static final int HEIGHT = 1500;
  private static final int MARGIN = 10;
  private static final int LEFT_MARGIN = 45;
  private static final int SPACING = 5;

  private Person person;

  private JComboBox<Activity> candidate_activities = new JComboBox<>();

  public Timetable() {
    header.add(candidate_activities);

    JButton register_button = new JButton("+");
    register_button.addActionListener(event -> {
      person.addActivities((Activity)candidate_activities.getSelectedItem());
      buildTimetable();
    });
    header.add(register_button);
  }

  @Override
  protected void paintPanel(Graphics2D graphics_2d) {
    graphics_2d.setColor(Color.gray);

    for (int i = 0; i <= 24; i++) {
      String time = Activity.timeString(i, false);

      graphics_2d.drawString(time, LEFT_MARGIN - graphics_2d.getFontMetrics().stringWidth(time),
                          timeToY(i) + graphics_2d.getFontMetrics().getAscent()/2);
      graphics_2d.drawLine(LEFT_MARGIN + SPACING, timeToY(i), panelWidth() - SPACING, timeToY(i));
    }

    if (person != UserSelector.currentUser)
      buildTimetable();
  }

  @Override
  protected int panelHeight() {
    return HEIGHT;
  }

  private void buildTimetable() {
    removeAll();

    (person = UserSelector.currentUser).activities()
                                       .forEach(ActivityButton::new);

    candidate_activities.removeAllItems();

    activities.stream()
              .filter(candidate -> person.activities()
                                         .noneMatch(registered -> registered.startTime < candidate.endTime &&
                                                                  registered.endTime > candidate.startTime))
              .forEach(candidate_activities::addItem);
  }

  private int timeToY(double time) {
    return (int)(MARGIN + (HEIGHT - 2*MARGIN)*time/24.0);
  }

  private class ActivityButton extends JButton implements FocusListener {
    private static final long serialVersionUID = 6495591952070127440L;

    private Activity activity;

    private ActivityButton(Activity activity) {
      super(activity.toString());

      this.activity = activity;

      setBounds(LEFT_MARGIN + SPACING, timeToY(activity.startTime), panelWidth() -
                LEFT_MARGIN - 2*SPACING, timeToY(activity.endTime) - timeToY(activity.startTime) + 2);

      addFocusListener(this);

      addActionListener(event -> {
        if (showConfirmDialog(this, "Delete \"" + activity + "\"?", "Confirm delete", OK_CANCEL_OPTION,
            PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
          person.removeActivity(activity);
          buildTimetable();
        }
      });

      Timetable.this.add(this);
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
