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

package graph;

import static gui.admin.tab.BipartiteView.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import graphics.Vector;
import gui.admin.*;

public abstract class Node implements Drawable {
  public static List<Node>      nodes           = new ArrayList<>();
  protected static Node         selectedNode;

  public static void clearSelection() {
    if (selectedNode != null)
      selectedNode.selected = false;

    selectedNode = null;
  }

  public Vector                 location;
  protected boolean             selected;

  public Map<Node, Double>      edges           = new HashMap<>();

  public Node(Vector location) {
    this.location = location;

    nodes.add(this);
  }

  @Override
  public void draw() {
    edges.entrySet()
         .forEach(entry -> drawArrow(location, entry.getKey().location));
    drawPoint(location, selected? 400: 200, selected? color().brighter(): color());
  }

  public void select() {
    if (selectedNode != null)
      selectedNode.selected = false;

    selectedNode = this;
    selected = true;
  }

  public abstract Color color();
}
