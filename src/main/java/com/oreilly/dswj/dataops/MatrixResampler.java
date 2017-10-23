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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author Michael Brzustowicz
 */
public class MatrixResampler {

    RealMatrix features;
    RealMatrix labels;
    List<Integer> indeces;
    List<Integer> trainingIndeces;
    List<Integer> validationIndeces;
    List<Integer> testingIndeces;

    public MatrixResampler(RealMatrix features, RealMatrix labels) {
        this.features = features;
        this.labels = labels;
        indeces = new ArrayList<>();
        for (int i = 0; i < features.getRowDimension(); i++) {
            indeces.add(i);
        }
    }

    public void calculateTestTrainSplit(int testSize, long seed) {
        
        //TODO if numRows is 0 or 1 this is bad ...
        int numRows = features.getRowDimension();
        
        testSize = testSize < numRows - 1 ? testSize : numRows - 1;
        
        Random rnd = new Random(seed);

        Collections.shuffle(indeces, rnd);
        
        /* subList has inclusive fromIndex and exclusive toIndex */
        testingIndeces = indeces.subList(0, testSize);
        trainingIndeces = indeces.subList(testSize, features.getRowDimension());
    }
    
    public void calculateTestTrainSplit(int testSize) {
        calculateTestTrainSplit(testSize, 0L);
    }
    
    public void calculateTestTrainSplit(double testFraction, long seed) {
        
        Random rnd = new Random(seed);
        
//        for (int i = 1; i <= features.getRowDimension(); i++) {
        for (int i = 0; i < features.getRowDimension(); i++) {
            indeces.add(i);
        }
        Collections.shuffle(indeces, rnd);
        
        int testSize = new Long(Math.round(testFraction * features.getRowDimension())).intValue();
        
        /* subList has inclusive fromIndex and exclusive toIndex */
        testingIndeces = indeces.subList(0, testSize);
        trainingIndeces = indeces.subList(testSize, features.getRowDimension());
    }
    
    
    
    public RealMatrix getTrainingFeatures() {
        int numRows = trainingIndeces.size();
        int[] rowIndeces = new int[numRows];
        int counter = 0;
        for (Integer trainingIndex : trainingIndeces) {
            rowIndeces[counter++] = trainingIndex;
        }
        
        int numCols = features.getColumnDimension();
        int[] columnIndeces = new int[numCols];
        for (int i = 0; i < numCols; i++) {
            columnIndeces[i] = i;
        }
        
        return features.getSubMatrix(rowIndeces, columnIndeces);
    }
    
    public RealMatrix getTrainingLabels() {
        int numRows = trainingIndeces.size();
        int[] rowIndeces = new int[numRows];
        int counter = 0;
        for (Integer trainingIndex : trainingIndeces) {
            rowIndeces[counter++] = trainingIndex;
        }
        
        int numCols = labels.getColumnDimension();
        int[] columnIndeces = new int[numCols];
        for (int i = 0; i < numCols; i++) {
            columnIndeces[i] = i;
        }
        
        return labels.getSubMatrix(rowIndeces, columnIndeces);
    }
    
    public RealMatrix getTestingFeatures() {
        int numRows = testingIndeces.size();
        int[] rowIndeces = new int[numRows];
        int counter = 0;
        for (Integer trainingIndex : testingIndeces) {
            rowIndeces[counter++] = trainingIndex;
        }
        
        int numCols = features.getColumnDimension();
        int[] columnIndeces = new int[numCols];
        for (int i = 0; i < numCols; i++) {
            columnIndeces[i] = i;
        }
        
        return features.getSubMatrix(rowIndeces, columnIndeces);
    }
    
    public RealMatrix getTestingLabels() {
        int numRows = testingIndeces.size();
        int[] rowIndeces = new int[numRows];
        int counter = 0;
        for (Integer trainingIndex : testingIndeces) {
            rowIndeces[counter++] = trainingIndex;
        }
        
        int numCols = labels.getColumnDimension();
        int[] columnIndeces = new int[numCols];
        for (int i = 0; i < numCols; i++) {
            columnIndeces[i] = i;
        }
        
        return labels.getSubMatrix(rowIndeces, columnIndeces);
    }

}
