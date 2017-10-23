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

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.MultivariateSummaryStatistics;

/**
 *
 * @author Michael Brzustowicz
 */
public class NaiveBayes { 

    private Map <Integer, MultivariateSummaryStatistics> statistics;
    private final Map<String, MultivariateSummaryStatistics> stats;
    private final ConditionalProbabilityEstimator conditionalProbabilityEstimator;
    private int numberOfPoints; // total number of points the model was trained on

    public NaiveBayes() {
        this(new GaussianConditionalProbabilityEstimator());
    }

    /**
     * provide the strategy for implementing the conditional probability
     * @param conditionalProbabilityEstimator 
     */
    public NaiveBayes(ConditionalProbabilityEstimator conditionalProbabilityEstimator) {
        stats = new HashMap<>();
        statistics = new HashMap<>();
        this.conditionalProbabilityEstimator = conditionalProbabilityEstimator;
        numberOfPoints = 0;
    }
    
    
    /**
     * 
     * @param input
     * @param target multi class OR one hot encoded labels
     */
    public void learn(RealMatrix input, RealMatrix target) {
        // if numTargetCols == 1 then multiclass e.g. 0, 1, 2, 3
        // else one-hot e.g. 1000, 0100, 0010, 0001
        
        numberOfPoints += input.getRowDimension();
        
        for (int i = 0; i < input.getRowDimension(); i++) {
            
            double[] rowData = input.getRow(i);
            int label;
            
            if (target.getColumnDimension()==1) {
                label = new Double(target.getEntry(i, 0)).intValue();
            } else {
                label = target.getRowVector(i).getMaxIndex();
            }

            if(!statistics.containsKey(label)) {
                statistics.put(label, new MultivariateSummaryStatistics(rowData.length, true));
            }
            
            statistics.get(label).addValue(rowData);
            
        }
    }
    
    public RealMatrix predict(RealMatrix input) {
        
        int numRows = input.getRowDimension();
        int numCols = statistics.size();
        RealMatrix predictions = new BlockRealMatrix(numRows, numCols);

        for (int i = 0; i < numRows; i++) {
            
//            double[] rowData = input.getRow(i);
            double[] probs = new double[numCols];
            double sumProbs = 0;
            
            for (Map.Entry<Integer, MultivariateSummaryStatistics> entrySet : statistics.entrySet()) {
                
                Integer classNumber = entrySet.getKey(); // assumes these are 0, 1, 2 ... n-1
                MultivariateSummaryStatistics mss = entrySet.getValue();

                /* prior prob n_k / N ie num points in class divided by total points */
                double prob = new Long(mss.getN()).doubleValue() / numberOfPoints;
                
                /* depends on type ... Gaussian, Multinomial or Bernoulli */
                prob *= conditionalProbabilityEstimator.getProbability(mss, input.getRow(i));
                
                probs[classNumber] = prob;
                sumProbs += prob;
            }

            /* L1 norm the probs */
            for (int j = 0; j < numCols; j++) {
                probs[j] /= sumProbs;
            }
            
            predictions.setRow(i, probs);
        
        }
        
        return predictions;
    }

}
