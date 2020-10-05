package gui;

import static java.awt.Color.*;
import static java.lang.Math.*;
import static javax.swing.KeyStroke.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

import graph.bipartite.*;
import graphics.*;
import graphics.Vector;

public class View extends JPanel {
  private static final long serialVersionUID = 2621550208556045621L;

  private Vector translate = new Vector(0, 0, 0);

  public View() {
    setBackground(white);
    setFocusable(true);

    ViewListener listener = new ViewListener();
    addMouseListener(listener);
    addMouseMotionListener(listener);

    bindKeys(() -> translateAction( 1,  0,  0), "A", "LEFT", "released D", "released RIGHT");
    bindKeys(() -> translateAction(-1,  0,  0), "D", "RIGHT", "released A", "released LEFT");
    bindKeys(() -> translateAction( 0,  1,  0), "SPACE", "released SHIFT");
    bindKeys(() -> translateAction( 0, -1,  0), "shift SHIFT", "released SPACE");
    bindKeys(() -> translateAction( 0,  0,  1), "S", "DOWN", "released W", "released UP");
    bindKeys(() -> translateAction( 0,  0, -1), "W", "UP", "released S", "released DOWN");

    bindKeys(this::transferFocusUpCycle, "ESCAPE");

    new Timer(10, event -> {
      Transform.translate(translate);
      repaint();
    }).start();
  }

  private void bindKeys(Runnable action, String... keystrokes) {
    String uuid = UUID.randomUUID().toString();
    for (String keystroke: keystrokes)
      getInputMap().put(getKeyStroke(keystroke), uuid);

    getActionMap().put(uuid, new AbstractAction() {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e) {
        action.run();
      }
    });
  }

  private void translateAction(double dx, double dy, double dz) {
    translate.x = min(1, max(-1, translate.x + dx));
    translate.y = min(1, max(-1, translate.y + dy));
    translate.z = min(1, max(-1, translate.z + dz));
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    Graphics2D graphics_2d = (Graphics2D)graphics;

    graphics_2d.translate(getWidth()/2, getHeight()/2);

    Vector[] corners = new Vector[] {
        new Vector(0, 0, 0),
        new Vector(0, 0, 10),
        new Vector(0, 10, 0),
        new Vector(0, 10, 10),
        new Vector(10, 0, 0),
        new Vector(10, 0, 10),
        new Vector(10, 10, 0),
        new Vector(10, 10, 10)
    };

    for (Vector first: corners) {
      for (Vector second: corners) {
        drawLine(graphics, first, second);
      }
    }

    graphics.setColor(gray);
    for (Person person: Person.people) {
      drawLine(graphics, new Vector(person.index*6, 0, 0), new Vector(person.index*6, -24, 0));
      Vector name_position = Transform.transform(new Vector(person.index*6, -24, 0));

      graphics_2d.drawString(person.name, (int)name_position.x + 2, (int)name_position.y + 2);
    }
  }

  private void drawLine(Graphics graphics, Vector start, Vector end) {
    Vector start_transformed = Transform.transform(start);
    Vector   end_transformed = Transform.transform(end);

    graphics.drawLine((int)start_transformed.x, (int)start_transformed.y,
                      (int)  end_transformed.x, (int)  end_transformed.y);
  }

  private class ViewListener extends MouseAdapter {
    private Point previous;

    @Override
    public void mousePressed(MouseEvent event) {
      requestFocusInWindow();
      previous = event.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        Transform.rotate((previous.y - event.getY())/1000.0, (event.getX() - previous.x)/1000.0);
        previous = event.getPoint();
    }
  }
}
