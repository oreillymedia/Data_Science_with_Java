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

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Michael Brzustowicz
 */
public class RandomizedMatrix {
    
    private AbstractRealDistribution distribution;

    public RandomizedMatrix(AbstractRealDistribution distribution, long seed) {
        this.distribution = distribution;
        distribution.reseedRandomGenerator(seed);
    }

    public RandomizedMatrix() {
        this(new UniformRealDistribution(-1, 1), 0L);
    }

    public void fillMatrix(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            matrix.setRow(i, distribution.sample(matrix.getColumnDimension()));
        }
    }
    
    public RealMatrix getMatrix(int numRows, int numCols) {
        RealMatrix output = new BlockRealMatrix(numRows, numCols);
        for (int i = 0; i < numRows; i++) {
            output.setRow(i, distribution.sample(numCols));
        }
        return output;
    }
    
    public void fillVector(RealVector vector) {
        for (int i = 0; i < vector.getDimension(); i++) {
            vector.setEntry(i, distribution.sample());
        }
    }
    
    public RealVector getVector(int dim) {
        return new ArrayRealVector(distribution.sample(dim));
    }
    
    public static RealMatrix getTruncatedGaussian(int numRows, int numCols, long seed) {
        RealMatrix out = new BlockRealMatrix(numRows, numCols);
        NormalDistribution dist = new NormalDistribution(0.0, 0.5);
        dist.reseedRandomGenerator(seed);
        for (int i = 0; i < numRows; i++) {
            out.setRow(i, dist.sample(numCols));
        }
        return out;
    }
    
    public static RealMatrix getUniform(int numRows, int numCols, long seed) {
        RealMatrix out = new Array2DRowRealMatrix(numRows, numCols);
        UniformRealDistribution dist = new UniformRealDistribution(-1, 1);
        dist.reseedRandomGenerator(seed);
        for (int i = 0; i < numRows; i++) {
            out.setRow(i, dist.sample(numCols));
        }
        return out;
    }
    
    
    
}
