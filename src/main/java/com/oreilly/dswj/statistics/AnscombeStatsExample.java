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
package com.oreilly.dswj.statistics;

import com.oreilly.dswj.datasets.Anscombe;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author Michael Brzustowicz
 */
public class AnscombeStatsExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DescriptiveStatistics x1 = new DescriptiveStatistics(Anscombe.x1);
        DescriptiveStatistics y1 = new DescriptiveStatistics(Anscombe.y1);
        DescriptiveStatistics x2 = new DescriptiveStatistics(Anscombe.x2);
        DescriptiveStatistics y2 = new DescriptiveStatistics(Anscombe.y2);
        DescriptiveStatistics x3 = new DescriptiveStatistics(Anscombe.x3);
        DescriptiveStatistics y3 = new DescriptiveStatistics(Anscombe.y3);
        DescriptiveStatistics x4 = new DescriptiveStatistics(Anscombe.x4);
        DescriptiveStatistics y4 = new DescriptiveStatistics(Anscombe.y4);
        
        System.out.println(x1);
        System.out.println(y1);
        System.out.println(x2);
        System.out.println(y2);
        System.out.println(x3);
        System.out.println(y3);
        System.out.println(x4);
        System.out.println(y4);
        
    }
    
}
