/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient;

import static adminclient.helper.Global.APP_SHIFT_REPORT;
import static adminclient.helper.Global.AVERAGE;
import static adminclient.helper.Global.BIGBREAK;
import static adminclient.helper.Global.BREAK;
import static adminclient.helper.Global.OVERTIME;
import static adminclient.helper.Global.SOCKETIO;
import adminclient.helper.ModelsObj;
import adminclient.helper.MyModelsObj;
import adminclient.helper.Sales;
import adminclient.helper.Trainer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.CustomProxyConfig;
import com.teamdev.jxbrowser.chromium.events.ConsoleEvent;
import com.teamdev.jxbrowser.chromium.events.ConsoleListener;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.controlsfx.control.Notifications;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import static jdk.nashorn.internal.objects.Global.Infinity;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import potato.jx.crack.JxBrowserHackUtil;
import potato.jx.crack.JxVersion;

/**
 *
 * @author dev
 */
public class FXMLAdminDashbordController implements Initializable {

    private Socket socket;

    private int initval = 0;

    private String selectedModel = "";

    private String localModelProfile;

    private Browser browser;

    private BrowserView webView;

    @FXML
    private ImageView spinnerMyModels, spinnerAllModels, spinnerModelProfile;

    @FXML
    private TextField filterField;

    double screenHeight, screenWidth;

    private ArrayList<Integer> taskList = new ArrayList<>();

    double size;

    private Stage dialogModelStage, trainerModalStage, breakReasonModalStage, totalBreakReasonModalStage, averageReasonModalStage,overtimeModalStage;

    @FXML
    private BorderPane vpnA, mainBorderPane;

    @FXML
    private VBox notificationBox, vBoxLeft, vBoxCenter, vboxChat, taskVBox;

    @FXML
    private TabPane aDashboard;

    @FXML
    private ScrollPane rScrollBox, centerScrollPane, notificationScroll;

    @FXML
    private Button taskBtn1, taskBtn2, taskBtn3, taskBtn4, taskBtn5;

    @FXML
    private TableView<Sales> tableSales;

    @FXML
    private TableView tableview_TimeOnline;

    @FXML
    private ImageView modelProfilePic, trainerProfile;

    @FXML
    private Label modelTotalPeriod, totalEarnings;

    @FXML
    private TextField artisticEmail, artisticPassword;

    @FXML
    private Tab vpn;

    private int startShiftTimeModdel;

    @FXML
    private AnchorPane scrollModels, scrollMyModels, anchorPaneCenter, anchorPaneChat;

    @FXML
    private Label trainerName, trainerStudio, selectedModelNickname, labelAllModels, labelMyModels, startTime, endTime;

    @FXML
    private javafx.scene.control.Button closeButton;

    @FXML
    private Label freeChatTotal, privateChatTotal, memberChatTotal, vipChatTotal, offlineTotal, totalTime;

    @FXML
    private Label lbltaskBtn1, lbltaskBtn2, lbltaskBtn3, minIcon;

    @FXML
    private AnchorPane charts2;

    @FXML
    private TabPane tabpaneChat;

    @FXML
    private VBox chatArea;

    @FXML
    private TextField chatField;

    private int interval = 5;

    @FXML
    private TextField shiftRoomInput, shiftPlaceInput, shiftPointsInput;

    @FXML
    private TextArea shiftFieldInput1, shiftFieldInput2, shiftFieldInput3, shiftFieldInput4, shiftFieldInput5;

    @FXML
    private Button shiftBtnSave;

    private double xOffset = 0;
    private double yOffset = 0;

    String model_artistic_email = "no email";
    String model_artistic_password = "no password";

    private double tabChatSize;

