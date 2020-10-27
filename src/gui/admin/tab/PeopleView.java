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

package gui.admin.tab;

import static java.awt.BorderLayout.*;
import static java.util.Arrays.*;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import graph.algorithm.*;
import gui.admin.activity.*;

public class PeopleView extends JPanel {
  private static final long     serialVersionUID        = 3870531388356358843L;

  private JButton               integrateButton         = new JButton("Calculate integral");
  private JLabel                spectralRadiusLabel     = new JLabel();

  public PeopleView() {
    super(new BorderLayout());

    JPanel header = new JPanel();
    header.add(integrateButton);
    header.add(spectralRadiusLabel);

    add(header, NORTH);

    integrateButton.addActionListener(event -> {
      removeAll();

      double[][] similarity_matrix = GraphIntegral.integrate();
      GraphLaplacianTransform.unnormalisedLaplacian(similarity_matrix);

      OptionalDouble spectral_radius = stream(QRSpectralDecomposition.decompose(similarity_matrix)).max();

      if (spectral_radius.isPresent())
        spectralRadiusLabel.setText("Laplacian Spectral Radius: " + spectral_radius.getAsDouble());

      add(header, NORTH);
      add(new SimilarityMatrixPanel(GraphIntegral.integrate(), "Probability", true));
    });
  }
}
