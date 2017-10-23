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
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Michael Brzustowicz
 */
public class NetworkLayer extends LinearModel {
    
    RealMatrix input;
    RealMatrix inputError;
    RealMatrix output;
    RealMatrix outputError;
    Optimizer optimizer;
    
    public NetworkLayer(int inputDimension, int outputDimension,
            OutputFunction outputFunction, Optimizer optimizer) {
        super(inputDimension, outputDimension, outputFunction);
        this.optimizer = optimizer;
    }

    public void update() {
        //back propagate error
        /* D = eps o f'(XW) where o is Hadamard product or J f'(XW) where J is Jacobian */
        RealMatrix deltas = getOutputFunction().getDelta(outputError, output);
        
        /* E_out = D W^T */
        inputError = deltas.multiply(getWeight().transpose());
        
        /* W = W - alpha * delta * input */
        RealMatrix weightGradient = input.transpose().multiply(deltas);
 
        /* w_{t+1} = w_{t} + \delta w_{t} */
        addUpdateToWeight(optimizer.getWeightUpdate(weightGradient));
        
        // this essentially sums the columns of delta and that vector is grad_b
        RealVector h = new ArrayRealVector(input.getRowDimension(), 1.0);
        RealVector biasGradient = deltas.preMultiply(h);
        addUpdateToBias(optimizer.getBiasUpdate(biasGradient));
    }
    
    public void setOutputError(RealMatrix outputError) {
        this.outputError = outputError;
    }

    public void setInputError(RealMatrix inputError) {
        this.inputError = inputError;
    }

    public void setInput(RealMatrix input) {
        this.input = input;
    }

    public void setOutput(RealMatrix output) {
        this.output = output;
    }

    public RealMatrix getInputError() {
        return inputError;
    }
    
}
