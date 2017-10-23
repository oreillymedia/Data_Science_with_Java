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
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.correlation.Covariance;

/**
 *
 * @author Michael Brzustowicz
 */
public class PCAEIGImplementation implements PCAImplementation {
    
    private transient RealMatrix data;
    private RealMatrix d; // eigenvalue matrix
    private RealMatrix v; // eigenvector matrix
    private RealVector explainedVariances;
    private transient EigenDecomposition eig;
    private final MatrixScaler matrixScaler;

    public PCAEIGImplementation() {
        matrixScaler = new MatrixScaler(MatrixScaleType.CENTER);
    }

    @Override
    public void compute(RealMatrix data) {
        this.data = data;
        eig = new EigenDecomposition(new Covariance(data).getCovarianceMatrix());
        d = eig.getD();
        v = eig.getV();
    }
    
    @Override
    public RealVector getExplainedVariances() {
        //TODO just make this a getter and compute in compute method
        int n = eig.getD().getColumnDimension(); //colD = rowD
        explainedVariances = new ArrayRealVector(n);
        double[] eigenValues = eig.getRealEigenvalues();
        double cumulative = 0.0;
        for (int i = 0; i < n; i++) {
            double var = eigenValues[i];
            cumulative += var;
            explainedVariances.setEntry(i, var);
        }
        /* dividing the vector by the last (highest) value maximizes to 1 */
        return explainedVariances.mapDivideToSelf(cumulative);
    }

    @Override
    public RealMatrix getPrincipalComponents(int k) {
        int m = eig.getV().getColumnDimension(); // rowD = colD
        matrixScaler.transform(data);
//        MatrixScaler.center(data);
        return data.multiply(eig.getV().getSubMatrix(0, m-1, 0, k-1));
    }

    

    @Override
    public RealMatrix getPrincipalComponents(int numComponents, RealMatrix otherData) {
        int numRows = v.getRowDimension();
        // NEW data transformed under OLD means
        matrixScaler.transform(otherData);
        return otherData.multiply(v.getSubMatrix(0, numRows-1, 0, numComponents-1));
    }
    
}
