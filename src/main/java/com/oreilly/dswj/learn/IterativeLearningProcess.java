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

import com.oreilly.dswj.dataops.Batch;
import com.oreilly.dswj.dataops.LossFunction;
import com.oreilly.dswj.dataops.QuadraticLossFunction;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author Michael Brzustowicz
 */
public class IterativeLearningProcess {

    private boolean converged;
    private int numIterations;
    private int maxIterations;
    private double loss;
    private double tolerance;
    private int batchSize; // if == 0 then uses ALL data
    private LossFunction lossFunction;

    public IterativeLearningProcess(LossFunction lossFunction) {
        this.lossFunction = lossFunction;
        loss = 0;
        converged = false;
        numIterations = 0;
        maxIterations = 200;
        tolerance = 10E-6;
        batchSize = 100;
    }

    public IterativeLearningProcess() {
        this(new QuadraticLossFunction());
    }
    
    public void learn(RealMatrix input, RealMatrix target) {
        
        double priorLoss = tolerance;
        
        numIterations = 0;
        
        loss = 0;

        converged = false;
        
        Batch batch = new Batch(input, target);
        RealMatrix inputBatch;
        RealMatrix targetBatch;
        
        
        while(numIterations < maxIterations && !converged) {

            if(batchSize > 0 && batchSize < input.getRowDimension()) {
                
                batch.calcNextBatch(batchSize);
                inputBatch = batch.getInputBatch();
                targetBatch = batch.getTargetBatch();
                
            } else {
                
                inputBatch = input;
                targetBatch = target;
                
            }
            
            RealMatrix outputBatch = predict(inputBatch);
            
            loss = lossFunction.getMeanLoss(outputBatch, targetBatch);
            
            if(Math.abs(priorLoss - loss) < tolerance) {
            
                converged = true;
            
            } else {
                
                update(inputBatch, targetBatch, outputBatch);
            
                priorLoss = loss;
            
            }
            
            numIterations++;
        }
        
    }
    
    public RealMatrix predict(RealMatrix input) {
        throw new UnsupportedOperationException("Implement the predict method!");
    }
    
    protected void update(RealMatrix input, RealMatrix target, RealMatrix output) {
        throw new UnsupportedOperationException("Implement the update method!");
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public int getNumIterations() {
        return numIterations;
    }

    public double getLoss() {
        return loss;
    }

    public LossFunction getLossFunction() {
        return lossFunction;
    }

    public void setLossFunction(LossFunction lossFunction) {
        this.lossFunction = lossFunction;
    }

    public boolean isConverged() {
        return converged;
    }   
}
