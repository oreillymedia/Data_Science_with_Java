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

import com.oreilly.dswj.datasets.MultiNormalMixtureDataset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;

/**
 *
 * @author Michael Brzustowicz
 */
public class DBSCANOptimizeExample {

    int dim = 2;
    int numComponents = 4;
    double boxSize = 10.0;
    long seed = 0L;
    int numPoints = 1000;
    List<DoublePoint> clusterPoints;
    List<DoublePoint> outliers;
    List<Cluster<DoublePoint>> results;

    public DBSCANOptimizeExample() {
        outliers = new ArrayList<>();
    }

    public double[][] getSimulatedClusterData() {//int dim, int numComponents, double boxSize, long seed, int numPoints) {
        List<DoublePoint> data = new ArrayList<>();
        MultiNormalMixtureDataset mnData = new MultiNormalMixtureDataset(dim);
        mnData.createRandomMixtureModel(numComponents, boxSize, seed);
        double[][] simData = mnData.getSimulatedData(numPoints);
        return simData;
    }

    public List<DoublePoint> getClusterablePoints(double[][] input) {
        List<DoublePoint> data = new ArrayList<>();
        for (double[] row : input) {
            data.add(new DoublePoint(row));
        }
        return data;
    }

    /**
     * copies the cluster points into a new List labeled points are removed from
     * this list during clustering leaving behind the outliers
     */
    public void initOutliers() {
        for (DoublePoint dp : clusterPoints) {
            outliers.add(new DoublePoint(dp.getPoint()));
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        DBSCANOptimizeExample dbExam = new DBSCANOptimizeExample();
        dbExam.dim = 2;
        dbExam.boxSize = 10.0;
        dbExam.numComponents = 4;
        dbExam.numPoints = 1000;
        dbExam.seed = 0L;

        /* simulate cluster data from params */
        double[][] simData = dbExam.getSimulatedClusterData();
        dbExam.clusterPoints = dbExam.getClusterablePoints(simData);
        dbExam.initOutliers();

        int minPts = 3;

        double[] epsVals = {0.15, 0.16, 0.17, 0.18, 0.19, 0.20, 0.21, 0.22, 0.23, 0.24, 0.25};
        for (double epsVal : epsVals) {

            DBSCANClusterer clusterer = new DBSCANClusterer(epsVal, minPts);
            dbExam.results = clusterer.cluster(dbExam.clusterPoints);

            if (dbExam.results.isEmpty()) {

                System.out.println("No clusters where found");

            } else {

                int clusterNum = 0;

                for (Cluster<DoublePoint> result : dbExam.results) {
                    /* each clusters points are in here */
                    List<DoublePoint> points = result.getPoints();
                    System.out.println("cluster " + clusterNum + " size = " + points.size());

                    /* remove these cluster points from the data copy "outliers"
                     which will contain ONLY the outliers after all of the
                     cluster points are removed
                     */
                    dbExam.outliers.removeAll(points);

                    clusterNum++;
                }

                SilhouetteCoefficient s = new SilhouetteCoefficient(dbExam.results);
                System.out.println("eps = " + epsVal + " numClusters = " + dbExam.results.size() + " s = " + s.getCoefficient());
                System.out.println("****************************************");
            }
        }

        /**
         * eps = 0.15 numClusters = 7 s = 0.5476551318499049
         * eps = 0.16 numClusters = 7 s = 0.534243977492931
         * eps = 0.17 numClusters = 7 s = 0.5331127290858255
         * eps = 0.18 numClusters = 6 s = 0.6873454240309635
         * eps = 0.19 numClusters = 6 s = 0.6834235573762396
         * eps = 0.2  numClusters = 6 s = 0.6774338391641875
         * eps = 0.21 numClusters = 5 s = 0.6834872347662153
         * eps = 0.22 numClusters = 4 s = 0.7007320651372885
         * eps = 0.23 numClusters = 3 s = 0.6886103822156766
         * eps = 0.24 numClusters = 3 s = 0.6876656144428981
         * eps = 0.25 numClusters = 3 s = 0.685718354092989
         */
    }

}
