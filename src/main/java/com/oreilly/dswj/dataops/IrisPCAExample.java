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
package com.oreilly.dswj.dataops;

import com.oreilly.dswj.datasets.Iris;
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
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author Michael Brzustowicz
 */
public class IrisPCAExample extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Iris iris = new Iris();

        PCA pca = new PCA(iris.getData(), new PCAEIGImplementation());
        System.out.println(pca.getExplainedVariances());
        System.out.println(pca.getCumulativeVariances());
        RealMatrix irispca = pca.getPrincipalComponents(0.98);//2);
        System.out.println(irispca);

        
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < irispca.getRowDimension(); i++) {
            series.getData().add(new XYChart.Data(irispca.getEntry(i, 0), irispca.getEntry(i, 1)));
        }
        
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("X1");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("X2");
        ScatterChart<Number,Number> scatterChart = new ScatterChart<>(xAxis,yAxis);
        scatterChart.getData().add(series);
        scatterChart.setAnimated(false);
        
        Scene scene  = new Scene(scatterChart,800,600);
        
        /*
         tell the stage what scene to use and render it!
        */
        stage.setScene(scene);
        stage.show();
        
//        WritableImage image = scatterChart.snapshot(new SnapshotParameters(), null);

        // TODO: probably use a file chooser here
//        File outFile = new File("Iris2PCA.png");
//
//        try {
//            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outFile);
//        } catch (IOException e) {
//            // TODO: handle exception here
//        }
        
    }
    
}
