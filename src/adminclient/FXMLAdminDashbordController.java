/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient;

import static adminclient.helper.Global.SOCKETIO;
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
import java.util.List;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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

    @FXML
    private AnchorPane scrollModels, scrollMyModels;

    @FXML
    private Label trainerName;
    
    @FXML
    private Label selectedModelNickname;

    @FXML
    private void handleClose(MouseEvent event) {
        System.exit(0);
    }

    List<String> list = new ArrayList<String>();

    // Now add observability by wrapping it with ObservableList.
    ObservableList<String> observableList = FXCollections.observableList(list);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Trainer trainer = Trainer.getInstance();

        observableList.addListener(new ListChangeListener() {

            @Override
            public void onChanged(ListChangeListener.Change change) {
                System.out.println("Detected a change! " + change.getList().toString());

                Gson gson = new Gson();
                JSONObject obj = new JSONObject();
                try {
                    obj.put("email", trainer.email);
                    obj.put("models", change.getList().toString());
                } catch (JSONException ex) {
                    Logger.getLogger(FXMLAdminDashbordController.class.getName()).log(Level.SEVERE, null, ex);
                }
                socket.emit("updatemymodels", obj);
            }
        });

        
        trainerName.setText(trainer.name);

        startWebSocketConnection();

        socket.emit("allmodels");
        
        socket.emit("getmymodels", trainer.email);

    }

    private void updateMyModels(String model) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.

                Group root = new Group();

                // a horizontal panel to hold the list view and the label.
                HBox listViewPanel = new HBox();
                listViewPanel.setSpacing(10);

                if (!observableList.contains(model)) {
                    observableList.add(model);
                }

                ListView<String> m_listView = new ListView<String>(FXCollections.observableArrayList(observableList));
                m_listView.prefHeight(200);
                m_listView.setMaxHeight(200);

                m_listView.getSelectionModel().selectedItemProperty()
                        .addListener(new ChangeListener<String>() {

                            public void changed(
                                    ObservableValue<? extends String> observable,
                                    String oldValue, String newValue) {
                                        System.out.println("model : " + newValue);
                                        selectedModelNickname.setText(newValue);
                                    }
                        });
                listViewPanel.getChildren().addAll(m_listView);
                root.getChildren().addAll(listViewPanel);
                scrollMyModels.getChildren().add(root);

            }
        });

    }

    private void updateAllModels(JsonArray arr) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Update UI here.

                Group root = new Group();

                // a horizontal panel to hold the list view and the label.
                HBox listViewPanel = new HBox();
                listViewPanel.setSpacing(10);

                List<String> list = new ArrayList<String>();

                // Now add observability by wrapping it with ObservableList.
                ObservableList<String> observableList = FXCollections.observableList(list);
                observableList.addListener(new ListChangeListener() {

                    @Override
                    public void onChanged(ListChangeListener.Change change) {
                        //System.out.println("Detected a change! ");
                    }
                });
                //observableList.add("item one");

                for (int i = 0; i < arr.size(); i++) {
                    String model = arr.get(i).getAsString();
                    observableList.add(model);
                }

                ListView<String> m_listView = new ListView<String>(FXCollections.observableArrayList(observableList));

                m_listView.getSelectionModel().selectedItemProperty()
                        .addListener(new ChangeListener<String>() {

                            public void changed(
                                    ObservableValue<? extends String> observable,
                                    String oldValue, String newValue) {
                                        System.out.println("model : " + newValue);
                                        updateMyModels(newValue);
                                    }
                        });

                listViewPanel.getChildren().addAll(m_listView);
                root.getChildren().addAll(listViewPanel);
                scrollModels.getChildren().add(root);

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
                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    System.out.println("event: " + args);
                }

            }).on("allmodels", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    System.out.println("All Models: " + data);

                    JsonObject jobject = data.getAsJsonObject();
                    JsonArray arr = jobject.getAsJsonArray("data");

                    updateAllModels(arr);

                }

            }).on("retrivemymodels", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    //System.out.println("mymodels: " + args[0]);

                    Gson gson = new Gson();
                    JsonElement data = gson.fromJson(args[0].toString(), JsonElement.class);
                    //System.out.println("mymodels: " + data);
                    
                    JsonObject jobject = data.getAsJsonObject();
                    Boolean success = jobject.getAsJsonPrimitive("success").getAsBoolean();
                    
                    if (success) {
                        String arr = jobject.getAsJsonPrimitive("data").getAsString();
                        //System.out.println("mymodels: " + arr);
                        String requiredString = arr.substring(arr.indexOf("[") + 1, arr.indexOf("]"));
                                System.out.println("mymodels: " + requiredString);
                                String[] split = requiredString.split(",");
                        for(int i = 0; i < split.length; i++) updateMyModels(split[i].replaceAll("\\s+",""));
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
