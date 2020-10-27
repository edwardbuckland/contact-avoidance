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

package graph.algorithm;

import java.util.stream.*;

public class GraphLaplacianTransform {
    public static void unnormalisedLaplacian(double[][] similarity_matrix) {
      int n = similarity_matrix.length;

      double[] degree = new double[n];

      for (int i = 0; i < n; i++)
        degree[i] = DoubleStream.of(similarity_matrix[i]).sum() - similarity_matrix[i][i];

      for (int i = 0; i < n; i ++)
        for (int j = 0; j < n; j++)
          similarity_matrix[i][j] = i == j? degree[i]: - similarity_matrix[i][j];
    }
}
