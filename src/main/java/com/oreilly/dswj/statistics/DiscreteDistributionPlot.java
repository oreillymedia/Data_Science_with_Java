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
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;

/**
 *
 * @author Michael Brzustowicz
 */
public class DiscreteDistributionPlot extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* make the dataset */
        
//        BinomialDistribution dist = new BinomialDistribution(40, 0.5);
        PoissonDistribution dist = new PoissonDistribution(5.0);
        int n = 21;
        double[] x = new double[n];
        double[] y = new double[n];
        double min = 0;
        double max = 21;
        double delta = max - min;
        for (int i = 0; i < n; i++) {
            x[i] = i;
            y[i] = dist.probability(i); // PMF
//            y[i] = dist.cumulativeProbability(i); //CDF
        }
        
        /* */
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < x.length; i++) {
            series.getData().add(new XYChart.Data(x[i], y[i]));
        }
        
        final NumberAxis xAxis = new NumberAxis("k", 0, 20, 5);
        final NumberAxis yAxis = new NumberAxis();

        yAxis.setLabel("f(k)");
//        xAxis.setTickLabelFont(new Font(12));
//        xAxis.setTickUnit(1.0 / 41.0);
        xAxis.setMinorTickVisible(false);
        yAxis.setMinorTickVisible(false);
        
        //discrete
        final ScatterChart<Number,Number> chart = new ScatterChart<>(xAxis,yAxis);
        
        chart.setAnimated(false);
        
        chart.getData().addAll(series);
        chart.setBackground(null);
        chart.setLegendVisible(false);

        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.setVerticalZeroLineVisible(false);
//        lineChart.setCreateSymbols(false);
//        lineChart.setStyle(".chart-plot-background {-fx-background-color: #ffffff;}");
       
        Scene scene  = new Scene(chart,800,600);
        scene.getStylesheets().add("css/chart_lineplot.css");
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
