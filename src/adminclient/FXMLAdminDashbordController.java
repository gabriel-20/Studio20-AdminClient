/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient;

import static adminclient.helper.Global.SOCKETIO;
import adminclient.helper.ModelsObj;
import adminclient.helper.MyModelsObj;
import adminclient.helper.Sales;
import adminclient.helper.Trainer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dev
 */
public class FXMLAdminDashbordController implements Initializable {

    private Socket socket;

    private int initval = 0;

    String localModelProfile;

    @FXML
    private TextField filterField;

    @FXML
    private TableView<Sales> tableSales;

    @FXML
    private TableView tableview_TimeOnline;

    @FXML
    private ImageView modelProfilePic, trainerProfile;

    @FXML
    private Label modelTotalPeriod;

    @FXML
    private AnchorPane scrollModels, scrollMyModels;

    @FXML
    private Label trainerName, trainerStudio, selectedModelNickname, labelAllModels, labelMyModels, startTime, endTime;

    @FXML
    private Label freeChatTotal, privateChatTotal, memberChatTotal, vipChatTotal, totalTime;

    @FXML
    private AnchorPane charts2;

    @FXML
    private TabPane tabpaneChat;

    @FXML
    private VBox chatArea;

    @FXML
    private TextField chatField;

    @FXML
    private void handleClose(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleChat(MouseEvent event) {
        sendChatMessage();
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

        TableColumn indexFieldCol = new TableColumn("#");
        indexFieldCol.setMinWidth(100);
        indexFieldCol.setCellValueFactory(new PropertyValueFactory<>("indexField"));

        TableColumn firstDayCol = new TableColumn("Day");
        firstDayCol.setMinWidth(100);
        firstDayCol.setCellValueFactory(new PropertyValueFactory<>("firstDay"));

        TableColumn lastSaleCol = new TableColumn("Sale");
        lastSaleCol.setMinWidth(100);
        lastSaleCol.setCellValueFactory(new PropertyValueFactory<>("lastSale"));

        TableColumn lastHourCol = new TableColumn("Hours");
        lastHourCol.setMinWidth(100);
        lastHourCol.setCellValueFactory(new PropertyValueFactory<>("lastHour"));

        tableSales.setItems(dataSales);
        tableSales.getColumns().addAll(indexFieldCol, firstDayCol, lastSaleCol, lastHourCol);

        openTabs.add("GENERAL CHAT");

        chatField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                sendChatMessage();
            }
        });

        System.out.println("trainer :" + trainer.profile);
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
        myTable.setMaxHeight(250);
        myTable.setPrefHeight(250);

        scrollMyModels.getChildren().addAll(myTable);
        scrollMyModels.setMaxHeight(250);
        scrollMyModels.setPrefHeight(250);

        myTable.setRowFactory(tv -> {
            TableRow<MyModelsObj> row = new TableRow<>();
            row.setOnMouseClicked(event -> {

                if ((event.getButton() == MouseButton.SECONDARY) && (!row.isEmpty())) {
                    System.out.println("click dreapta");

                    String[] dataRegister = {trainer.name, row.getItem().getName(), String.valueOf(trainer.studio)};
                    JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));
                    socket.emit("getOnlineTrainersStudio", mJSONArray);

                }

                if (event.getClickCount() == 1 && (!row.isEmpty()) && (event.getButton() == MouseButton.PRIMARY)) {
                    MyModelsObj rowData = row.getItem();
                    String name = rowData.getName();
                    System.out.println("Click on: " + name);

                    Tab tab = new Tab(name);
                    tab.setOnClosed(new EventHandler<Event>() {
                        @Override
                        public void handle(Event t) {
                            System.out.println("Closed!" + tab.getText());
                            openTabs.remove(tab.getText());
                            t.consume();
                        }
                    });

                    if (!openTabs.contains(name)) {
                        ScrollPane spane = new ScrollPane();
                        VBox vbox = new VBox();
                        vbox.setMinWidth(1130);
                        spane.setContent(vbox);
                        tab.setContent(spane);
                        tabpaneChat.getTabs().add(tab);
                        openTabs.add(name);
                        tabpaneChat.getSelectionModel().select(tab);
                    }

                    String[] dataRegister = {trainer.name, name};
                    JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));

                    socket.emit("getmodelinfo", mJSONArray);

                }
            });
            return row;
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

        filterField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                updateFilteredData();
            }
        });

        scrollModels.getChildren().addAll(table);

        table.setRowFactory(tv -> {
            TableRow<ModelsObj> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ModelsObj rowData = row.getItem();
                    System.out.println("Double click on: " + rowData.getName());

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("email", trainer.email);
                        obj.put("models", rowData.getName());
                    } catch (JSONException ex) {
                        Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    socket.emit("updatemymodels", obj);
                    System.out.println("emit updatemymodels " + obj);
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

    private void updateSelectedModelUI(String modelname, String profilePic, JsonObject jTotal) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                selectedModelNickname.setText(modelname);
                String imageUrl = "http:" + profilePic;
                localModelProfile = imageUrl;

                Image image = new Image(imageUrl);
                modelProfilePic.setImage(image);
                //System.out.println("PIC:" + profilePic);

//                freeChatTotal.setText(String.valueOf(total_free));
//                privateChatTotal.setText(String.valueOf(total_private));
//                memberChatTotal.setText(String.valueOf(total_member));
//                vipChatTotal.setText(String.valueOf(total_vip));
//                totalTime.setText(String.valueOf(total_free + total_private + total_member + total_vip));
                freeChatTotal.setText(formatSeconds(total_free));
                //System.out.println("free:" + String.valueOf(total_free));
                privateChatTotal.setText(formatSeconds(total_private));
                memberChatTotal.setText(formatSeconds(total_member));
                vipChatTotal.setText(formatSeconds(total_vip));
                totalTime.setText(formatSeconds(total_free + total_private + total_member + total_vip));

                int total = jTotal.get("totalamount").getAsInt();
                modelTotalPeriod.setText(String.valueOf(total) + "$");

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

                Rectangle r = new Rectangle();
                r.setX(lineStartX);
                r.setY(3);
                r.setWidth(lineEndX - lineStartX - 1);
                r.setHeight(lineheight);
                Tooltip tooltip = new Tooltip(diff);
                Tooltip.install(r, tooltip);

                if (status.equals("free_chat")) {
                    r.setStroke(javafx.scene.paint.Color.GREEN);
                    r.setFill(javafx.scene.paint.Color.GREEN);
                }

                if (status.equals("private_chat")) {
                    r.setStroke(javafx.scene.paint.Color.ORANGE);
                    r.setFill(javafx.scene.paint.Color.ORANGE);
                }

                if (status.equals("member_chat")) {
                    r.setStroke(javafx.scene.paint.Color.BLUE);
                    r.setFill(javafx.scene.paint.Color.BLUE);
                }

                if (status.equals("vip_show")) {
                    r.setStroke(javafx.scene.paint.Color.MAGENTA);
                    r.setFill(javafx.scene.paint.Color.MAGENTA);
                }

                if (status.equals("offline")) {
                    r.setStroke(javafx.scene.paint.Color.RED);
                    r.setFill(javafx.scene.paint.Color.RED);
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
                charts2.getChildren().clear();
                freeChatTotal.setText("00:00:00");
                privateChatTotal.setText("00:00:00");
                memberChatTotal.setText("00:00:00");
                vipChatTotal.setText("00:00:00");
                totalTime.setText("00:00:00");
                total_time = 0;
                total_free = 0;
                total_private = 0;
                total_member = 0;
                total_vip = 0;

            }
        });

    }

    private void updateMyModels(JsonArray arr) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.

                myDataTable.clear();

                for (int i = 0; i < arr.size(); i++) {
                    JsonObject job = arr.get(i).getAsJsonObject();
                    String rName = job.get("name").getAsString();
                    String rStatus = job.get("status").getAsString();
                    String rChat = job.get("chat").getAsString();
                    myDataTable.add(new MyModelsObj(rName, rStatus, rChat));
                }

                labelMyModels.setText("MY MODELS - " + String.valueOf(arr.size()));

            }
        });

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

                System.out.println("trainer: " + train);
                System.out.println("model: " + model);
                System.out.println("message: " + message);

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
            }
        });

    }

    private void showPopupTrainers(JsonArray jArrTrainers, String model) {

        Platform.runLater(() -> {
            // Update UI here.
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);

            VBox vbox = new VBox(new Text("Hi"), new Button("Ok."));

            for (int i = 0; i < jArrTrainers.size(); i++) {

                JsonObject job = jArrTrainers.get(i).getAsJsonObject();
                String name = job.get("name").getAsString();

                Label lbl = new Label(name);
                lbl.setOnMouseClicked(e -> {
                    System.out.println(lbl.getText());
                    
                    String[] dataRegister = {trainer.name, model, name};
                    JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));
                    
                    socket.emit("realocateRemoveModel", mJSONArray);
                });

                vbox.getChildren().add(lbl);
            }
            Label lbl = new Label("Remove");
            lbl.setOnMouseClicked(e -> {
                    System.out.println(lbl.getText());
                    
                    String[] dataRegister = {trainer.name, model, "Remove"};
                    JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));
                    
                    socket.emit("realocateRemoveModel", mJSONArray);
                });
            
            vbox.getChildren().add(lbl);

            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(15));

            dialogStage.setScene(new Scene(vbox));
            dialogStage.show();
        });

    }

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

    private void startWebSocketConnection() {

        String[] dataRegister = {trainer.name, "2"};
        JSONArray mJSONArray = new JSONArray(Arrays.asList(dataRegister));
        System.out.println("Socket init");

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

                    int delay = 500;
                    int period = 60000;  // repeat every min.
                    Timer timer = new Timer();

                    timer.scheduleAtFixedRate(new TimerTask() {
                        public void run() {

                            String[] dataMessage = new String[4];
                            dataMessage[0] = trainer.name;
                            dataMessage[1] = String.valueOf(trainer.studio);

                            JSONArray mJSONArray = new JSONArray(Arrays.asList(dataMessage));

                            socket.emit("allmodels", mJSONArray);

                            socket.emit("getmymodels", trainer.email);
                        }
                    }, delay, period);
                }

            }).on("modelRealocate", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("event: " + args[0]);
                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    JsonObject jobject = data.getAsJsonObject();
                    Boolean success = jobject.getAsJsonPrimitive("success").getAsBoolean();

                    if (success) {
                        String model = jobject.getAsJsonPrimitive("model").getAsString();
                        System.out.println("for model: " + model);
                        JsonArray jTrainers = jobject.getAsJsonArray("trainers");

                        showPopupTrainers(jTrainers, model);
//                        String modelname = jData.get("sync_Modelname").getAsString();
//                        int priority = jData.get("priority").getAsInt();
//                        int id = jData.get("id").getAsInt();
//                        int hits_today = jData.get("hits_today").getAsInt();
//                        String data_language = jData.get("data_language").getAsString();
//                        String data_streamQuality = jData.get("data_streamQuality").getAsString();
//                        String data_willingnesses = jData.get("data_willingnesses").getAsString();
//                        String data_sex = jData.get("data_sex").getAsString();
//                        String data_age = jData.get("data_age").getAsString();
                    }

                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //System.out.println("event: " + args);
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
                        JsonObject jData = jobject.getAsJsonObject("data");
                        String modelname = jData.get("sync_Modelname").getAsString();
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

                        System.out.println("shift data: " + jShiftArr);
                        for (int i = 0; i < jShiftArr.size(); i++) {

                            JsonObject job = jShiftArr.get(i).getAsJsonObject();
                            String lStatus = job.get("last_status").getAsString();
                            int startStatus = job.get("status_start").getAsInt();
                            int endStatus = job.get("status_end").getAsInt();
                            if (i == 0) {

                                Date date = Date.from(Instant.ofEpochSecond(startStatus));
                                System.out.println("start->data:" + date);
                                printDate("start", date.toString());
                                //startTime.setText(date.toString());
                                initval = startStatus;
                            }

                            if (i == jShiftArr.size() - 1) {

                                Date date = Date.from(Instant.ofEpochSecond(endStatus));
                                printDate("end", date.toString());
                                System.out.println("end->data:" + date);
                                //endTime.setText(date.toString());
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

                        System.out.println("sales :" + jSales);
                        System.out.println("total :" + jTotal);

                    }
                }

            }).on(trainer.name, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("msg trainerrr: " + args);
                    //addTexttoChat(args.toString());
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

                }

            }).on("retrivemymodels", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //System.out.println("mymodels: " + args[0]);

                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    System.out.println("mymodels: " + data);

                    JsonObject jobject = data.getAsJsonObject();
                    Boolean success = jobject.getAsJsonPrimitive("success").getAsBoolean();

                    if (success) {
                        JsonArray arr = jobject.getAsJsonArray("data");
                        //System.out.println("mymodels: " + arr);

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
