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
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * 
 * 
 * Anscombe's quartet
I	II	III	IV
x	y	x	y	x	y	x	y
10.0	8.04	10.0	9.14	10.0	7.46	8.0	6.58
8.0	6.95	8.0	8.14	8.0	6.77	8.0	5.76
13.0	7.58	13.0	8.74	13.0	12.74	8.0	7.71
9.0	8.81	9.0	8.77	9.0	7.11	8.0	8.84
11.0	8.33	11.0	9.26	11.0	7.81	8.0	8.47
14.0	9.96	14.0	8.10	14.0	8.84	8.0	7.04
6.0	7.24	6.0	6.13	6.0	6.08	8.0	5.25
4.0	4.26	4.0	3.10	4.0	5.39	19.0	12.50
12.0	10.84	12.0	9.13	12.0	8.15	8.0	5.56
7.0	4.82	7.0	7.26	7.0	6.42	8.0	7.91
5.0	5.68	5.0	4.74	5.0	5.73	8.0	6.89

 * @author Michael Brzustowicz
 */
public class AnscombesPlotExample extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Double[] x1 = {10.0, 8.0, 13.0, 9.0, 11.0, 14.0, 6.0, 4.0, 12.0, 7.0, 5.0};
        Double[] y1 = {8.04, 6.95, 7.58, 8.81, 8.33, 9.96, 7.24, 4.26, 10.84, 4.82, 5.68};
        Double[] x2 = {10.0, 8.0, 13.0, 9.0, 11.0, 14.0, 6.0, 4.0, 12.0, 7.0, 5.0};
        Double[] y2 = {9.14, 8.14, 8.74, 8.77, 9.26, 8.10, 6.13, 3.10, 9.13, 7.26, 4.74};
        Double[] x3 = {10.0, 8.0, 13.0, 9.0, 11.0, 14.0, 6.0, 4.0, 12.0, 7.0, 5.0};
        Double[] y3 = {7.46, 6.77, 12.74, 7.11, 7.81, 8.84, 6.08, 5.39, 8.15, 6.42, 5.73};
        Double[] x4 = {8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 19.0, 8.0, 8.0, 8.0};
        Double[] y4 = {6.58, 5.76, 7.71, 8.84, 8.47, 7.04, 5.25, 12.50, 5.56, 7.91, 6.89};
        
        final LineChart<Number, Number> lc1 = createLineChart(x1, y1);
        final LineChart<Number, Number> lc2 = createLineChart(x2, y2);
        final LineChart<Number, Number> lc3 = createLineChart(x3, y3);
        final LineChart<Number, Number> lc4 = createLineChart(x4, y4);
        
        lc1.getXAxis().setLabel("x1");
        lc1.getYAxis().setLabel("y1");
        lc2.getXAxis().setLabel("x2");
        lc2.getYAxis().setLabel("y2");
        lc3.getXAxis().setLabel("x3");
        lc3.getYAxis().setLabel("y3");
        lc4.getXAxis().setLabel("x4");
        lc4.getYAxis().setLabel("y4");
        
        FlowPane root = new FlowPane();
        root.setBackground(null); // otherwise it's grey
        root.getChildren().addAll(lc1, lc2, lc3, lc4);

        Scene scene = new Scene(root, 1024, 800);

        primaryStage.setTitle("Anscombe's Quartet");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        /* write the plot to a file */
//        WritableImage image = root.snapshot(new SnapshotParameters(), null);
//
//        // TODO: probably use a file chooser here
//        File file = new File("anscombes_quartet.png");
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        } catch (IOException e) {
//            // TODO: handle exception here
//        }
    }
    
    public LineChart<Number,Number> createLineChart(Double[] x, Double[] y) {
        final NumberAxis xAxis = new NumberAxis("x", 3, 20, 2);
        final NumberAxis yAxis = new NumberAxis("y", 4, 13, 2);
        xAxis.setMinorTickCount(0);
        xAxis.setMinorTickLength(0);
        yAxis.setMinorTickCount(0);
        yAxis.setMinorTickLength(0);
        
        //creating the chart
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setAnimated(false); // required for writing to file !
//        lineChart.setTitle("title goes here");
        
        //defining a data series
        XYChart.Series series = new XYChart.Series();
        series.setName("data");
        for (int i = 0; i < x.length; i++) {
            series.getData().add(new XYChart.Data(x[i], y[i]));
        }

        //defining a simulation series for y = 3.00 + 0.500 x
        XYChart.Series fit = new XYChart.Series();
        fit.setName("fit");
        int numPoints = 11;
        double xMin = 4.0;
        double xMax = 19.0;
        double delta = (xMax - xMin) / numPoints;
        for (int i = 0; i < numPoints; i++) {
            double x_fit = xMin + i * delta;
            double y_fit = 3.00 + 0.500 * x_fit;
            fit.getData().add(new XYChart.Data(x_fit, y_fit));
        }

        /**
         * in css, fit comes first, then series data
         * this way, data floats on top of line, as it should!
         */
        lineChart.getData().addAll(fit, series);
        lineChart.setBackground(null);
        lineChart.setLegendVisible(false);

        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        
        lineChart.getStylesheets().add("css/data_with_fit_line.css");
        
        return lineChart;
    }
    
}
