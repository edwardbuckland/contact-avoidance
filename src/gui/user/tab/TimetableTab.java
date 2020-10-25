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

package gui.user.tab;

import static graph.bipartite.Activity.*;
import static java.awt.GridBagConstraints.*;
import static java.lang.Math.*;
import static java.util.Calendar.*;
import static javax.swing.JOptionPane.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.*;

import javax.swing.*;
import javax.swing.Timer;

import graph.bipartite.*;
import gui.*;
import gui.user.*;

public class TimetableTab extends UserTab {
  private static final long     serialVersionUID    = 1432849598520383947L;

  private static final int      HEIGHT              = 1300;
  private static final int      SPACING             = 8;
  private static final int      LEFT_MARGIN         = 45;

  private AutoCompleteTextField textField           = new AutoCompleteTextField(activities);
  private JButton               registerButton      = new JButton("+");
  private JButton               monthButton;

  private Person                person;

  public TimetableTab() {
    super(new GridBagLayout());

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = HORIZONTAL;

    monthButton = new JButton("< " + getInstance().getDisplayName(MONTH, SHORT, getLocale()));
    header.add(monthButton, constraints);

    constraints.weightx = 1;

    textField.setEnabled(false);
    textField.addMouseListener(new GrowListener());
    header.add(textField, constraints);

    constraints.weightx = 0;

    registerButton.addActionListener(event -> {
      activities.stream()
                .filter(activity -> activity.toString().equals(textField.getText()))
                .forEach(person::addActivities);
      buildTimetable();
      textField.setText("");
    });
    registerButton.setEnabled(false);
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

    List<Activity> candidate_activities = activities.stream()
                                                    .filter(candidate -> person.activities().noneMatch(registered ->
                                                              registered.startTime < candidate.endTime &&
                                                              registered.endTime > candidate.startTime))
                                                    .collect(Collectors.toList());

    textField.options = candidate_activities;

    boolean enabled = !candidate_activities.isEmpty() && person != null;
    textField.setEnabled(enabled);
    registerButton.setEnabled(enabled);
  }

  private int timeToY(double time) {
    return (int)(SPACING + (HEIGHT - 2*SPACING)*time/24.0);
  }

  private class GrowListener extends MouseAdapter {
    private static final int    STEP_SIZE           = 5;

    private int                 step;
    private Dimension           initialSize         = monthButton.getPreferredSize();

    private Timer timer = new Timer(1, event -> {
      int new_width = max(0, min(initialSize.width, monthButton.getWidth() + step*STEP_SIZE));

      if (new_width == 0 || new_width == initialSize.width)
        ((Timer)event.getSource()).stop();

      monthButton.setPreferredSize(new Dimension(new_width, initialSize.height));
      monthButton.setMinimumSize(monthButton.getPreferredSize());
      monthButton.revalidate();
    });

    @Override
    public void mouseEntered(MouseEvent event) {
      step = -1;
      timer.restart();
    }

    @Override
    public void mouseExited(MouseEvent e) {
      step = 1;
      timer.restart();
    }
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
        if (showInternalConfirmDialog(getRootPane().getContentPane(), "Delete \"" + activity + "\"?",
            "Confirm delete", OK_CANCEL_OPTION, PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
          person.removeActivity(activity);
          buildTimetable();
        }
      });

      TimetableTab.this.add(this);
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
      activity.select();
    }

    @Override
    public void focusLost(FocusEvent e) {}
  }
}
