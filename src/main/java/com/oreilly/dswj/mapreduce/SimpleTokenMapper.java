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

import com.oreilly.dswj.dataops.SimpleTokenizer;
import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @author Michael Brzustowicz
 */
public class SimpleTokenMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    SimpleTokenizer tokenizer;
    
    @Override
    protected void setup(Context context) throws IOException {
        
        tokenizer = new SimpleTokenizer(3); // mintokensize = 3 !!!
    }
    
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        
        String[] tokens = tokenizer.getTokens(value.toString());

        for (String token : tokens) {
            context.write(new Text(token), new LongWritable(1L));
        }
    }
}
