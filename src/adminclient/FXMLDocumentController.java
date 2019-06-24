/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient;

import static adminclient.helper.Global.APP_UPDATE_PATH;
import static adminclient.helper.Global.CLIENT_NAME;
import static adminclient.helper.Global.LOGIN_URL;
import static adminclient.helper.Global.MANAGEMENT_TOKEN;
import adminclient.helper.Trainer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
    private Hyperlink loginStatus;
    
    private static String path;

    @FXML
    private JFXTextField user;
    
    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton btn_login;

    private String content;

    private Trainer train;

    @FXML
    private void closeButtonAction(MouseEvent event) {
        System.exit(0);
    }
    
    @FXML
    private void handleEnterKey(KeyEvent ev ){
        
        if (ev.getCode().equals(KeyCode.ENTER)) {
            if(!btn_login.isDisable())  login();
        } 
    }
    
    private void login(){
    
        String trainer = user.getText();
        String pass = password.getText();

        //if login success continue else error
        if (trainer.length() > 2) {
            
            //disable login btn while waitting for response
            btn_login.setDisable(true);

            Task<Void> task = new Task<Void>() {
                @Override
                public Void call() throws Exception {

                    String url = LOGIN_URL;

                    System.out.println("task: " + url);

                    content = fetchContent(url, trainer, pass);
                    System.out.println(content);

                    return null;
                }
            };
            task.setOnSucceeded(ee -> {
                
                btn_login.setDisable(false);

                Gson gson = new Gson();
                JsonElement data = gson.fromJson(content.toString(), JsonElement.class);
                System.out.println("response: " + data);
                JsonObject jobject = data.getAsJsonObject();

                boolean success = jobject.getAsJsonPrimitive("success").getAsBoolean();
                if (success) {
                    JsonObject datares = jobject.getAsJsonObject("data").getAsJsonObject();
                    boolean isShift = jobject.getAsJsonPrimitive("shiftStart").getAsBoolean();
                    String shiftStart = jobject.getAsJsonPrimitive("shiftStartHour").getAsString();
                    String proxy = jobject.getAsJsonPrimitive("proxy").getAsString();
                    
                    boolean isShiftEnd = jobject.getAsJsonPrimitive("shiftEnd").getAsBoolean();
                    String shiftEnd = jobject.getAsJsonPrimitive("shiftEndHour").getAsString();
                    
                    System.out.println("data from response: " + datares);
                    String name = datares.getAsJsonPrimitive("name").getAsString();
                    int id = datares.getAsJsonPrimitive("id").getAsInt();
                    String email = datares.getAsJsonPrimitive("email").getAsString();
                    String profile = datares.getAsJsonPrimitive("profile").getAsString();
                    int studio = datares.getAsJsonPrimitive("studio").getAsInt();
                    String studioName = datares.getAsJsonPrimitive("studioName").getAsString();
                    
                    train.name = name;
                    train.id = id;
                    train.email = email;
                    train.profile = profile;
                    train.studio = studio;
                    train.studioName = studioName;
                    train.isShift = isShift;
                    train.isShiftEnd = isShiftEnd;
                    train.proxy = proxy;
                    if (isShift) train.shiftStart = shiftStart;
                    if (isShift) train.shiftEnd = shiftEnd;

                    Stage stage2 = (Stage) closeButton.getScene().getWindow();
                    stage2.close();

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLAdminDashbord.fxml"));
                        Parent rootl = (Parent) fxmlLoader.load();
                        
                        Scene scene = new Scene(rootl);
                        scene.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());

                        Stage stage = new Stage();
                        
                        stage.setScene(scene);
                        
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

    @FXML
    private void handleButtonAction(MouseEvent event) {

        login();
       
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        train = Trainer.getInstance();
        
        loginStatus.setText("v."+train.BUILD);
        double currentBuild = Double.parseDouble(train.BUILD);
        
        String uri = APP_UPDATE_PATH;
        
        try {
            if (exists(uri) > currentBuild) {
                loginStatus.setText("Please update your version!");
                //loginStatus
                System.out.println("You need update!"); 
                btn_login.setDisable(true);
            }
            else System.out.println("You have the latest version!");
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @FXML
    private void updatelink(ActionEvent event) {

        try {
            
            Desktop.getDesktop().browse(new URL(path).toURI());
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static double exists(String URLName) throws IOException{
    
        String build;
        
        Document doc = Jsoup.connect(URLName).get();
        for (Element file : doc.select("td a")) {
            //System.out.println(file.attr("href"));
            String list = file.attr("href");
            if (list.contains(CLIENT_NAME)) {
                String buildexe = list.substring(12,list.length());
                System.out.println("buildexe:"+buildexe);
                build = buildexe.substring(0, buildexe.length() - 4);
                System.out.println("build:"+build);
                double d = Double.parseDouble(build);
                path = APP_UPDATE_PATH + list;

                return d;
            }
        }
        
        return 0.0;
  }

    private static String fetchContent(String uri, String email, String pass) throws IOException {

        String urlParameters = "email=" + email + "&pass=" + pass;
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
