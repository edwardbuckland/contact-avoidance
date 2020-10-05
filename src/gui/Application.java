package gui;

import java.awt.*;

import javax.swing.*;

public class Application extends JPanel {
  private static final long serialVersionUID = -6569935836612715941L;

  public static Application panel;

  private static String title = "MapAPL Demo";

  public static void main(String[] args) {
    JFrame frame = new JFrame(title);
    frame.setContentPane(panel = new Application());
    frame.setMinimumSize(new Dimension(1000, 600));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.pack();
    frame.setVisible(true);
  }

  private Application() {
    super(new GridBagLayout());

    GridBagConstraints constraint = new GridBagConstraints();
    constraint.fill = GridBagConstraints.BOTH;
    constraint.weightx = 1;
    constraint.insets = new Insets(4, 4, 4, 4);
    constraint.gridwidth = GridBagConstraints.REMAINDER;

    add(new JButton("test"), constraint);

    constraint.weighty = 1;

    add(new View(), constraint);

    constraint.weighty = 0;

    add(new JButton("test2"), constraint);
  }
}
