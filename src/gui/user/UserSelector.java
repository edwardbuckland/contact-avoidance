package gui.user;

import java.awt.*;

import javax.swing.*;

import graph.bipartite.*;

public class UserSelector extends JPanel {
  private static final long serialVersionUID = -1812097228826180716L;

  public static Person currentUser;

  private JLabel label = new JLabel();
  private JButton button = new JButton();

  public UserSelector() {
    super(new BorderLayout());

    add(label);
    add(button, BorderLayout.EAST);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (currentUser == null)
      button.setText("Log in...");
    else {
      button.setText("Switch Users...");
      if (label.getText() != currentUser.name)
        label.setText(currentUser.name);
    }
  }
}
