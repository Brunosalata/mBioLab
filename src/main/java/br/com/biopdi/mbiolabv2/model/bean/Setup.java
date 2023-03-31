package br.com.biopdi.mbiolabv2.model.bean;

import java.util.Date;
import java.util.Objects;

public class Setup {
    private Integer setupId, userId, MC1M1, MC2M1, MC3M1, MC4M1, MC5M1, MC6M1, MC7M1, MC8M1, MC9M1, MC10M1, MC11M1, MC12M1, MC13M1, MC14M1, MC15M1, MC16M1, MC17M1, MC18M1, MC19M1, MC20M1, MC21M1, MC22M1, MC23M1, MC24M1, MC25M1, MC26M1, MC27M1, MC28M1, MC29M1, MC30M1, channel1ext1, channel2ext1, channel3ext1, channel4ext1, channel1ext2, channel2ext2, channel3ext2, channel4ext2;
    private String setupName, setupAuthor;

    private String setupDate;

    public Setup() {
    }
    public Setup(Integer userId, Integer MC1M1, Integer MC2M1, Integer MC3M1, Integer MC4M1, Integer MC5M1, Integer MC6M1, Integer MC7M1, Integer MC8M1, Integer MC9M1, Integer MC10M1, Integer MC11M1, Integer MC12M1, Integer MC13M1, Integer MC14M1, Integer MC15M1, Integer MC16M1, Integer MC17M1, Integer MC18M1, Integer MC19M1, Integer MC20M1, Integer MC21M1, Integer MC22M1, Integer MC23M1, Integer MC24M1, Integer MC25M1, Integer MC26M1, Integer MC27M1, Integer MC28M1, Integer MC29M1, Integer MC30M1, Integer channel1ext1, Integer channel2ext1, Integer channel3ext1, Integer channel4ext1, Integer channel1ext2, Integer channel2ext2, Integer channel3ext2, Integer channel4ext2, String setupName, String setupAuthor, String setupDate) {
        this.userId = userId;
        this.MC1M1 = MC1M1;
        this.MC2M1 = MC2M1;
        this.MC3M1 = MC3M1;
        this.MC4M1 = MC4M1;
        this.MC5M1 = MC5M1;
        this.MC6M1 = MC6M1;
        this.MC7M1 = MC7M1;
        this.MC8M1 = MC8M1;
        this.MC9M1 = MC9M1;
        this.MC10M1 = MC10M1;
        this.MC11M1 = MC11M1;
        this.MC12M1 = MC12M1;
        this.MC13M1 = MC13M1;
        this.MC14M1 = MC14M1;
        this.MC15M1 = MC15M1;
        this.MC16M1 = MC16M1;
        this.MC17M1 = MC17M1;
        this.MC18M1 = MC18M1;
        this.MC19M1 = MC19M1;
        this.MC20M1 = MC20M1;
        this.MC21M1 = MC21M1;
        this.MC22M1 = MC22M1;
        this.MC23M1 = MC23M1;
        this.MC24M1 = MC24M1;
        this.MC25M1 = MC25M1;
        this.MC26M1 = MC26M1;
        this.MC27M1 = MC27M1;
        this.MC28M1 = MC28M1;
        this.MC29M1 = MC29M1;
        this.MC30M1 = MC30M1;
        this.channel1ext1 = channel1ext1;
        this.channel2ext1 = channel2ext1;
        this.channel3ext1 = channel3ext1;
        this.channel4ext1 = channel4ext1;
        this.channel1ext2 = channel1ext2;
        this.channel2ext2 = channel2ext2;
        this.channel3ext2 = channel3ext2;
        this.channel4ext2 = channel4ext2;
        this.setupName = setupName;
        this.setupAuthor = setupAuthor;
        this.setupDate = setupDate;
    }
    public Setup(Integer setupId, Integer userId, Integer MC1M1, Integer MC2M1, Integer MC3M1, Integer MC4M1, Integer MC5M1, Integer MC6M1, Integer MC7M1, Integer MC8M1, Integer MC9M1, Integer MC10M1, Integer MC11M1, Integer MC12M1, Integer MC13M1, Integer MC14M1, Integer MC15M1, Integer MC16M1, Integer MC17M1, Integer MC18M1, Integer MC19M1, Integer MC20M1, Integer MC21M1, Integer MC22M1, Integer MC23M1, Integer MC24M1, Integer MC25M1, Integer MC26M1, Integer MC27M1, Integer MC28M1, Integer MC29M1, Integer MC30M1, Integer channel1ext1, Integer channel2ext1, Integer channel3ext1, Integer channel4ext1, Integer channel1ext2, Integer channel2ext2, Integer channel3ext2, Integer channel4ext2, String setupName, String setupAuthor, String setupDate) {
        this.setupId = setupId;
        this.userId = userId;
        this.MC1M1 = MC1M1;
        this.MC2M1 = MC2M1;
        this.MC3M1 = MC3M1;
        this.MC4M1 = MC4M1;
        this.MC5M1 = MC5M1;
        this.MC6M1 = MC6M1;
        this.MC7M1 = MC7M1;
        this.MC8M1 = MC8M1;
        this.MC9M1 = MC9M1;
        this.MC10M1 = MC10M1;
        this.MC11M1 = MC11M1;
        this.MC12M1 = MC12M1;
        this.MC13M1 = MC13M1;
        this.MC14M1 = MC14M1;
        this.MC15M1 = MC15M1;
        this.MC16M1 = MC16M1;
        this.MC17M1 = MC17M1;
        this.MC18M1 = MC18M1;
        this.MC19M1 = MC19M1;
        this.MC20M1 = MC20M1;
        this.MC21M1 = MC21M1;
        this.MC22M1 = MC22M1;
        this.MC23M1 = MC23M1;
        this.MC24M1 = MC24M1;
        this.MC25M1 = MC25M1;
        this.MC26M1 = MC26M1;
        this.MC27M1 = MC27M1;
        this.MC28M1 = MC28M1;
        this.MC29M1 = MC29M1;
        this.MC30M1 = MC30M1;
        this.channel1ext1 = channel1ext1;
        this.channel2ext1 = channel2ext1;
        this.channel3ext1 = channel3ext1;
        this.channel4ext1 = channel4ext1;
        this.channel1ext2 = channel1ext2;
        this.channel2ext2 = channel2ext2;
        this.channel3ext2 = channel3ext2;
        this.channel4ext2 = channel4ext2;
        this.setupName = setupName;
        this.setupAuthor = setupAuthor;
        this.setupDate = setupDate;
    }

