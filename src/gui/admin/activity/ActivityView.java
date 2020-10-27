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

package gui.admin.activity;

import static graph.bipartite.Activity.*;
import static java.awt.GridBagConstraints.*;

import java.awt.*;
import java.awt.Dialog.*;
import java.awt.event.*;

import javax.swing.*;

import graph.bipartite.*;

public class ActivityView extends JPanel {
  private static final long                 serialVersionUID        = -2605691859436704986L;

  private static final Insets               INSETS                  = new Insets(5, 5, 5, 5);

  private static final GridBagConstraints   LEFT_COLUMN             = columnConstraints(0, 1, 0);
  private static final GridBagConstraints   RIGHT_COLUMN            = columnConstraints(1, 1, 0);
  private static final GridBagConstraints   DOUBLE_COLUMN           = columnConstraints(0, 2, 0);

  private static GridBagConstraints columnConstraints(int column, int width, double weighty) {
    GridBagConstraints constraints = new GridBagConstraints();

    constraints.gridx     = column;
    constraints.gridwidth = width;
    constraints.weighty   = weighty;
    constraints.insets    = INSETS;
    constraints.anchor    = column == 0? NORTHWEST: NORTHEAST;
    constraints.fill      = width > 1?   BOTH:      NONE;

    return constraints;
  }

  public static void createActivityViewDialog(Activity activity, Component component) {
    JDialog dialog = new JDialog(SwingUtilities.windowForComponent(component), "Manage Activity",
                                 ModalityType.APPLICATION_MODAL);
    dialog.setMinimumSize(new Dimension(800, 500));
    dialog.setContentPane(new ActivityView(activity));
    dialog.pack();
    dialog.setLocationRelativeTo(component);
    dialog.setVisible(true);
  }

  private Activity                          activity;

  public ActivityView(Activity activity) {
    super(new GridBagLayout());

    this.activity = activity;

    add(new JLabel(activity.name, JLabel.CENTER),                   DOUBLE_COLUMN);
    add(new JSeparator(),                                           DOUBLE_COLUMN);

    add(new JLabel("Start time: "),                                 LEFT_COLUMN);
    add(new JLabel(timeString(activity.startTime, true)),           RIGHT_COLUMN);

    add(new JLabel("End time: "),                                   LEFT_COLUMN);
    add(new JLabel(timeString(activity.endTime, true)),             RIGHT_COLUMN);

    add(new JLabel("Status: "),                                     LEFT_COLUMN);
    JLabel status_label = new JLabel(activity.status());
    status_label.setForeground(activity.color());
    add(status_label,                                               RIGHT_COLUMN);

    add(new JLabel("Participants: "),                               LEFT_COLUMN);
    add(new JLabel(activity.edges.size() + ""),                     RIGHT_COLUMN);

    JScrollPane table_pane = new JScrollPane(new LocationsTable(activity));
    table_pane.setPreferredSize(new Dimension(0, 0));
    add(table_pane,                                                 columnConstraints(0, 2, 1));

    if (activity.pending() || activity.scheduled()) {
      add(new SplitButton("Random", activity::randomSplit),         DOUBLE_COLUMN);
      add(new SplitButton("Clustered", activity::clusteredSplit),   DOUBLE_COLUMN);
    }
    if (activity.scheduled())
      add(new ActivityActionButton("Approve", activity::approve),   DOUBLE_COLUMN);
    if (!activity.approved())
      add(new ActivityActionButton("Reject", activity::reject),     DOUBLE_COLUMN);

    GridBagConstraints big_constraint = columnConstraints(2, 1, 1);

    big_constraint.fill       = BOTH;
    big_constraint.gridheight = REMAINDER;
    big_constraint.weightx    = 1;

    add(new SimilarityMatrixPanel(activity.similarityMatrix(), "similarity", false), big_constraint);

    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "dispose");
    getActionMap().put("dispose", new AbstractAction() {
      private static final long             serialVersionUID            = -6359204723151660L;

      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
  }

  private void dispose() {
    ((JDialog)SwingUtilities.getRoot(this)).dispose();
  }

  private class ActivityActionButton extends JButton {
    private static final long               serialVersionUID            = -4875816072232181047L;

    private ActivityActionButton(String text, Runnable action) {
      super(text);

      addActionListener(event -> {
        action.run();
        dispose();
      });
    }
  }

  private class SplitButton extends ActivityActionButton {
    private static final long               serialVersionUID            = 6262590133732957174L;

    private SplitButton(String text, Runnable action) {
      super(text + " split", action);
    }

    @Override
    protected void paintComponent(Graphics g) {
      setEnabled(!activity.locations.isEmpty());
      super.paintComponent(g);
    }
  }
}
