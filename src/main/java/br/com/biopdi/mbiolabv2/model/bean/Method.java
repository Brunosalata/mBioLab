package br.com.biopdi.mbiolabv2.model.bean;

/*
 *  Copyright (c) 2023. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 *  Licensed under the Biopdi® License, Version 1.0.
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://biopdi.com.br/
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class Method {

    private Integer id, autoBreakIndex, specimenTypeIndex, normIndex, essayTypeIndex, extensometer1Index,
            extensometer2Index, chartViewIndex;
    private String methodName, methodDate;
    private Double essayVelocity, specimenAValue, specimenBValue, specimenCrossSectionArea, specimenCrossSectionLength,
            offsetIntersectionLine, gainIntersectionLine;

    public Method() {
    }

    public Method(Integer id, Integer autoBreakIndex, Integer specimenTypeIndex, Integer normIndex,
                  Integer essayTypeIndex, Integer extensometer1Index, Integer extensometer2Index,
                  Integer chartViewIndex, String methodName, String methodDate, Double essayVelocity,
                  Double specimenAValue, Double specimenBValue, Double specimenCrossSectionArea,
                  Double specimenCrossSectionLength, Double offsetIntersectionLine, Double gainIntersectionLine) {
        this.id = id;
        this.autoBreakIndex = autoBreakIndex;
        this.specimenTypeIndex = specimenTypeIndex;
        this.normIndex = normIndex;
        this.essayTypeIndex = essayTypeIndex;
        this.extensometer1Index = extensometer1Index;
        this.extensometer2Index = extensometer2Index;
        this.chartViewIndex = chartViewIndex;
        this.methodName = methodName;
        this.methodDate = methodDate;
        this.essayVelocity = essayVelocity;
        this.specimenAValue = specimenAValue;
        this.specimenBValue = specimenBValue;
        this.specimenCrossSectionArea = specimenCrossSectionArea;
        this.specimenCrossSectionLength = specimenCrossSectionLength;
        this.offsetIntersectionLine = offsetIntersectionLine;
        this.gainIntersectionLine = gainIntersectionLine;
    }

    public Method(Integer autoBreakIndex, Integer specimenTypeIndex, Integer normIndex, Integer essayTypeIndex,
                  Integer extensometer1Index, Integer extensometer2Index, Integer chartViewIndex, String methodName,
                  String methodDate, Double essayVelocity, Double specimenAValue, Double specimenBValue,
                  Double specimenCrossSectionArea, Double specimenCrossSectionLength, Double offsetIntersectionLine,
                  Double gainIntersectionLine) {
        this.autoBreakIndex = autoBreakIndex;
        this.specimenTypeIndex = specimenTypeIndex;
        this.normIndex = normIndex;
        this.essayTypeIndex = essayTypeIndex;
        this.extensometer1Index = extensometer1Index;
        this.extensometer2Index = extensometer2Index;
        this.chartViewIndex = chartViewIndex;
        this.methodName = methodName;
        this.methodDate = methodDate;
        this.essayVelocity = essayVelocity;
        this.specimenAValue = specimenAValue;
        this.specimenBValue = specimenBValue;
        this.specimenCrossSectionArea = specimenCrossSectionArea;
        this.specimenCrossSectionLength = specimenCrossSectionLength;
        this.offsetIntersectionLine = offsetIntersectionLine;
        this.gainIntersectionLine = gainIntersectionLine;
    }

    public Integer getId() {
        return id;
    }
    public Integer getAutoBreakIndex() {
        return autoBreakIndex;
    }
    public void setAutoBreakIndex(Integer autoBreakIndex) {
        this.autoBreakIndex = autoBreakIndex;
    }
    public Integer getSpecimenTypeIndex() {
        return specimenTypeIndex;
    }
    public void setSpecimenTypeIndex(Integer specimenTypeIndex) {
        this.specimenTypeIndex = specimenTypeIndex;
    }
    public Integer getNormIndex() {
        return normIndex;
    }
    public void setNormIndex(Integer normIndex) {
        this.normIndex = normIndex;
    }
    public Integer getEssayTypeIndex() {
        return essayTypeIndex;
    }
    public void setEssayTypeIndex(Integer essayTypeIndex) {
        this.essayTypeIndex = essayTypeIndex;
    }
    public Integer getExtensometer1Index() {
        return extensometer1Index;
    }
    public void setExtensometer1Index(Integer extensometer1Index) {
        this.extensometer1Index = extensometer1Index;
    }
    public Integer getExtensometer2Index() {
        return extensometer2Index;
    }
    public void setExtensometer2Index(Integer extensometer2Index) {
        this.extensometer2Index = extensometer2Index;
    }
    public Integer getChartViewIndex() {
        return chartViewIndex;
    }
    public void setChartViewIndex(Integer chartViewIndex) {
        this.chartViewIndex = chartViewIndex;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public String getMethodDate() {
        return methodDate;
    }
    public void setMethodDate(String methodDate) {
        this.methodDate = methodDate;
    }
    public Double getEssayVelocity() {
        return essayVelocity;
    }
    public void setEssayVelocity(Double essayVelocity) {
        this.essayVelocity = essayVelocity;
    }
    public Double getSpecimenAValue() {
        return specimenAValue;
    }
    public void setSpecimenAValue(Double specimenAValue) {
        this.specimenAValue = specimenAValue;
    }
    public Double getSpecimenBValue() {
        return specimenBValue;
    }
    public void setSpecimenBValue(Double specimenBValue) {
        this.specimenBValue = specimenBValue;
    }
    public Double getSpecimenCrossSectionArea() {
        return specimenCrossSectionArea;
    }
    public void setSpecimenCrossSectionArea(Double specimenCrossSectionArea) {
        this.specimenCrossSectionArea = specimenCrossSectionArea;
    }
    public Double getSpecimenCrossSectionLength() {
        return specimenCrossSectionLength;
    }
    public void setSpecimenCrossSectionLength(Double specimenCrossSectionLength) {
        this.specimenCrossSectionLength = specimenCrossSectionLength;
    }
    public Double getOffsetIntersectionLine() {
        return offsetIntersectionLine;
    }
    public void setOffsetIntersectionLine(Double offsetIntersectionLine) {
        this.offsetIntersectionLine = offsetIntersectionLine;
    }
    public Double getGainIntersectionLine() {
        return gainIntersectionLine;
    }
    public void setGainIntersectionLine(Double gainIntersectionLine) {
        this.gainIntersectionLine = gainIntersectionLine;
    }

    @Override
    public String toString() {
        return "Method{" +
                "id=" + id +
                ", autoBreakIndex=" + autoBreakIndex +
                ", specimenTypeIndex=" + specimenTypeIndex +
                ", normIndex=" + normIndex +
                ", essayTypeIndex=" + essayTypeIndex +
                ", extensometer1Index=" + extensometer1Index +
                ", extensometer2Index=" + extensometer2Index +
                ", chartViewIndex=" + chartViewIndex +
                ", methodName='" + methodName + '\'' +
                ", methodDate='" + methodDate + '\'' +
                ", essayVelocity=" + essayVelocity +
                ", specimenAValue=" + specimenAValue +
                ", specimenBValue=" + specimenBValue +
                ", specimenCrossSectionArea=" + specimenCrossSectionArea +
                ", specimenCrossSectionLength=" + specimenCrossSectionLength +
                ", offsetIntersectionLine=" + offsetIntersectionLine +
                ", gainIntersectionLine=" + gainIntersectionLine +
                '}';
    }
}