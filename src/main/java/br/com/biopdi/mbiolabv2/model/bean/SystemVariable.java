package br.com.biopdi.mbiolabv2.model.bean;

public class SystemVariable {
    private Integer id, userId;
    private double force, position;
    public SystemVariable(Integer id, double force, double position) {
        this.id = id;
        this.force = force;
        this.position = position;
    }

    public SystemVariable(Integer id, double force, double position, Integer userId) {
        this.id = id;
        this.force = force;
        this.position = position;
        this.userId = userId;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public double getForce() {
        return force;
    }
    public void setForce(double force) {
        force = force;
    }
    public double getPosition() {
        return position;
    }
    public void setPosition(double position) {
        position = position;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUseId(Integer userId) {
        this.userId = userId;
    }
}
