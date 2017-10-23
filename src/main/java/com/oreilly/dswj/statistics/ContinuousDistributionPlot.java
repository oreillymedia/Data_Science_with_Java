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
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

/**
 *
 * @author Michael Brzustowicz
 */
public class ContinuousDistributionPlot extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        /* make the dataset */
        NormalDistribution dist = new NormalDistribution();
//        LogNormalDistribution dist = new LogNormalDistribution();
//        UniformRealDistribution dist = new UniformRealDistribution();
        
        int n = 1000;
        double[] x = new double[n];
        double[] y = new double[n];
        double min = -5;
        double max = 5;
        double delta = max - min;
        for (int i = 0; i < n; i++) {

            x[i] = min + i * delta / n;
            y[i] = dist.density(x[i]); // this is for PDF
//            y[i] = dist.cumulativeProbability(x[i]); // this is for CDF
        }
        
        /* CREATE THE PLOT */
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < x.length; i++) {
            series.getData().add(new XYChart.Data(x[i], y[i]));
        }
        NumberAxis xAxis = new NumberAxis("x", -5, 5, 1);
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel("f(x)"); // this is for PDF
//        yAxis.setLabel("F(x)"); // this is for CDF
        xAxis.setMinorTickVisible(false);
        yAxis.setMinorTickVisible(false);
        

        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setAnimated(false); // need this to save file
        lineChart.getData().addAll(series);
        lineChart.setBackground(null);
        lineChart.setLegendVisible(false);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setVerticalZeroLineVisible(false);
        lineChart.setCreateSymbols(false);
       
        Scene scene  = new Scene(lineChart,800,600);
//        scene.getStylesheets().add("css/<something.css>");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        /* uncomment below to write to file in addition to screen rendering */
//        WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);

        // TODO: probably use a file chooser here
//        File file = new File("plot.png");

//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        } catch (IOException e) {
            // TODO: handle exception here
//        }
        
        
    }
    
}
