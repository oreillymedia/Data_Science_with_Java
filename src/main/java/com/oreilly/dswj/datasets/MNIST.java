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
package com.oreilly.dswj.datasets;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * all external data files are in src/main/resources/
 * these four files in src/main/resources/datasets/mnist/
 * @author Michael Brzustowicz
 */
public class MNIST {
    
    public RealMatrix trainingData;
    public RealMatrix trainingLabels;
    public RealMatrix testingData;
    public RealMatrix testingLabels;

    public MNIST() throws IOException {
        trainingData = new BlockRealMatrix(60000, 784); // image to vector
        trainingLabels = new OpenMapRealMatrix(60000, 10); // the one hot label
        testingData = new BlockRealMatrix(10000, 784); // image to vector
        testingLabels = new OpenMapRealMatrix(10000, 10); // the one hot label
        loadTrainingData("/datasets/mnist/train-images-idx3-ubyte"); // loads from jar
        loadTrainingLabels("/datasets/mnist/train-labels-idx1-ubyte");
        loadTestingData("/datasets/mnist/t10k-images-idx3-ubyte");
        loadTestingLabels("/datasets/mnist/t10k-labels-idx1-ubyte");
        
    }
    
    private void loadTrainingData(String filename) throws FileNotFoundException, IOException {
        try (DataInputStream di = new DataInputStream(new BufferedInputStream(getClass().getResourceAsStream(filename)))) {
            int magicNumber = di.readInt(); //2051
            int numImages = di.readInt(); // 60000
            int numRows = di.readInt(); // 28
            int numCols = di.readInt(); // 28
            int vecSize = numRows * numCols; // 784
            for (int i = 0; i < numImages; i++) {
                for (int j = 0; j < vecSize; j++) {
                    // values are 0 to 255, so normalize
                    trainingData.setEntry(i, j, di.readUnsignedByte() / 255.0);
                }
            }
        }
    }
    
    private void loadTestingData(String filename) throws FileNotFoundException, IOException {

        try (DataInputStream di = new DataInputStream(new BufferedInputStream(getClass().getResourceAsStream(filename)))) {
            int magicNumber = di.readInt(); //2051
            int numImages = di.readInt(); // 10000
            int numRows = di.readInt(); // 28
            int numCols = di.readInt(); // 28
            for (int i = 0; i < numImages; i++) {
                for (int j = 0; j < 784; j++) {
                    // values are 0 to 255, so normalize
                    testingData.setEntry(i, j, di.readUnsignedByte() / 255.0);
                }
            }
        } 
    }
    
    private void loadTrainingLabels(String filename) throws FileNotFoundException, IOException {
        try (DataInputStream di = new DataInputStream( new BufferedInputStream(getClass().getResourceAsStream(filename)))) {
            int magicNumber = di.readInt(); //2049
            int numImages = di.readInt(); // 60000
            for (int i = 0; i < numImages; i++) {
                // one-hot-encoding, column of 0-9 is given one all else 0
                trainingLabels.setEntry(i, di.readUnsignedByte(), 1.0);
            }
        } 
    }
    
    private void loadTestingLabels(String filename) throws FileNotFoundException, IOException {
        try (DataInputStream di = new DataInputStream( new BufferedInputStream(getClass().getResourceAsStream(filename)))) {
            int magicNumber = di.readInt(); //2049
            int numImages = di.readInt(); // 10000
            for (int i = 0; i < numImages; i++) {
                // one-hot-encoding, column of 0-9 is given one all else 0
                testingLabels.setEntry(i, di.readUnsignedByte(), 1.0);
            }
        } 
    }
    
}
