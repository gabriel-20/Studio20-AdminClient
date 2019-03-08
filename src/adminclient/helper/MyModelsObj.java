/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient.helper;

/**
 *
 * @author dev
 */
public class MyModelsObj {
    String  name;
    String  status;
    String  chat;
    
     public MyModelsObj(String name, String status, String chat) {
        this.name = name;
        this.status = status;
        this.chat = chat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
