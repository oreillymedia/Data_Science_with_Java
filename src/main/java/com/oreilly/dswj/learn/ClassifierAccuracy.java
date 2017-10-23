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

import com.oreilly.dswj.dataops.ProbabilityEncoder;
import org.apache.commons.math3.analysis.function.Abs;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Michael Brzustowicz
 */
public class ClassifierAccuracy {
    
    private final RealMatrix predictions;
    private final RealMatrix targets;
    private final ProbabilityEncoder probabilityEncoder;
    private RealVector classCount;

    /**
     * 
     * @param predictions assumes probabilities
     * @param targets assumes binary multi-label OR one-hot-encoding
     */
    public ClassifierAccuracy(RealMatrix predictions, RealMatrix targets) {
        this.predictions = predictions;
        this.targets = targets;
        probabilityEncoder = new ProbabilityEncoder();
        //tally the binary class occurances per dimension
        classCount = new ArrayRealVector(targets.getColumnDimension());
        for (int i = 0; i < targets.getRowDimension(); i++) {
            classCount = classCount.add(targets.getRowVector(i));
        }
    }
    
    /**
     * assumes one hot encoding
     * @return 
     */
    public RealVector getAccuracyPerDimension() {

        RealVector accuracy = new ArrayRealVector(predictions.getColumnDimension());
        
        for (int i = 0; i < predictions.getRowDimension(); i++) {
            
            RealVector binarized = probabilityEncoder.getOneHot(predictions.getRowVector(i));
            
            // 0*0, 0*1, 1*1 = 0 and 1*1 = 1 giving only true positives as 1 and all other 0
            RealVector decision = binarized.ebeMultiply(targets.getRowVector(i));

            // append TP counts to accuracy
            accuracy = accuracy.add(decision);
        }
        return accuracy.ebeDivide(classCount);
    }
    
    /**
     * assumes one hot encoding
     * @return 
     */
    public double getAccuracy() {
        // convert accuracy_per_dim back to counts, then sum and divide by total rows
        return getAccuracyPerDimension().ebeMultiply(classCount).getL1Norm() / targets.getRowDimension();
    }
        
    // implements jaccard similarity scores
    public RealVector getAccuracyPerDimension(double threshold) { // assumes un-correlated multi-output
        
        RealVector accuracy = new ArrayRealVector(targets.getColumnDimension());
        
        for (int i = 0; i < predictions.getRowDimension(); i++) {
            
            //binarize the row vector according to the threshold
            RealVector binarized = probabilityEncoder.getBinary(predictions.getRowVector(i), threshold);
            
            // 0-0 (TN) and 1-1 (TP) = 0 while 1-0 = 1 and 0-1 = -1
            RealVector decision = binarized.subtract(targets.getRowVector(i)).map(new Abs()).mapMultiply(-1).mapAdd(1);
            
            // append either TP and TN counts to accuracy
            accuracy = accuracy.add(decision);
        }
        return accuracy.mapDivide((double) predictions.getRowDimension()); // accuracy for each dimension, given the threshold
    }
    
    public double getAccuracy(double threshold) {
        // mean of the accuracy vector
        return getAccuracyPerDimension(threshold).getL1Norm() / targets.getColumnDimension();
    }
    
    
    
}
