package br.com.biopdi.mbiolabv2.model.bean;

public class Method {

    private Integer id, autoBreakIndex, specimenTypeIndex;
    private String methodName, norm, essayType, normDescription, extensometer1, extensometer2, methodDate;
    private Double essayVelocity, specimenAValue, specimenBValue, specimenCrossSectionArea, specimenCrossSectionLength, offsetIntersectionLine, gainIntersectionLine;

    public Method() {
    }
    public Method(Integer id, Integer autoBreakIndex, Integer specimenTypeIndex, String methodName, String norm, String essayType, String normDescription, String extensometer1, String extensometer2, String methodDate, Double essayVelocity, Double specimenAValue, Double specimenBValue, Double specimenCrossSectionArea, Double specimenCrossSectionLength, Double offsetIntersectionLine, Double gainIntersectionLine) {
        this.id = id;
        this.autoBreakIndex = autoBreakIndex;
        this.specimenTypeIndex = specimenTypeIndex;
        this.methodName = methodName;
        this.norm = norm;
        this.essayType = essayType;
        this.normDescription = normDescription;
        this.extensometer1 = extensometer1;
        this.extensometer2 = extensometer2;
        this.methodDate = methodDate;
        this.essayVelocity = essayVelocity;
        this.specimenAValue = specimenAValue;
        this.specimenBValue = specimenBValue;
        this.specimenCrossSectionArea = specimenCrossSectionArea;
        this.specimenCrossSectionLength = specimenCrossSectionLength;
        this.offsetIntersectionLine = offsetIntersectionLine;
        this.gainIntersectionLine = gainIntersectionLine;
    }
    public Method(Integer autoBreakIndex, Integer specimenTypeIndex, String methodName, String norm, String essayType, String normDescription, String extensometer1, String extensometer2, String methodDate, Double essayVelocity, Double specimenAValue, Double specimenBValue, Double specimenCrossSectionArea, Double specimenCrossSectionLength, Double offsetIntersectionLine, Double gainIntersectionLine) {
        this.autoBreakIndex = autoBreakIndex;
        this.specimenTypeIndex = specimenTypeIndex;
        this.methodName = methodName;
        this.norm = norm;
        this.essayType = essayType;
        this.normDescription = normDescription;
        this.extensometer1 = extensometer1;
        this.extensometer2 = extensometer2;
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
    public void setId(Integer id) {
        this.id = id;
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
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public String getNorm() {
        return norm;
    }
    public void setNorm(String norm) {
        this.norm = norm;
    }
    public String getEssayType() {
        return essayType;
    }
    public void setEssayType(String essayType) {
        this.essayType = essayType;
    }
    public String getNormDescription() {
        return normDescription;
    }
    public void setNormDescription(String normDescription) {
        this.normDescription = normDescription;
    }
    public String getExtensometer1() {
        return extensometer1;
    }
    public void setExtensometer1(String extensometer1) {
        this.extensometer1 = extensometer1;
    }
    public String getExtensometer2() {
        return extensometer2;
    }
    public void setExtensometer2(String extensometer2) {
        this.extensometer2 = extensometer2;
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
                ", methodName='" + methodName + '\'' +
                ", norm='" + norm + '\'' +
                ", essayType='" + essayType + '\'' +
                ", normDescription='" + normDescription + '\'' +
                ", extensometer1='" + extensometer1 + '\'' +
                ", extensometer2='" + extensometer2 + '\'' +
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
