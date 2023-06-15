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

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import win.zqxu.jrviewer.JRViewerFX;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salata Lima - 25/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project Essay.java
 */
public class Report {
    @FXML
    private JRViewerFX jrDashboardReport = new JRViewerFX();
    private Image img;
    @FXML
    public void reportCreator(List<?> objList, InputStream reportPath){
        try{
//            // Converte lista para JRBeanCollectionDataSource
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(objList);
//
//            //Map para Armazenar os parametros do relatorio Jasper
            Map parameters = new HashMap();
            parameters.put("CollectionBeanParam", itemsJRBean);

            JasperDesign jasperDesign = JRXmlLoader.load(reportPath);

            // Compilando jrxml com a classe JasperReport
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            // Gerar pdf a partir do objeto jasperReport
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JRBeanCollectionDataSource(objList));

            // Chamar ferramentas jasper para expor o relatorio na janela jasperviewer
            JasperViewer jv = new JasperViewer(jasperPrint, false);
            jv.setTitle("Emissão de relatório");
            jv.setVisible(true);

            jrDashboardReport.setReport(jasperPrint);
            jrDashboardReport.print();
        } catch (JRException e) {
            throw new RuntimeException(e);
        }


    }

}
