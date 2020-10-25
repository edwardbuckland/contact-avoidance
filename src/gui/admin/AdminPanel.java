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

package gui.admin;

import java.awt.*;

import javax.swing.*;

import gui.admin.tab.*;

public class AdminPanel extends JTabbedPane {
  private static final long     serialVersionUID    = 8504882476208078769L;

  public AdminPanel() {
    addTab("Activities", null, new JScrollPane(new ActivitiesTable()), "Activities View");

    JPanel bipartite_panel = new JPanel(new BorderLayout());
    bipartite_panel.add(BipartiteView.view);
    bipartite_panel.add(new Footer(), BorderLayout.SOUTH);
    addTab("Bipartite", null, bipartite_panel, "Bipartite View");

    addTab("Map", null, new AggregatedMapView(), "Map View");

    JPanel integrated_panel = new JPanel(new BorderLayout());
    integrated_panel.add(new Footer(), BorderLayout.SOUTH);
    integrated_panel.add(new PeopleView());
    addTab("People", null, integrated_panel, "People View");
  }
}
