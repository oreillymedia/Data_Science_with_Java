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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;

/**
 *
 * @author Michael Brzustowicz
 */
public class GaussianMixtureClusteringExample extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        int dimension = 5;
        int numPoints = 10000;
        int numComponents = 7;
        double boxSize = 10;
        long seed = 0L;
        MultiNormalMixtureDataset dataset = new MultiNormalMixtureDataset(dimension);
        dataset.createRandomMixtureModel(numComponents, boxSize, seed);
        double[][] data = dataset.getSimulatedData(numPoints);
        
        /* Mixture Multinorm MLE */
        MultivariateNormalMixtureExpectationMaximization mixEM = new MultivariateNormalMixtureExpectationMaximization(data);

        
        int minNumClusters = 2;
        int maxNumClusters = 10; // inclusive
        Map<Integer, Double> ll = new HashMap<>();
        
        for (int i = minNumClusters; i <= maxNumClusters; i++) {

//            try {
        
                /* need a guess as where to start */
                MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, i);

                /* perform the fit */
                mixEM.fit(initialMixture);

                /* this is the fitted model */
                MixtureMultivariateNormalDistribution fittedModel = mixEM.getFittedModel();
                
                ll.put(i, mixEM.getLogLikelihood());
                
                /* print out the loglikelihood */
                System.out.println("k = " + i + ", ll = " + mixEM.getLogLikelihood());
            
//            } catch(NotStrictlyPositiveException | DimensionMismatchException | SingularMatrixException e) {
//                System.out.println("num clusters: " + i + ". " + e.getMessage());
//            }
        }
        
        
        
        
        XYChart.Series series = new XYChart.Series();
        
        for (Map.Entry<Integer, Double> entrySet : ll.entrySet()) {
            Integer key = entrySet.getKey();
            Double value = entrySet.getValue();
            series.getData().add(new XYChart.Data(key, -value));
        }
        
//        double limit = boxSize;// / 2.0;
        NumberAxis xAxis = new NumberAxis(minNumClusters-1, maxNumClusters+1, 1.0);//-limit, limit, 1.0);
        xAxis.setLabel("number of clusters");
        NumberAxis yAxis = new NumberAxis(4.5, 6.5, 1.0);//-limit, limit, 1.0);
        yAxis.setLabel("Log Loss");
        
        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.getData().add(series);
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
//        // TODO: probably use a file chooser here
//        File file = new File("GaussCluster5D.png");
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        } catch (IOException e) {
//            // TODO: handle exception here
//        }
        
        
    }
    
}
