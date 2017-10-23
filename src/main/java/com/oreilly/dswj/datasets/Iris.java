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

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author Michael Brzustowicz
 */
public class Iris {

    private final RealMatrix data;
    private final RealMatrix labels;
    private static final String FILEPATH = "/datasets/iris/iris_data.csv";

    public Iris() throws IOException {
        
        data = new Array2DRowRealMatrix(150, 4);
        labels = new Array2DRowRealMatrix(150, 3); // binarized
        
        try(InputStream inputStream = getClass().getResourceAsStream(FILEPATH)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int rowCounter = 0;
            while ((line = br.readLine()) != null) {

                String[] s = line.split(",");
                double sepalLength = Double.parseDouble(s[0].trim());
                double sepalWidth = Double.parseDouble(s[1].trim());
                double petalLength = Double.parseDouble(s[2].trim());
                double petalWidth = Double.parseDouble(s[3].trim());
                String plantClass = s[4].trim();
                
                data.setEntry(rowCounter, 0, sepalLength);
                data.setEntry(rowCounter, 1, sepalWidth);
                data.setEntry(rowCounter, 2, petalLength);
                data.setEntry(rowCounter, 3, petalWidth);
                
                if (null != plantClass) switch (plantClass) {
                    case "Iris-setosa":
                        labels.setEntry(rowCounter, 0, 1);
                        break;
                    case "Iris-versicolor":
                        labels.setEntry(rowCounter, 1, 1);
                        break;
                    case "Iris-virginica":
                        labels.setEntry(rowCounter, 3, 1);
                        break;
                    default:
                        System.out.println("something wrong with " + plantClass);
                        break;
                }
                
                rowCounter++;
            }
        }
    }

    public RealMatrix getData() {
        return data;
    }

    public RealMatrix getLabels() {
        return labels;
    }

}
