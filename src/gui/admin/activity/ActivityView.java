package gui.admin.activity;

import static graph.bipartite.Activity.*;
import static java.awt.GridBagConstraints.*;

import java.awt.*;

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

  public ActivityView(Activity activity) {
    super(new GridBagLayout());

    add(new JLabel(activity.name, JLabel.CENTER),                   DOUBLE_COLUMN);
    add(new JSeparator(),                                           DOUBLE_COLUMN);

    add(new JLabel("Start time: "),                                 LEFT_COLUMN);
    add(new JLabel(timeString(activity.startTime, true)),           RIGHT_COLUMN);

    add(new JLabel("End time: "),                                   LEFT_COLUMN);
    add(new JLabel(timeString(activity.endTime, true)),             RIGHT_COLUMN);

    add(new JLabel("Status: "),                                     LEFT_COLUMN);
    JLabel status_label = new JLabel(activity.status.toString());
    status_label.setForeground(activity.color());
    add(status_label,                                               RIGHT_COLUMN);

    add(new JLabel("Participants: "),                               LEFT_COLUMN);
    add(new JLabel(activity.edges.size() + ""),                     RIGHT_COLUMN);

    JScrollPane table_pane = new JScrollPane(new LocationsTable(activity));
    table_pane.setPreferredSize(new Dimension(0, 0));
    add(table_pane,                                                 columnConstraints(0, 2, 1));

    JButton random_split = new JButton("Random split");
    add(random_split,                                               DOUBLE_COLUMN);

    JButton clustered_split = new JButton("Clustered split");
    add(clustered_split,                                            DOUBLE_COLUMN);

    GridBagConstraints big_constraint = columnConstraints(2, 1, 1);

    big_constraint.fill       = BOTH;
    big_constraint.gridheight = REMAINDER;
    big_constraint.weightx    = 1;

    add(new JButton(" "), big_constraint);
  }
}
