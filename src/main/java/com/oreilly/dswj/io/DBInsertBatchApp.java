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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Brzustowicz
 */
public class DBInsertBatchApp {

    public static List<Record> getData() {
        List<Record> data = new ArrayList<>();
        data.add(new Record(1, 2015, "San Francisco"));
        data.add(new Record(2, 2014, "New York"));
        data.add(new Record(3, 2012, "Los Angeles"));
        return data;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String uri = "jdbc:mysql://localhost:3306/mydb?user=root";

        try(Connection c = DriverManager.getConnection(uri)) {
            
            /* DROP / CREATE TABLE */
            String dropSQL = "DROP TABLE IF EXISTS data";
            String createSQL = "CREATE TABLE IF NOT EXISTS data(id INTEGER PRIMARY KEY, yr INTEGER, city VARCHAR(80))";
           
            try (Statement stmt = c.createStatement()) {
                
                stmt.execute(dropSQL);
                
                stmt.execute(createSQL);

            }

            /* INSERT BATCH DATA */
            String insertSQL = "INSERT INTO data(id, yr, city) VALUES(?, ?, ?)";

            try (PreparedStatement ps = c.prepareStatement(insertSQL)) {
                
                for (Record data : getData()) {
                    
                    ps.setInt(1, data.id);
                    ps.setInt(2, data.year);
                    ps.setString(3, data.city);
                    
                    /* add record to the batch !!! */
                    ps.addBatch();
                }
                
                /* note this is different than the regular execute */
                ps.executeBatch();
  
            }
            
            /* SELECT DATA */
            String selectSQL = "SELECT id, yr, city FROM data";
            try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(selectSQL)) {

                while(rs.next()) {
                    // TODO ... do something with data
                    System.out.println(rs.getInt("id")+" "+rs.getInt("yr")+" "+rs.getString("city"));
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }    
    }
}
