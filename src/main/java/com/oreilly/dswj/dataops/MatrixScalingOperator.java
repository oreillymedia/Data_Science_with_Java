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

import org.apache.commons.math3.linear.RealMatrixChangingVisitor;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descriptive.MultivariateSummaryStatistics;

/**
 *
 * @author Michael Brzustowicz
 */
public class MatrixScalingOperator implements RealMatrixChangingVisitor {

    RealVector normals;
    MultivariateSummaryStatistics mss;
    MatrixScaleType scaleType;
    
    public MatrixScalingOperator(MultivariateSummaryStatistics mss, MatrixScaleType scaleType) {
        this.normals = null;
        this.mss = mss;
        this.scaleType = scaleType;
    }

    public MatrixScalingOperator(RealVector normals, MatrixScaleType scaleType) {
        this.normals = normals;
        this.mss = null;
        this.scaleType = scaleType;
    }

    @Override
    public void start(int rows, int columns, int startRow, int endRow,
            int startColumn, int endColumn) {
        // nothing
    }

    @Override
    public double visit(int row, int column, double value) {
        double min, max, avg, std, rowNormal;
        double scaledValue = Double.NaN;

        switch (scaleType) {

            case MINMAX:
                min = mss.getMin()[column];
                max = mss.getMax()[column];
                scaledValue = (max > min) ? (value - min) / (max - min) : (value - min);
                break;

            case CENTER:
                avg = mss.getMean()[column];
                scaledValue = value - avg;
                break;

            case ZSCORE:
                avg = mss.getMean()[column];
                std = mss.getStandardDeviation()[column];
                scaledValue = std > 0 ? (value - avg) / std : (value - avg);
                break;

            case L1:
                rowNormal = normals.getEntry(row);
                scaledValue = rowNormal > 0 ? value / rowNormal : 0;
                break;

            case L2:
                rowNormal = normals.getEntry(row);
                scaledValue = rowNormal > 0 ? value / rowNormal : 0;
                break;

            default:
                break;
        }

        return scaledValue;
    }

    @Override
    public double end() {
        return 0.0;
    }

}
