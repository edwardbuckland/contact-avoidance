package gui;

import java.awt.*;

import javax.swing.*;

import gui.admin.*;
import gui.user.*;

public class Application{
  public static void main(String[] args) {
    JFrame admin = new JFrame("MapAPL Demo");
    admin.setContentPane(new AdminPanel());
    admin.setMinimumSize(new Dimension(1000, 600));
    admin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    admin.setLocationRelativeTo(null);
    admin.pack();
    admin.setVisible(true);

    JFrame user = new JFrame("MapAPL Demo App");
    user.setContentPane(new UserPanel());
    user.setMinimumSize(new Dimension(355, 718));
    user.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    user.setResizable(false);
    user.pack();
    user.setVisible(true);
  }
}
