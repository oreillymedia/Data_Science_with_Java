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
package com.oreilly.dswj.learn;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Michael Brzustowicz
 */
public class GradientDescentMomentum extends GradientDescent {
    
    private final double momentum;
    private RealMatrix priorWeightUpdate;
    private RealVector priorBiasUpdate;

    /**
     * 
     * @param learningRate good default is 0.0001
     * @param momentum good default is 0.95
     */
    public GradientDescentMomentum(double learningRate, double momentum) {
        super(learningRate);
        this.momentum = momentum;
        priorWeightUpdate = null;
        priorBiasUpdate = null;
    }
    
    @Override
    public RealMatrix getWeightUpdate(RealMatrix weightGradient) {
        // creates matrix of zeros same size as gradients if not already exists
        if(priorWeightUpdate == null) {
            priorWeightUpdate = new BlockRealMatrix(weightGradient.getRowDimension(), weightGradient.getColumnDimension());
        }
        // add term from GradientDescent since it is already negative ( - eta * gradW )
        RealMatrix update = priorWeightUpdate.scalarMultiply(momentum).add(super.getWeightUpdate(weightGradient));
        priorWeightUpdate = update;
        return update;
    }

    @Override
    public RealVector getBiasUpdate(RealVector biasGradient) {
        if(priorBiasUpdate == null) {
            priorBiasUpdate = new ArrayRealVector(biasGradient.getDimension());
        }
        // add term from GradientDescent since it is already negative ( - eta * gradW )
        RealVector update = priorBiasUpdate.mapMultiply(momentum).add(super.getBiasUpdate(biasGradient));
        priorBiasUpdate = update;
        return update;
    }
}
