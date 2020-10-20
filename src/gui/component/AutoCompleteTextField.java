package gui.component;

import static java.awt.event.KeyEvent.*;
import static javax.swing.SwingUtilities.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;

public class AutoCompleteTextField extends JTextField implements FocusListener, WindowFocusListener {
  private static final long         serialVersionUID        = 1817729898247259327L;

  private SuggestionsPanel          panel                   = new SuggestionsPanel();
  private JWindow                   window;

  private List<?>                   options;

  public AutoCompleteTextField(List<?> options) {
    this.options = options;

    addFocusListener(this);
  }

  @Override
  public void focusGained(FocusEvent event) {
    if (window == null) {
      window = new JWindow(getWindowAncestor(this));
      window.setContentPane(panel);
      window.addWindowFocusListener(this);
      window.setBackground(new Color(0, 0, 0, 0));
      window.setAlwaysOnTop(true);
    }

    transferFocus();

    invokeLater(() -> {
      window.pack();
      window.setLocation(getLocationOnScreen());
      window.setVisible(true);

      panel.textField.requestFocusInWindow();
    });
  }

  @Override
  public void focusLost(FocusEvent event) {}

  @Override
  public void windowGainedFocus(WindowEvent e) {
    updateOptions();
  }

  @Override
  public void windowLostFocus(WindowEvent e) {
    window.setVisible(false);
  }

  private void updateOptions() {
    setText(panel.textField.getText());
    panel.textField.setPreferredSize(getSize());

    panel.list.setListData(options.stream()
                                  .filter(option -> option.toString().toLowerCase().contains(getText().toLowerCase()))
                                  .toArray());

    window.pack();
  }

  private class SuggestionsPanel extends JPanel {
    private static final long       serialVersionUID        = -5520568909171146719L;

    private JTextField              textField               = new JTextField();
    private JList<Object>           list                    = new JList<>();

    private SuggestionsPanel() {
      super(new BorderLayout());

      textField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent event) {
          invokeLater(() -> {
            updateOptions();

            switch (event.getKeyCode()) {
            case VK_ESCAPE:
              window.setVisible(false);

              break;
            case VK_ENTER:
              window.setVisible(false);
              fireActionPerformed();
            }
          });
        }
      });
      add(textField, BorderLayout.NORTH);

      list.addListSelectionListener(event -> {
        if (list.getSelectedValue() != null)
          textField.setText(list.getSelectedValue().toString());
          updateOptions();
          window.setVisible(false);
          fireActionPerformed();
      });
      add(new JScrollPane(list));
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

    @Override
    public Insets getInsets() {
      return AutoCompleteTextField.this != null? AutoCompleteTextField.this.getParent().getInsets(): super.getInsets();
    }
  }
}
