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

import org.apache.commons.math3.stat.descriptive.MultivariateSummaryStatistics;

/**
 *
 * @author Michael Brzustowicz
 */
public class MultinomialConditionalProbabilityEstimator implements ConditionalProbabilityEstimator {

    private double alpha;

    public MultinomialConditionalProbabilityEstimator(double alpha) {
        this.alpha = alpha; // Lidstone smoothing 0 > alpha > 1
    }

    public MultinomialConditionalProbabilityEstimator() {
        this(1); // Laplace smoothing
    }
    
    @Override
    public double getProbability(MultivariateSummaryStatistics mss, double[] features) {
        int n = features.length;
        double prob = 1.0;
        double[] sum = mss.getSum(); // array of x_i sums for this class
        double total = 0.0; // total count of all features
        for (int i = 0; i < n; i++) {
            total += sum[i];
        }
        
        /* works great for smaller x_i ie features[i] */
//        for (int i = 0; i < n; i++) {
//            prob *= Math.pow((sum[i] + alpha) / (total + alpha * n), features[i]);
//        }
//        return prob;
        
        /* for large x_i need to solve in log space and convert back with exp */
        prob = 0;
        for (int i = 0; i < n; i++) {
            prob += features[i] * Math.log((sum[i] + alpha) / (total + alpha * n));
        }
        return Math.exp(prob);
    }

}
