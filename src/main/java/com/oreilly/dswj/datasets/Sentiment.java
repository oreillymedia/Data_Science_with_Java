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
package com.oreilly.dswj.datasets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Sentiment labelled sentences
 * https://archive.ics.uci.edu/ml/datasets/Sentiment+Labelled+Sentences
 * contains data from imdb, yelp and amazon
 * imdb has 1000 sent with 500 pos (1) and 500 neg (0)
 * yelp has 3729 sent with 500 pos (1) and 500 neg (0)
 * amzn has 15004 sent with 500 pos (1) and 500 neg (0)
 * @author Michael Brzustowicz
 */
public class Sentiment {

    private final List<String> documents = new ArrayList<>();
    private final List<Integer> sentiments = new ArrayList<>();
    private static final String IMDB_RESOURCE = "/datasets/sentiment/imdb_labelled.txt";
    private static final String YELP_RESOURCE = "/datasets/sentiment/yelp_labelled.txt";
    private static final String AMZN_RESOURCE = "/datasets/sentiment/amazon_cells_labelled.txt";
    public enum DataSource {IMDB, YELP, AMZN};

    public Sentiment() throws IOException {
        parseResource(IMDB_RESOURCE); // 1000 sentences
        parseResource(YELP_RESOURCE); // 1000 sentences
        parseResource(AMZN_RESOURCE); // 1000 sentences
    }

    public List<Integer> getSentiments(DataSource dataSource) {
        int fromIndex = 0; // inclusive
        int toIndex = 3000; // exclusive
        switch(dataSource) {
            case IMDB:
                fromIndex = 0;
                toIndex = 1000;
                break;
            case YELP:
                fromIndex = 1000;
                toIndex = 2000;
                break;
            case AMZN:
                fromIndex = 2000;
                toIndex = 3000;
                break;
        }
        return sentiments.subList(fromIndex, toIndex);
    }
    
    public List<String> getDocuments(DataSource dataSource) {
        int fromIndex = 0; // inclusive
        int toIndex = 3000; // exclusive
        switch(dataSource) {
            case IMDB:
                fromIndex = 0;
                toIndex = 1000;
                break;
            case YELP:
                fromIndex = 1000;
                toIndex = 2000;
                break;
            case AMZN:
                fromIndex = 2000;
                toIndex = 3000;
                break;
        }
        return documents.subList(fromIndex, toIndex);
    }
    
    public List<Integer> getSentiments() {
        return sentiments;
    }

    public List<String> getDocuments() {
        return documents;
    }
    
    private void parseResource(String resource) throws IOException {
        try(InputStream inputStream = getClass().getResourceAsStream(resource)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split("\t");
                // both yelp and amzn have many sentences with no label
                if (splitLine.length > 1) {
                    documents.add(splitLine[0]);
                    sentiments.add(Integer.parseInt(splitLine[1]));
                }
            }
        }
    }
}
