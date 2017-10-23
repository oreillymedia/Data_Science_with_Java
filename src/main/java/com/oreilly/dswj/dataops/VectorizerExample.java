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

import com.oreilly.dswj.datasets.Sentiment;
import java.io.IOException;
import org.apache.commons.math3.linear.RealMatrix;

/**
 *
 * @author Michael Brzustowicz
 */
public class VectorizerExample {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        Sentiment sentiment = new Sentiment();
        
        TermDictionary termDictionary = new TermDictionary();
        
        SimpleTokenizer tokenizer = new SimpleTokenizer();
        
        for (String document : sentiment.getDocuments()) {
            String[]tokens = tokenizer.getTokens(document);
            termDictionary.addTerms(tokens);
        }
        
//        System.out.println(termDictionary.getTermIndex("iasdfasd"));
//        System.exit(0);
        
        
        Vectorizer vectorizer = new Vectorizer(termDictionary, tokenizer, false);
        RealMatrix counts = vectorizer.getCountMatrix(sentiment.getDocuments());
        System.out.println(counts.getSubMatrix(0, 5, 0, 5));
        
        Vectorizer binaryVectorizer = new Vectorizer(termDictionary, tokenizer, true);
        RealMatrix binCounts = binaryVectorizer.getCountMatrix(sentiment.getDocuments());
        System.out.println(binCounts.getSubMatrix(0, 5, 0, 5));
        
        TFIDFVectorizer tfidfVectorizer = new TFIDFVectorizer(termDictionary, tokenizer);
        RealMatrix tfidf = tfidfVectorizer.getTFIDF(sentiment.getDocuments());
        System.out.println(tfidf.getSubMatrix(0, 5, 0, 5));
        
    }
    
}
