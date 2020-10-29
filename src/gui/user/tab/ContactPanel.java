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

import static java.awt.GridBagConstraints.*;
import static javax.swing.JOptionPane.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.*;

import graph.bipartite.*;
import gui.admin.tab.*;

public class ContactPanel extends JPanel {
  private static final long     serialVersionUID    = -5890434546158970568L;

  private static final int      INSETS              = 2;

  private static final String   AM                  = "am";
  private static final String   PM                  = "pm";

  public ContactPanel() {
    super(new GridBagLayout());

    setBorder(BorderFactory.createEmptyBorder(0, INSETS, 0, INSETS));

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = BOTH;
    constraints.gridwidth = REMAINDER;
    constraints.weightx = 1;
    constraints.insets = new Insets(INSETS, 0, INSETS, 0);

    add(new JLabel("Name"), constraints);
    add(new JTextField(), constraints);

    add(new JLabel("Email"), constraints);
    add(new JTextField(), constraints);

    add(new JLabel("Proposed Activity"), constraints);

    JTextField name = new JTextField();
    add(name, constraints);

    constraints.gridwidth = 1;

    add(new JLabel("Start time"), constraints);

    constraints.weightx = 0;

    TimeSpinner start_hour = new TimeSpinner(1, 12);
    add(start_hour, constraints);

    add(new JLabel(":"), constraints);

    TimeSpinner start_minute = new TimeSpinner(0, 59);
    add(start_minute, constraints);

    constraints.gridwidth = REMAINDER;

    PeriodComboBox start_period = new PeriodComboBox();
    add(start_period, constraints);

    constraints.gridwidth = 1;
    constraints.weightx = 1;

    add(new JLabel("End time"), constraints);

    constraints.weightx = 0;

    TimeSpinner end_hour = new TimeSpinner(1, 12);
    add(end_hour, constraints);

    add(new JLabel(":"), constraints);

    TimeSpinner end_minute = new TimeSpinner(0, 59);
    add(end_minute, constraints);

    constraints.gridwidth = REMAINDER;

    PeriodComboBox end_period = new PeriodComboBox();
    add(end_period, constraints);

    constraints.weightx = 1;

    add(new JLabel("Comments:"), constraints);

    constraints.weighty = 1;

    add(new JTextArea(), constraints);

    constraints.weighty = 0;

    JButton submit_button = new JButton("Submit");
    submit_button.addActionListener(event -> {
      double start_time = getTime(start_hour, start_minute, start_period);
      double   end_time = getTime(  end_hour,   end_minute,   end_period);

      if (name.getText().isEmpty()) {
        showMessage("Activity name cannot be empty");
      }
      else if (start_time > end_time) {
        showMessage("Activity may not conclude before it has begun");
      }
      else {
        new Activity(name.getText(), start_time, end_time - start_time);

        ActivitiesTable.update();
        TimetableTab.timetable.buildTimetable();

        for (Component component: getComponents()) {
          if (component instanceof JTextComponent) {
            ((JTextComponent)component).setText("");
          }
        }

        showMessage("Request submitted");
      }
    });
    add(submit_button, constraints);
  }

  private double getTime(TimeSpinner hour, TimeSpinner minute, PeriodComboBox period) {
    return hour.value()%12 + minute.value()/60.0 + (period.getSelectedItem() == AM? 0: 12);
  }

  private void showMessage(String message) {
    showInternalMessageDialog(getRootPane().getContentPane(), message, "", PLAIN_MESSAGE);
  }

  private class TimeSpinner extends JSpinner {
    private static final long   serialVersionUID    = 2776297750782941556L;

    private TimeSpinner(int minimum, int maximum) {
      super(new SpinnerNumberModel(minimum, minimum, maximum, 1));

      setEditor(new NumberEditor(this, "00"));
    }

    private int value() {
      return (int)getValue();
    }
  }

  private class PeriodComboBox extends JComboBox<String> {
    private static final long   serialVersionUID    = -721664299896238433L;

    private PeriodComboBox() {
      super(new String[] {AM, PM});
    }
  }
}
