package gui.admin;

import javax.swing.*;

import graph.bipartite.*;

public class ActivityView extends JPanel {
  private static final long     serialVersionUID    = -2605691859436704986L;

  public ActivityView(Activity activity) {
    super(null);

    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    add(new JLabel(activity.toString()));

    add(new JLabel("Participants"));

    activity.edges.keySet()
                  .stream()
                  .map(PersonNode.class::cast)
                  .forEach(node -> add(new JLabel(node.person.name)));
  }
}