    //private final HostServices services;
    @FXML
    private void handleClose(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleChat(MouseEvent event) {
        sendChatMessage();
    }

    @FXML
    private void saveShiftFeedback(ActionEvent event) {

        String room = shiftRoomInput.getText();

        String place = shiftPlaceInput.getText();

        String points = shiftPointsInput.getText();

        String field1 = shiftFieldInput1.getText();

        String field2 = shiftFieldInput2.getText();

        String field3 = shiftFieldInput3.getText();

        String field4 = shiftFieldInput4.getText();

        String field5 = shiftFieldInput5.getText();

        String[] dataJson = {trainer.id + "", selectedModel, room, place, points, field1, field2, field3, field4, field5};
        JSONArray mJArray = new JSONArray(Arrays.asList(dataJson));

        System.out.println("Send Shift report data: " + mJArray);
        socket.emit("sendShiftReportData", mJArray);

    }

    @FXML
    private void takePicture(MouseEvent event) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {

                    WritableImage image = webView.snapshot(new SnapshotParameters(), null);

                    // TODO: probably use a file chooser here
                    Date date = new Date();
                    long time = date.getTime();
                    String filename = selectedModel + "-" + time + ".png";
                    File file = new File(filename);

                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                        System.out.println("pic take!!");
                    } catch (IOException e) {
                        // TODO: handle exception here
                    }

                } catch (Exception ignored) {
                } finally {
                    // Dispose Browser instance
                    //browser.dispose();
                }
            }
        });
    }

    @FXML
    private void modelCenterLogin(MouseEvent event) {
        System.out.println("Click view profile");

        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.get("https://modelcenter.jasmin.com/en/login");

        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                Thread.sleep(2000);

                WebElement emailBox = null;
                WebElement passBox = null;

                List<WebElement> someElements = driver.findElements(By.cssSelector("input"));

                for (WebElement anElement : someElements) {
                    if (anElement.getAttribute("type").equals("email")) {
                        emailBox = anElement;
                    }

                    if (anElement.getAttribute("type").equals("password")) {
                        passBox = anElement;
                    }
                }

                emailBox.sendKeys(model_artistic_email);
                passBox.sendKeys(model_artistic_password);
                System.out.println(model_artistic_email + " : " + model_artistic_password);

                return null;
            }
        };
        task.setOnSucceeded(ee -> {

        });
        new Thread(task).start();

    }

    @FXML
    private void handleTask(MouseEvent event) throws URISyntaxException, IOException {

        java.awt.Desktop.getDesktop().browse(new URI(APP_SHIFT_REPORT));

        if (!trainer.isShift) {
            //show pop-up
            Platform.runLater(() -> {
                try {
                // Update UI here.

                    TimeUnit.SECONDS.sleep(5);

                    if (trainerModalStage != null) {
                        trainerModalStage.close();
                    }

                    FXMLLoader modalLoader = new FXMLLoader(getClass().getResource("TrainerStartShiftPopup.fxml"));
                    Parent rootl = (Parent) modalLoader.load();

                    trainerModalStage = new Stage();
                    trainerModalStage.initModality(Modality.WINDOW_MODAL);

                    Button btnYes = (Button) modalLoader.getNamespace().get("modalStartTrainerShiftYes");
                    btnYes.setOnMouseClicked(e -> {

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();

                        String[] dataMessage = new String[2];
                        dataMessage[0] = trainer.id + "";
                        dataMessage[1] = dateFormat.format(date);

                        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));

                        enableFirstTask();
                        socket.emit("TRAINER_START_SHIFT", mJSONArray);
                        trainer.isShift = true;

                         //Stage stage = (Stage) modelPresentBtn.getScene().getWindow();
                        //stage.close();
                    });

                    Button btnNo = (Button) modalLoader.getNamespace().get("modalStartTrainerShiftNo");
                    btnNo.setOnMouseClicked(e -> {

                        Stage stage = (Stage) btnNo.getScene().getWindow();
                        stage.close();
                    });

                    Scene modal = new Scene(rootl);

                    //move around here
                    modal.setOnMouseDragged(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            trainerModalStage.setX(event.getScreenX() - xOffset);
                            trainerModalStage.setY(event.getScreenY() - yOffset);
                        }
                    });

                    trainerModalStage.setScene(modal);
                    trainerModalStage.initStyle(StageStyle.UNDECORATED);
                    trainerModalStage.setWidth(200);
                    trainerModalStage.setHeight(200);
                    trainerModalStage.show();

                } catch (IOException | InterruptedException ex) {
                    Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

//        if (!trainer.isShift) {
//            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//            String[] dataMessage = new String[2];
//            dataMessage[0] = trainer.id+"";
//            dataMessage[1] = timestamp.toString();
//            JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));
//            
//            Date date = new Date();
//            String strDateFormat = "HH:mm";
//            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
//            String formattedDate = dateFormat.format(date);
//            lbltaskBtn1.setText(formattedDate);
//        
//
//            enableFirstTask();
//            socket.emit("TRAINER_START_SHIFT", mJSONArray);
//            trainer.isShift = true;
//        } else {
//            
//            try {
//                java.awt.Desktop.getDesktop().browse(new URI(APP_SHIFT_REPORT));
//            } catch (IOException ex) {
//                Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//        }
    }

    private void enableSecondTask() {

        lbltaskBtn2.setText(trainer.shiftEnd);
        taskBtn2.setText("SHIFT FINISH");
        taskBtn2.setStyle("-fx-background-color: red;");

    }

    private void enableFirstTask() {

        taskBtn1.setText("VIEW SHIFT");
        taskBtn1.setStyle("-fx-background-color: green;");

        if (trainer.isShift) {
            lbltaskBtn1.setText(trainer.shiftStart);
        }

    }

    private void sendChatMessage() {
        //detect active tab

        String text = chatField.getText();
        chatField.clear();
        String name = tabpaneChat.getSelectionModel().getSelectedItem().getText();

        System.out.println("chat name" + name);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        prepareChatTab(trainer.name, name, text, "right", timestamp);

        String[] dataMessage = new String[4];
        dataMessage[0] = trainer.name;
        dataMessage[1] = name;
        dataMessage[2] = text;
        dataMessage[3] = timestamp.toString();

        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));

        socket.emit("TRAINER_SEND_CHAT", mJSONArray);
    }

    ArrayList<String> openTabs = new ArrayList<>();

    int total_free = 0;
    int total_private = 0;
    int total_member = 0;
    int total_vip = 0;
    int total_offline = 0;
    int total_time = 0;

    private TableView table = new TableView();
    private TableView myTable = new TableView();

    Trainer trainer = Trainer.getInstance();

    ObservableList<ModelsObj> dataTable = FXCollections.observableArrayList();
    ObservableList<ModelsObj> filteredList = FXCollections.observableArrayList();

    ObservableList<MyModelsObj> myDataTable = FXCollections.observableArrayList();

    private final ObservableList<Sales> dataSales = FXCollections.observableArrayList();

    List<String> list = new ArrayList<String>();

    ObservableList<String> observableList1 = FXCollections.observableList(list);

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        spinnerMyModels.setVisible(false);
        spinnerAllModels.setVisible(false);
        spinnerModelProfile.setVisible(false);

        if (trainer.isShift) {
            enableFirstTask();
        }

        if (trainer.isShiftEnd) {
            enableSecondTask();
        }

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        screenHeight = primaryScreenBounds.getHeight();
        screenWidth = primaryScreenBounds.getWidth();

        //mainBorderPane
        //System.out.println("Screen width : " + screenWidth);
        //System.out.println("Screen height : " + screenHeight);
        mainBorderPane.setMinWidth(screenWidth - 400);
        mainBorderPane.setMaxWidth(screenWidth - 400);

        //vBoxCenter.setMaxWidth(screenWidth - 700);
        //vBoxCenter.setPrefWidth(screenWidth - 700);
        size = screenHeight - 170;

        aDashboard.setMaxHeight(size - (size / 3) - 110);
        aDashboard.setPrefHeight(size - (size / 3) - 110);

        aDashboard.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                    //System.out.println("Tab Selection changed");

                        startProxyBrowser();
                    }
                }
        );

        anchorPaneChat.setMaxHeight(size / 3);
        anchorPaneChat.setPrefHeight(size / 3);

        vboxChat.setMaxHeight(size / 3);
        vboxChat.setPrefHeight(size / 3);

        shiftBtnSave.setDisable(true);
        rScrollBox.setMaxHeight(screenHeight - 270);
        rScrollBox.setPrefHeight(screenHeight - 270);

        //notificationScroll.setMaxHeight(aDashboard.getHeight() - centerScrollPane.getHeight() - 50);
        //notificationScroll.setPrefHeight(aDashboard.getHeight() - centerScrollPane.getHeight() - 50);
        notificationScroll.setMaxHeight(222);
        notificationScroll.setPrefHeight(222);

        //initBrowser();
        minIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                Stage stage = (Stage) minIcon.getScene().getWindow();
                stage.setIconified(true);
            }
        });

        TableColumn indexFieldCol = new TableColumn("#");
        indexFieldCol.setMinWidth(25);
        indexFieldCol.setMaxWidth(25);
        indexFieldCol.setPrefWidth(25);
        indexFieldCol.setCellValueFactory(new PropertyValueFactory<>("indexField"));

        TableColumn firstDayCol = new TableColumn("Day");
        firstDayCol.setMinWidth(80);
        firstDayCol.setMaxWidth(80);
        firstDayCol.setPrefWidth(80);
        firstDayCol.setCellValueFactory(new PropertyValueFactory<>("firstDay"));

        TableColumn lastSaleCol = new TableColumn("Sale");
        //lastSaleCol.setMinWidth(100);
        lastSaleCol.setCellValueFactory(new PropertyValueFactory<>("lastSale"));

        TableColumn lastHourCol = new TableColumn("Hours");
        //lastHourCol.setMinWidth(100);
        lastHourCol.setCellValueFactory(new PropertyValueFactory<>("lastHour"));

        tableSales.setItems(dataSales);
        tableSales.getColumns().addAll(indexFieldCol, firstDayCol, lastSaleCol, lastHourCol);

        openTabs.add("GENERAL CHAT");

        chatField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                sendChatMessage();
            }
        });

        //System.out.println("trainer :" + trainer.profile);
        Image image = new Image(trainer.profile);
        trainerProfile.setImage(image);

        TableColumn myFirstNameCol = new TableColumn("Model Name");
        TableColumn myLastNameCol = new TableColumn("Status");
        TableColumn myChatNameCol = new TableColumn("Chat");

        myFirstNameCol.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        myLastNameCol.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );
        myChatNameCol.setCellValueFactory(
                new PropertyValueFactory<>("chat")
        );

        myTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        myFirstNameCol.setMaxWidth(1f * Integer.MAX_VALUE * 40); // 40% width
        myLastNameCol.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30% width
        myChatNameCol.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30% width

        myTable.setItems(myDataTable);
        myTable.getColumns().addAll(myFirstNameCol, myLastNameCol, myChatNameCol);

        myTable.setMaxHeight((screenHeight - 100) / 3);
        myTable.setPrefHeight((screenHeight - 100) / 3);

        scrollMyModels.getChildren().addAll(myTable);
        scrollMyModels.setMaxHeight((screenHeight - 100) / 3);
        scrollMyModels.setPrefHeight((screenHeight - 100) / 3);

        myTable.setRowFactory(tv -> new TableRow<MyModelsObj>() {

            @Override
            public void updateItem(MyModelsObj item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setStyle("");
                } else if (item.isShift() && item.isShiftEnd()) {
                    setStyle("");
                } else if (item.isShift()) {
                    setStyle("-fx-background-color: LIGHTGREEN");
                } else {
                    setStyle("");
                }

                this.setOnMouseClicked(event -> {

                    if ((event.getButton() == MouseButton.SECONDARY) && (!this.isEmpty())) {

                        if (trainer.isShift) {

                            System.out.println("click dreapta");
                            System.out.println("Shift  started: " + this.getItem().isShift());
                            System.out.println("Shift  end: " + this.getItem().isShiftEnd());

                            String[] dataRegister = {trainer.name, this.getItem().getName(), String.valueOf(trainer.studio)};
                            JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));
                            //socket.emit("getOnlineTrainersStudio", mJSONArray);

                            showPopupTrainers(this.getItem().getName(), this.getItem().isShift(), this.getItem().isShiftEnd());
                        } else {
                            Date date = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                            String formattedDate = dateFormat.format(date);
                            showNotification("System", "You need to start your shift!", formattedDate);
                        }

                    }

                    if (event.getClickCount() == 1 && (!this.isEmpty()) && (event.getButton() == MouseButton.PRIMARY)) {

                        MyModelsObj rowData = this.getItem();
                        String name = rowData.getName();
                        //System.out.println("Click on: " + name);
                        selectedModel = name;

                        String tab1 = aDashboard.getSelectionModel().getSelectedItem().getText();

                        if (tab1.equals("Check Online")) {
                            execproxy();
                        }

                    //startProxyBrowser();
                        if (this.getItem().isShift() && trainer.isShift) {
                            //System.out.println("!!!!!!!!!" + this.getItem().getName() + " has shift started!!!!");
                            shiftBtnSave.setDisable(false);
                        }

                        JsonObject jobj = rowData.getJobj();

                        if (jobj != null) {
                            System.out.println("jobj data : " + jobj);
                            String room = jobj.get("room").isJsonNull() ? "" : jobj.get("room").getAsString();
                            String place_awd = jobj.get("place_awd").isJsonNull() ? "" : jobj.get("place_awd").getAsString();
                            String awd_points = jobj.get("awd_points").isJsonNull() ? "" : jobj.get("awd_points").getAsString();
                            String field1 = jobj.get("field1").isJsonNull() ? "" : jobj.get("field1").getAsString();
                            String field2 = jobj.get("field2").isJsonNull() ? "" : jobj.get("field2").getAsString();
                            String field3 = jobj.get("field3").isJsonNull() ? "" : jobj.get("field3").getAsString();
                            String field4 = jobj.get("field4").isJsonNull() ? "" : jobj.get("field4").getAsString();
                            String field5 = jobj.get("field5").isJsonNull() ? "" : jobj.get("field5").getAsString();
                            String field6 = jobj.get("field6").isJsonNull() ? "" : jobj.get("field6").getAsString();
                            System.out.println("Data from if : OK");
                            updateSelectedModelShiftReportUI(room, place_awd, awd_points, field1, field2, field3, field4, field5, field6);
                        } else {
                            System.out.println("Data from if : Not OK");
                            updateSelectedModelShiftReportUI("", "", "", "", "", "", "", "", "");
                        }

                        String[] dataJson = {trainer.name, selectedModel};
                        JSONArray mJArray = new JSONArray(Arrays.asList(dataJson));

                        Tab tab = new Tab(name);
                        tab.setOnClosed(new EventHandler<Event>() {
                            @Override
                            public void handle(Event t) {
                                System.out.println("Closed!" + tab.getText());
                                openTabs.remove(tab.getText());
                                t.consume();
                            }
                        });

                        String[] dataRegister = {trainer.name, name};
                        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));

                        if (!openTabs.contains(name)) {
                            ScrollPane spane = new ScrollPane();
                            VBox vbox = new VBox();
                            vbox.setMinWidth(1130);
                            spane.setContent(vbox);
                            tab.setContent(spane);
                            tabpaneChat.getTabs().add(tab);
                            openTabs.add(name);
                            tabpaneChat.getSelectionModel().select(tab);
                            socket.emit("trainergetchathistory", mJSONArray);

                        }
                        socket.emit("getmodelinfo", mJSONArray);
                        spinnerModelProfile.setVisible(true);

                        PauseTransition pause = new PauseTransition(Duration.seconds(1));

                        TextArea[] textArea = new TextArea[6];
                        textArea[0] = shiftFieldInput1;
                        textArea[1] = shiftFieldInput2;
                        textArea[2] = shiftFieldInput3;
                        textArea[3] = shiftFieldInput4;
                        textArea[4] = shiftFieldInput5;

                        TextField[] textField = new TextField[3];
                        textField[0] = shiftRoomInput;
                        textField[1] = shiftPlaceInput;
                        textField[2] = shiftPointsInput;

                        for (int i = 0; i < 5; i++) {
                            textArea[i].textProperty().addListener(new ChangeListener<String>() {
                                @Override
                                public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                                    // this will run whenever text is changed
                                    pause.setOnFinished(event -> shiftBtnSave.fire());
                                    pause.playFromStart();
                                    System.out.println("Change Detected!!!!");

                                }
                            });
                        }

                        for (int i = 0; i < 3; i++) {
                            textField[i].textProperty().addListener(new ChangeListener<String>() {
                                @Override
                                public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                                    // this will run whenever text is changed
                                    pause.setOnFinished(event -> shiftBtnSave.fire());
                                    pause.playFromStart();
                                    System.out.println("Change Detected!!!!");

                                }
                            });
                        }

                    }
                });
            }
        });

        TableColumn firstNameCol = new TableColumn("Model Name");
        TableColumn lastNameCol = new TableColumn("Status");

        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<ModelsObj, String>("name")
        );
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<ModelsObj, String>("status")
        );

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        firstNameCol.setMaxWidth(1f * Integer.MAX_VALUE * 70); // 70% width
        lastNameCol.setMaxWidth(1f * Integer.MAX_VALUE * 30); // 30% width

        table.setItems(filteredList);
        table.getColumns().addAll(firstNameCol, lastNameCol);

        table.setMaxHeight((screenHeight - 100) / 3);
        table.setPrefHeight((screenHeight - 100) / 3);

        filterField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                updateFilteredData();
            }
        });

        scrollModels.getChildren().addAll(table);
        scrollModels.setMaxHeight((screenHeight - 100) / 3);
        scrollModels.setPrefHeight((screenHeight - 100) / 3);

        table.setRowFactory(tv -> {
            TableRow<ModelsObj> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ModelsObj rowData = row.getItem();
                    System.out.println("Double click on: " + rowData.getName());

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("id", trainer.id);
                        obj.put("models", rowData.getName());
                    } catch (JSONException ex) {
                        Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    socket.emit("updatemymodels", obj);
                    System.out.println("emit updatemymodels " + obj);

                    socket.emit("getmymodels", trainer.id);
                    //updateMyModels(rowData.getName());
                }
            });
            return row;
        });

        trainerName.setText(trainer.name);
        //trainerStudio.setText(String.valueOf(trainer.studio));
        trainerStudio.setText(String.valueOf(trainer.studioName));

        startWebSocketConnection();

    }

    public static final void saveAsPng(final Node NODE, final String FILE_NAME) {
        final WritableImage SNAPSHOT = NODE.snapshot(new SnapshotParameters(), null);
        final String NAME = FILE_NAME.replace("\\.[a-zA-Z]{3,4}", "");
        final File FILE = new File(NAME + ".png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(SNAPSHOT, null), "png", FILE);
        } catch (IOException exception) {
            // handle exception here
        }
    }

    private void startProxyBrowser() {

        String tab = aDashboard.getSelectionModel().getSelectedItem().getText();
        //System.out.println("Tab Selection changed   " + tab);

        if (tab.equals("Check Online")) {
            //webView
            initBrowser();
            execproxy();

            tabpaneChat.setPrefHeight(30);
            tabpaneChat.setMinHeight(30);
            tabpaneChat.setMaxHeight(30);

            anchorPaneChat.setPrefHeight(30);
            anchorPaneChat.setMinHeight(30);
            anchorPaneChat.setMaxHeight(30);

            aDashboard.setMaxHeight(size - 150);
            aDashboard.setPrefHeight(size - 150);
            aDashboard.setMinHeight(size - 150);

            vboxChat.setMaxHeight(30);
            vboxChat.setPrefHeight(30);
            vboxChat.setMinHeight(30);

        } else {
            tabpaneChat.setPrefHeight(233);
            tabpaneChat.setMinHeight(233);
            tabpaneChat.setMaxHeight(233);
            anchorPaneChat.setPrefHeight(233);
            anchorPaneChat.setMinHeight(233);
            anchorPaneChat.setMaxHeight(233);

            aDashboard.setMaxHeight(size - (size / 3) - 110);
            aDashboard.setPrefHeight(size - (size / 3) - 110);
            aDashboard.setPrefHeight(size - (size / 3) - 110);

            vboxChat.setMaxHeight(size / 3);
            vboxChat.setPrefHeight(size / 3);
            vboxChat.setMinHeight(size / 3);

            if (webView != null) {
                webView.getChildren().clear();
                browser.dispose();
            }

        }

    }

    private void initBrowser() {

        JxBrowserHackUtil.hack(JxVersion.V6_22);
        String identity = UUID.randomUUID().toString();

        BrowserContextParams params = new BrowserContextParams("temp/browser/" + identity);

            //String proxyRules = "http=136.243.76.199:3128;https=136.243.76.199:3128";
        //String proxyRules = "http=157.230.246.199:8080;https=157.230.246.199:8080";
        //String proxyRules = "http=142.93.103.225:3128;https=142.93.103.225:3128";
        String proxyRules = trainer.proxy;
        System.out.println("Proxy Url:" + proxyRules);

        params.setProxyConfig(new CustomProxyConfig(proxyRules));

        BrowserContext context1 = new BrowserContext(params);
        browser = new Browser(BrowserType.LIGHTWEIGHT, context1);

            //vpn.setContent(null);
        webView = new BrowserView(browser);

        vpnA.setCenter(new BorderPane(webView));

            //vpn.setContent(webView);
        vpn.setContent(vpnA);

    }

    private void execproxy() {

        browser.loadURL("https://www.livejasmin.com/ro/chat-html5/" + selectedModel);
            //browser.loadURL("https://www.google.com/");

        browser.addConsoleListener(new ConsoleListener() {
            public void onMessage(ConsoleEvent event) {
                System.out.println("Message: " + event.getMessage());
            }
        });

    }

    private void updateSelectedModelShiftReportUI(String room, String place_awd, String awd_points, String field1, String field2, String field3, String field4, String field5, String field6) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.

                shiftRoomInput.setText(room);
                shiftPlaceInput.setText(place_awd);
                shiftPointsInput.setText(awd_points);
                shiftFieldInput1.setText(field1);
                shiftFieldInput2.setText(field2);
                shiftFieldInput3.setText(field3);
                shiftFieldInput4.setText(field4);
                shiftFieldInput5.setText(field5);

            }
        });

    }

    private void updateSelectedModelUI(String modelname, String profilePic, JsonObject jTotal) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.

                artisticEmail.setText(model_artistic_email);
                artisticPassword.setText(model_artistic_password);
                selectedModelNickname.setText(modelname);
                String imageUrl = "http:" + profilePic;
                localModelProfile = imageUrl;

                Image image = new Image(imageUrl);
                modelProfilePic.setImage(image);

                freeChatTotal.setText(formatSeconds(total_free));
                privateChatTotal.setText(formatSeconds(total_private));
                memberChatTotal.setText(formatSeconds(total_member));
                vipChatTotal.setText(formatSeconds(total_vip));
                offlineTotal.setText(formatSeconds(total_offline));
                totalTime.setText(formatSeconds(total_free + total_private + total_member + total_vip));

                int total = jTotal.get("totalamount").getAsInt();
                double totalThisShift = jTotal.get("earningsToday").getAsDouble();
                totalEarnings.setText(totalThisShift + "$");
                modelTotalPeriod.setText("Total This Period: " + String.valueOf(total) + "$");

            }
        });

    }

    public static String formatSeconds(int timeInSeconds) {
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10) {
            formattedTime += "0";
        }
        formattedTime += hours + ":";

        if (minutes < 10) {
            formattedTime += "0";
        }
        formattedTime += minutes + ":";

        if (seconds < 10) {
            formattedTime += "0";
        }
        formattedTime += seconds;

        return formattedTime;
    }

    private void generateChart(int end, int start, String status) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.

                int lineheight = 18;
                int stretch = 300;

                String diff = formatSeconds(end - start);

                int startX = ((start - initval) / 60);
                int endX = ((end - initval) / 60);

                int lineStartX = (startX * stretch / 1440) + 5;
                int lineEndX = (endX * stretch / 1440) + 5;

                if (status.equals("free_chat")) {
                    total_free += (end - start);
                }

                if (status.equals("private_chat")) {
                    total_private += (end - start);
                }

                if (status.equals("member_chat")) {
                    total_member += (end - start);
                }

                if (status.equals("vip_show")) {
                    total_vip += (end - start);
                }

                if (status.equals("offline")) {
                    total_offline += (end - start);
                }

                Rectangle r = new Rectangle();
                r.setX(lineStartX);
                r.setY(3);
                r.setWidth(lineEndX - lineStartX - 1);
                r.setHeight(lineheight);
                Tooltip tooltip = new Tooltip(diff);
                Tooltip.install(r, tooltip);

                if (status.equals("free_chat")) {
                    r.setStroke(javafx.scene.paint.Color.BLUE);
                    r.setFill(javafx.scene.paint.Color.BLUE);
                }

                if (status.equals("private_chat")) {
                    r.setStroke(javafx.scene.paint.Color.RED);
                    r.setFill(javafx.scene.paint.Color.RED);
                }

                if (status.equals("member_chat")) {
                    r.setStroke(javafx.scene.paint.Color.ORANGE);
                    r.setFill(javafx.scene.paint.Color.ORANGE);
                }

                if (status.equals("vip_show")) {
                    r.setStroke(javafx.scene.paint.Color.MAGENTA);
                    r.setFill(javafx.scene.paint.Color.MAGENTA);
                }

                if (status.equals("offline")) {
                    r.setStroke(javafx.scene.paint.Color.WHITE);
                    r.setFill(javafx.scene.paint.Color.WHITE);
                }

                charts2.getChildren().add(r);

            }
        });

    }

    private void printDate(String label, String date) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                if (label.equals("start")) {
                    startTime.setText(date);
                }

                if (label.equals("end")) {
                    endTime.setText(date);
                }

            }
        });

    }

    private void clearChart() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.

                dataSales.clear();
                charts2.getChildren().clear();
                freeChatTotal.setText("00:00:00");
                privateChatTotal.setText("00:00:00");
                memberChatTotal.setText("00:00:00");
                vipChatTotal.setText("00:00:00");
                offlineTotal.setText("00:00:00");
                totalTime.setText("00:00:00");
                total_time = 0;
                total_free = 0;
                total_private = 0;
                total_member = 0;
                total_vip = 0;
                total_offline = 0;

            }
        });

    }

    private void clearChatArea() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                chatArea.getChildren().clear();

            }
        });
    }

    private void readMyModels() {

        myDataTable.forEach((row) -> {

            System.out.println(row.getName() + " : " + row.getStatus());

            if (row.isShift() && !row.isShiftEnd()) {

                String[] dataMessage = new String[4];
                dataMessage[0] = trainer.name;
                dataMessage[1] = row.getLoginTime() + "";
                dataMessage[2] = row.getName();
                dataMessage[3] = row.getShiftId() + "";

                JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));

                System.out.println("send data : " + mJSONArray);

                socket.emit("checkShiftBreak", mJSONArray);

                        //socket.emit("getmymodels", trainer.id);
            }

        });

    }

    private void updateMyModels(JsonArray arr) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.

                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                String formattedDate = dateFormat.format(date);

                //showNotification("Status Update", "refresh", formattedDate);
                myDataTable.forEach((row) -> {
                    System.out.println(row.getName() + " : " + row.getStatus());

                    for (int i = 0; i < arr.size(); i++) {
                        JsonObject job = arr.get(i).getAsJsonObject();
                        String rName = job.get("name").getAsString();
                        String rStatus = job.get("status").getAsString();
                        String rChat = job.get("chat").getAsString();
                        boolean rShift = job.get("shift").getAsBoolean();

                        if (rName.equals(row.getName())) {
                            if (!rStatus.equals(row.getStatus())) {
                                showNotification(rName, rStatus, formattedDate);
                                System.out.println("status change detected " + rName + " - > " + rStatus + " - > " + row.getStatus());
                            }
                        }
                    }

                });

                myDataTable.clear();
                for (int i = 0; i < arr.size(); i++) {
                    JsonObject job = arr.get(i).getAsJsonObject();
                    String rName = job.get("name").getAsString();
                    String rStatus = job.get("status").getAsString();
                    String rChat = job.get("chat").getAsString();
                    boolean rShift = job.get("shift").getAsBoolean();

                    JsonElement element = job.get("shiftReport");
                    JsonObject shiftObj = null;
                    if (!(element instanceof JsonNull)) {
                        shiftObj = (JsonObject) element;
                        //System.out.println("shift obj:" + shiftObj);

                    }

                    myDataTable.add(new MyModelsObj(rName, rStatus, rChat, rShift, shiftObj));

//                    myTable.setRowFactory(tv -> {
//                            TableRow<MyModelsObj> row = new TableRow<>();
//
//                            if (row.getItem().isShift()){
//                                row.setStyle("-fxbackground-color:green");
//                            }
//
//                            return row;
//                        });
                }

                labelMyModels.setText("MY MODELS - " + String.valueOf(arr.size()));
                //colorTable();

            }

        });

    }

    private void colorTable() {

        System.out.println("tstartr :");

        Set<Node> tableRowCell = myTable.lookupAll(".table-row-cell");
        TableRow<?> row = null;
        for (Node tableRow : tableRowCell) {
            TableRow<?> r = (TableRow<?>) tableRow;
//        if (r.getIndex() == rowIndex) {
//            row = r;
//            break;
//        }
            System.out.println("tstartr 22:" + r.getItem().toString());
        }

    }

    private void setTimer() {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (interval > 0) {
                    Platform.runLater(() -> taskBtn1.setText("PENDING " + interval));
                    System.out.println(interval);
                    interval--;
                } else {
                    timer.cancel();
                }
            }
        }, 1000, 1000);
    }

    private void prepareChatTab(String train, String model, String message, String side, Timestamp timestamp) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.

                //timestamp = new Timestamp(System.currentTimeMillis());
                Date time = new Date(timestamp.getTime());

                String imageUrl = trainer.profile;
                if (side.equals("right")) {
                    imageUrl = trainer.profile;
                }

                Image image = new Image(imageUrl);

                if (side.equals("left")) {
                    image = new Image("http://planetbotanix.com/wp-content/uploads/2017/08/Female-Avatar-1-300x300.jpg");

                }

                ImageView imview = new ImageView(image);
                imview.setFitHeight(36);
                imview.setFitWidth(36);
                Rectangle clip = new Rectangle(
                        imview.getFitWidth(), imview.getFitHeight()
                );
                clip.setArcWidth(36);
                clip.setArcHeight(36);
                imview.setClip(clip);
                SnapshotParameters parameters = new SnapshotParameters();
                WritableImage image2 = imview.snapshot(parameters, null);
                imview.setClip(null);
                imview.setImage(image2);

                //System.out.println("trainer: " + train);
                //System.out.println("model: " + model);
                //System.out.println("message: " + message);
                Tab tab = new Tab(model);
                tab.setOnClosed(new EventHandler<Event>() {
                    @Override
                    public void handle(Event t) {
                        System.out.println("Closed!" + tab.getText());
                        openTabs.remove(tab.getText());
                        t.consume();
                    }
                });

                ScrollPane spane = new ScrollPane();
                VBox vbox = new VBox();
                vbox.setMinWidth(1130);
                HBox hbox = new HBox();
                hbox.setMinWidth(1130);

                spane.setMinWidth(Region.USE_PREF_SIZE);
                Label chatLabel = new Label(time + "\n" + message);
                chatLabel.setStyle("-fx-background-color: lightgrey;-fx-background-radius: 20;-fx-text-fill: black;-fx-font-size: 16px;-fx-label-padding: 5px;");

                if (side.equals("right")) {
                    chatLabel.setAlignment(Pos.CENTER_RIGHT);
                    chatLabel.setTextAlignment(TextAlignment.RIGHT);
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                    chatLabel.setStyle("-fx-background-color: #0084FF;-fx-background-radius: 20;-fx-text-fill: white;-fx-font-size: 16px;-fx-label-padding: 5px;");
                }

                if (!openTabs.contains(model)) {

                    if (side.equals("right")) {
                        hbox.getChildren().addAll(chatLabel, imview);
                    } else {
                        hbox.getChildren().addAll(imview, chatLabel);
                    }
                    vbox.getChildren().add(hbox);
                    spane.setContent(vbox);
                    tab.setContent(spane);
                    tabpaneChat.getTabs().add(tab);
                    openTabs.add(model);
                    tabpaneChat.getSelectionModel().select(tab);

                    socket.emit("getmodelinfo", model);

                } else {
                    int tabsize = tabpaneChat.getTabs().size();
                    for (int i = 0; i < tabsize; i++) {
                        if (tabpaneChat.getTabs().get(i).getText().equals(model)) {
                            spane = (ScrollPane) tabpaneChat.getTabs().get(i).getContent();
                            vbox = (VBox) spane.getContent();
                            if (side.equals("right")) {
                                hbox.getChildren().addAll(chatLabel, imview);
                            } else {
                                hbox.getChildren().addAll(imview, chatLabel);
                            }
                            vbox.getChildren().add(hbox);
                            spane.setContent(vbox);

                            tabpaneChat.getTabs().get(i).setContent(spane);

                        }

                    }
                }

                spane.setVvalue(1.0);
            }
        });

    }

    private void showNotification(String modelname, String status, String time) {
        Notifications.create()
                .title(modelname)
                .text(status + " " + time)
                .showWarning();

        Label not = new Label(modelname + " : " + status + " : " + time);
        not.setPrefHeight(50);
        not.setMaxHeight(50);
        not.setFont(Font.font("Verdana", 14));
        HBox hb = new HBox();
        hb.getStyleClass().add("taskHBox");
        //hb.setStyle("-fx-border-color:firebrick;-fx-background-color:white");
        hb.prefWidthProperty().bind(notificationBox.prefWidthProperty());
        hb.setPrefHeight(50);
        hb.setMaxHeight(50);
        hb.getChildren().add(not);
        notificationBox.getChildren().add(hb);

    }

    @FXML
    private void task1_onMouseEnter(MouseEvent Event) {
        if (!trainer.isShift) {
            taskBtn1.setText("START SHIFT");
        }
    }

    @FXML
    private void task1_onMouseExit(MouseEvent Event) {
        if (!trainer.isShift) {
            taskBtn1.setText("PENDING");
        }
    }

    @FXML
    private void task2_onMouseEnter(MouseEvent Event) {
        if (!trainer.isShiftEnd) {
            taskBtn2.setText("END SHIFT");
        }
    }

    @FXML
    private void task2_onMouseExit(MouseEvent Event) {
        if (!trainer.isShiftEnd) {
            taskBtn2.setText("PENDING");
        }
    }

    @FXML
    private void handleTask2() {

        if (!trainer.isShiftEnd) {

            Platform.runLater(() -> {
                try {
                    // Update UI here.

                    FXMLLoader modalLoader = new FXMLLoader(getClass().getResource("FXMLModal2.fxml"));
                    Parent rootl = (Parent) modalLoader.load();

                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.WINDOW_MODAL);

                    Button btnEndShift = (Button) modalLoader.getNamespace().get("btnEndShift");
                    btnEndShift.setOnMouseClicked(e -> {

                        enableSecondTask();

                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        String[] dataMessage = new String[2];
                        dataMessage[0] = trainer.id + "";
                        dataMessage[1] = timestamp.toString();
                        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));

                        socket.emit("TRAINER_END_SHIFT", mJSONArray);

                        System.exit(0);

                    });

                    Scene modal = new Scene(rootl);

                    //move around here
                    modal.setOnMouseDragged(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            dialogStage.setX(event.getScreenX() - xOffset);
                            dialogStage.setY(event.getScreenY() - yOffset);
                        }
                    });

                    dialogStage.setScene(modal);
                    dialogStage.initStyle(StageStyle.UNDECORATED);
                    dialogStage.setWidth(200);
                    dialogStage.setHeight(200);
                    dialogStage.show();

                } catch (IOException ex) {
                    Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        }
    }

    private boolean condition() {

        return (!shiftRoomInput.getText().isEmpty()
                && !shiftPlaceInput.getText().isEmpty()
                && !shiftPointsInput.getText().isEmpty()
                && !shiftFieldInput1.getText().isEmpty()
                && !shiftFieldInput2.getText().isEmpty()
                && !shiftFieldInput3.getText().isEmpty()
                && !shiftFieldInput4.getText().isEmpty()
                && !shiftFieldInput5.getText().isEmpty());

    }

    private void showPopupTrainers(String model, boolean shiftStarted, boolean shiftEnded) {

        Platform.runLater(() -> {
            try {
                // Update UI here.

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLModal.fxml"));
                Parent rootl = (Parent) fxmlLoader.load();
                Label modelname = (Label) fxmlLoader.getNamespace().get("modelnameModal");
                modelname.setText(model);
                Button remove = (Button) fxmlLoader.getNamespace().get("removeModal");
                remove.setOnMouseClicked(e -> {
                    System.out.println(remove.getText());

                    String[] dataRegister = {trainer.name, model, "Remove"};
                    JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));

                    socket.emit("realocateRemoveModel", mJSONArray);

                    Stage stage = (Stage) remove.getScene().getWindow();
                    stage.close();
                });
                System.out.println("shift started : " + shiftStarted);
                Button modelPresentBtn = (Button) fxmlLoader.getNamespace().get("modelPresentModal");
                modelPresentBtn.setDisable(true);
                if (shiftStarted && shiftEnded) {
                    modelPresentBtn.setDisable(false);
                }
                if (!shiftStarted) {
                    modelPresentBtn.setDisable(false);
                }
                modelPresentBtn.setOnMouseClicked(e -> {

                    System.out.println("Model Present");
                    Date date = new Date();

                    String time = (date.getTime() / 1000L) + "";
                    System.out.println("Time in Milliseconds: " + time);

                    String[] dataRegister = {trainer.id + "", model, "Start Shift", time, trainer.trainerShiftId + ""};
                    JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));

                        //socket.emit("startShift", mJSONArray);
                    socket.emit("startShift2", mJSONArray);

                    spinnerMyModels.setVisible(true);
                    //socket.emit("getmymodels", trainer.id);
                    System.out.println("call get my models!!");

                    Stage stage = (Stage) modelPresentBtn.getScene().getWindow();
                    stage.close();
                });

                Button modelEndShift = (Button) fxmlLoader.getNamespace().get("modelEndShift");

                modelEndShift.setDisable(true);
                if (shiftStarted && !shiftEnded) {
                    modelEndShift.setDisable(false);
                }

                modelEndShift.setOnMouseClicked(e -> {

                    if (condition()) {

                        String roomNumber = shiftRoomInput.getText();
                        String awardsPoints = shiftPointsInput.getText();
                        String placeAwards = shiftPlaceInput.getText();

                        System.out.println("room : " + roomNumber);

                        System.out.println("Model End Shift");
                        Date date = new Date();
                        String time = (date.getTime() / 1000L) + "";
                        System.out.println("Time in Milliseconds: " + time);

                        String[] dataRegister = {trainer.id + "", model, "End Shift", time, roomNumber, awardsPoints, placeAwards};
                        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));

                        socket.emit("endShift", mJSONArray);
                        spinnerMyModels.setVisible(true);
                        //socket.emit("getmymodels", trainer.id);

                        Stage stage = (Stage) modelPresentBtn.getScene().getWindow();
                        stage.close();

                    } else {
                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                        String formattedDate = dateFormat.format(date);
                        showNotification("System", "You need to fill all fields in Shift Report!", formattedDate);
                    }

                });

                //VBox vbox = new VBox();
