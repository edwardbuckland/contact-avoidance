package gui;

import static graph.Graph.*;

import java.awt.*;

import javax.swing.*;

import graph.algorithm.*;

public class Footer extends JPanel {
  private static final long serialVersionUID = 3279546830043780324L;

  private JLabel label = new JLabel();

  public Footer() {
    add(label);

    JToggleButton play_pause_button = new JToggleButton("\u25b6");
    play_pause_button.addActionListener(event -> {
      if (play_pause_button.isSelected()) {
        FruchtermanReingold.timer.start();
        play_pause_button.setText("\u25fc");
      }
      else {
        FruchtermanReingold.timer.stop();
        play_pause_button.setText("\u25b6");
      }
    });
    add(play_pause_button);

    add(new JLabel("Temperature: "));
    JSlider temperature_slider = new JSlider();
    temperature_slider.addChangeListener(event -> FruchtermanReingold.temperature.set(temperature_slider.getValue()));
    add(temperature_slider);

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
