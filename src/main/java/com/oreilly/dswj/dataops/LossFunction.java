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

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * 
 * @author Michael Brzustowicz
 */
public interface LossFunction {
    
    /**
     * loss of one dimension of one sample output
     * @param predicted
     * @param target
     * @return 
     */
    public double getSampleLoss(double predicted, double target);
    
    /**
     * combined loss over all dimensions of one sample output
     * @param predicted
     * @param target
     * @return 
     */
    public double getSampleLoss(RealVector predicted, RealVector target);
    
    /**
     * average loss over all samples
     * @param predicted
     * @param target
     * @return 
     */
    public double getMeanLoss(RealMatrix predicted, RealMatrix target);
    
    /**
     * derivative of loss of one dimension of one sample output
     * @param predicted
     * @param target
     * @return 
     */
    public double getSampleLossGradient(double predicted, double target);
    
    /**
     * derivative of loss over all dimensions of one sample output
     * @param predicted
     * @param target
     * @return 
     */
    public RealVector getSampleLossGradient(RealVector predicted, RealVector target);
    
    /**
     * 
     * @param predicted
     * @param target
     * @return 
     */
    public RealMatrix getLossGradient(RealMatrix predicted, RealMatrix target);
}
