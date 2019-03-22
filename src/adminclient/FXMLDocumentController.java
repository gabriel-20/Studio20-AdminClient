/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient;

import static adminclient.helper.Global.LOGIN_URL;
import static adminclient.helper.Global.MANAGEMENT_TOKEN;
import adminclient.helper.Trainer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
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
    private JFXTextField user;

    @FXML
    private JFXButton btn_login;

    private String content;

    private Trainer train;

    @FXML
    private void closeButtonAction(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleButtonAction(MouseEvent event) {

        String trainer = user.getText();

        //if login success continue else error
        if (trainer.length() > 2) {

            Task<Void> task = new Task<Void>() {
                @Override
                public Void call() throws Exception {

                    String url = LOGIN_URL;

                    System.out.println("task: " + url);

                    content = fetchContent(url, trainer);
                    System.out.println(content);

                    return null;
                }
            };
            task.setOnSucceeded(ee -> {

                Gson gson = new Gson();
                JsonElement data = gson.fromJson(content.toString(), JsonElement.class);
                System.out.println("response: " + data);
                JsonObject jobject = data.getAsJsonObject();

                boolean success = jobject.getAsJsonPrimitive("success").getAsBoolean();
                if (success) {
                    JsonObject datares = jobject.getAsJsonObject("data").getAsJsonObject();
                    System.out.println("data from response: " + datares);
                    String name = datares.getAsJsonPrimitive("name").getAsString();
                    String email = datares.getAsJsonPrimitive("email").getAsString();
                    String profile = datares.getAsJsonPrimitive("profile").getAsString();
                    int studio = datares.getAsJsonPrimitive("studio").getAsInt();
                    String studioName = datares.getAsJsonPrimitive("studioName").getAsString();
                    train.name = name;
                    train.email = email;
                    train.profile = profile;
                    train.studio = studio;
                    train.studioName = studioName;

                    Stage stage2 = (Stage) closeButton.getScene().getWindow();
                    stage2.close();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLAdminDashbord.fxml"));
                        Parent rootl = (Parent) fxmlLoader.load();

                        Stage stage = new Stage();
                        
                        stage.setScene(new Scene(rootl));
                        
                        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

                        //set Stage boundaries to visible bounds of the main screen

                        double width = primaryScreenBounds.getWidth();
                        double height = primaryScreenBounds.getHeight();

                        stage.setX((width - (width * 0.9))/2);
                        stage.setY((height - (height * 0.9))/2);
                        stage.setWidth(width * 0.9);
                        stage.setHeight(height * 0.9);
        
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

                        
                        stage.show();

                    } catch (IOException e) {
                        Logger logger = Logger.getLogger(getClass().getName());
                        logger.log(Level.SEVERE, "Failed to create new Window.", e);
                    }

                }

            });
            new Thread(task).start();

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        train = Trainer.getInstance();

    }

    private static String fetchContent(String uri, String email) throws IOException {

        String urlParameters = "email=" + email;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        final int OK = 200;
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Java client");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + MANAGEMENT_TOKEN);

        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(postData);
        }

        StringBuilder content;

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {

            String line;
            content = new StringBuilder();

            while ((line = in.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        }

        return content.toString();

    }

}
