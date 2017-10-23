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

/**
 *
 * @author Michael Brzustowicz
 */
public class EntropyPlotExample extends Application {

    public static double entropy(double p) {
        double out = 0;
        if (p>0 && p<1){
            out = - ((1-p) * Math.log(1-p) + p * Math.log(p)) / Math.log(2);
        }
        return out;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        int n = 100;
        double[] p = new double[n];
        double[] h = new double[n];
        double delta = 1.0 / (n-1);
        for (int i = 0; i < n; i++) {
            p[i] = i * delta;
            h[i] = entropy(p[i]);
        }
        
        XYChart.Series series = new XYChart.Series();

        for (int i = 0; i < p.length; i++) {
            series.getData().add(new XYChart.Data(p[i], h[i]));
        }
        
        NumberAxis xAxis = new NumberAxis("p", 0, 1, 0.25);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("entropy");
        xAxis.setMinorTickVisible(false);

        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setAnimated(false);
        lineChart.getData().addAll(series);
        lineChart.setBackground(null);
        lineChart.setLegendVisible(false);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        lineChart.setVerticalZeroLineVisible(false);
        lineChart.setCreateSymbols(false);
        
        Scene scene  = new Scene(lineChart,800,600);
        scene.getStylesheets().add("css/chart_lineplot.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        /* uncomment below to write to file in addition to screen rendering */
//        WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);


        // TODO: probably use a file chooser here
//        File file = new File("entropy.png");
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        } catch (IOException e) {
//            // TODO: handle exception here
//        }
            
        
        
        
        
    }
    
}
