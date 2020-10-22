package example;

import java.awt.event.*;

import javax.swing.*;

public class ExampleMenu extends JMenu {
  private static final long         serialVersionUID    = -3782680998637307965L;

  public static JMenuBar createMenuBar() {
    JMenuBar menu_bar = new JMenuBar();
    menu_bar.add(new ExampleMenu());
    return menu_bar;
  }

  private ExampleMenu() {
    super("Examples");

    ExampleController.examples.forEach((name, example) -> add(new JMenuItem(new AbstractAction(name) {
      private static final long     serialVersionUID    = 2398512433134647385L;
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          ExampleController.loadExample(example.getDeclaredConstructor().newInstance());
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    })));
  }
}
