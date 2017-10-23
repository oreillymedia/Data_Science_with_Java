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
import com.oreilly.dswj.dataops.SoftMaxCrossEntropyLossFunction;
import java.io.IOException;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author Michael Brzustowicz
 */
public class SoftmaxLinearModelExample {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Iris iris = new Iris();
        MatrixResampler resampler = new MatrixResampler(iris.getData(), iris.getLabels());
        resampler.calculateTestTrainSplit(0.40, 0L);
        
        LinearModelEstimator estimator = new LinearModelEstimator(
                        new LinearModel(4, 3, new SoftmaxOutputFunction()),
                        new SoftMaxCrossEntropyLossFunction(),
                        new DeltaRule(0.001));
        
        
//        /* this is the SAME thing as a lone layer network */        
//        DeepNetwork estimator = new DeepNetwork();
//        estimator.setLossFunction(new SoftMaxCrossEntropyLossFunction());
//        estimator.addLayer(new NetworkLayer(4, 3, new SoftmaxOutputFunction(), new GradientDescent(0.001)));
//        estimator.setBatchSize(0);
        
        
        estimator.setMaxIterations(6000);
        estimator.setTolerance(10E-6);
        
        estimator.learn(resampler.getTrainingFeatures(), resampler.getTrainingLabels());
        
        RealMatrix prediction = estimator.predict(resampler.getTestingFeatures());
        
        ClassifierAccuracy accuracy = new ClassifierAccuracy(prediction, resampler.getTestingLabels());

        System.out.println("isConverged " + estimator.isConverged());
        System.out.println("numIterations " + estimator.getNumIterations());
        System.out.println("loss " + estimator.getLoss());
        System.out.println("accuracy " + accuracy.getAccuracy());
        System.out.println("accuracy per dim " + accuracy.getAccuracyPerDimension());
        
//isConverged true
//numIterations 3094
//loss 0.07695531148974591
//accuracy 0.9833333333333333
//accuracy per dim {1; 0.9230769231; 1}     
        

    }
    
}
