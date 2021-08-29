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

import static java.lang.Math.*;
import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.AbstractMap.*;
import java.util.Map.*;
import java.util.stream.*;

import org.apache.commons.math3.linear.*;

public class SpectralCluster {
  public static int[] spectralCluster(double[][] similarity_matrix, int k) {
    int n = similarity_matrix.length;

    GraphLaplacianTransform.unnormalisedLaplacian(similarity_matrix);

    double[] eigenvalues = QRSpectralDecomposition.decompose(similarity_matrix);

    List<Integer> indexes = IntStream.range(0, n)
                                     .mapToObj(i ->  new SimpleEntry<>(eigenvalues[i], i))
                                     .sorted((first, second) -> (int)signum(first.getKey() - second.getKey()))
                                     .map(Entry::getValue)
                                     .limit(k)
                                     .collect(toList());

    for (int i = 0; i < n; i++) {
      final int final_i = i;
      similarity_matrix[i] = indexes.stream()
                                    .mapToDouble(index -> similarity_matrix[final_i][index])
                                    .toArray();
    }

    return KMeans.cluster(similarity_matrix, k);
  }
  
  public static int[] spectralClusterApacheCommons(double[][] similarity_matrix, int k) {
    int n = similarity_matrix.length;
	  
    EigenDecomposition decomposition = new EigenDecomposition(new Array2DRowRealMatrix(similarity_matrix));
  	
    double[]   eigenvalues  = decomposition.getRealEigenvalues();
    double[][] eigenvectors = decomposition.getV().getData();

    List<Integer> indexes = IntStream.range(0, n)
                                     .mapToObj(i ->  new SimpleEntry<>(eigenvalues[i], i))
                                     .sorted((first, second) -> (int)signum(first.getKey() - second.getKey()))
                                     .map(Entry::getValue)
                                     .limit(k)
                                     .collect(toList());

    for (int i = 0; i < n; i++) {
      final int final_i = i;
      similarity_matrix[i] = indexes.stream()
                                    .mapToDouble(index -> eigenvectors[final_i][index])
                                    .toArray();
    }
    
    return KMeans.cluster(eigenvectors, k);
  }
}
