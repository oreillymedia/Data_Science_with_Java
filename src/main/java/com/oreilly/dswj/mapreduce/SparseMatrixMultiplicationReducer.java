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
package com.oreilly.dswj.mapreduce;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;

import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * @author Michael Brzustowicz
 */
public class SparseMatrixMultiplicationReducer extends Reducer<IntWritable, SparseMatrixWritable, IntWritable, DoubleWritable>{
    
    private RealVector vector;
    
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        /* unserialize the RealVector object */
        // NOTE this is just the filename, please include the resource itself in the dist cache via -files at runtime
        //TODO set the filename in Job conf with set("vectorFileName", "actual file name here")
        String vectorFileName = context.getConfiguration().get("vectorFileName");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(vectorFileName))) {
            vector = (RealVector) in.readObject();
        } catch(ClassNotFoundException e) {
            //TODO
        }
    }
    
    @Override
    protected void reduce(IntWritable key, Iterable<SparseMatrixWritable> values, Context context) throws IOException, InterruptedException { 
        
        /* rely on the fact that rowVector dim has to be same as input vector dim */
        RealVector rowVector = new OpenMapRealVector(vector.getDimension());
        
        for (SparseMatrixWritable value : values) {
            rowVector.setEntry(value.columnIndex, value.entry);
        }
        
        double dotProduct = rowVector.dotProduct(vector);
        
        /* only write the nonzero outputs, since the Matrix-Vector product is probably sparse */
        if(dotProduct != 0.0) {
            /* this outputs the vector index and it's value */
            context.write(key, new DoubleWritable(dotProduct));
        }
    }
}
