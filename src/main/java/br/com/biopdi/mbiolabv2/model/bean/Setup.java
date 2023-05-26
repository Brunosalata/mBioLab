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

import java.util.Objects;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class Setup {
    private Integer setupId, MC1M1, MC2M1, MC3M1, MC4M1, MC5M1, MC6M1, MC7M1, MC8M1, MC9M1, MC10M1, MC11M1,
            MC12M1, MC13M1, MC14M1, MC15M1, MC16M1, MC17M1, MC18M1, MC19M1, MC20M1, MC21M1, MC22M1, MC23M1, MC24M1,
            MC25M1, MC26M1, MC27M1, MC28M1, MC29M1, MC30M1;

    public Setup() {
    }
    public Setup(Integer setupId, Integer MC1M1, Integer MC2M1, Integer MC3M1, Integer MC4M1,
                 Integer MC5M1, Integer MC6M1, Integer MC7M1, Integer MC8M1, Integer MC9M1, Integer MC10M1,
                 Integer MC11M1, Integer MC12M1, Integer MC13M1, Integer MC14M1, Integer MC15M1, Integer MC16M1,
                 Integer MC17M1, Integer MC18M1, Integer MC19M1, Integer MC20M1, Integer MC21M1, Integer MC22M1,
                 Integer MC23M1, Integer MC24M1, Integer MC25M1, Integer MC26M1, Integer MC27M1, Integer MC28M1,
                 Integer MC29M1, Integer MC30M1) {
        this.setupId = setupId;
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
    }
    public Setup(Integer MC1M1, Integer MC2M1, Integer MC3M1, Integer MC4M1, Integer MC5M1,
                 Integer MC6M1, Integer MC7M1, Integer MC8M1, Integer MC9M1, Integer MC10M1, Integer MC11M1,
                 Integer MC12M1, Integer MC13M1, Integer MC14M1, Integer MC15M1, Integer MC16M1, Integer MC17M1,
                 Integer MC18M1, Integer MC19M1, Integer MC20M1, Integer MC21M1, Integer MC22M1, Integer MC23M1,
                 Integer MC24M1, Integer MC25M1, Integer MC26M1, Integer MC27M1, Integer MC28M1, Integer MC29M1,
                 Integer MC30M1) {
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
    }

    public Integer getSetupId() {
        return setupId;
    }
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
        return Objects.equals(setupId, setup.setupId) && Objects.equals(MC1M1, setup.MC1M1) && Objects.equals(MC2M1, setup.MC2M1) && Objects.equals(MC3M1, setup.MC3M1) && Objects.equals(MC4M1, setup.MC4M1) && Objects.equals(MC5M1, setup.MC5M1) && Objects.equals(MC6M1, setup.MC6M1) && Objects.equals(MC7M1, setup.MC7M1) && Objects.equals(MC8M1, setup.MC8M1) && Objects.equals(MC9M1, setup.MC9M1) && Objects.equals(MC10M1, setup.MC10M1) && Objects.equals(MC11M1, setup.MC11M1) && Objects.equals(MC12M1, setup.MC12M1) && Objects.equals(MC13M1, setup.MC13M1) && Objects.equals(MC14M1, setup.MC14M1) && Objects.equals(MC15M1, setup.MC15M1) && Objects.equals(MC16M1, setup.MC16M1) && Objects.equals(MC17M1, setup.MC17M1) && Objects.equals(MC18M1, setup.MC18M1) && Objects.equals(MC19M1, setup.MC19M1) && Objects.equals(MC20M1, setup.MC20M1) && Objects.equals(MC21M1, setup.MC21M1) && Objects.equals(MC22M1, setup.MC22M1) && Objects.equals(MC23M1, setup.MC23M1) && Objects.equals(MC24M1, setup.MC24M1) && Objects.equals(MC25M1, setup.MC25M1) && Objects.equals(MC26M1, setup.MC26M1) && Objects.equals(MC27M1, setup.MC27M1) && Objects.equals(MC28M1, setup.MC28M1) && Objects.equals(MC29M1, setup.MC29M1) && Objects.equals(MC30M1, setup.MC30M1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(setupId, MC1M1, MC2M1, MC3M1, MC4M1, MC5M1, MC6M1, MC7M1, MC8M1, MC9M1, MC10M1, MC11M1, MC12M1, MC13M1, MC14M1, MC15M1, MC16M1, MC17M1, MC18M1, MC19M1, MC20M1, MC21M1, MC22M1, MC23M1, MC24M1, MC25M1, MC26M1, MC27M1, MC28M1, MC29M1, MC30M1);
    }

    @Override
    public String toString() {
        return "Setup{" +
                "setupId=" + setupId +
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
                '}';
    }
}