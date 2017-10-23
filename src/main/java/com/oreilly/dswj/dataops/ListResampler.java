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

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Michael Brzustowicz
 * @param <T>
 */
public class ListResampler<T> {

    private final List<T> data;
    private final int trainingSetSize;
    private final int testSetSize;
    private final int validationSetSize;

    public ListResampler(List<T> data, double testFraction, long seed) {
        this(data, testFraction, 0.0, seed);
    }
    
    public ListResampler(List<T> data, double testFraction, double validationFraction, long seed) {
        this(data, testFraction, validationFraction, seed, false);
    }
    
    public ListResampler(List<T> data, double testFraction, double validationFraction, long seed, boolean deepCopy) {
        this.data = deepCopy ? data : data; // deep copy so as not to alter original data!!!
        validationSetSize = new Double(validationFraction * data.size()).intValue();
        testSetSize = new Double(testFraction * data.size()).intValue();
        trainingSetSize = data.size() - (testSetSize + validationSetSize);
        Random rnd = new Random(seed);
        Collections.shuffle(data, rnd);
    }

    public List<T> getValidationSet() {
        return data.subList(0, validationSetSize);
    }
    
    public List<T> getTestSet() {
        return data.subList(validationSetSize, validationSetSize + testSetSize);
    }
    
    public List<T> getTrainingSet() {
        return data.subList(validationSetSize + testSetSize, data.size());
    }

}
