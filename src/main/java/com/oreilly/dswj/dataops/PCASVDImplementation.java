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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;

/**
 *
 * @author Michael Brzustowicz
 */
public class PCASVDImplementation implements PCAImplementation {
    private RealMatrix u;
    private RealMatrix s;
    private RealMatrix v;
    private MatrixScaler matrixScaler;
    private SingularValueDecomposition svd;

    @Override
    public void compute(RealMatrix data) {
        //centers the data in place and stores the column stats for later use
        matrixScaler = new MatrixScaler(data, MatrixScaleType.CENTER);
        svd = new SingularValueDecomposition(data);
        u = svd.getU();
        s = svd.getS();
        v = svd.getV();
    }
    
    @Override
    public RealVector getExplainedVariances() {

        double[] singularValues = svd.getSingularValues();
        int n = singularValues.length;
        int m = u.getRowDimension(); // number of rows in U is same as in data
        RealVector explainedVariances = new ArrayRealVector(n);
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            double var = Math.pow(singularValues[i], 2) / (double)(m-1);
            sum += var;
            explainedVariances.setEntry(i, var);
        }
        /* dividing the vector by the last (highest) value maximizes to 1 */
        return explainedVariances.mapDivideToSelf(sum);
        
    }

    @Override
    public RealMatrix getPrincipalComponents(int numComponents) {
        int numRows = svd.getU().getRowDimension();
        /* submatrix limits are inclusive */
        RealMatrix uk = u.getSubMatrix(0, numRows-1, 0, numComponents-1);
        RealMatrix sk = s.getSubMatrix(0, numComponents-1, 0, numComponents-1);
        return uk.multiply(sk);
    }

    @Override
    public RealMatrix getPrincipalComponents(int numComponents, RealMatrix otherData) {
        // center the (new) data on means from original data
        matrixScaler.transform(otherData);
        int numRows = v.getRowDimension();
        // subMatrix indeces are inclusive
        return otherData.multiply(v.getSubMatrix(0, numRows-1, 0, numComponents-1));
    }
    
}
