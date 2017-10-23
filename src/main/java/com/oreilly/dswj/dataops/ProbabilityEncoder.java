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
package com.oreilly.dswj.dataops;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Michael Brzustowicz
 */
public class ProbabilityEncoder {
    
    public RealVector getBinary(RealVector probabilities, double threshhold) {
        RealVector out = new ArrayRealVector(probabilities.getDimension());
        for (int i = 0; i < probabilities.getDimension(); i++) {
            double entry = probabilities.getEntry(i) >= threshhold ? 1 : 0;
            out.setEntry(i, entry);
        }
        return out;
    }
    
    public RealVector getOneHot(RealVector probabilities) {
        RealVector out = new ArrayRealVector(probabilities.getDimension());
        out.setEntry(probabilities.getMaxIndex(), 1);
        return out;
    }
    
    public RealMatrix getOneHot(RealMatrix probabilities) {
        int numRows = probabilities.getRowDimension();
        int numCols = probabilities.getColumnDimension();
        RealMatrix out = new Array2DRowRealMatrix(numRows, numCols);
        for (int i = 0; i < numRows; i++) {
            int maxIndex = probabilities.getRowVector(i).getMaxIndex();
            out.setEntry(i, maxIndex, 1);
        }
        return out;
    }
}

