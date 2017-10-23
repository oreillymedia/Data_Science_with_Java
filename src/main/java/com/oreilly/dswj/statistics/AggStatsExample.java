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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.AggregateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummaryValues;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author Michael Brzustowicz
 */
public class AggStatsExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        List<SummaryStatistics> ls = new ArrayList<>();
        
        SummaryStatistics ss = new SummaryStatistics();
        ss.addValue(1.0);
        ss.addValue(11.0);
        ss.addValue(5.0);
        
        SummaryStatistics ss2 = new SummaryStatistics();
        ss2.addValue(2.0);
        ss2.addValue(12.0);
        ss2.addValue(6.0);
        
        SummaryStatistics ss3 = new SummaryStatistics();
        ss3.addValue(0.0);
        ss3.addValue(10.0);
        ss3.addValue(4.0);
        
        
        ls.add(ss);
        ls.add(ss2);
        ls.add(ss3);
        
        StatisticalSummaryValues s = AggregateSummaryStatistics.aggregate(ls);
        
        System.out.println(s);
        
    }
    
}
