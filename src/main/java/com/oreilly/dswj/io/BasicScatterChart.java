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
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Michael Brzustowicz
 */
public class BasicScatterChart extends Application {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);   
    }

    @Override
    public void start(Stage stage) throws Exception {
        int[] xData = {1, 2, 3, 4, 5};
        double[] yData = {1.3, 2.1, 3.3, 4.0, 4.8};

        /*
         create some data
        */
        Series series = new Series();
        for (int i = 0; i < yData.length; i++) {
            series.getData().add(new Data(xData[i], yData[i]));
        }
        
        
        //defining the axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel("y");
        
        //creating the scatter chart
        ScatterChart<Number,Number> scatterChart = new ScatterChart<>(xAxis,yAxis);
        scatterChart.setAnimated(false);
        scatterChart.getData().add(series);
        scatterChart.setTitle("x vs. y");
        scatterChart.setHorizontalGridLinesVisible(false);
        scatterChart.setVerticalGridLinesVisible(false);
        scatterChart.setVerticalZeroLineVisible(false);
        
        /* 
        create a scene using the chart
        */
        Scene scene  = new Scene(scatterChart,800,600);
        
        /*
         tell the stage what scene to use and render it!
        */
        stage.setScene(scene);
        stage.show();
        
//        WritableImage image = scatterChart.snapshot(new SnapshotParameters(), null);
//        File file = new File("chart.png");
//        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        
    }
    
}
