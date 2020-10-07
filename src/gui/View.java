package gui;

import static graphics.Transform.*;
import static java.awt.AlphaComposite.*;
import static java.awt.Color.*;
import static java.lang.Math.*;
import static javax.swing.KeyStroke.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

import graph.*;
import graph.bipartite.*;
import graphics.Vector;

public class View extends JPanel {
  private static final long serialVersionUID = 2621550208556045621L;
  private static final double SPEED = 0.2;
  private static final double RESOLUTION = 100;
  private static final double ARROW_SIZE = 8;

  private static final int CUBE = 100;

  private static Graphics2D graphics2D;

  public static View view = new View();

  private Vector translate = new Vector(0, 0, 0);

  public View() {
    setBackground(white);
    setFocusable(true);

    ViewListener listener = new ViewListener();
    addMouseListener(listener);
    addMouseMotionListener(listener);

    bindKeys(() -> translateAction( SPEED/2,  0, 0), "A", "LEFT", "released D", "released RIGHT");
    bindKeys(() -> translateAction(-SPEED/2,  0, 0), "D", "RIGHT", "released A", "released LEFT");
    bindKeys(() -> translateAction( 0,  SPEED/3, 0), "SPACE", "released SHIFT");
    bindKeys(() -> translateAction( 0, -SPEED/3, 0), "shift SHIFT", "released SPACE");
    bindKeys(() -> translateAction( 0,  0,   SPEED), "S", "DOWN", "released W", "released UP");
    bindKeys(() -> translateAction( 0,  0,  -SPEED), "W", "UP", "released S", "released DOWN");

    bindKeys(this::transferFocusUpCycle, "ESCAPE");

    new Timer(10, event -> {
      translate(translate);
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
    translate.x = min(SPEED/2, max(-SPEED/2, translate.x + dx));
    translate.y = min(SPEED/3, max(-SPEED/3, translate.y + dy));
    translate.z = min(SPEED,   max(-SPEED,   translate.z + dz));
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    graphics2D = (Graphics2D)graphics;
    graphics2D.translate(getWidth()/2, getHeight()/2);
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    graphics2D.setComposite(getInstance(SRC_OVER, 0.5f));

    for (int i = -CUBE; i <= CUBE; i += 10)
      for (int j = -CUBE; j <= CUBE; j += 10) {
        drawPoint(new Vector( CUBE,     i,     j), 500, new Color(1f, (float)((i + CUBE)/2.0/CUBE), (float)((j + CUBE)/2.0/CUBE)));
        drawPoint(new Vector(-CUBE,     i,     j), 500, new Color(0f, (float)((i + CUBE)/2.0/CUBE), (float)((j + CUBE)/2.0/CUBE)));
        drawPoint(new Vector(    i,  CUBE,     j), 500, new Color((float)((i + CUBE)/2.0/CUBE), 1f, (float)((j + CUBE)/2.0/CUBE)));
        drawPoint(new Vector(    i, -CUBE,     j), 500, new Color((float)((i + CUBE)/2.0/CUBE), 0f, (float)((j + CUBE)/2.0/CUBE)));
        drawPoint(new Vector(    i,     j,  CUBE), 500, new Color((float)((i + CUBE)/2.0/CUBE), (float)((j + CUBE)/2.0/CUBE), 1f));
        drawPoint(new Vector(    i,     j, -CUBE), 500, new Color((float)((i + CUBE)/2.0/CUBE), (float)((j + CUBE)/2.0/CUBE), 0f));
      }

    graphics2D.setComposite(getInstance(SRC_OVER, 0.7f));
    graphics2D.setColor(lightGray);
    Person.people.forEach(Drawable::draw);

    graphics2D.setStroke(new BasicStroke(1.5f));
    graphics2D.setColor(black);
    Graph.nodes.forEach(Drawable::draw);

    graphics2D = null;
  }

  public static void drawLine(Vector start, Vector end) {
    double scaled_resolution = RESOLUTION/max(distance(start), distance(end));
    Vector step = end.minus(start).unit().divide(scaled_resolution);
    Vector current = start.copy();

    for (int i = 0; i < end.minus(start).norm()*scaled_resolution - 1; i++) {
      Point2D first = transform(current).point2D(), second = transform(current.plus(step)).point2D();

      if (Double.isFinite(first.getX() + first.getY() + second.getX() + second.getY()))
        graphics2D.draw(new Line2D.Double(transform(current).point2D(), transform(current.plus(step)).point2D()));

      current = current.plus(step);
    }
  }

  public static void drawArrow(Vector start, Vector end) {
    drawLine(start, end);

    Vector tip      = transform(end);
    Vector parallel = transform(start).minus(tip).unit();
    Vector normal   = new Vector(-parallel.y, parallel.x);

    Vector next_point = tip;

    Path2D arrow_head = new Path2D.Double();
    arrow_head.moveTo(next_point.x, next_point.y);

    next_point = tip.plus(parallel.times(ARROW_SIZE)).plus(normal.times(ARROW_SIZE/2));
    arrow_head.lineTo(next_point.x, next_point.y);

    next_point = tip.plus(parallel.times(ARROW_SIZE*0.75));
    arrow_head.lineTo(next_point.x, next_point.y);

    next_point = tip.plus(parallel.times(ARROW_SIZE)).plus(normal.times(-ARROW_SIZE/2));
    arrow_head.lineTo(next_point.x, next_point.y);

    arrow_head.closePath();

    graphics2D.fill(arrow_head);
  }

  public static void drawText(String text, Vector position) {
    Vector transformed = transform(position);

    Color color = graphics2D.getColor();
    Composite composite = graphics2D.getComposite();

    graphics2D.setColor(black);
    graphics2D.setComposite(getInstance(SRC_OVER, 0.7f));

    graphics2D.drawString(text, (float)(transformed.x + 5), (float)(transformed.y - 5));

    graphics2D.setColor(color);
    graphics2D.setComposite(composite);
  }

  public static void drawPoint(Vector position, double size, Color color) {
    double scaled_size = size/distance(position);
    Vector transformed = transform(position);

    Ellipse2D point = new Ellipse2D.Double(transformed.x - scaled_size/2,
                                           transformed.y - scaled_size/2,
                                           scaled_size, scaled_size);

    graphics2D.setColor(color);
    graphics2D.fill(point);

    graphics2D.setColor(black);
    graphics2D.draw(point);
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
        rotate((previous.y - event.getY())/1000.0, (event.getX() - previous.x)/1000.0);
        previous = event.getPoint();
    }
  }
}
