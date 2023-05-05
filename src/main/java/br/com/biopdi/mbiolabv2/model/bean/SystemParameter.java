package br.com.biopdi.mbiolabv2.model.bean;

public class SystemParameter {

    private Integer id;
    private String portName, systemLanguage, soundOn, theme;

    public SystemParameter() {
    }
    public SystemParameter(String portName, String systemLanguage, String soundOn, String theme) {
        this.portName = portName;
        this.systemLanguage = systemLanguage;
        this.soundOn = soundOn;
        this.theme = theme;
    }

    public SystemParameter(Integer id, String portName, String systemLanguage, String soundOn, String theme) {
        this.id = id;
        this.portName = portName;
        this.systemLanguage = systemLanguage;
        this.soundOn = soundOn;
        this.theme = theme;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPortName() {
        return portName;
    }
    public void setPortName(String portName) {
        this.portName = portName;
    }
    public String getSystemLanguage() {
        return systemLanguage;
    }
    public void setSystemLanguage(String systemLanguage) {
        this.systemLanguage = systemLanguage;
    }
    public String getSoundOn() {
        return soundOn;
    }
    public void setSoundOn(String soundOn) {
        this.soundOn = soundOn;
    }
    public String getTheme() {
        return theme;
    }
    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Override
    public String toString() {
        return "SystemParameter{" +
                "id=" + id +
                ", portName='" + portName + '\'' +
                ", systemLanguage='" + systemLanguage + '\'' +
                ", soundOn='" + soundOn + '\'' +
                ", theme='" + theme + '\'' +
                '}';
    }
}
