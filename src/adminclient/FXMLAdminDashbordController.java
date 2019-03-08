/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient;

import static adminclient.helper.Global.SOCKETIO;
import adminclient.helper.ModelsObj;
import adminclient.helper.MyModelsObj;
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
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
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

    @FXML
    private TableView tableview_TimeOnline;

    @FXML
    private ImageView modelProfilePic;

    @FXML
    private AnchorPane scrollModels, scrollMyModels;

    @FXML
    private Label trainerName, selectedModelNickname, labelAllModels, labelMyModels;

    @FXML
    private Label freeChatTotal, privateChatTotal, memberChatTotal, vipChatTotal, totalTime;

    @FXML
    private AnchorPane charts2;

    @FXML
    private void handleClose(MouseEvent event) {
        System.exit(0);
    }

    int total_free = 0;
    int total_private = 0;
    int total_member = 0;
    int total_vip = 0;
    int total_time = 0;

    private TableView table = new TableView();
    private TableView myTable = new TableView();

    Trainer trainer = Trainer.getInstance();

    ObservableList<ModelsObj> dataTable = FXCollections.observableArrayList();
    ObservableList<MyModelsObj> myDataTable = FXCollections.observableArrayList();

    List<String> list = new ArrayList<String>();

    ObservableList<String> observableList1 = FXCollections.observableList(list);

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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

        scrollMyModels.getChildren().addAll(myTable);

        myTable.setRowFactory(tv -> {
            TableRow<MyModelsObj> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    MyModelsObj rowData = row.getItem();
                    System.out.println("Double click on: " + rowData.getName());
                    //updateMyModels(rowData.getName());

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

        table.setItems(dataTable);
        table.getColumns().addAll(firstNameCol, lastNameCol);

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

//        observableList.addListener(new ListChangeListener() {
//
//            @Override
//            public void onChanged(ListChangeListener.Change change) {
//
//                Gson gson = new Gson();
//                JSONObject obj = new JSONObject();
//                try {
//                    obj.put("email", trainer.email);
//                    obj.put("models", change.getList().toString());
//                } catch (JSONException ex) {
//                    Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                socket.emit("updatemymodels", obj);
//            }
//        });

        trainerName.setText(trainer.name);

        startWebSocketConnection();

    }

    private void updateSelectedModelUI(String modelname, String profilePic) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                selectedModelNickname.setText(modelname);
                String imageUrl = "http:" + profilePic;

                Image image = new Image(imageUrl);
                modelProfilePic.setImage(image);
                //System.out.println("PIC:" + profilePic);

//                freeChatTotal.setText(String.valueOf(total_free));
//                privateChatTotal.setText(String.valueOf(total_private));
//                memberChatTotal.setText(String.valueOf(total_member));
//                vipChatTotal.setText(String.valueOf(total_vip));
//                totalTime.setText(String.valueOf(total_free + total_private + total_member + total_vip));
                freeChatTotal.setText(formatSeconds(total_free));
                System.out.println("free:" + String.valueOf(total_free));
                privateChatTotal.setText(formatSeconds(total_private));
                memberChatTotal.setText(formatSeconds(total_member));
                vipChatTotal.setText(formatSeconds(total_vip));
                totalTime.setText(formatSeconds(total_free + total_private + total_member + total_vip));

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

//    private void updateMyModels1(String model) {
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                // Update UI here.
//
//                Group root = new Group();
//
//                // a horizontal panel to hold the list view and the label.
//                HBox listViewPanel = new HBox();
//                listViewPanel.setSpacing(10);
//
//                if (!observableList.contains(model)) {
//                    observableList.add(model);
//                }
//
//                ListView<String> m_listView = new ListView<String>(FXCollections.observableArrayList(observableList));
//                m_listView.prefHeight(200);
//                m_listView.setMaxHeight(200);
//
//                m_listView.getSelectionModel().selectedItemProperty()
//                        .addListener(new ChangeListener<String>() {
//
//                            public void changed(
//                                    ObservableValue<? extends String> observable,
//                                    String oldValue, String newValue) {
//                                        socket.emit("getmodelinfo", newValue);
//                                    }
//                        });
//                listViewPanel.getChildren().addAll(m_listView);
//                root.getChildren().addAll(listViewPanel);
//                scrollMyModels.getChildren().add(root);
//
//            }
//        });
//
//    }

    private void generateChart(int end, int start, String status) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.

                int lineheight = 20;
                int stretch = 400;

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

                for (int i = 0; i <= lineheight; i++) {
                    Line line = new Line();

                    line.setStartX(lineStartX);
                    line.setStartY(4 + i);
                    line.setEndX(lineEndX - 1);
                    line.setEndY(4 + i);
                    line.setStrokeWidth(1);
                    Bloom bloom = new Bloom();
                    bloom.setThreshold(1.0);
                    line.setEffect(bloom);

                    if (status.equals("free_chat")) {
                        line.setStroke(javafx.scene.paint.Color.GREEN);
                    }

                    if (status.equals("private_chat")) {
                        line.setStroke(javafx.scene.paint.Color.ORANGE);
                    }

                    if (status.equals("member_chat")) {
                        line.setStroke(javafx.scene.paint.Color.BLUE);
                    }

                    if (status.equals("vip_show")) {
                        line.setStroke(javafx.scene.paint.Color.MAGENTA);
                    }

                    if (status.equals("offline")) {
                        line.setStroke(javafx.scene.paint.Color.RED);
                    }

                    charts2.getChildren().add(line);

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

    private void updateMyModels2(JsonArray arr) {

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

    private void updateAllModels(JsonObject arr) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.
                dataTable.clear();
                int i = 1;
                for (Iterator iterator = arr.keySet().iterator(); iterator.hasNext();) {
                    String key = (String) iterator.next();
                    //System.out.println(arr.get(key));
                    dataTable.add(new ModelsObj(key, arr.get(key).toString()));
                    i++;
                }

                labelAllModels.setText("ALL MODELS - " + String.valueOf(i));

            }
        });

    }

    private void startWebSocketConnection() {

        String[] dataRegister = {"trainer", "2"};
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
                    socket.emit("username", mJSONArray);
                    //socket.disconnect();

                    int delay = 500;
                    int period = 60000;  // repeat every min.
                    Timer timer = new Timer();

                    timer.scheduleAtFixedRate(new TimerTask() {
                        public void run() {

                            socket.emit("allmodels");

                            socket.emit("getmymodels", trainer.email);
                        }
                    }, delay, period);
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //System.out.println("event: " + args);
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
                                //printDate("start", date.toString());
                                //startTime.setText(date.toString());
                                initval = startStatus;
                            }

                            if (i == jShiftArr.size() - 1) {

                                Date date = Date.from(Instant.ofEpochSecond(endStatus));
                                //printDate("end", date.toString());
                                System.out.println("end->data:" + date);
                                //endTime.setText(date.toString());
                            }

                            generateChart(endStatus, startStatus, lStatus);
                        }

                        updateSelectedModelUI(modelname, data_profilePictureUrl);

                    }
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

                        updateMyModels2(arr);
                    }

                }

            }).on("chat", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("chat: " + args[0]);

                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    System.out.println("message: " + data);

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
