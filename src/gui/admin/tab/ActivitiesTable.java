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

package gui.admin.tab;

import static graph.bipartite.Activity.*;
import static java.lang.String.*;
import static java.util.stream.Collectors.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.function.*;

import javax.swing.*;
import javax.swing.table.*;

import graph.bipartite.*;
import gui.admin.activity.*;

public class ActivitiesTable extends JTable {
  private static final long         serialVersionUID        = 6480308553783945657L;

  private static final String       NAME                    = "Name";
  private static final String       STATUS                  = "Status";
  private static final String       START_TIME              = "Start time";
  private static final String       END_TIME                = "End time";
  private static final String       PARTICIPANTS            = "Participants";
  private static final String       LOCATIONS               = "Locations";

  private static ActivitiesTable    table;

  public static void update() {
    table.model.update();
  }

  private ActivitiesTableModel      model                   = new ActivitiesTableModel();

  public ActivitiesTable() {
    super(null);

    table = this;

    setModel(model);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        if (e.getClickCount() > 1 && getSelectedRow() != -1)
          ActivityView.createActivityViewDialog((Activity)getValueAt(getSelectedRow(), 0), getParent());
      }
    });

    setDefaultRenderer(Object.class, new StatusRenderer());

    update();
  }

  private class ActivitiesTableModel extends DefaultTableModel {
    private static final long       serialVersionUID        = 2010801522400352715L;

    private ActivitiesTableModel() {
      super(new Object[0][0], new String[] {NAME, STATUS, START_TIME, END_TIME, PARTICIPANTS, LOCATIONS});
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }

    private void update() {
      getDataVector().removeAllElements();
      setRowSorter(null);

      activities.forEach(activity -> {
        addRow(Collections.nCopies(getColumnCount(), activity).toArray());
      });

      TableRowSorter<ActivitiesTableModel> sorter = new TableRowSorter<>(model);
      sorter.setStringConverter(new ActivitiesTableConverter());
      setRowSorter(sorter);
    }
  }

  private class ActivitiesTableConverter extends TableStringConverter {
    @Override
    public String toString(TableModel model, int row, int column) {
      Activity activity = (Activity)model.getValueAt(row, column);

      Function<Number, String> number_to_string = number -> String.format("%010f", number.doubleValue());

      switch (model.getColumnName(column)) {
      case NAME:
        return activity.name;

      case STATUS:
        return activity.approved()? "0": activity.scheduled()? "1": activity.pending()? "2": "3";

      case START_TIME:
        return number_to_string.apply(activity.startTime);

      case END_TIME:
        return number_to_string.apply(activity.endTime);

      case PARTICIPANTS:
        return number_to_string.apply(activity.edges.size());

      case LOCATIONS:
        return number_to_string.apply(activity.locations.size());

      default:
        return "";
      }
    }
  }

  private class StatusRenderer extends DefaultTableCellRenderer {
    private static final long       serialVersionUID        = -8260022819285161937L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      Activity activity = (Activity)value;

      Function<Object, Component> super_component = new_value ->
        super.getTableCellRendererComponent(table, new_value, isSelected, hasFocus, row, column);

      super_component.apply("").setForeground(Color.black);

      switch (getColumnName(column)) {
      case NAME:
        return super_component.apply(activity.name);

      case STATUS:
        Component component = super_component.apply(activity.status());
        component.setForeground(activity.color());
        return component;

      case START_TIME:
        return super_component.apply(timeString(activity.startTime, true));

      case END_TIME:
        return super_component.apply(timeString(activity.endTime, true));

      case PARTICIPANTS:
        return super_component.apply(activity.edges.size());

      case LOCATIONS:
        return super_component.apply(join(", ", activity.locations.stream().map(Object::toString).collect(toList())));

      default:
        return null;
      }
    }
  }
}
