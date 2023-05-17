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
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SystemVariable{" +
                "id=" + id +
                ", userId=" + userId +
                ", force=" + force +
                ", position=" + position +
                '}';
    }
}
