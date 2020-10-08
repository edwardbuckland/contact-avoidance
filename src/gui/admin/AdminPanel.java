package gui.admin;

import java.awt.*;

import javax.swing.*;

public class AdminPanel extends JPanel {
  private static final long serialVersionUID = 8504882476208078769L;

  public AdminPanel() {
    super(new BorderLayout());

    add(View.view);

    add(new Footer(), BorderLayout.SOUTH);
  }
}
