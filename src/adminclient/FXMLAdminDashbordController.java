/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient;

import static adminclient.helper.Global.SOCKETIO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import org.json.JSONArray;

/**
 *
 * @author dev
 */
public class FXMLAdminDashbordController implements Initializable {
    
    private Socket socket;
    
      @Override
    public void initialize(URL url, ResourceBundle rb) {

        startWebSocketConnection();
        System.out.println("Socket init11111111");
   
   
      
       
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

            }).on("chat", new Emitter.Listener() {
                
                @Override
                public void call(Object... args) {
                    System.out.println("chat: " + args[0]);
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