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
public class DeepNetworkIrisExample {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Iris iris = new Iris();
        MatrixResampler mr = new MatrixResampler(iris.getData(), iris.getLabels());
        mr.calculateTestTrainSplit(0.4, 0L);
        DeepNetwork net = new DeepNetwork();
        net.addLayer(new NetworkLayer(4, 10, new TanhOutputFunction(), new GradientDescent(0.001)));
        net.addLayer(new NetworkLayer(10, 3, new SoftmaxOutputFunction(), new GradientDescent(0.001)));
        net.setLossFunction(new SoftMaxCrossEntropyLossFunction());
        net.setBatchSize(0);
        net.setMaxIterations(6000);
        net.setTolerance(10E-6);
        net.learn(mr.getTrainingFeatures(), mr.getTrainingLabels());
        RealMatrix predictions = net.predict(mr.getTestingFeatures());
        ClassifierAccuracy acc = new ClassifierAccuracy(predictions, mr.getTestingLabels());
        System.out.println("converged = " + net.isConverged());
        System.out.println("iterations = " + net.getNumIterations());
        System.out.println("accuracy = " + acc.getAccuracy());
    }
    
}
