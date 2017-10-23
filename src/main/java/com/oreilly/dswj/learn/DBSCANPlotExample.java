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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;

/**
 *
 * @author Michael Brzustowicz
 */
public class DBSCANPlotExample extends Application {
    
    int dim = 2;
    int numComponents = 4;
    double boxSize = 10.0;
    long seed = 0L;
    int numPoints = 1000;
    List<DoublePoint> clusterPoints;
    List<DoublePoint> outliers;
    List<Cluster<DoublePoint>> results;

    public DBSCANPlotExample() {
        outliers = new ArrayList<>();
    }
    

    public double[][] getSimulatedClusterData() {
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
     * copies the cluster points into a new List
     * labeled points are removed from this list during clustering
     * leaving behind the outliers
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
                
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        
        DBSCANPlotExample dbExam = new DBSCANPlotExample();
        dbExam.dim = 2;
        dbExam.boxSize = 10.0;
        dbExam.numComponents = 4;
        dbExam.numPoints = 1000;
        dbExam.seed = 0L; // 0L and 2L both have two of four clusters touching
        
        /* simulate cluster data from params */
        double[][] simData = dbExam.getSimulatedClusterData();
        dbExam.clusterPoints = dbExam.getClusterablePoints(simData);
        dbExam.initOutliers();
        
        double eps = 0.22;
        int minPts = 3;
        

                   
        DBSCANClusterer clusterer = new DBSCANClusterer(eps, minPts);
        /*List<Cluster<DoublePoint>>*/ results = clusterer.cluster(dbExam.clusterPoints);

        if(results.isEmpty()) {

            System.out.println("No clusters where found");

        } else {

            int clusterNum = 0;

            for (Cluster<DoublePoint> result : results) {
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



        }
        
        NumberAxis xAxis = new NumberAxis();//-limit, limit, 1.0);
        NumberAxis yAxis = new NumberAxis();//-limit, limit, 1.0);
        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        System.out.println("results size " + results.size());
        
        
        int clusterNum = 0;
        /* iterate through cluster results and add to plot */
        for (Cluster<DoublePoint> cluster : results) {
            
            XYChart.Series series = new XYChart.Series();
            series.setName("Cluster " + clusterNum);
            
            for (DoublePoint point : cluster.getPoints()) {
                double[] datum = point.getPoint();
                series.getData().add(new XYChart.Data(datum[0], datum[1]));
            }
            
            scatterChart.getData().add(series);
            clusterNum++;
        }
        
        if(dbExam.outliers.size() > 0) {
            XYChart.Series outlierSeries = new XYChart.Series();
            outlierSeries.setName("Outliers");
            for (DoublePoint point : dbExam.outliers) {
                double[] datum = point.getPoint();
                outlierSeries.getData().add(new XYChart.Data(datum[0], datum[1]));
            }
            scatterChart.getData().add(outlierSeries);
        }

        scatterChart.setHorizontalGridLinesVisible(false);
        scatterChart.setVerticalGridLinesVisible(false);
        scatterChart.setHorizontalZeroLineVisible(false);
        scatterChart.setVerticalZeroLineVisible(false);
        
        scatterChart.setAnimated(false);
        
        Scene scene = new Scene(scatterChart, 800, 600);
        stage.setScene(scene);
        stage.show();
        
//        WritableImage image = scatterChart.snapshot(new SnapshotParameters(), null);
//
//        File file = new File("DBSCAN.png");
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        } catch (IOException e) {
//            // TODO: handle exception here
//        }
        
        
    }
    
}
