package br.com.biopdi.mbiolabv2.model.bean;

import java.util.Date;

public class Essay {

    private int userId, essayId;
    private String essayIdentification, essayNorm, ChargeCell, usedMachine;
    private double initialForce, finalForce, initialPosition, finalPosition, displacementVelocity, temperature, preCharge, relativeHumidity;
    private Date essayData;

    public Essay() {
    }
    public Essay(int userId, int essayId, String essayIdentification, String essayNorm, String chargeCell, String usedMachine, double initialForce, double finalForce, double initialPosition, double finalPosition, double displacementVelocity, double temperature, double preCharge, double relativeHumidity, Date essayData) {
        this.userId = userId;
        this.essayId = essayId;
        this.essayIdentification = essayIdentification;
        this.essayNorm = essayNorm;
        ChargeCell = chargeCell;
        this.usedMachine = usedMachine;
        this.initialForce = initialForce;
        this.finalForce = finalForce;
        this.initialPosition = initialPosition;
        this.finalPosition = finalPosition;
        this.displacementVelocity = displacementVelocity;
        this.temperature = temperature;
        this.preCharge = preCharge;
        this.relativeHumidity = relativeHumidity;
        this.essayData = essayData;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getEssayId() {
        return essayId;
    }
    public void setEssayId(int essayId) {
        this.essayId = essayId;
    }
    public String getEssayIdentification() {
        return essayIdentification;
    }
    public void setEssayIdentification(String essayIdentification) {
        this.essayIdentification = essayIdentification;
    }
    public String getEssayNorm() {
        return essayNorm;
    }
    public void setEssayNorm(String essayNorm) {
        this.essayNorm = essayNorm;
    }
    public String getChargeCell() {
        return ChargeCell;
    }
    public void setChargeCell(String chargeCell) {
        ChargeCell = chargeCell;
    }
    public String getUsedMachine() {
        return usedMachine;
    }
    public void setUsedMachine(String usedMachine) {
        this.usedMachine = usedMachine;
    }
    public double getInitialForce() {
        return initialForce;
    }
    public void setInitialForce(double initialForce) {
        this.initialForce = initialForce;
    }
    public double getFinalForce() {
        return finalForce;
    }
    public void setFinalForce(double finalForce) {
        this.finalForce = finalForce;
    }
    public double getInitialPosition() {
        return initialPosition;
    }
    public void setInitialPosition(double initialPosition) {
        this.initialPosition = initialPosition;
    }
    public double getFinalPosition() {
        return finalPosition;
    }
    public void setFinalPosition(double finalPosition) {
        this.finalPosition = finalPosition;
    }
    public double getDisplacementVelocity() {
        return displacementVelocity;
    }
    public void setDisplacementVelocity(double displacementVelocity) {
        this.displacementVelocity = displacementVelocity;
    }
    public double getTemperature() {
        return temperature;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    public double getPreCharge() {
        return preCharge;
    }
    public void setPreCharge(double preCharge) {
        this.preCharge = preCharge;
    }
    public double getRelativeHumidity() {
        return relativeHumidity;
    }
    public void setRelativeHumidity(double relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }
    public Date getEssayData() {
        return essayData;
    }
    public void setEssayData(Date essayData) {
        this.essayData = essayData;
    }
}
