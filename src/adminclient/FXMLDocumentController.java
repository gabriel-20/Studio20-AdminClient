/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author dev
 */
public class FXMLDocumentController implements Initializable {
    
    private double xOffset = 0;
    private double yOffset = 0;
  
    @FXML
    private Label closeButton;

    @FXML
    private JFXButton btn_login;
    
    @FXML
    private void closeButtonAction(MouseEvent event){
        System.exit(0);
    }

    @FXML
    private void handleButtonAction(MouseEvent event) {

        // get a handle to the stage
        Stage stage2 = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage2.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLAdminDashbord.fxml"));
            Parent rootl = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Admin Dashboard");

            //grab your root here
            rootl.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            //move around here
            rootl.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            stage.setScene(new Scene(rootl));
            stage.show();

        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
