package gui.admin.activity;

import static java.util.Arrays.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.*;

import javax.swing.*;
import javax.swing.table.*;

import graph.bipartite.*;
import gui.component.*;
import gui.user.tab.map.*;

public class LocationsTable extends JTable {
  private static final long             serialVersionUID        = 3621498615403139608L;

  private Activity                      activity;

  public LocationsTable(Activity activity) {
    this.activity = activity;

    BuildingsTableModel model = new BuildingsTableModel();
    setModel(model);

    if (activity.pending()) {
      DefaultCellEditor editor = new DefaultCellEditor(new AutoCompleteTextField());
      editor.setClickCountToStart(1);

      setDefaultEditor(Object.class, editor);
      setDefaultRenderer(Object.class, new BuildingsRenderer());
      getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "delete");
      getActionMap().put("delete", new AbstractAction() {
        private static final long         serialVersionUID        = -8243629630787622182L;

        @Override
        public void actionPerformed(ActionEvent e) {
          int index = getSelectedRow();

          if (index != -1) {
            model.removeRow(index);
            activity.locations.remove(index);
            getParent().getParent().getParent().repaint();
          }
        }
      });
    }
  }

  private class BuildingsTableModel extends DefaultTableModel {
    private static final long           serialVersionUID        = -28099522645792295L;

    private BuildingsTableModel() {
      super(activity.locations
                    .stream()
                    .map(building -> new Vector<>(asList(building)))
                    .collect(Collectors.toCollection(Vector::new)),
            new Vector<>(asList("Locations")));

      addRow(new Object[0]);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return activity.pending() && row == getRowCount() - 1;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
      if (!value.toString().isEmpty())
        try {
          Building building = Building.getValue(value.toString());
          activity.locations.add(building);
          super.setValueAt(building, row, column);
          addRow(new Object[0]);
          getParent().getParent().getParent().repaint();
        }
        catch (Exception e) {
          JOptionPane.showMessageDialog(getRootPane(), "Building \"" + value + "\" could not be found.");
        }
    }
  }

  private class BuildingsRenderer extends DefaultTableCellRenderer {
    private static final long               serialVersionUID        = -3872579756500107424L;

    private final JLabel                    ADD_LOCATION_LABEL      = new JLabel("+", JLabel.CENTER);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      return row == table.getRowCount() - 1? ADD_LOCATION_LABEL: super.getTableCellRendererComponent(table, value, isSelected,
                                                                                                     hasFocus, row, column);
    }
  }
}
