package gui.component;

import static java.awt.event.KeyEvent.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class AutoCompleteTextField extends JTextField implements FocusListener {
  private static final long         serialVersionUID        = 1817729898247259327L;

  private SuggestionsPanel          panel                   = new SuggestionsPanel();
  private JWindow                   window;

  public AutoCompleteTextField() {
    addFocusListener(this);
  }

  @Override
  public void focusGained(FocusEvent e) {
    if (window == null) {
      window = new JWindow(SwingUtilities.getWindowAncestor(this));
      window.setContentPane(panel);
    }

    if (!window.isVisible()) {
      window.pack();
      window.setLocation(getLocationOnScreen());
      window.setVisible(true);
      window.setAlwaysOnTop(true);
      panel.textField.requestFocusInWindow();
    }
    else {
      setText(panel.textField.getText());
      window.setVisible(false);
      transferFocus();
    }
  }

  @Override
  public void focusLost(FocusEvent e) {}

  private class SuggestionsPanel extends JPanel {
    private static final long       serialVersionUID        = -5520568909171146719L;

    private JTextField              textField               = new JTextField();

    private SuggestionsPanel() {
      super(null);
      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

      textField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          switch (e.getKeyCode()) {
          case VK_ESCAPE:
            AutoCompleteTextField.this.requestFocus();

            break;
          case VK_ENTER:
            window.setVisible(false);
            setText(textField.getText());
            fireActionPerformed();
          }
        }
      });
      add(textField);
    }

    @Override
    public Dimension getMinimumSize() {
      return new Dimension(AutoCompleteTextField.this.getWidth(), super.getMinimumSize().height);
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(AutoCompleteTextField.this.getWidth(), super.getPreferredSize().height);
    }

    @Override
    public Dimension getMaximumSize() {
      return new Dimension(AutoCompleteTextField.this.getWidth(), super.getMaximumSize().height);
    }
  }
}
