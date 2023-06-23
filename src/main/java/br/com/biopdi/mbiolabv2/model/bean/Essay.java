package br.com.biopdi.mbiolabv2.model.bean;

/*
 *  Copyright (c) 2023.
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

import br.com.biopdi.mbiolabv2.controller.repository.dao.EssayDAO;

import java.util.List;
import java.util.Objects;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */

public class Essay {

    private Integer essayId, userId;
    private String essayIdentification, essayNorm, essayUsedMachine, essayChart, essayDay, essayHour;
    private Double essayChargeCell, essayInitialForce, essayFinalForce, essayInitialPosition, essayFinalPosition,
            essayDislocationVelocity, essayTemperature, essayPreCharge, essayRelativeHumidity, essayMaxForce,
            essayMaxPosition, essayMaxTension, essayEscapeTension, essayAlong, essayAreaRed, essayMYoung;

    public Essay() {
    }

    public Essay(Integer essayId, Integer userId, String essayIdentification, String essayNorm, String essayUsedMachine,
                 double essayChargeCell, double essayInitialForce, double essayFinalForce, double essayInitialPosition,
                 double essayFinalPosition, double essayDislocationVelocity, double essayTemperature, double essayPreCharge,
                 double essayRelativeHumidity, double essayMaxForce, double essayMaxPosition, double essayMaxTension,
                 double essayEscapeTension, double essayAlong, double essayAreaRed, double essayMYoung, String essayChart,
                 String essayDay, String essayHour) {
        this.essayId = essayId;
        this.userId = userId;
        this.essayIdentification = essayIdentification;
        this.essayNorm = essayNorm;
        this.essayUsedMachine = essayUsedMachine;
        this.essayChargeCell = essayChargeCell;
        this.essayInitialForce = essayInitialForce;
        this.essayFinalForce = essayFinalForce;
        this.essayInitialPosition = essayInitialPosition;
        this.essayFinalPosition = essayFinalPosition;
        this.essayDislocationVelocity = essayDislocationVelocity;
        this.essayTemperature = essayTemperature;
        this.essayPreCharge = essayPreCharge;
        this.essayRelativeHumidity = essayRelativeHumidity;
        this.essayMaxForce = essayMaxForce;
        this.essayMaxPosition = essayMaxPosition;
        this.essayMaxTension = essayMaxTension;
        this.essayEscapeTension = essayEscapeTension;
        this.essayAlong = essayAlong;
        this.essayAreaRed = essayAreaRed;
        this.essayMYoung = essayMYoung;
        this.essayChart = essayChart;
        this.essayDay = essayDay;
        this.essayHour = essayHour;
    }
    public Essay(Integer userId, String essayIdentification, String essayNorm, String essayUsedMachine,
                 double essayChargeCell, double essayInitialForce, double essayFinalForce, double essayInitialPosition,
                 double essayFinalPosition, double essayDislocationVelocity, double essayTemperature, double essayPreCharge,
                 double essayRelativeHumidity, double essayMaxForce, double essayMaxPosition, double essayMaxTension,
                 double essayEscapeTension, double essayAlong, double essayAreaRed, double essayMYoung, String essayChart,
                 String essayDay, String essayHour) {
        this.userId = userId;
        this.essayIdentification = essayIdentification;
        this.essayNorm = essayNorm;
        this.essayUsedMachine = essayUsedMachine;
        this.essayChargeCell = essayChargeCell;
        this.essayInitialForce = essayInitialForce;
        this.essayFinalForce = essayFinalForce;
        this.essayInitialPosition = essayInitialPosition;
        this.essayFinalPosition = essayFinalPosition;
        this.essayDislocationVelocity = essayDislocationVelocity;
        this.essayTemperature = essayTemperature;
        this.essayPreCharge = essayPreCharge;
        this.essayRelativeHumidity = essayRelativeHumidity;
        this.essayMaxForce = essayMaxForce;
        this.essayMaxPosition = essayMaxPosition;
        this.essayMaxTension = essayMaxTension;
        this.essayEscapeTension = essayEscapeTension;
        this.essayAlong = essayAlong;
        this.essayAreaRed = essayAreaRed;
        this.essayMYoung = essayMYoung;
        this.essayChart = essayChart;
        this.essayDay = essayDay;
        this.essayHour = essayHour;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getEssayId() {
        return essayId;
    }

    public void setEssayId(Integer essayId) {
        this.essayId = essayId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getEssayUsedMachine() {
        return essayUsedMachine;
    }

    public void setEssayUsedMachine(String essayUsedMachine) {
        this.essayUsedMachine = essayUsedMachine;
    }

    public double getEssayChargeCell() {
        return essayChargeCell;
    }

    public void setEssayChargeCell(Double essayChargeCell) {
        this.essayChargeCell = essayChargeCell;
    }

    public double getEssayInitialForce() {
        return essayInitialForce;
    }

    public void setEssayInitialForce(double essayInitialForce) {
        this.essayInitialForce = essayInitialForce;
    }

    public double getEssayFinalForce() {
        return essayFinalForce;
    }

    public void setEssayFinalForce(double essayFinalForce) {
        this.essayFinalForce = essayFinalForce;
    }

    public double getEssayInitialPosition() {
        return essayInitialPosition;
    }

    public void setEssayInitialPosition(double essayInitialPosition) {
        this.essayInitialPosition = essayInitialPosition;
    }

    public double getEssayFinalPosition() {
        return essayFinalPosition;
    }

    public void setEssayFinalPosition(double essayFinalPosition) {
        this.essayFinalPosition = essayFinalPosition;
    }

    public double getEssayDislocationVelocity() {
        return essayDislocationVelocity;
    }

    public void setEssayDislocationVelocity(double essayDislocationVelocity) {
        this.essayDislocationVelocity = essayDislocationVelocity;
    }

    public double getEssayTemperature() {
        return essayTemperature;
    }

    public void setEssayTemperature(Double essayTemperature) {
        this.essayTemperature = essayTemperature;
    }

    public double getEssayPreCharge() {
        return essayPreCharge;
    }

    public void setEssayPreCharge(Double essayPreCharge) {
        this.essayPreCharge = essayPreCharge;
    }

    public double getEssayRelativeHumidity() {
        return essayRelativeHumidity;
    }

    public void setEssayRelativeHumidity(Double essayRelativeHumidity) {
        this.essayRelativeHumidity = essayRelativeHumidity;
    }

    public String getEssayChart() {
        return essayChart;
    }

    public void setEssayChart(String essayChart) {
        this.essayChart = essayChart;
    }

    public double getEssayMaxForce() {
        return essayMaxForce;
    }

    public void setEssayMaxForce(double essayMaxForce) {
        this.essayMaxForce = essayMaxForce;
    }

    public double getEssayMaxPosition() {
        return essayMaxPosition;
    }

    public void setEssayMaxPosition(double essayMaxPosition) {
        this.essayMaxPosition = essayMaxPosition;
    }

    public double getEssayMaxTension() {
        return essayMaxTension;
    }

    public void setEssayMaxTension(double essayMaxTension) {
        this.essayMaxTension = essayMaxTension;
    }

    public double getEssayEscapeTension() {
        return essayEscapeTension;
    }

    public void setEssayEscapeTension(double essayEscapeTension) {
        this.essayEscapeTension = essayEscapeTension;
    }

    public double getEssayAlong() {
        return essayAlong;
    }

    public void setEssayAlong(double essayAlong) {
        this.essayAlong = essayAlong;
    }

    public double getEssayAreaRed() {
        return essayAreaRed;
    }

    public void setEssayAreaRed(double essayAreaRed) {
        this.essayAreaRed = essayAreaRed;
    }

    public double getEssayMYoung() {
        return essayMYoung;
    }

    public void setEssayMYoung(double essayMYoung) {
        this.essayMYoung = essayMYoung;
    }

    public String getEssayDay() {
        return essayDay;
    }

    public void setEssayDay(String essayDay) {
        this.essayDay = essayDay;
    }

    public String getEssayHour() {
        return essayHour;
    }

    public void setEssayHour(String essayHour) {
        this.essayHour = essayHour;
    }

    /**
     * Método equals e hashCode, para permitir a comparação entre elementos
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Essay essay = (Essay) o;
        return Double.compare(essay.essayChargeCell, essayChargeCell) == 0 &&
                Double.compare(essay.essayInitialForce, essayInitialForce) == 0 &&
                Double.compare(essay.essayFinalForce, essayFinalForce) == 0 &&
                Double.compare(essay.essayInitialPosition, essayInitialPosition) == 0 &&
                Double.compare(essay.essayFinalPosition, essayFinalPosition) == 0 &&
                Double.compare(essay.essayDislocationVelocity, essayDislocationVelocity) == 0 &&
                Double.compare(essay.essayTemperature, essayTemperature) == 0 &&
                Double.compare(essay.essayPreCharge, essayPreCharge) == 0 &&
                Double.compare(essay.essayRelativeHumidity, essayRelativeHumidity) == 0 &&
                Double.compare(essay.essayMaxForce, essayMaxForce) == 0 &&
                Double.compare(essay.essayMaxPosition, essayMaxPosition) == 0 &&
                Double.compare(essay.essayMaxTension, essayMaxTension) == 0 &&
                Double.compare(essay.essayEscapeTension, essayEscapeTension) == 0 &&
                Double.compare(essay.essayAlong, essayAlong) == 0 &&
                Double.compare(essay.essayAreaRed, essayAreaRed) == 0 &&
                Double.compare(essay.essayMYoung, essayMYoung) == 0 &&
                Objects.equals(essayId, essay.essayId) &&
                Objects.equals(userId, essay.userId) &&
                Objects.equals(essayIdentification, essay.essayIdentification) &&
                Objects.equals(essayNorm, essay.essayNorm) &&
                Objects.equals(essayUsedMachine, essay.essayUsedMachine) &&
                Objects.equals(essayChart, essay.essayChart) &&
                Objects.equals(essayDay, essay.essayDay) &&
                Objects.equals(essayHour, essay.essayHour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(essayId, userId, essayIdentification, essayNorm, essayUsedMachine, essayChargeCell,
                essayInitialForce, essayFinalForce, essayInitialPosition, essayFinalPosition, essayDislocationVelocity,
                essayTemperature, essayPreCharge, essayRelativeHumidity, essayMaxForce, essayMaxPosition, essayMaxTension,
                essayEscapeTension, essayAlong, essayAreaRed, essayMYoung, essayChart, essayDay, essayHour);
    }

    /**
     * Metodo de retorno String do objeto
     * @return
     */
    @Override
    public String toString() {
        return "Essay{" +
                "essayId=" + essayId +
                ", userId=" + userId +
                ", essayIdentification='" + essayIdentification + '\'' +
                ", essayNorm='" + essayNorm + '\'' +
                ", essayUsedMachine='" + essayUsedMachine + '\'' +
                ", essayChargeCell=" + essayChargeCell +
                ", essayInitialForce=" + essayInitialForce +
                ", essayFinalForce=" + essayFinalForce +
                ", essayInitialPosition=" + essayInitialPosition +
                ", essayFinalPosition=" + essayFinalPosition +
                ", essayDislocationVelocity=" + essayDislocationVelocity +
                ", essayTemperature=" + essayTemperature +
                ", essayPreCharge=" + essayPreCharge +
                ", essayRelativeHumidity=" + essayRelativeHumidity +
                ", essayMaxForce=" + essayMaxForce +
                ", essayMaxPosition=" + essayMaxPosition +
                ", essayMaxTension=" + essayMaxTension +
                ", essayEscapeTension=" + essayEscapeTension +
                ", essayAlong=" + essayAlong +
                ", essayAreaRed=" + essayAreaRed +
                ", essayMYoung=" + essayMYoung +
                ", essayChart='" + essayChart + '\'' +
                ", essayDay='" + essayDay + '\'' +
                ", essayHour='" + essayHour + '\'' +
                '}';
    }

    //------------------ DAO VERIFY -----------------

    private static EssayDAO dao = new EssayDAO();

    /**
     * Método save() que pode ser chamado diretamente da classe Essay
     */
    public void save(Essay essay){
        if(essayId != null && dao.findById(essayId) != null){
            dao.update(essay);
        } else {
            dao.create(essay);
        }
    }
    /**
     * Método delete() que pode ser chamado diretamente da classe Essay
     */
    public void delete(int i){
        if(dao.findById(essayId) != null){
            dao.delete(this);
        }
    }
    /**
     * Método findAll() que pode ser chamado diretamente da classe Essay
     */
    public static List<Essay> findAll(){
        return dao.findAll();
    }
    /**
     * Método find() que pode ser chamado diretamente da classe Essay
     */
    public static Essay findById(int pk){
        return dao.findById(pk);
    }

}
