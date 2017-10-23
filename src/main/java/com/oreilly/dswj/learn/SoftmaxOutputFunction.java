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

import com.oreilly.dswj.dataops.MatrixScaler;
import com.oreilly.dswj.linalg.MatrixOperations;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author Michael Brzustowicz
 */
public class SoftmaxOutputFunction implements OutputFunction {

    @Override
    public RealMatrix getOutput(RealMatrix input, RealMatrix weight, RealVector bias) {
        RealMatrix output = MatrixOperations.XWplusB(input, weight, bias, new Exp());
        MatrixScaler.l1(output);
        return output;
    }

    @Override
    public RealMatrix getDelta(RealMatrix error, RealMatrix output) {
        
        RealMatrix delta = new BlockRealMatrix(error.getRowDimension(), error.getColumnDimension());
        
        for (int i = 0; i < output.getRowDimension(); i++) {
            delta.setRowVector(i, getJacobian(output.getRowVector(i)).preMultiply(error.getRowVector(i)));
        }
        
        return delta;
    }
    
    private RealMatrix getJacobian(RealVector output) {
        
        int numRows = output.getDimension();
        
        int numCols = output.getDimension();
        
        RealMatrix jacobian = new BlockRealMatrix(numRows, numCols);
        
        for (int i = 0; i < numRows; i++) {
            
            double output_i = output.getEntry(i);
            
            for (int j = i; j < numCols; j++) {
                
                double output_j = output.getEntry(j);
                
                if(i==j) {
                    
                    jacobian.setEntry(i, i, output_i*(1-output_i));
                    
                } else {
                    
                    jacobian.setEntry(i, j, -1.0 * output_i * output_j);
                    jacobian.setEntry(j, i, -1.0 * output_j * output_i);
                }
                
            }
        }
        return jacobian;
    }
    
}