//                for (int i = 0; i < jArrTrainers.size(); i++) {
//
//                    JsonObject job = jArrTrainers.get(i).getAsJsonObject();
//                    String name = job.get("name").getAsString();
//
//                    Button lbl = new Button(name);
//
//                    lbl.setOnMouseClicked(e -> {
//                        System.out.println(lbl.getText());
//
//                        String[] dataRegister = {trainer.name, model, name};
//                        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));
//
//                        socket.emit("realocateRemoveModel", mJSONArray);
//                        
//                        Stage stage = (Stage) lbl.getScene().getWindow();
//                        stage.close();
//                    });
//
//                    vbox.getChildren().add(lbl);
//                }
//                Label lbl = new Label("Remove");
//                lbl.setOnMouseClicked(e -> {
//                    System.out.println(lbl.getText());
//                    
//                    String[] dataRegister = {trainer.name, model, "Remove"};
//                    JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));
//
//                    socket.emit("realocateRemoveModel", mJSONArray);
//                });
//                vbox.getChildren().add(lbl);
                //vbox.setAlignment(Pos.CENTER);
                //vbox.setPadding(new Insets(15));
                //((VBox) rootl).getChildren().add(vbox);
                if (dialogModelStage != null) {
                    dialogModelStage.close();
                }

                dialogModelStage = new Stage();
                dialogModelStage.initModality(Modality.WINDOW_MODAL);

                Scene modal = new Scene(rootl);

                modal.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    }
                });

                //move around here
                modal.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        dialogModelStage.setX(event.getScreenX() - xOffset);
                        dialogModelStage.setY(event.getScreenY() - yOffset);
                    }
                });

                dialogModelStage.setScene(modal);
                dialogModelStage.initStyle(StageStyle.UNDECORATED);
                dialogModelStage.setWidth(200);
                dialogModelStage.setHeight(200);
                dialogModelStage.show();

            } catch (IOException ex) {
                Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

//    private void showPopupTrainers(JsonArray jArrTrainers, String model, boolean shiftStarted) {
//
//        Platform.runLater(() -> {
//            try {
//                // Update UI here.
//
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLModal.fxml"));
//                Parent rootl = (Parent) fxmlLoader.load();
//                Label modelname = (Label)fxmlLoader.getNamespace().get("modelnameModal");
//                modelname.setText(model);
//
//                System.out.println("shift started : " + shiftStarted);
//                Button modelPresentBtn = (Button)fxmlLoader.getNamespace().get("modelPresentModal");
//                modelPresentBtn.setDisable(shiftStarted);
//                modelPresentBtn.setOnMouseClicked(e -> {
//                        System.out.println("Model Present");
//                        Date date= new Date();
// 
//                        String time = (date.getTime()/ 1000L)+"";
//                        System.out.println("Time in Milliseconds: " + time);
//
//                        String[] dataRegister = {trainer.id+"", model, "Start Shift", time};
//                        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));
//
//                        socket.emit("startShift", mJSONArray);
//                        
//                        socket.emit("getmymodels", trainer.id);
//                        System.out.println("call get my models!!");
//                        
//                         Stage stage = (Stage) modelPresentBtn.getScene().getWindow();
//                         stage.close();
//                    });
//                
//                
//                
//                Button modelEndShift = (Button)fxmlLoader.getNamespace().get("modelEndShift");
//                modelEndShift.setDisable(!shiftStarted);
//                modelEndShift.setOnMouseClicked(e -> {
//                    
//                    TextField roomNumberField = (TextField)fxmlLoader.getNamespace().get("roomNumber");
//                    String roomNumber = roomNumberField.getText();
//                    TextField awardsPointsField = (TextField)fxmlLoader.getNamespace().get("awardsPoints");
//                    String awardsPoints = awardsPointsField.getText();
//                    TextField placeAwardsField = (TextField)fxmlLoader.getNamespace().get("placeAwards");
//                    String placeAwards = placeAwardsField.getText();
//                    
//                    System.out.println("room : " + roomNumber);
//                
//                        System.out.println("Model End Shift");
//                        Date date= new Date();
//                        String time = (date.getTime()/ 1000L)+"";
//                        System.out.println("Time in Milliseconds: " + time);
//
//                        String[] dataRegister = {trainer.id+"", model, "End Shift", time, roomNumber, awardsPoints, placeAwards};
//                        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));
//
//                        socket.emit("endShift", mJSONArray);
//                        
//                         Stage stage = (Stage) modelPresentBtn.getScene().getWindow();
//                         stage.close();
//                    });
//
//
//                Stage dialogStage = new Stage();
//                dialogStage.initModality(Modality.WINDOW_MODAL);
//
//                Scene modal = new Scene(rootl);
//
//                modal.setOnMousePressed(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        xOffset = event.getSceneX();
//                        yOffset = event.getSceneY();
//                    }
//                });
//
//                //move around here
//                modal.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        dialogStage.setX(event.getScreenX() - xOffset);
//                        dialogStage.setY(event.getScreenY() - yOffset);
//                    }
//                });
//
//                dialogStage.setScene(modal);
//                dialogStage.initStyle(StageStyle.UNDECORATED);
//                dialogStage.setWidth(200);
//                dialogStage.setHeight(200);
//                dialogStage.show();
//            } catch (IOException ex) {
//                Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });
//
//    }
    private void updateAllModels(JsonObject arr) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                dataTable.clear();
                int i = 1;
                for (String key : arr.keySet()) {
                    String status_ = arr.get(key).toString();
                    status_ = status_.substring(1, status_.length() - 1);
                    //System.out.println(arr.get(key));
                    dataTable.add(new ModelsObj(key, status_));
                    i++;
                }
                filteredList.clear();
                filteredList.addAll(dataTable);

                dataTable.addListener(new ListChangeListener<ModelsObj>() {
                    @Override
                    public void onChanged(ListChangeListener.Change<? extends ModelsObj> change) {
                        updateFilteredData();
                    }
                });

                labelAllModels.setText("ALL MODELS - " + String.valueOf(i));

            }
        });

    }

    private void updateFilteredData() {
        filteredList.clear();

        for (ModelsObj p : dataTable) {
            if (matchesFilter(p)) {
                filteredList.add(p);
            }
        }

        // Must re-sort table after items changed
        reapplyTableSortOrder();
    }

    private boolean matchesFilter(ModelsObj person) {
        String filterString = filterField.getText();
        if (filterString == null || filterString.isEmpty()) {
            // No filter --> Add all.
            return true;
        }

        String lowerCaseFilterString = filterString.toLowerCase();

        if (person.getName().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        } else if (person.getStatus().toLowerCase().indexOf(lowerCaseFilterString) != -1) {
            return true;
        }

        return false; // Does not match
    }

    private void reapplyTableSortOrder() {
        ArrayList<TableColumn<ModelsObj, ?>> sortOrder = new ArrayList<>(table.getSortOrder());
        table.getSortOrder().clear();
        table.getSortOrder().addAll(sortOrder);
    }

    private void insertTask(JsonObject jobj) {
        //{"event_id":9,"break":840,"success":true,"trainer":"trainer3","shift_id":"132","eventname":"BREAKALERT","break_alert":200}
        //"task_payload":{"model_shift_id":132,"reason":null,"trainer":"trainer3","resolved_at":null,"name":"BREAKALERT","created_at":"2019-06-28 12:38:06","id":13}

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                // Update UI here.
                String model = jobj.get("model").getAsString();

                if (!jobj.get("task_payload").isJsonNull()) {

                    JsonObject payload = jobj.get("task_payload").getAsJsonObject();

                    if (!payload.get(BREAK).isJsonNull()) {

                        Object obj = payload.get(BREAK);
                        if (obj instanceof JsonObject) {

                            JsonObject payloadBreak = payload.get(BREAK).getAsJsonObject();
                            String ename = payloadBreak.get("name").getAsString();
                            String date = payloadBreak.get("created_at").getAsString();
                            int id_task = payloadBreak.get("id").getAsInt();
                            boolean isDone = payloadBreak.get("isDone").getAsBoolean();

                            HBox hbox = new HBox();
                            hbox.setMaxWidth(1.7976931348623157E308);
                            hbox.setPrefHeight(45.0);
                            hbox.setMinWidth(-Infinity);
                            hbox.getStyleClass().add("taskHBox");
                            AnchorPane.setLeftAnchor(hbox, 0.0);
                            AnchorPane.setRightAnchor(hbox, 0.0);

                            Label lbl1 = new Label();
                            lbl1.setMaxWidth(1.7976931348623157E308);
                            lbl1.setPrefHeight(45.0);
                            lbl1.setMinWidth(-Infinity);
                            lbl1.getStyleClass().add("taskLbl1");
                            HBox.setHgrow(lbl1, Priority.ALWAYS);
                            Label lbl2 = new Label();
                            lbl2.setMaxWidth(1.7976931348623157E308);
                            lbl2.setPrefHeight(45.0);
                            lbl2.setMinWidth(-Infinity);
                            lbl2.getStyleClass().add("taskLbl2");

                            Button btn = new Button();
                            btn.setMaxWidth(1.7976931348623157E308);
                            btn.setPrefHeight(45.0);
                            btn.setPrefWidth(100.0);
                            btn.setMinWidth(-Infinity);
                //System.out.println(btn.getStyleClass());
                            //btn.getStyleClass().clear();
                            btn.getStyleClass().add("taskBtnDefault");

                            btn.setOnMouseClicked(e -> {

                                try {
                                    //
                                    if (breakReasonModalStage != null) {
                                        breakReasonModalStage.close();
                                    }

                                    FXMLLoader modalLoader = new FXMLLoader(getClass().getResource("modalBreakReason.fxml"));
                                    Parent rootl = (Parent) modalLoader.load();

                                    breakReasonModalStage = new Stage();
                                    breakReasonModalStage.initModality(Modality.WINDOW_MODAL);

                                    Label btnClose = (Label) modalLoader.getNamespace().get("closeBreakButton");
                                    btnClose.setOnMouseClicked(eee -> {

                                        Stage stage = (Stage) btnClose.getScene().getWindow();
                                        stage.close();
                                    });

                                    Button submitBtn = (Button) modalLoader.getNamespace().get("modalBreakSubmit");
                                    submitBtn.setOnMouseClicked(ee -> {

                                        TextArea reasonTxtArea = (TextArea) modalLoader.getNamespace().get("textAreaBreak");
                                        String content = reasonTxtArea.getText();

                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date1 = new Date();

                                        String[] dataMessage = new String[4];
                                        dataMessage[0] = id_task + "";
                                        dataMessage[1] = dateFormat.format(date1);
                                        dataMessage[2] = content;
                                        dataMessage[3] = trainer.name;

                                        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));

                                        socket.emit("resolveTask", mJSONArray);

                                        Stage stage = (Stage) submitBtn.getScene().getWindow();
                                        stage.close();
                                    });

                                    Scene modal = new Scene(rootl);

                                    //move around here
                                    modal.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            breakReasonModalStage.setX(event.getScreenX() - xOffset);
                                            breakReasonModalStage.setY(event.getScreenY() - yOffset);
                                        }
                                    });

                                    breakReasonModalStage.setScene(modal);
                                    breakReasonModalStage.initStyle(StageStyle.UNDECORATED);
                                    breakReasonModalStage.show();
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            });

                            lbl1.setText("Warning! - The model " + model + " has over 60 minutes of break time. Please take action!");
                            lbl1.setFont(Font.font(14));

                            lbl2.setText(date);
                            lbl2.setFont(Font.font(14));
                            lbl2.setPadding(new Insets(0, 10, 0, 10));

                            btn.setText("PENDING");
                            btn.setFont(Font.font(12));
                            //btn.setStyle("-fx-background-color: #9c1b21");

                            btn.setId(id_task + "");

                            if (isDone) {
                                btn.setText("DONE");
                                btn.getStyleClass().clear();
                                btn.getStyleClass().addAll("button", "taskBtnDone");
                                btn.setDisable(true);
                                //btn.setStyle("-fx-background-color: green");
                            }

                            Date date1 = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                            String formattedDate = dateFormat.format(date1);

                            if (!taskList.contains(id_task)) {
                                taskList.add(id_task);
                                hbox.getChildren().addAll(lbl1, lbl2, btn);
                                taskVBox.getChildren().add(hbox);

                                if (!isDone) {
                                    showNotification("System", "Warning! - The model " + model + " has over 60 minutes of break time. Please take action!", formattedDate);
                                } 

                                System.out.println("butonn id is = " + btn.getId());
                            } else {

                                if (isDone) {

                                    Button tb = (Button) taskVBox.lookup("#" + id_task);
                                    System.out.println("tb contains" + tb);

                                    tb.setText("DONE");
                                    tb.getStyleClass().clear();
                                    tb.getStyleClass().addAll("button", "taskBtnDone");
                                    tb.setDisable(true);
                        //tb.setStyle("-fx-background-color: green");

                        //showNotification("System", "Warning! - The model "+model+" has over 60 minutes of break time. Task Complete!", formattedDate);
                                } 

                            }

                        }

                    }

                    if (!payload.get(BIGBREAK).isJsonNull()) {

                        Object obj = payload.get(BIGBREAK);
                        if (obj instanceof JsonObject) {

                            JsonObject payloadBreak = payload.get(BIGBREAK).getAsJsonObject();
                            String ename = payloadBreak.get("name").getAsString();
                            String date = payloadBreak.get("created_at").getAsString();
                            int id_task = payloadBreak.get("id").getAsInt();
                            boolean isDone = payloadBreak.get("isDone").getAsBoolean();

                            HBox hbox = new HBox();
                            hbox.setMaxWidth(1.7976931348623157E308);
                            hbox.setPrefHeight(45.0);
                            hbox.setMinWidth(-Infinity);
                            hbox.getStyleClass().add("taskHBox");
                            AnchorPane.setLeftAnchor(hbox, 0.0);
                            AnchorPane.setRightAnchor(hbox, 0.0);

                            Label lbl1 = new Label();
                            lbl1.setMaxWidth(1.7976931348623157E308);
                            lbl1.setPrefHeight(45.0);
                            lbl1.setMinWidth(-Infinity);
                            HBox.setHgrow(lbl1, Priority.ALWAYS);
                            Label lbl2 = new Label();
                            lbl2.setMaxWidth(1.7976931348623157E308);
                            lbl2.setPrefHeight(45.0);
                            lbl2.setMinWidth(-Infinity);

                            Button btn = new Button();
                            btn.setMaxWidth(1.7976931348623157E308);
                            btn.setPrefHeight(45.0);
                            btn.setPrefWidth(100.0);
                            btn.setMinWidth(-Infinity);

                            lbl1.setText("Warning! - The model " + model + " has over 240 minutes of break time. The model shift automatically closed!");
                            lbl1.setFont(Font.font(14));

                            lbl2.setText(date);
                            lbl2.setFont(Font.font(14));
                            lbl2.setPadding(new Insets(0, 10, 0, 10));

                            
                            btn.setFont(Font.font(12));
                            lbl1.getStyleClass().add("taskLbl1");
                            lbl2.getStyleClass().add("taskLbl2");
                            
                            btn.setId(id_task + "");
                            

                            if (!taskList.contains(id_task)) {

                                taskList.add(id_task);
                                hbox.getChildren().addAll(lbl1, lbl2, btn);
                                taskVBox.getChildren().add(hbox);

                                Date date2 = new Date();
                                DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                                String formattedDate = dateFormat.format(date2);

                                showNotification("System", "Warning! - The model " + model + " has over 240 minutes of break time. The model shift automatically closed!", formattedDate);

                            }
                            
                            if (!isDone){
                                
                                btn.setText("PENDING");
                                btn.getStyleClass().add("taskBtnDefault");
                                
                                btn.setOnMouseClicked(e -> {

                                try {
                                    //
                                    if (totalBreakReasonModalStage != null) {
                                        totalBreakReasonModalStage.close();
                                    }

                                    FXMLLoader modalLoader = new FXMLLoader(getClass().getResource("modalTotalBreakReason.fxml"));
                                    Parent rootl = (Parent) modalLoader.load();

                                    totalBreakReasonModalStage = new Stage();
                                    totalBreakReasonModalStage.initModality(Modality.WINDOW_MODAL);

                                    Label btnClose = (Label) modalLoader.getNamespace().get("closeBreakButton");
                                    btnClose.setOnMouseClicked(eee -> {

                                        Stage stage = (Stage) btnClose.getScene().getWindow();
                                        stage.close();
                                    });

                                    Button submitBtn = (Button) modalLoader.getNamespace().get("modalBreakSubmit");
                                    submitBtn.setOnMouseClicked(ee -> {

                                        TextArea reasonTxtArea = (TextArea) modalLoader.getNamespace().get("textAreaBreak");
                                        String content = reasonTxtArea.getText();

                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date1 = new Date();

                                        String[] dataMessage = new String[4];
                                        dataMessage[0] = id_task + "";
                                        dataMessage[1] = dateFormat.format(date1);
                                        dataMessage[2] = content;
                                        dataMessage[3] = trainer.name;

                                        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));

                                        socket.emit("resolveTask", mJSONArray);

                                        Stage stage = (Stage) submitBtn.getScene().getWindow();
                                        stage.close();
                                    });

                                    Scene modal = new Scene(rootl);

                                    //move around here
                                    modal.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            totalBreakReasonModalStage.setX(event.getScreenX() - xOffset);
                                            totalBreakReasonModalStage.setY(event.getScreenY() - yOffset);
                                        }
                                    });

                                    totalBreakReasonModalStage.setScene(modal);
                                    totalBreakReasonModalStage.initStyle(StageStyle.UNDECORATED);
                                    totalBreakReasonModalStage.show();
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            });
                                
                            } else {
                                
                                Button tb = (Button) taskVBox.lookup("#" + id_task);
                                    System.out.println("tb contains" + tb);

                                    tb.setText("DONE");
                                    tb.getStyleClass().clear();
                                    tb.getStyleClass().addAll("button", "taskBtnDone");
                                    tb.setDisable(true);
                            
                            }

                        }

                    }
                    
                    if (!payload.get(AVERAGE).isJsonNull()) {

                        Object obj = payload.get(AVERAGE);
                        if (obj instanceof JsonObject) {

                            JsonObject payloadBreak = payload.get(AVERAGE).getAsJsonObject();
                            String ename = payloadBreak.get("name").getAsString();
                            String date = payloadBreak.get("created_at").getAsString();
                            int id_task = payloadBreak.get("id").getAsInt();
                            boolean isDone = payloadBreak.get("isDone").getAsBoolean();
                            double avg = payloadBreak.get("other_data").getAsDouble();

                            HBox hbox = new HBox();
                            hbox.setMaxWidth(1.7976931348623157E308);
                            hbox.setPrefHeight(45.0);
                            hbox.setMinWidth(-Infinity);
                            hbox.getStyleClass().add("taskHBox");
                            AnchorPane.setLeftAnchor(hbox, 0.0);
                            AnchorPane.setRightAnchor(hbox, 0.0);

                            Label lbl1 = new Label();
                            lbl1.setMaxWidth(1.7976931348623157E308);
                            lbl1.setPrefHeight(45.0);
                            lbl1.setMinWidth(-Infinity);
                            HBox.setHgrow(lbl1, Priority.ALWAYS);
                            Label lbl2 = new Label();
                            lbl2.setMaxWidth(1.7976931348623157E308);
                            lbl2.setPrefHeight(45.0);
                            lbl2.setMinWidth(-Infinity);

                            Button btn = new Button();
                            btn.setMaxWidth(1.7976931348623157E308);
                            btn.setPrefHeight(45.0);
                            btn.setPrefWidth(100.0);
                            btn.setMinWidth(-Infinity);

                            lbl1.setText("Help " + model + " get a better average/hour! Current: " + avg + "$ < Target: 35$");
                            lbl1.setFont(Font.font(14));

                            lbl2.setText(date);
                            lbl2.setFont(Font.font(14));
                            lbl2.setPadding(new Insets(0, 10, 0, 10));

                            
                            btn.setFont(Font.font(12));
                            lbl1.getStyleClass().add("taskLbl1");
                            lbl2.getStyleClass().add("taskLbl2");
                            
                            btn.setId(id_task + "");
                            

                            if (!taskList.contains(id_task)) {

                                taskList.add(id_task);
                                hbox.getChildren().addAll(lbl1, lbl2, btn);
                                taskVBox.getChildren().add(hbox);

                                Date date2 = new Date();
                                DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                                String formattedDate = dateFormat.format(date2);

                                if (!isDone){ 
                                    showNotification("System", "Warning! - Help " + model + "$ get a better average/hour! Current: " + avg + " target: 35$", formattedDate);
                                }
                                
                            }
                            
                            if (!isDone){
                                
                                btn.setText("PENDING");
                                btn.getStyleClass().add("taskBtnDefault");
                                
                                btn.setOnMouseClicked(e -> {

                                try {
                                    //
                                    if (averageReasonModalStage != null) {
                                        averageReasonModalStage.close();
                                    }

                                    FXMLLoader modalLoader = new FXMLLoader(getClass().getResource("modalAverageReason.fxml"));
                                    Parent rootl = (Parent) modalLoader.load();

                                    averageReasonModalStage = new Stage();
                                    averageReasonModalStage.initModality(Modality.WINDOW_MODAL);

                                    Label btnClose = (Label) modalLoader.getNamespace().get("closeBreakButton");
                                    btnClose.setOnMouseClicked(eee -> {

                                        Stage stage = (Stage) btnClose.getScene().getWindow();
                                        stage.close();
                                    });

                                    Button submitBtn = (Button) modalLoader.getNamespace().get("modalBreakSubmit");
                                    submitBtn.setOnMouseClicked(ee -> {

                                        TextArea reasonTxtArea = (TextArea) modalLoader.getNamespace().get("textAreaBreak");
                                        String content = reasonTxtArea.getText();

                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date1 = new Date();

                                        String[] dataMessage = new String[4];
                                        dataMessage[0] = id_task + "";
                                        dataMessage[1] = dateFormat.format(date1);
                                        dataMessage[2] = content;
                                        dataMessage[3] = trainer.name;

                                        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));

                                        socket.emit("resolveTask", mJSONArray);

                                        Stage stage = (Stage) submitBtn.getScene().getWindow();
                                        stage.close();
                                    });

                                    Scene modal = new Scene(rootl);

                                    //move around here
                                    modal.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            averageReasonModalStage.setX(event.getScreenX() - xOffset);
                                            averageReasonModalStage.setY(event.getScreenY() - yOffset);
                                        }
                                    });

                                    averageReasonModalStage.setScene(modal);
                                    averageReasonModalStage.initStyle(StageStyle.UNDECORATED);
                                    averageReasonModalStage.show();
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            });
                                
                            } else {
                                
                                Button tb = (Button) taskVBox.lookup("#" + id_task);
                                    System.out.println("tb contains" + tb);

                                    tb.setText("DONE");
                                    tb.getStyleClass().clear();
                                    tb.getStyleClass().addAll("button", "taskBtnDone");
                                    tb.setDisable(true);
                            
                            }

                        }

                    }
                    
                    if (!payload.get(OVERTIME).isJsonNull()) {

                        Object obj = payload.get(OVERTIME);
                        if (obj instanceof JsonObject) {

                            JsonObject payloadBreak = payload.get(OVERTIME).getAsJsonObject();
                            String ename = payloadBreak.get("name").getAsString();
                            String date = payloadBreak.get("created_at").getAsString();
                            int id_task = payloadBreak.get("id").getAsInt();
                            boolean isDone = payloadBreak.get("isDone").getAsBoolean();

                            HBox hbox = new HBox();
                            hbox.setMaxWidth(1.7976931348623157E308);
                            hbox.setPrefHeight(45.0);
                            hbox.setMinWidth(-Infinity);
                            hbox.getStyleClass().add("taskHBox");
                            AnchorPane.setLeftAnchor(hbox, 0.0);
                            AnchorPane.setRightAnchor(hbox, 0.0);

                            Label lbl1 = new Label();
                            lbl1.setMaxWidth(1.7976931348623157E308);
                            lbl1.setPrefHeight(45.0);
                            lbl1.setMinWidth(-Infinity);
                            HBox.setHgrow(lbl1, Priority.ALWAYS);
                            Label lbl2 = new Label();
                            lbl2.setMaxWidth(1.7976931348623157E308);
                            lbl2.setPrefHeight(45.0);
                            lbl2.setMinWidth(-Infinity);

                            Button btn = new Button();
                            btn.setMaxWidth(1.7976931348623157E308);
                            btn.setPrefHeight(45.0);
                            btn.setPrefWidth(100.0);
                            btn.setMinWidth(-Infinity);

                            lbl1.setText(model + " has a full shift, see if she is willing to work more and raise her average!");
                            lbl1.setFont(Font.font(14));

                            lbl2.setText(date);
                            lbl2.setFont(Font.font(14));
                            lbl2.setPadding(new Insets(0, 10, 0, 10));

                            
                            btn.setFont(Font.font(12));
                            lbl1.getStyleClass().add("taskLbl1");
                            lbl2.getStyleClass().add("taskLbl2");
                            
                            btn.setId(id_task + "");
                            

                            if (!taskList.contains(id_task)) {

                                taskList.add(id_task);
                                hbox.getChildren().addAll(lbl1, lbl2, btn);
                                taskVBox.getChildren().add(hbox);

                                Date date2 = new Date();
                                DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                                String formattedDate = dateFormat.format(date2);

                                if (!isDone){ 
                                    showNotification("System", model + " has a full shift, see if she is willing to work more and raise her average!", formattedDate);
                                }
                                
                            }
                            
                            if (!isDone){
                                
                                btn.setText("PENDING");
                                btn.getStyleClass().add("taskBtnDefault");
                                
                                btn.setOnMouseClicked(e -> {

                                try {
                                    //
                                    if (overtimeModalStage != null) {
                                        overtimeModalStage.close();
                                    }

                                    FXMLLoader modalLoader = new FXMLLoader(getClass().getResource("modalOvertimeReason.fxml"));
                                    Parent rootl = (Parent) modalLoader.load();

                                    overtimeModalStage = new Stage();
                                    overtimeModalStage.initModality(Modality.WINDOW_MODAL);

                                    Label btnClose = (Label) modalLoader.getNamespace().get("closeBreakButton");
                                    btnClose.setOnMouseClicked(eee -> {

                                        Stage stage = (Stage) btnClose.getScene().getWindow();
                                        stage.close();
                                    });

                                    Button submitBtn = (Button) modalLoader.getNamespace().get("modalBreakSubmit");
                                    submitBtn.setOnMouseClicked(ee -> {

                                        TextArea reasonTxtArea = (TextArea) modalLoader.getNamespace().get("textAreaBreak");
                                        String content = reasonTxtArea.getText();

                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date1 = new Date();

                                        String[] dataMessage = new String[4];
                                        dataMessage[0] = id_task + "";
                                        dataMessage[1] = dateFormat.format(date1);
                                        dataMessage[2] = content;
                                        dataMessage[3] = trainer.name;

                                        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));

                                        socket.emit("resolveTask", mJSONArray);

                                        Stage stage = (Stage) submitBtn.getScene().getWindow();
                                        stage.close();
                                    });

                                    Scene modal = new Scene(rootl);

                                    //move around here
                                    modal.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                        @Override
                                        public void handle(MouseEvent event) {
                                            overtimeModalStage.setX(event.getScreenX() - xOffset);
                                            overtimeModalStage.setY(event.getScreenY() - yOffset);
                                        }
                                    });

                                    overtimeModalStage.setScene(modal);
                                    overtimeModalStage.initStyle(StageStyle.UNDECORATED);
                                    overtimeModalStage.show();
                                } catch (IOException ex) {
                                    Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            });
                                
                            } else {
                                
                                Button tb = (Button) taskVBox.lookup("#" + id_task);
                                    System.out.println("tb contains" + tb);

                                    tb.setText("DONE");
                                    tb.getStyleClass().clear();
                                    tb.getStyleClass().addAll("button", "taskBtnDone");
                                    tb.setDisable(true);
                            
                            }

                        }

                    }

                }

            }
        });

    }

    private void startWebSocketConnection() {

        String[] dataRegister = {trainer.name, trainer.BUILD, trainer.id + ""};
        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));
        //System.out.println("Socket init");

        try {
            String socketUrl = SOCKETIO;
            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            }};
            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();

            IO.Options opts = new IO.Options();
            opts.callFactory = okHttpClient;
            opts.webSocketFactory = okHttpClient;
            socket = IO.socket(socketUrl, opts);

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    socket.emit("trainerusername", mJSONArray);
                    //socket.disconnect();

                    String[] dataMessage = new String[4];
                    dataMessage[0] = trainer.name;
                    dataMessage[1] = String.valueOf(trainer.studio);

                    JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));

                    socket.emit("allmodels", mJSONArray);
                    spinnerAllModels.setVisible(true);

                    socket.emit("getmymodels", trainer.id);
                    spinnerMyModels.setVisible(true);

                    int delay = 500;
                    int period = 60000;  // repeat every min.
                    Timer timer = new Timer();

                    timer.scheduleAtFixedRate(new TimerTask() {
                        public void run() {

                            //String[] dataMessage = new String[4];
                            //dataMessage[0] = trainer.name;
                            //dataMessage[1] = String.valueOf(trainer.studio);
                            //JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));
                            //socket.emit("allmodels", mJSONArray);
                            //socket.emit("getmymodels", trainer.id);
                            //iterate through 
                            readMyModels();

                        }
                    }, delay, period);
                }

            }).on("modelRealocate", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
//                    System.out.println("event: " + args[0]);
//                    Gson gson = new Gson();
//                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
//                    JsonObject jobject = data.getAsJsonObject();
//                    Boolean success = jobject.getAsJsonPrimitive("success").getAsBoolean();
//
//                    if (success) {
//                        String model = jobject.getAsJsonPrimitive("model").getAsString();
//                        Boolean shift = jobject.getAsJsonPrimitive("shift_start").getAsBoolean();
//                        
//                        System.out.println("for model: " + model);
//                        JsonArray jTrainers = jobject.getAsJsonArray("trainers");
//                        
//                        myDataTable.forEach((row) -> {
//
//                                if (model.equals(row.getName())) {
//                                     showPopupTrainers(model, shift);
//                                }
//
//                        });
//
//                       
//                        
//
//                    }

                }

            }).on("shiftStarted", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("shift started: " + args[0]);

                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    JsonObject jobject = data.getAsJsonObject();
                    boolean success = jobject.get("success").getAsBoolean();

                    if (success) {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (trainerModalStage != null) {
                                    trainerModalStage.close();
                                }

                                trainer.trainerShiftId = jobject.get("trainerShiftId").getAsInt();

                                String time_shift = jobject.get("time").getAsString();
                                lbltaskBtn1.setText(time_shift);
                            }
                        });

                    }

                }

            }).on("CLOSE_APP", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //System.out.println("close app: ");
                    System.exit(0);
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //System.out.println("event: " + args);
                }

            }).on("chatHistory", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    //clearChatArea();
                    //System.out.println("chatHistory: " + args[0]);
                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    JsonObject jobject = data.getAsJsonObject();
                    JsonArray jdata = jobject.getAsJsonArray("data");

                    for (int i = 0; i < jdata.size(); i++) {

                        JsonObject job = jdata.get(i).getAsJsonObject();
                        String toid = job.get("toID").getAsString();
                        String fromid = job.get("fromID").getAsString();
                        String message = job.get("message").getAsString();
                        long time = job.get("time").getAsLong();

                        //System.out.println("message :" + message);
                        String modelname = fromid;

                        if (fromid.equals(trainer.name)) {
                            modelname = toid;
                        }

                        //long time = jobject.get(3).getAsLong();
                        Timestamp timestamp = new Timestamp(time);
                        if (fromid.equals(trainer.name)) {
                            prepareChatTab(trainer.name, modelname, message, "right", timestamp);
                        } else {
                            prepareChatTab(trainer.name, modelname, message, "left", timestamp);
                        }

                    }
                }

            }).on("retriveShiftReportData", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //System.out.println("shift report data: " + args[0].toString());
//                    Gson gson = new Gson();
//                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
//                    JsonObject jobject = data.getAsJsonObject();
//                    Boolean success = jobject.getAsJsonPrimitive("success").getAsBoolean();
//                    
//                    if (success) {
//                        JsonObject jData = jobject.getAsJsonObject("data");
//                        System.out.println("get model data shift report xx:" + jData);
//
//                        String room = jData.get("room").isJsonNull() ? "" : jData.get("room").getAsString();
//                        String place_awd = jData.get("place_awd").isJsonNull() ? "" : jData.get("place_awd").getAsString();
//                        String awd_points = jData.get("awd_points").isJsonNull() ? "" : jData.get("awd_points").getAsString();
//                        String field1 = jData.get("field1").isJsonNull() ? "" : jData.get("field1").getAsString();
//                        String field2 = jData.get("field2").isJsonNull() ? "" : jData.get("field2").getAsString();
//                        String field3 = jData.get("field3").isJsonNull() ? "" : jData.get("field3").getAsString();
//                        String field4 = jData.get("field4").isJsonNull() ? "" : jData.get("field4").getAsString();
//                        String field5 = jData.get("field5").isJsonNull() ? "" : jData.get("field5").getAsString();
//                        String field6 = jData.get("field6").isJsonNull() ? "" : jData.get("field6").getAsString();
//                        
//                        updateSelectedModelShiftReportUI(room, place_awd, awd_points, field1, field2, field3, field4, field5, field6);
//                        
//                       
//                    }
                }

            }).on("modelchat", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //System.out.println("event: " + args[0].toString());
                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    JsonArray jobject = data.getAsJsonArray();
                    //System.out.println("arg: " + jobject.get(0));
                    String model = jobject.get(1).toString();
                    String model_ = model.substring(1, model.length() - 1);
                    String message = jobject.get(2).toString();
                    String message_ = message.substring(1, message.length() - 1);
                    long time = jobject.get(3).getAsLong();
                    Timestamp timestamp = new Timestamp(time);
                    prepareChatTab(jobject.get(0).toString(), model_, message_, "left", timestamp);
                }

            }).on("retriveselectedmodel", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //System.out.println("event: " + args);
                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    JsonObject jobject = data.getAsJsonObject();
                    Boolean success = jobject.getAsJsonPrimitive("success").getAsBoolean();

                    if (success) {
                        spinnerModelProfile.setVisible(false);
                        JsonObject jData = jobject.getAsJsonObject("data");
                        //System.out.println("get model info:" + jData);

                        String modelname = jData.get("sync_Modelname").getAsString();

                        model_artistic_email = jData.get("artistic_email").getAsString();
                        model_artistic_password = jData.get("artistic_password").getAsString();

                        int priority = jData.get("priority").getAsInt();
                        int id = jData.get("id").getAsInt();
                        int hits_today = jData.get("hits_today").getAsInt();
                        String data_language = jData.get("data_language").getAsString();
                        String data_streamQuality = jData.get("data_streamQuality").getAsString();
                        String data_willingnesses = jData.get("data_willingnesses").getAsString();
                        String data_sex = jData.get("data_sex").getAsString();
                        String data_age = jData.get("data_age").getAsString();
                        String data_category = jData.get("data_category").getAsString();
                        String data_bannedCountries = jData.get("data_bannedCountries").getAsString();
                        String data_modelRating = jData.get("data_modelRating").getAsString();
                        String data_chargeAmount = jData.get("data_chargeAmount").getAsString();
                        String data_profilePictureUrl = jData.get("data_profilePictureUrl").getAsString();
                        String time = jData.get("time").getAsString();
                        String first_login = jData.get("first_login").getAsString();

                        JsonArray jShiftArr = jData.getAsJsonArray("shift");

                        clearChart();

                        //System.out.println("shift data: " + jShiftArr);
                        for (int i = 0; i < jShiftArr.size(); i++) {

                            JsonObject job = jShiftArr.get(i).getAsJsonObject();
                            String lStatus = job.get("last_status").getAsString();
                            int startStatus = job.get("status_start").getAsInt();
                            int endStatus = job.get("status_end").getAsInt();
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            if (i == 0) {

                                Date date = Date.from(Instant.ofEpochSecond(startStatus));
                                printDate("start", "Shift Start: " + dateFormat.format(date));
                                initval = startStatus;
                            }

                            if (i == jShiftArr.size() - 1) {

                                Date date = Date.from(Instant.ofEpochSecond(endStatus));
                                printDate("end", "Last Check: " + dateFormat.format(date));
                            }

                            // generateChart(endStatus, startStatus, lStatus);
                            generateChart(endStatus, startStatus, lStatus);
                        }

                        JsonArray jSales = jobject.getAsJsonArray("sales");
                        JsonObject jTotal = jobject.getAsJsonObject("total");

                        for (int i = 0; i < jSales.size(); i++) {
                            JsonObject jsonobject = jSales.get(i).getAsJsonObject();

                            String jsData = jsonobject.get("date").getAsString();
                            String jsSum = jsonobject.get("sum").getAsString();
                            String jsHours = jsonobject.get("hours").getAsString();
                            dataSales.add(new Sales(Integer.toString(i + 1), jsData, jsSum, jsHours));
                        }

                        updateSelectedModelUI(modelname, data_profilePictureUrl, jTotal);

                        //System.out.println("sales :" + jSales);
                        //System.out.println("total :" + jTotal);
                    }
                }

            }).on(trainer.name, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("msg trainerrr: " + args);
                    //addTexttoChat(args.toString());
                }

            }).on("updateMyModels", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    socket.emit("getmymodels", trainer.id);
                }

            }).on("refreshtask", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    //socket.emit("getmymodels", trainer.id);
                    readMyModels();
                }

            }).on("TASKALERT", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    System.out.println("Event detected: " + data);

                    JsonObject jobject = data.getAsJsonObject();
                    //JsonArray arr = jobject.getAsJsonArray("data");
                    //JsonObject arr = jobject.getAsJsonObject("data");

                    insertTask(jobject);

                }

            }).on("allmodels", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    //System.out.println("All Models: " + data);

                    JsonObject jobject = data.getAsJsonObject();
                    //JsonArray arr = jobject.getAsJsonArray("data");
                    JsonObject arr = jobject.getAsJsonObject("data");

                    updateAllModels(arr);
                    spinnerAllModels.setVisible(false);

                }

            }).on("retrivemymodels", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //System.out.println("mymodels: " + args[0]);
                    //System.out.println("MY MODELS!!");

                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    //System.out.println("mymodels->new: " + data);

                    JsonObject jobject = data.getAsJsonObject();
                    Boolean success = jobject.getAsJsonPrimitive("success").getAsBoolean();

                    if (success) {
                        spinnerMyModels.setVisible(false);
                        JsonArray arr = jobject.getAsJsonArray("shift_data");
                        //System.out.println("mymodels!!!!!!!!!!!!: " + arr);
                        //System.out.println("Receive get my models!!! with success");
                        updateMyModels(arr);

                    }

                }

            }).on("chat", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("chat: " + args[0]);

                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    System.out.println("message: " + data);

                    //addTexttoChat(data.toString());
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        socket.connect();

    }

}
