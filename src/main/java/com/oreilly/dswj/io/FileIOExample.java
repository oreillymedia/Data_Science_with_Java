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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Michael Brzustowicz
 */
public class FileIOExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String filename = "<filename with path goes here>";
        
        // or use args to get filename
        
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            
            String line;
            
            while ((line = br.readLine()) != null) {
                // TODO ... do something with line
                // TODO ... parse line e.g. CSV, TSV, JSON
                // TODO ... check each value if required
                System.out.println(line);
            }
            
        } catch (IOException e) {
            System.err.println(e);
        }
        
    }
    
}
