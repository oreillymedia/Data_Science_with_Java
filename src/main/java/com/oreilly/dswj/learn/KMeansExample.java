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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.evaluation.SumOfClusterVariances;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

/**
 *
 * @author Michael Brzustowicz
 */
public class KMeansExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int p = 7; //dimensions
        int n = 1000; //num points

        List<DoublePoint> data = new ArrayList<>();
        NormalDistribution dist = new NormalDistribution();
        for (int i = 0; i < 1000; i++) {
            data.add(new DoublePoint(dist.sample(p)));
        }

        for (int i = 1; i < 5; i++) {

            System.out.println("cluster: " + i);
            
            KMeansPlusPlusClusterer<DoublePoint> kmpp = new KMeansPlusPlusClusterer<>(i);
            List<CentroidCluster<DoublePoint>> results = kmpp.cluster(data);

            /* use cluster vars to observe cluster quality */
            SumOfClusterVariances<DoublePoint> clusterVar = new SumOfClusterVariances<>(new EuclideanDistance());
            System.out.println("score: " + clusterVar.score(results));

            for (CentroidCluster<DoublePoint> result : results) {
                DoublePoint centroid = (DoublePoint) result.getCenter();
//                System.out.println(centroid);
//                result.getPoints();
            }
        }
        
        /* performs k++ numTrials times and returns only the best result */
        int numTrials = 10;
        for (int i = 1; i < 5; i++) {
            System.out.println("MULTI " + i);
            KMeansPlusPlusClusterer<DoublePoint> kmpp2 = new KMeansPlusPlusClusterer<>(i);
            MultiKMeansPlusPlusClusterer<DoublePoint> multiKMPP = new MultiKMeansPlusPlusClusterer<>(kmpp2, numTrials);
            List<CentroidCluster<DoublePoint>> multiResults = multiKMPP.cluster(data);
            /* use cluster vars to observe cluster quality */
            SumOfClusterVariances<DoublePoint> clusterVar = new SumOfClusterVariances<>(new EuclideanDistance());
            System.out.println("score: " + clusterVar.score(multiResults));
            for (CentroidCluster<DoublePoint> multiResult : multiResults) {
//                System.out.println(multiResult.getCenter());
            }
        }
        
        
    }

}
