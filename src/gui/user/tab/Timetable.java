package gui.user.tab;

import static graph.bipartite.Activity.*;
import static java.awt.GridBagConstraints.*;
import static javax.swing.JOptionPane.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import graph.bipartite.*;
import gui.user.*;

public class Timetable extends UserTab {
  private static final long     serialVersionUID    = 1432849598520383947L;

  private static final int      HEIGHT              = 1500;
  private static final int      SPACING             = 8;
  private static final int      LEFT_MARGIN         = 45;

  private JComboBox<Activity>   candidateActivities = new JComboBox<>();
  private JButton               registerButton      = new JButton("+");

  private Person                person;

  public Timetable() {
    super(new GridBagLayout());

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = HORIZONTAL;
    constraints.weightx = 1;
    constraints.gridwidth = REMAINDER;

    header.add(new JTextField(), constraints);

    constraints.gridwidth = RELATIVE;

    header.add(candidateActivities, constraints);

    constraints.weightx = 0;

    registerButton.addActionListener(event -> {
      person.addActivities((Activity)candidateActivities.getSelectedItem());
      buildTimetable();
    });
    header.add(registerButton, constraints);
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

    candidateActivities.removeAllItems();

    activities.stream()
              .filter(candidate -> person.activities()
                                         .noneMatch(registered -> registered.startTime < candidate.endTime &&
                                                                  registered.endTime > candidate.startTime))
              .forEach(candidateActivities::addItem);

    registerButton.setEnabled(candidateActivities.getSelectedIndex() != -1);
  }

  private int timeToY(double time) {
    return (int)(SPACING + (HEIGHT - 2*SPACING)*time/24.0);
  }

  private class ActivityButton extends JButton implements FocusListener {
    private static final long serialVersionUID = 6495591952070127440L;

    private Activity activity;

    private ActivityButton(Activity activity) {
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
    protected void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);

      if (activity != null)
      {
        if (!getText().equals(activity.toString()))
          setText(activity.toString());

        graphics.setColor(activity.color());
        graphics.fillOval(SPACING, SPACING, SPACING, SPACING);
      }
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
