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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author Michael Brzustowicz
 */
public class DeepNetwork extends IterativeLearningProcess {

    private final List<NetworkLayer> layers;

    public DeepNetwork() {
        this.layers = new ArrayList<>();
    }

    public void addLayer(NetworkLayer networkLayer) {
        layers.add(networkLayer);
    }

    public List<NetworkLayer> getLayers() {
        return layers;
    }
    
    @Override
    public RealMatrix predict(RealMatrix input) {
        
        /* the initial input MUST BE DEEP COPIED or is overwritten */
        RealMatrix layerInput = input.copy();
        
        for (NetworkLayer layer : layers) {
            
            layer.setInput(layerInput);
            
            /* calc the output and set to next layer input*/
            RealMatrix output = layer.getOutput(layerInput);
            layer.setOutput(output);
            
            /* 
                does not need a deep copy, but be aware that 
                every layer input shares memory of prior layer output
            */
            layerInput = output;
   
        }

        /* layerInput is holding the final output ... get a deep copy */
        return layerInput.copy();

    }
    
    @Override
    protected void update(RealMatrix input, RealMatrix target, RealMatrix output) {
        
        /* this is the gradient of the network error and starts the back prop process */
        RealMatrix layerError = getLossFunction().getLossGradient(output, target).copy();
        
        /* create list iterator and set cursor to last! */
        ListIterator li = layers.listIterator(layers.size());
        
        while (li.hasPrevious()) {

            NetworkLayer layer = (NetworkLayer) li.previous(); 
            
            /* get error input from higher layer */
            layer.setOutputError(layerError);
            
            /* this back propagates the error and updates weights */
            layer.update();
            
            /* pass along error to next layer down */
            layerError = layer.getInputError();
            
        }
    }
}
