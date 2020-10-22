package gui.admin;

import java.awt.*;

import javax.swing.*;

import gui.admin.tab.*;

public class AdminPanel extends JTabbedPane {
  private static final long     serialVersionUID    = 8504882476208078769L;

  public AdminPanel() {
    addTab("Activities", null, new JScrollPane(new ActivitiesTable()), "Activities View");

    JPanel integrated_panel = new JPanel(new BorderLayout());
    integrated_panel.add(new Footer(), BorderLayout.SOUTH);
    integrated_panel.add(new PeopleView());
    addTab("People", null, integrated_panel, "People View");

    JPanel bipartite_panel = new JPanel(new BorderLayout());
    bipartite_panel.add(BipartiteView.view);
    bipartite_panel.add(new Footer(), BorderLayout.SOUTH);
    addTab("Bipartite", null, bipartite_panel, "Bipartite View");
  }
}
