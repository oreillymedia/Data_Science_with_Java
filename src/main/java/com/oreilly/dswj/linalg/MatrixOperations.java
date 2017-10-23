/* 
 * Copyright 2017 Michael Brzustowicz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oreilly.dswj.linalg;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Michael Brzustowicz
 */
public class MatrixOperations {

    // TODO name this similar to BLAS ???
    public static RealMatrix XWplusB(RealMatrix X, RealMatrix W, RealVector b) {
        RealVector h = new ArrayRealVector(X.getRowDimension(), 1.0);
        return X.multiply(W).add(h.outerProduct(b));
    }

    public static RealMatrix XWplusB(RealMatrix X, RealMatrix W, RealVector b, UnivariateFunction univariateFunction) {
        RealMatrix z = XWplusB(X, W, b);
        z.walkInOptimizedOrder(new UnivariateFunctionMapper(univariateFunction));
        return z;
    }

    public static RealMatrix ebeMultiply(RealMatrix a, RealMatrix b) {
        int rowDimension = a.getRowDimension();
        int columnDimension = a.getColumnDimension();
        //TODO a and b should have same dimensions
        RealMatrix output = new BlockRealMatrix(rowDimension, columnDimension);
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                output.setEntry(i, j, a.getEntry(i, j) * b.getEntry(i, j));
            }
        }
        return output;
    }

}
