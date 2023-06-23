package br.com.biopdi.mbiolabv2.controller.SceneController.switchScene;

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

import br.com.biopdi.mbiolabv2.mBioLabv2Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

/**
 * @author Bruno Salata Lima - 16/05/2023
 * github.com/Brunosalata
 * @version 1.0
 * @project mBioLabv2
 */
public class FxmlLoader {
    private Pane view;

    public Pane getPage(String fileName) {
        try {
            URL fileUrl = mBioLabv2Application.class.getResource(fileName);
            if (fileUrl == null) {
                throw new java.io.FileNotFoundException("O arquivo FXML não foi encontrado!");
            }
            view = new FXMLLoader().load(fileUrl);

        } catch (Exception e) {
            System.out.println("A página " + fileName + " não foi encontrada. Por favor, verifique a classe FxmlLoader.");
            throw new RuntimeException(e);
        }
        return view;
    }
}