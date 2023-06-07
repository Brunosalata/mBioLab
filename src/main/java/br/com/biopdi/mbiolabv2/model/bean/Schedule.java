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
 * @author Bruno Salata Lima - 07/06/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project Essay.java
 */
public class Schedule {
    private int id, userId;
    private String date;

    public Schedule() {
    }
    public Schedule(int id, int userId, String date) {
        this.id = id;
        this.userId = userId;
        this.date = date;
    }
    public Schedule(int userId, String date) {
        this.userId = userId;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                '}';
    }
}
