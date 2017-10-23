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
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.EmpiricalDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author Michael Brzustowicz
 */
public class HistogramExample extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        
        int numPoints = 2500;
        NormalDistribution nd = new NormalDistribution();
        double[] data = nd.sample(numPoints);
        
        // default constructor assigns bins = 1000
        // better to try numPoints / 10
        EmpiricalDistribution dist = new EmpiricalDistribution(25);
        dist.load(data); // can also load from file or url
        
        // implemented in separate method below
        BarChart<String, Number> chart = getHistogram(dist);
        
        /* 
        create a scene using the chart
        */
        Scene scene  = new Scene(chart,800,600);
        
        /*
         tell the stage what scene to use and render it!
        */
        stage.setScene(scene);
        stage.show();
        
//        WritableImage image = chart.snapshot(new SnapshotParameters(), null);
//        File file = new File("histogram.png");
//        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
    }
    
    private BarChart<String, Number> getHistogram(EmpiricalDistribution dist) {
        
        List<SummaryStatistics> ss = dist.getBinStats();
        
        XYChart.Series series = new XYChart.Series();
        
        long totalCount = dist.getSampleStats().getN();
        double binWidth = (dist.getSampleStats().getMax() - dist.getSampleStats().getMin())/ (dist.getBinCount()+1);
        long cumCount = 0;
        long maxCount = 0;
        double cum = 0.0;
        // the bins are ordered by their list index
        int binNum = 0;
        for (SummaryStatistics s : ss) {
            maxCount = Math.max(s.getN(), maxCount);
            cumCount += s.getN();
            double p = s.getN() / binWidth / totalCount;
            cum += p;
            
            /* counts */
            series.getData().add(new Data(Integer.toString(binNum++), s.getN()));
            
            /* display average value of bin instead of bin_num */
//            series.getData().add(new Data(Double.toString(s.getMean()), s.getN()));
            
            /* pdf */
//            series.getData().add(new Data(Integer.toString(bin_num++), p));//s.getN()));
        }
        
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();//"count", 0, maxCount, maxCount / 10);
        xAxis.setLabel("Bin Number");
        yAxis.setLabel("Count");
        yAxis.setMinorTickCount(0);
        yAxis.setMinorTickLength(0);
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setAnimated(false); // required if saving to file
        chart.getData().add(series);
        chart.setTitle("Distribution of Random Normal");
        chart.setBackground(null);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.setVerticalZeroLineVisible(false);
        chart.setBarGap(0.0);
        chart.setCategoryGap(0.0);
        chart.setLegendVisible(false);
        
        return chart;
        
        
    }
    
}
