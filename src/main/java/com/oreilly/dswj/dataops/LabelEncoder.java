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

import java.util.Arrays;
import java.util.List;

/**
 * Encode labels of type T to an ArrayList with optional sorting
 * @author Michael Brzustowicz
 * @param <T>
 */
public class LabelEncoder<T> {

    private final List<T> classes;
    
    public LabelEncoder(T[] labels) {
        // Arrays.sort(labels); // can sort first but not required
        classes = Arrays.asList(labels);
    }

    public List<T> getClasses() {
        return classes;
    }

    public int encode(T label) {
        return classes.indexOf(label);
    }
    
    public T decode(int index) {
        return classes.get(index);
    }
    
    public int[] encodeOneHot(T label) {
        int[] oneHot = new int[classes.size()];
        oneHot[encode(label)] = 1;
        return oneHot;
    }
    
    public T decodeOneHot(int[] oneHot) {
        return decode(Arrays.binarySearch(oneHot, 1));
    }
}
