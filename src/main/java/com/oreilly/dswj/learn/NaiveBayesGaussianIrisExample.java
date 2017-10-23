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

import com.oreilly.dswj.dataops.MatrixResampler;
import com.oreilly.dswj.datasets.Iris;
import java.io.IOException;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author Michael Brzustowicz
 */
public class NaiveBayesGaussianIrisExample {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
                   
        Iris iris = new Iris();
        MatrixResampler mr = new MatrixResampler(iris.getData(), iris.getLabels());
        mr.calculateTestTrainSplit(0.4, 0L);
        
        NaiveBayes nb = new NaiveBayes(new GaussianConditionalProbabilityEstimator());
        nb.learn(mr.getTrainingFeatures(), mr.getTrainingLabels());
        
        RealMatrix predictions = nb.predict(mr.getTestingFeatures());
        
        ClassifierAccuracy acc = new ClassifierAccuracy(predictions, mr.getTestingLabels());
        System.out.println(acc.getAccuracyPerDimension());
        System.out.println(acc.getAccuracy());
        
        
        
        
    }
    
}
