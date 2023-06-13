package br.com.biopdi.mbiolabv2.controller.report;
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * @author Bruno Salata Lima - 13/06/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project Essay.java
 */
public class JRDataSource implements net.sf.jasperreports.engine.JRDataSource {
    @Override
    public boolean next() throws JRException {
        return false;
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        return null;
    }
}
