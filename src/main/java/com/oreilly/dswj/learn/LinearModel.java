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

import com.oreilly.dswj.linalg.RandomizedMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Michael Brzustowicz
 */
public class LinearModel {
    
    private RealMatrix weight;
    private RealVector bias;    
    private final OutputFunction outputFunction;

    public LinearModel(int inputDimension, int outputDimension,
            OutputFunction outputFunction) {
        RandomizedMatrix randM = new RandomizedMatrix();
        weight = randM.getMatrix(inputDimension, outputDimension);
        bias = randM.getVector(outputDimension);
        this.outputFunction = outputFunction;
    }

    public RealMatrix getOutput(RealMatrix input) {
        return outputFunction.getOutput(input, weight, bias);
    }

    public void addUpdateToWeight(RealMatrix weightUpdate) {
        weight = weight.add(weightUpdate);
    }
    
    public void addUpdateToBias(RealVector biasUpdate) {
        bias = bias.add(biasUpdate);
    }
    
    /* setter and getters */
    
    public void setWeight(RealMatrix weight) {
        this.weight = weight;
    }

    public void setBias(RealVector bias) {
        this.bias = bias;
    }
    
    public RealMatrix getWeight() {
        return weight;
    }

    public RealVector getBias() {
        return bias;
    }

    public OutputFunction getOutputFunction() {
        return outputFunction;
    }   
}
