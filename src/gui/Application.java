package gui;

import static javax.swing.WindowConstants.*;

import java.awt.*;

import javax.swing.*;

import example.*;
import gui.admin.*;
import gui.user.*;

public class Application{
  public static void main(String[] args) {
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    
    JFrame admin = new JFrame("MapAPL Demo");
    admin.setContentPane(new AdminPanel());
    admin.setJMenuBar(ExampleMenu.createMenuBar());
    admin.setPreferredSize(new Dimension(1000, 600));
    admin.setDefaultCloseOperation(EXIT_ON_CLOSE);
    admin.pack();
    admin.setLocationRelativeTo(null);

    JFrame user = new JFrame("MapAPL Demo App");
    user.setContentPane(new UserPanel());
    user.setJMenuBar(ExampleMenu.createMenuBar());
    user.setPreferredSize(new Dimension(355, 718));
    user.setDefaultCloseOperation(EXIT_ON_CLOSE);
    user.setResizable(false);
    user.pack();

    admin.setVisible(true);
    user.setVisible(true);
  }
}
