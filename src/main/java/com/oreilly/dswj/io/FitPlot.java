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
package com.oreilly.dswj.io;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.Random;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

/**
 *
 * @author Michael Brzustowicz
 */
public class FitPlot extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        // simulate some random data points
        double sigma = 0.1;
        double damp = 0.5;
        int n = 50;
        double x[] = new double[n];
        double y[] = new double[n];
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            x[i] = i * 5*2*Math.PI / n;
            y[i] = Math.exp(-x[i]*damp*damp/2) * Math.cos(x[i]) + sigma * r.nextGaussian();
        }
        
        // these are the model and 2 sigma bounds
        int nSim = 1000;
        double xSim[] = new double[nSim];
        double ySim[] = new double[nSim];
        double ySimUp[] = new double[nSim];
        double ySimDn[] = new double[nSim];
        for (int i = 0; i < nSim; i++) {
            xSim[i] = i * 5*2*Math.PI / nSim;
            ySim[i] = Math.exp(-xSim[i]*damp*damp/2) * Math.cos(xSim[i]);
            ySimUp[i] = ySim[i] + 2*sigma;
            ySimDn[i] = ySim[i] - 2*sigma;
        }
        
        
        // MAKE THE PLOT
        
        stage.setTitle("Fit Plot");
        
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel("f(x)");
        
        //creating the chart
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setAnimated(false);
        lineChart.setTitle("Plot title goes here");
        
        //defining a data series
        XYChart.Series series = new XYChart.Series();
        series.setName("data");
        for (int i = 0; i < x.length; i++) {
            series.getData().add(new XYChart.Data(x[i], y[i]));
        }

        //defining a simulation series
        XYChart.Series sim = new XYChart.Series();
        sim.setName("simulation");
        for (int i = 0; i < xSim.length; i++) {
            sim.getData().add(new XYChart.Data(xSim[i], ySim[i]));
        }

        //defining a simulation upper band series
        XYChart.Series simUp = new XYChart.Series();
        simUp.setName("upper band");
        for (int i = 0; i < xSim.length; i++) {
            simUp.getData().add(new XYChart.Data(xSim[i], ySimUp[i]));
        }
        
        //defining a simulation lower band series
        XYChart.Series simDn = new XYChart.Series();
        simDn.setName("lower band");
        for (int i = 0; i < xSim.length; i++) {
            simDn.getData().add(new XYChart.Data(xSim[i], ySimDn[i]));
        }

        lineChart.getData().addAll(series, sim, simUp, simDn);
        lineChart.setBackground(null);
        lineChart.setLegendVisible(false);

        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
        
        lineChart.setAnimated(false); // NEED to do this if writing to file !!!
       
        Scene scene  = new Scene(lineChart,800,600);
        scene.getStylesheets().add("css/chart.css"); // this is in resources dir
        stage.setScene(scene);
        stage.show();
        
        
        
        /* uncomment below to write to file in addition to screen rendering */
        
//        WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);
//
//        File file = new File("name_of_saved_file_goes_here.png");
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
//        } catch (IOException e) {
//            // TODO: handle exception here
//        }
    
    }
    
}
