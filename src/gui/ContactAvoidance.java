/*
 *  Copyright (C) 2020 Edward Buckland. Some rights reserved.
 *
 *  This file is part of "Contact Avoidance".
 *
 *  "Contact Avoidance" is distributed under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the License, or (at your option)
 *  any later version.
 *
 *  "Contact Avoidance" is a demonstration application only and is therefore not intended for
 *  general use. "Contact Avoidance" is distributed WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 *  You should have received a copy of the GNU Affero General Public License along with "Contact
 *  Avoidance". If not, see <https://www.gnu.org/licenses/agpl-3.0.en.html>.
 */

package gui;

import static java.awt.Font.*;
import static javax.swing.BorderFactory.*;
import static javax.swing.Box.*;
import static javax.swing.WindowConstants.*;

import java.awt.*;

import javax.swing.*;

import example.*;
import gui.admin.*;
import gui.admin.tab.*;
import gui.user.*;
import gui.user.tab.map.*;

public class ContactAvoidance extends JPanel {
  private static final long         serialVersionUID    = -1151202649231299213L;

  private static final String       TITLE               = "Contact Avoidance";
  private static final int          SPACING             = 20;
  private static final int          PROGRESS_STEPS      = 25;

  public static void main(String[] args) {
    System.setProperty("apple.laf.useScreenMenuBar", "true");

    new ContactAvoidance();
  }

  private JWindow                   window              = new JWindow();
  private JButton                   launchButton        = new JButton("Launch");
  private JProgressBar              progressBar         = new JProgressBar(0, PROGRESS_STEPS);
  private JLabel                    progressLabel       = new SizedLabel(" ", PLAIN, 12);

  private ContactAvoidance() {
    super(null);

    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    setBorder(createEmptyBorder(SPACING, SPACING, SPACING, SPACING));

    launchButton.setPreferredSize(new Dimension(launchButton.getPreferredSize().width, progressBar.getPreferredSize().height
                                                + progressLabel.getPreferredSize().height));
    launchButton.setMaximumSize(launchButton.getPreferredSize());
    launchButton.setAlignmentX(CENTER_ALIGNMENT);

    progressBar.setVisible(false);
    progressBar.setStringPainted(true);

    progressLabel.setVisible(false);

    add(new SizedLabel(TITLE + " Demo", BOLD, 36));
    add(new SizedLabel("http://contactavoidance.edwardbuckland.com", PLAIN, 12));

    add(createVerticalStrut(SPACING));

    add(new SizedLabel("Edward Buckland", PLAIN, 18));
    add(new SizedLabel("Primary Supervisor: Prof Egemen Tanin", PLAIN, 14));
    add(new SizedLabel("Acknowledgements: Dr Nic Geard, Dr Cameron Zachreson", PLAIN, 14));

    add(createVerticalStrut(SPACING));

    add(launchButton);
    add(progressBar);
    add(progressLabel);


    add(createVerticalStrut(SPACING));

    add(new SizedLabel("Copyright © 2020 Edward Buckland. Some rights reserved:", ITALIC, 10));
    add(new SizedLabel("this program is distributed under the terms of the GNU Affero GPL", ITALIC, 10));

    launchButton.addActionListener(event -> new LaunchTask().execute());

    window.setContentPane(this);
    window.pack();
    window.setLocationRelativeTo(null);
    window.setVisible(true);
  }

  private class SizedLabel extends JLabel {
    private static final long       serialVersionUID    = 8532102921305900308L;

    private SizedLabel(String text, int style, int font_size) {
      super(text);

      setFont(new Font("Iowan Old Style", style, font_size));
      setAlignmentX(CENTER_ALIGNMENT);
    }
  }

  private class LaunchTask extends SwingWorker<Void, Void> {
    private LaunchTask() {
      launchButton.setVisible(false);

      progressBar.setVisible(true);
      progressLabel.setVisible(true);
    }

    @Override
    protected Void doInBackground() throws Exception {
      incrementProgress("Loading resources");       while (MapTab.loadNextImage())
      incrementProgress();

      incrementProgress("Creating frames");         JFrame admin_frame = new JFrame(TITLE + " - Administrator");
      incrementProgress();                          admin_frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
      incrementProgress();                          JFrame user_frame = new JFrame(TITLE + " - Mobile Application");
      incrementProgress();                          user_frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
      incrementProgress();                          user_frame.setResizable(false);

      incrementProgress("Creating admin pane");     admin_frame.setContentPane(new AdminPanel());
      incrementProgress("Creating user pane");      user_frame.setContentPane(new UserPanel());

      incrementProgress("Creating menu bars");      admin_frame.setJMenuBar(ExampleMenu.createMenuBar());
      incrementProgress();                          user_frame.setJMenuBar(ExampleMenu.createMenuBar());

      incrementProgress("Laying out components");   admin_frame.setPreferredSize(new Dimension(1000, 600));
      incrementProgress();                          user_frame.setPreferredSize(new Dimension(355, 718));
      incrementProgress();                          admin_frame.pack();
      incrementProgress();                          user_frame.pack();
      incrementProgress();                          admin_frame.setLocationRelativeTo(null);

      incrementProgress("Loading data");            ExampleGenerator.small();
      incrementProgress();                          ActivitiesTable.update();

      incrementProgress("Launching application");

      admin_frame.setVisible(true);
      user_frame.setVisible(true);

      return null;
    }

    @Override
    protected void done() {
      window.dispose();
    }

    private void incrementProgress(String... description) {
      if (!progressLabel.getText().trim().isEmpty())
        setProgress(getProgress() + 1);

      progressBar.setValue(getProgress());
      if (description.length > 0) {
        progressLabel.setText(description[0]);
      }

      try {
        Thread.sleep(10);
      }
      catch (Exception exception) {}
    }
  }
}
