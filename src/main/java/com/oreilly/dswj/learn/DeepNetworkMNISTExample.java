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

import com.oreilly.dswj.datasets.MNIST;
import com.oreilly.dswj.dataops.SoftMaxCrossEntropyLossFunction;
import java.io.IOException;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author Michael Brzustowicz
 */
public class DeepNetworkMNISTExample {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        MNIST mnist = new MNIST();
        
        DeepNetwork network = new DeepNetwork();
        
        /* input, hidden and output layers */
        network.addLayer(new NetworkLayer(784, 500, new TanhOutputFunction(),
                new GradientDescentMomentum(0.0001, 0.95)));
        
        network.addLayer(new NetworkLayer(500, 300, new TanhOutputFunction(),
                new GradientDescentMomentum(0.0001, 0.95)));
        
        network.addLayer(new NetworkLayer(300, 10, new SoftmaxOutputFunction(),
                new GradientDescentMomentum(0.0001, 0.95)));
        
        /* runtime parameters */
        network.setLossFunction(new SoftMaxCrossEntropyLossFunction());
        network.setMaxIterations(10000);
        network.setTolerance(10E-6);
        network.setBatchSize(200);
        
        /* learn */
        network.learn(mnist.trainingData, mnist.trainingLabels);
        
        /* predict */
        RealMatrix prediction = network.predict(mnist.testingData);
        
        /* compute accuracy */
        ClassifierAccuracy accuracy = new ClassifierAccuracy(prediction, mnist.testingLabels);
        
        /* print report */
        System.out.println("isConverged = " + network.isConverged());
        System.out.println("numIter = " + network.getNumIterations());
        System.out.println("error = " + network.getLoss());
        System.out.println("accuracy = " + accuracy.getAccuracy());
        System.out.println("accuracy per dim = " + accuracy.getAccuracyPerDimension());
        
    }
    
}
