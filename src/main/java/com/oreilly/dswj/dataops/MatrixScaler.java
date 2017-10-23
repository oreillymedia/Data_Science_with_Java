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

import java.io.Serializable;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.MultivariateSummaryStatistics;

/**
 *
 * @author Michael Brzustowicz
 */
public class MatrixScaler implements Serializable{
    
    private static final long serialVersionUID = -3594871411162054132L;

    public static void minmax(RealMatrix matrix) {
        MultivariateSummaryStatistics mss = getStats(matrix);
        matrix.walkInOptimizedOrder(new MatrixScalingOperator(mss, MatrixScaleType.MINMAX));
    }
    
    public static void center(RealMatrix matrix) {
        MultivariateSummaryStatistics mss = getStats(matrix);
        matrix.walkInOptimizedOrder(new MatrixScalingOperator(mss, MatrixScaleType.CENTER));
    }
    
    public static void zscore(RealMatrix matrix) {
        MultivariateSummaryStatistics mss = getStats(matrix);
        matrix.walkInOptimizedOrder(new MatrixScalingOperator(mss, MatrixScaleType.ZSCORE));
    }
    
    public static void l1(RealMatrix matrix) {
        RealVector normals = getL1Normals(matrix);
        matrix.walkInOptimizedOrder(new MatrixScalingOperator(normals, MatrixScaleType.L1));
    }
    
    public static void l2(RealMatrix matrix) {
        RealVector normals = getL2Normals(matrix);
        matrix.walkInOptimizedOrder(new MatrixScalingOperator(normals, MatrixScaleType.L2));
    }
    
    private static RealVector getL1Normals(RealMatrix matrix) {
        RealVector normals = new OpenMapRealVector(matrix.getRowDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            double l1Norm = matrix.getRowVector(i).getL1Norm();
            if (l1Norm > 0) {
                normals.setEntry(i, l1Norm);
            }
        }
        return normals;
    }
    
    private static RealVector getL2Normals(RealMatrix matrix) {
        RealVector normals = new OpenMapRealVector(matrix.getRowDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            double l2Norm = matrix.getRowVector(i).getNorm();
            if (l2Norm > 0) {
                normals.setEntry(i, l2Norm);
            }
        }
        return normals;
    }
    
    private static MultivariateSummaryStatistics getStats(RealMatrix matrix) {
        MultivariateSummaryStatistics mss = new MultivariateSummaryStatistics(matrix.getColumnDimension(), true);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            mss.addValue(matrix.getRow(i));
        }
        return mss;
    }
    
    RealVector normals;
    MultivariateSummaryStatistics mss;
    MatrixScaleType scaleType;

    public MatrixScaler(MatrixScaleType scaleType) {
        this.scaleType = scaleType;
    }

    
    
    public MatrixScaler(RealMatrix matrix, MatrixScaleType scaleType) {
        this.scaleType = scaleType;
        switch (scaleType) {

            case MINMAX:
            case CENTER:
            case ZSCORE:
                mss = new MultivariateSummaryStatistics(matrix.getColumnDimension(), true);
                for (int i = 0; i < matrix.getRowDimension(); i++) {
                    mss.addValue(matrix.getRow(i));
                }
                matrix.walkInOptimizedOrder(new MatrixScalingOperator(mss, scaleType));
                break;

            case L1:
                normals = new OpenMapRealVector(matrix.getRowDimension());
                for (int i = 0; i < matrix.getRowDimension(); i++) {
                    double l1Norm = matrix.getRowVector(i).getL1Norm();
                    if (l1Norm > 0) {
                        normals.setEntry(i, l1Norm);
                    }
                }
                matrix.walkInOptimizedOrder(new MatrixScalingOperator(normals, scaleType));
                break;

            case L2:
                normals = new OpenMapRealVector(matrix.getRowDimension());
                for (int i = 0; i < matrix.getRowDimension(); i++) {
                    double l2Norm = matrix.getRowVector(i).getNorm();
                    if (l2Norm > 0) {
                        normals.setEntry(i, l2Norm);
                    }
                }
                matrix.walkInOptimizedOrder(new MatrixScalingOperator(normals, scaleType));
                break;

            default:
                break;
        }
        
        
    }
    
    public void transform(RealMatrix matrix) {
        switch (scaleType) {

            case MINMAX:
            case CENTER:
            case ZSCORE:
                mss = new MultivariateSummaryStatistics(matrix.getColumnDimension(), true);
                for (int i = 0; i < matrix.getRowDimension(); i++) {
                    mss.addValue(matrix.getRow(i));
                }
                matrix.walkInOptimizedOrder(new MatrixScalingOperator(mss, scaleType));
                break;

            case L1:
                normals = new OpenMapRealVector(matrix.getRowDimension());
                for (int i = 0; i < matrix.getRowDimension(); i++) {
                    double l1Norm = matrix.getRowVector(i).getL1Norm();
                    if (l1Norm > 0) {
                        normals.setEntry(i, l1Norm);
                    }
                }
                matrix.walkInOptimizedOrder(new MatrixScalingOperator(normals, scaleType));
                break;

            case L2:
                normals = new OpenMapRealVector(matrix.getRowDimension());
                for (int i = 0; i < matrix.getRowDimension(); i++) {
                    double l2Norm = matrix.getRowVector(i).getNorm();
                    if (l2Norm > 0) {
                        normals.setEntry(i, l2Norm);
                    }
                }
                matrix.walkInOptimizedOrder(new MatrixScalingOperator(normals, scaleType));
                break;

            default:
                break;
        } 
    }

}
