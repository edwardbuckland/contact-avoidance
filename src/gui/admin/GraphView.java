package gui.admin;

import static graph.Node.*;
import static graph.bipartite.Activity.*;
import static graphics.Transform.*;
import static java.awt.AlphaComposite.*;
import static java.awt.Color.*;
import static java.awt.RenderingHints.*;
import static java.lang.Math.*;
import static javax.swing.BorderFactory.*;
import static javax.swing.KeyStroke.*;

import java.awt.*;
import java.awt.Dialog.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.stream.*;

import javax.swing.*;
import javax.swing.Timer;

import graph.Node;
import graph.bipartite.*;
import graphics.Vector;
import gui.admin.activity.*;

public class GraphView extends JPanel {
  private static final long     serialVersionUID    = 2621550208556045621L;

  private static final double   SPEED               = 0.6;
  private static final double   RESOLUTION          = 20;
  private static final double   ARROW_SIZE          = 8;

  private static final int      CUBE                = 90;

  private static Graphics2D     graphics2D;

  public static GraphView            view                = new GraphView();
  public static boolean         drawAccessories     = true;

  private Vector                translate           = new Vector(0, 0, 0);

  public GraphView() {
    setBackground(white);
    setFocusable(true);

    Stream.of(new ViewListener())
          .peek(this::addMouseListener)
          .peek(this::addMouseMotionListener)
          .forEach(this::addFocusListener);

    bindKeys(() -> setTranslation( SPEED/2,  0, 0), "A", "LEFT", "released D", "released RIGHT");
    bindKeys(() -> setTranslation(-SPEED/2,  0, 0), "D", "RIGHT", "released A", "released LEFT");
    bindKeys(() -> setTranslation( 0,  SPEED/3, 0), "SPACE", "released SHIFT");
    bindKeys(() -> setTranslation( 0, -SPEED/3, 0), "shift SHIFT", "released SPACE");
    bindKeys(() -> setTranslation( 0,  0,   SPEED), "S", "DOWN", "released W", "released UP");
    bindKeys(() -> setTranslation( 0,  0,  -SPEED), "W", "UP", "released S", "released DOWN");

    bindKeys(this::transferFocusUpCycle, "ESCAPE");

    new Timer(5, event -> {
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

  private void setTranslation(double dx, double dy, double dz) {
    translate.x = min(SPEED/2, max(-SPEED/2, translate.x + dx));
    translate.y = min(SPEED/3, max(-SPEED/3, translate.y + dy));
    translate.z = min(SPEED,   max(-SPEED,   translate.z + dz));
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);

    graphics2D = (Graphics2D)graphics;

    AffineTransform transform = graphics2D.getTransform();
    graphics2D.translate(getWidth()/2, getHeight()/2);

    graphics2D.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
    graphics2D.setComposite(getInstance(SRC_OVER, 0.65f));

    for (int i = -CUBE; i <= CUBE; i += 10)
      for (int j = -CUBE; j <= CUBE; j += 10) {
        drawPoint(new Vector( CUBE,     i,     j), 500, new Color(1f, (float)((i + CUBE)/2.0/CUBE), (float)((j + CUBE)/2.0/CUBE)));
        drawPoint(new Vector(-CUBE,     i,     j), 500, new Color(0f, (float)((i + CUBE)/2.0/CUBE), (float)((j + CUBE)/2.0/CUBE)));
        drawPoint(new Vector(    i,  CUBE,     j), 500, new Color((float)((i + CUBE)/2.0/CUBE), 1f, (float)((j + CUBE)/2.0/CUBE)));
        drawPoint(new Vector(    i, -CUBE,     j), 500, new Color((float)((i + CUBE)/2.0/CUBE), 0f, (float)((j + CUBE)/2.0/CUBE)));
        drawPoint(new Vector(    i,     j,  CUBE), 500, new Color((float)((i + CUBE)/2.0/CUBE), (float)((j + CUBE)/2.0/CUBE), 1f));
        drawPoint(new Vector(    i,     j, -CUBE), 500, new Color((float)((i + CUBE)/2.0/CUBE), (float)((j + CUBE)/2.0/CUBE), 0f));
      }


    if (GraphView.drawAccessories) {
      graphics2D.setColor(lightGray);
      Person.people.forEach(Drawable::draw);
    }

    graphics2D.setStroke(new BasicStroke(1.5f));
    graphics2D.setColor(black);
    nodes.stream()
         .filter(node -> !(node instanceof Activity && node.edges.isEmpty()))
         .forEach(Drawable::draw);

    graphics2D.setTransform(transform);
    graphics2D = null;
  }

  public static void drawLine(Vector start, Vector end) {
    double scaled_resolution = RESOLUTION/max(distance(start), distance(end));
    Vector step = end.minus(start).unit().divide(scaled_resolution);
    Vector current = start.copy();

    Path2D line = null;

    for (int i = 0; i < max(1, end.minus(start).norm()*scaled_resolution); i++) {
      Vector transformed = transform(current);

      if (Double.isFinite(transformed.norm())) {
        if (line == null) {
          line = new Path2D.Double();
          line.moveTo(transformed.x, transformed.y);
        }
        else
          line.lineTo(transformed.x, transformed.y);
      }

      current = current.plus(step);
    }

    if (line != null) {
      current = transform(end);
      line.lineTo(current.x, current.y);

      graphics2D.draw(line);
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

    graphics2D.setColor(view.getBackground());
    graphics2D.translate(1, 1);
    for (int i = 0, j = 1; i < 4; i += j = 1 - j) {
      graphics2D.drawString(text, (float)(transformed.x + 5), (float)(transformed.y - 5));
      graphics2D.translate((i - 1)%2, (i - 2)%2);
    }
    graphics2D.translate(-1, -1);

    graphics2D.setColor(black);
    graphics2D.drawString(text, (float)(transformed.x + 5), (float)(transformed.y - 5));

    graphics2D.setColor(color);
  }

  public static void drawPoint(Vector position, double size, Color color) {

    double scaled_size = size/distance(position);
    Vector transformed = transform(position);

    Ellipse2D point = new Ellipse2D.Double(transformed.x - scaled_size/2,
                                           transformed.y - scaled_size/2,
                                           scaled_size, scaled_size);

    Stroke stroke = graphics2D.getStroke();

    graphics2D.setStroke(new BasicStroke((float)max(scaled_size/50, 0.4)));

    graphics2D.setColor(color);
    graphics2D.fill(point);

    graphics2D.setColor(black);
    graphics2D.draw(point);

    graphics2D.setStroke(stroke);
  }

  private class ViewListener extends MouseAdapter implements FocusListener {
    private Point previous;

    @Override
    public void mousePressed(MouseEvent event) {
      requestFocusInWindow();
      previous = event.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
      activities.stream()
                .filter(activity -> {
                  Vector screen_point = transform(activity.location).plus(new Vector(getWidth()/2, getHeight()/2, 0));
                  return event.getPoint().distance(screen_point.x, screen_point.y) < 200/distance(activity.location);
                })
                .sorted((first, second) -> (int)signum(distance(first.location) - distance(second.location)))
                .findFirst()
                .ifPresentOrElse(Node::select, Node::clearSelection);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
      rotate((previous.y - event.getY())/1000.0, (event.getX() - previous.x)/1000.0);
      previous = event.getPoint();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
      if (selectedActivity() != null)
      {
        JDialog dialog = new JDialog(SwingUtilities.windowForComponent(GraphView.this), "Manage Activity", ModalityType.APPLICATION_MODAL);
        dialog.setMinimumSize(new Dimension(800, 500));
        dialog.setContentPane(new ActivityView(selectedActivity()));
        dialog.pack();
        dialog.setLocationRelativeTo(GraphView.this);
        dialog.setVisible(true);
      }
    }

    @Override
    public void focusGained(FocusEvent event) {
      setBorder(createLineBorder(darkGray, 2));
    }

    @Override
    public void focusLost(FocusEvent event) {
      setTranslation(0, 0, 0);
      setBorder(createLineBorder(darkGray, 1));
    }
  }
}