    public Integer getSetupId() {
        return setupId;
    }
//    public void setSetupId(Integer setupId) {
//        this.setupId = setupId;
//    }
    public Integer getUserId() {
        return userId;
    }
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }
    public Integer getMC1M1() {
        return MC1M1;
    }
    public void setMC1M1(Integer MC1M1) {
        this.MC1M1 = MC1M1;
    }
    public Integer getMC2M1() {
        return MC2M1;
    }
    public void setMC2M1(Integer MC2M1) {
        this.MC2M1 = MC2M1;
    }
    public Integer getMC3M1() {
        return MC3M1;
    }
    public void setMC3M1(Integer MC3M1) {
        this.MC3M1 = MC3M1;
    }
    public Integer getMC4M1() {
        return MC4M1;
    }
    public void setMC4M1(Integer MC4M1) {
        this.MC4M1 = MC4M1;
    }
    public Integer getMC5M1() {
        return MC5M1;
    }
    public void setMC5M1(Integer MC5M1) {
        this.MC5M1 = MC5M1;
    }
    public Integer getMC6M1() {
        return MC6M1;
    }
    public void setMC6M1(Integer MC6M1) {
        this.MC6M1 = MC6M1;
    }
    public Integer getMC7M1() {
        return MC7M1;
    }
    public void setMC7M1(Integer MC7M1) {
        this.MC7M1 = MC7M1;
    }
    public Integer getMC8M1() {
        return MC8M1;
    }
    public void setMC8M1(Integer MC8M1) {
        this.MC8M1 = MC8M1;
    }
    public Integer getMC9M1() {
        return MC9M1;
    }
    public void setMC9M1(Integer MC9M1) {
        this.MC9M1 = MC9M1;
    }
    public Integer getMC10M1() {
        return MC10M1;
    }
    public void setMC10M1(Integer MC10M1) {
        this.MC10M1 = MC10M1;
    }
    public Integer getMC11M1() {
        return MC11M1;
    }
    public void setMC11M1(Integer MC11M1) {
        this.MC11M1 = MC11M1;
    }
    public Integer getMC12M1() {
        return MC12M1;
    }
    public void setMC12M1(Integer MC12M1) {
        this.MC12M1 = MC12M1;
    }
    public Integer getMC13M1() {
        return MC13M1;
    }
    public void setMC13M1(Integer MC13M1) {
        this.MC13M1 = MC13M1;
    }
    public Integer getMC14M1() {
        return MC14M1;
    }
    public void setMC14M1(Integer MC14M1) {
        this.MC14M1 = MC14M1;
    }
    public Integer getMC15M1() {
        return MC15M1;
    }
    public void setMC15M1(Integer MC15M1) {
        this.MC15M1 = MC15M1;
    }
    public Integer getMC16M1() {
        return MC16M1;
    }
    public void setMC16M1(Integer MC16M1) {
        this.MC16M1 = MC16M1;
    }
    public Integer getMC17M1() {
        return MC17M1;
    }
    public void setMC17M1(Integer MC17M1) {
        this.MC17M1 = MC17M1;
    }
    public Integer getMC18M1() {
        return MC18M1;
    }
    public void setMC18M1(Integer MC18M1) {
        this.MC18M1 = MC18M1;
    }
    public Integer getMC19M1() {
        return MC19M1;
    }
    public void setMC19M1(Integer MC19M1) {
        this.MC19M1 = MC19M1;
    }
    public Integer getMC20M1() {
        return MC20M1;
    }
    public void setMC20M1(Integer MC20M1) {
        this.MC20M1 = MC20M1;
    }
    public Integer getMC21M1() {
        return MC21M1;
    }
    public void setMC21M1(Integer MC21M1) {
        this.MC21M1 = MC21M1;
    }
    public Integer getMC22M1() {
        return MC22M1;
    }
    public void setMC22M1(Integer MC22M1) {
        this.MC22M1 = MC22M1;
    }
    public Integer getMC23M1() {
        return MC23M1;
    }
    public void setMC23M1(Integer MC23M1) {
        this.MC23M1 = MC23M1;
    }
    public Integer getMC24M1() {
        return MC24M1;
    }
    public void setMC24M1(Integer MC24M1) {
        this.MC24M1 = MC24M1;
    }
    public Integer getMC25M1() {
        return MC25M1;
    }
    public void setMC25M1(Integer MC25M1) {
        this.MC25M1 = MC25M1;
    }
    public Integer getMC26M1() {
        return MC26M1;
    }
    public void setMC26M1(Integer MC26M1) {
        this.MC26M1 = MC26M1;
    }
    public Integer getMC27M1() {
        return MC27M1;
    }
    public void setMC27M1(Integer MC27M1) {
        this.MC27M1 = MC27M1;
    }
    public Integer getMC28M1() {
        return MC28M1;
    }
    public void setMC28M1(Integer MC28M1) {
        this.MC28M1 = MC28M1;
    }
    public Integer getMC29M1() {
        return MC29M1;
    }
    public void setMC29M1(Integer MC29M1) {
        this.MC29M1 = MC29M1;
    }
    public Integer getMC30M1() {
        return MC30M1;
    }
    public void setMC30M1(Integer MC30M1) {
        this.MC30M1 = MC30M1;
    }
    public Integer getChannel1ext1() {
        return channel1ext1;
    }
    public void setChannel1ext1(Integer channel1ext1) {
        this.channel1ext1 = channel1ext1;
    }
    public Integer getChannel2ext1() {
        return channel2ext1;
    }
    public void setChannel2ext1(Integer channel2ext1) {
        this.channel2ext1 = channel2ext1;
    }
    public Integer getChannel3ext1() {
        return channel3ext1;
    }
    public void setChannel3ext1(Integer channel3ext1) {
        this.channel3ext1 = channel3ext1;
    }
    public Integer getChannel4ext1() {
        return channel4ext1;
    }
    public void setChannel4ext1(Integer channel4ext1) {
        this.channel4ext1 = channel4ext1;
    }
    public Integer getChannel1ext2() {
        return channel1ext2;
    }
    public void setChannel1ext2(Integer channel1ext2) {
        this.channel1ext2 = channel1ext2;
    }
    public Integer getChannel2ext2() {
        return channel2ext2;
    }
    public void setChannel2ext2(Integer channel2ext2) {
        this.channel2ext2 = channel2ext2;
    }
    public Integer getChannel3ext2() {
        return channel3ext2;
    }
    public void setChannel3ext2(Integer channel3ext2) {
        this.channel3ext2 = channel3ext2;
    }
    public Integer getChannel4ext2() {
        return channel4ext2;
    }
    public void setChannel4ext2(Integer channel4ext2) {
        this.channel4ext2 = channel4ext2;
    }
    public String getSetupName() {
        return setupName;
    }
    public void setSetupName(String setupName) {
        this.setupName = setupName;
    }
    public String getSetupAuthor() {
        return setupAuthor;
    }
    public void setSetupAuthor(String setupAuthor) {
        this.setupAuthor = setupAuthor;
    }
    public String getSetupDate() {
        return setupDate;
    }
    public void setSetupData(String setupData) {
        this.setupDate = setupData;
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
        Setup setup = (Setup) o;
        return setupId.equals(setup.setupId) && Objects.equals(userId, setup.userId) && Objects.equals(setupName, setup.setupName) && Objects.equals(setupAuthor, setup.setupAuthor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(setupId, userId, setupName, setupAuthor);
    }

    @Override
    public String toString() {
        return "Setup{" +
                "setupId=" + setupId +
                ", userId=" + userId +
                ", MC1M1=" + MC1M1 +
                ", MC2M1=" + MC2M1 +
                ", MC3M1=" + MC3M1 +
                ", MC4M1=" + MC4M1 +
                ", MC5M1=" + MC5M1 +
                ", MC6M1=" + MC6M1 +
                ", MC7M1=" + MC7M1 +
                ", MC8M1=" + MC8M1 +
                ", MC9M1=" + MC9M1 +
                ", MC10M1=" + MC10M1 +
                ", MC11M1=" + MC11M1 +
                ", MC12M1=" + MC12M1 +
                ", MC13M1=" + MC13M1 +
                ", MC14M1=" + MC14M1 +
                ", MC15M1=" + MC15M1 +
                ", MC16M1=" + MC16M1 +
                ", MC17M1=" + MC17M1 +
                ", MC18M1=" + MC18M1 +
                ", MC19M1=" + MC19M1 +
                ", MC20M1=" + MC20M1 +
                ", MC21M1=" + MC21M1 +
                ", MC22M1=" + MC22M1 +
                ", MC23M1=" + MC23M1 +
                ", MC24M1=" + MC24M1 +
                ", MC25M1=" + MC25M1 +
                ", MC26M1=" + MC26M1 +
                ", MC27M1=" + MC27M1 +
                ", MC28M1=" + MC28M1 +
                ", MC29M1=" + MC29M1 +
                ", MC30M1=" + MC30M1 +
                ", channel1ext1=" + channel1ext1 +
                ", channel2ext1=" + channel2ext1 +
                ", channel3ext1=" + channel3ext1 +
                ", channel4ext1=" + channel4ext1 +
                ", channel1ext2=" + channel1ext2 +
                ", channel2ext2=" + channel2ext2 +
                ", channel3ext2=" + channel3ext2 +
                ", channel4ext2=" + channel4ext2 +
                ", setupName='" + setupName + '\'' +
                ", setupAuthor='" + setupAuthor + '\'' +
                ", setupData=" + setupDate +
                '}';
    }
}