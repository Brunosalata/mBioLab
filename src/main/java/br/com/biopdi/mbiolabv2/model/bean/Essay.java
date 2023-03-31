package br.com.biopdi.mbiolabv2.model.bean;

import br.com.biopdi.mbiolabv2.controller.repository.dao.EssayDAO;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Essay {

    private Integer essayId, userId;
    private String essayIdentification, essayNorm, essayUsedMachine;
    private double essayChargeCell, essayInitialForce, essayFinalForce, essayInitialPosition, essayFinalPosition, essayDislocationVelocity, essayTemperature, essayPreCharge, essayRelativeHumidity;
    private byte[] essayGraphic;
    private String essayDate;

    public Essay() {
    }
    public Essay(Integer essayId, Integer userId, String essayIdentification, String essayNorm, String essayUsedMachine, double essayChargeCell, double essayInitialForce, double essayFinalForce, double essayInitialPosition, double essayFinalPosition, double essayDislocationVelocity, double essayTemperature, double essayPreCharge, double essayRelativeHumidity, byte[] essayGraphic, String essayDate) {
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
        this.essayGraphic = essayGraphic;
        this.essayDate = essayDate;
    }

    public Essay(Integer userId, String essayIdentification, String essayNorm, String essayUsedMachine, double essayChargeCell, double essayInitialForce, double essayFinalForce, double essayInitialPosition, double essayFinalPosition, double essayDislocationVelocity, double essayTemperature, double essayPreCharge, double essayRelativeHumidity, byte[] essayGraphic, String essayDate) {
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
        this.essayGraphic = essayGraphic;
        this.essayDate = essayDate;
    }

    public Integer getUserId() {
        return userId;
    }
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }
    public Integer getEssayId() {
        return essayId;
    }
//    public void setEssayId(Integer essayId) {
//        this.essayId = essayId;
//    }
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
    public double getEssayChargeCell() {
        return essayChargeCell;
    }
    public void setEssayChargeCell(double essayChargeCell) {
        this.essayChargeCell = essayChargeCell;
    }
    public String getEssayUsedMachine() {
        return essayUsedMachine;
    }
    public void setEssayUsedMachine(String essayUsedMachine) {
        this.essayUsedMachine = essayUsedMachine;
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
    public void setEssayTemperature(double essayTemperature) {
        this.essayTemperature = essayTemperature;
    }
    public double getEssayPreCharge() {
        return essayPreCharge;
    }
    public void setEssayPreCharge(double essayPreCharge) {
        this.essayPreCharge = essayPreCharge;
    }
    public double getEssayRelativeHumidity() {
        return essayRelativeHumidity;
    }
    public void setEssayRelativeHumidity(double essayRelativeHumidity) {
        this.essayRelativeHumidity = essayRelativeHumidity;
    }
    public byte[] getEssayGraphic() {
        return essayGraphic;
    }
    public void setEssayGraphic(byte[] essayGraphic) {
        this.essayGraphic = essayGraphic;
    }
    public String getEssayDate() {
        return essayDate;
    }
    public void setEssayData(String essayDate) {
        this.essayDate = essayDate;
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
        return essayId.equals(essay.essayId) && Objects.equals(userId, essay.userId) && Objects.equals(essayNorm, essay.essayNorm) && Objects.equals(essayUsedMachine, essay.essayUsedMachine) && Objects.equals(essayDate, essay.essayDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(essayId, userId, essayNorm, essayUsedMachine, essayDate);
    }

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
                ", essayGraphic=" + Arrays.toString(essayGraphic) +
                ", essayData=" + essayDate +
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
