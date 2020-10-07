package gui;

import static graph.Graph.*;

import java.awt.*;

import javax.swing.*;

import graph.algorithm.*;

public class Footer extends JPanel {
  private static final long serialVersionUID = 3279546830043780324L;

  private static final String PLAY = "\u25b6";
  private static final String STOP = "\u25fc";

  private JLabel label = new JLabel();

  public Footer() {
    add(label);

    JButton play_pause_button = new JButton(PLAY);
    play_pause_button.addActionListener(event -> {
      if (play_pause_button.getText() == PLAY) {
        FruchtermanReingold.timer.start();
        play_pause_button.setText(STOP);
      }
      else {
        FruchtermanReingold.timer.stop();
        play_pause_button.setText(PLAY);
      }
    });
    add(play_pause_button);

    add(new JLabel("Attraction: "));
    JSlider attraction_slider = new JSlider();
    attraction_slider.addChangeListener(event -> FruchtermanReingold.attraction.set(attraction_slider.getValue()));
    add(attraction_slider);
  }

  @Override
  protected void paintComponent(Graphics g) {
    if (statistics != null) {
      String text = String.format("Average path length: %.4f Spread coefficient: %.4f", statistics.getAverage(),
                                  coefficient());

      if (!label.getText().equals(text))
        label.setText(text);
    }
  }
}
