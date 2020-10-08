package gui.user;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import graph.bipartite.*;

public class UserSelector extends JPanel {
  private static final long serialVersionUID = -1812097228826180716L;

  public static Person currentUser;

  private JLabel label = new JLabel();
  private JButton button = new JButton();

  public UserSelector() {
    super(new BorderLayout());

    label.setHorizontalAlignment(JLabel.CENTER);
    add(label);

    button.addActionListener(event -> {
      String name = JOptionPane.showInputDialog(this, "Name: ", "Select User", JOptionPane.PLAIN_MESSAGE);
      if (name != null) {
        Optional<Person> new_user = Person.people.stream()
                                          .filter(person -> person.name.equals(name))
                                          .findFirst();

        if (new_user.isPresent())
          currentUser = new_user.get();
        else
          currentUser = new Person(name);
      }
    });
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
