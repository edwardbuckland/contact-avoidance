package gui.user.tab;

import java.awt.*;

import javax.swing.*;

public abstract class UserTab extends JPanel {
  private static final long serialVersionUID = -5757796270446851981L;

  protected JPanel header = new JPanel(null);
  private UserTabPanel panel = new UserTabPanel();
  private JScrollPane scrollPane = new JScrollPane(panel);

  protected UserTab() {
    super(new BorderLayout());

    header.setLayout(new BoxLayout(header, BoxLayout.LINE_AXIS));
    add(header, BorderLayout.NORTH);

    add(scrollPane, BorderLayout.CENTER);
  }

  @Override
  public Component add(Component comp) {
    return panel.add(comp);
  }

  @Override
  public void removeAll() {
    panel.removeAll();
  }

  protected int panelWidth() {
    return header.getWidth() - scrollPane.getVerticalScrollBar().getWidth() -
           scrollPane.getInsets().left - scrollPane.getInsets().right;
  }

  protected abstract int panelHeight();

  protected abstract void paintPanel(Graphics2D graphics_2d);

  private class UserTabPanel extends JPanel {
    private static final long serialVersionUID = 6149670756363370981L;

    private UserTabPanel() {
      super(null);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      paintPanel((Graphics2D)graphics);
    }

    @Override
    public Dimension getMinimumSize() {
      return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(panelWidth(), panelHeight());
    }

    @Override
    public Dimension getMaximumSize() {
      return getPreferredSize();
    }
  }
}
