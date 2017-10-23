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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.util.Pair;

/**
 *
 * @author Michael Brzustowicz
 */
public class Guassian2DExample extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        List<Pair<Double, MultivariateNormalDistribution>> mixture = new ArrayList<>();

        /* mixture component 1 */
        double alphaOne = 1.0;//0.70;
        double[] meansOne = {0.0, 0.0};
        double[][] covOne = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distOne = new MultivariateNormalDistribution(meansOne, covOne);
        Pair pairOne = new Pair(alphaOne, distOne);
        mixture.add(pairOne);

        /* mixture component 2 */
        double alphaTwo = 0.30;
        double[] meansTwo = {5.0, 5.0};
        double[][] covTwo = {{1.0, 0.0}, {0.0, 1.0}};
        MultivariateNormalDistribution distTwo = new MultivariateNormalDistribution(meansTwo, covTwo);
        Pair pairTwo = new Pair(alphaTwo, distTwo);
        mixture.add(pairTwo);

        /* add the list of pairs to the mixture model and sample the points */
        MixtureMultivariateNormalDistribution dist = new MixtureMultivariateNormalDistribution(mixture);

        /* we don't need a seed, but it helps if we want to recall the same data */
        dist.reseedRandomGenerator(0L);

        /* generate 10 random data from our dist */
        double[][] data = dist.sample(1000);
        
        double[][] dataOne = dist.getComponents().get(0).getSecond().sample(700);
        double[][] dataTwo = dist.getComponents().get(1).getSecond().sample(300);
        
        
        XYChart.Series series = new XYChart.Series();
        for (double[] datum : data) {
            series.getData().add(new XYChart.Data(datum[0], datum[1]));
        }
        
        XYChart.Series seriesOne = new XYChart.Series();
        for (double[] datum : dataOne) {
            seriesOne.getData().add(new XYChart.Data(datum[0], datum[1]));
        }
        
        XYChart.Series seriesTwo = new XYChart.Series();
        for (double[] datum : dataTwo) {
            seriesTwo.getData().add(new XYChart.Data(datum[0], datum[1]));
        }
        
        NumberAxis xAxis = new NumberAxis("x",-4, 4, 1.0);
        NumberAxis yAxis = new NumberAxis("y",-4, 4, 1.0);
        xAxis.setMinorTickVisible(false);
        yAxis.setMinorTickVisible(false);
        
        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.getData().add(seriesOne);
        scatterChart.setHorizontalGridLinesVisible(false);
        scatterChart.setVerticalGridLinesVisible(false);
        scatterChart.setHorizontalZeroLineVisible(false);
        scatterChart.setVerticalZeroLineVisible(false);
        scatterChart.setLegendVisible(false);
        scatterChart.setAnimated(false);
        scatterChart.setBackground(null);
        
        Scene scene = new Scene(scatterChart, 600, 600);
        scene.getStylesheets().add("css/chart.css");
        stage.setScene(scene);
        stage.show();
        
//        WritableImage image = scatterChart.snapshot(new SnapshotParameters(), null);
//
//        // TODO: probably use a file chooser here
//        File file = new File("src/main/resources/plots/multinormal_2D_test.png");
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        } catch (IOException e) {
//            // TODO: handle exception here
//        }
        
    }

}
