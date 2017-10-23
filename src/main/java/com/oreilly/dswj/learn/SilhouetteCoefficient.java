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

import java.util.List;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author Michael Brzustowicz
 */
public class SilhouetteCoefficient {
    private final List<Cluster<DoublePoint>> clusters;
    private double coefficient;// = 0.0;
    private int numClusters;
    private int numSamples;

    public SilhouetteCoefficient(List<Cluster<DoublePoint>> clusters) throws OutOfRangeException {
        
        this.clusters = clusters;

        if(checkClusterSize()) {
            calculateMeanCoefficient();
        } else {
            throw new OutOfRangeException(clusters.size(), 2, numSamples - 1);
        }
    }

    public double getCoefficient() {
        return coefficient;
    }
    

    private void calculateMeanCoefficient() {
        SummaryStatistics stats = new SummaryStatistics();
            int clusterNumber = 0;
            for (Cluster<DoublePoint> cluster : clusters) {
                for (DoublePoint point : cluster.getPoints()) {
                    double s = calculateCoefficientForOnePoint(point, clusterNumber);
                    stats.addValue(s);
                }
                clusterNumber++;
            }
            coefficient = stats.getMean();
    }
    
    private double calculateCoefficientForOnePoint(DoublePoint onePoint, int clusterLabel) {
        
        /* all other points will compared to this one */
        RealVector vector = new ArrayRealVector(onePoint.getPoint());
        
        double a = 0;
        double b = Double.MAX_VALUE;
        
        int clusterNumber = 0;
        
        for (Cluster<DoublePoint> cluster : clusters) {
            
            SummaryStatistics clusterStats = new SummaryStatistics();
            
            for (DoublePoint otherPoint : cluster.getPoints()) {
                RealVector otherVector = new ArrayRealVector(otherPoint.getPoint());
                double dist = vector.getDistance(otherVector);
                clusterStats.addValue(dist);
            }
            
            double avgDistance = clusterStats.getMean();
            
            if(clusterNumber==clusterLabel) {
                /* we have included a 0 distance of point with itself and need to subtract it out of the mean */
                double n = new Long(clusterStats.getN()).doubleValue();
                double correction = n / (n - 1.0);
                a = correction * avgDistance;
            } else {
                b = Math.min(avgDistance, b);
            }
            
            clusterNumber++;
        }
        
        return (b-a) / Math.max(a, b);
    }
    
    private boolean checkClusterSize() throws OutOfRangeException {
        numClusters = clusters.size();
        numSamples = 0;
        for (Cluster<DoublePoint> cluster : clusters) {
            numSamples += cluster.getPoints().size();
        }
        return numClusters >= 2 && numClusters <= (numSamples - 1) ;
    }
    
}
