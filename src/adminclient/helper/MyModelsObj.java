/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient.helper;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 *
 * @author dev
 */
public class MyModelsObj {

    String name;
    String status;
    String chat;
    boolean shift;
    boolean shiftEnd;
    JsonObject jobj;
    int shiftId;
    int loginTime;

    public int getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(int loginTime) {
        this.loginTime = loginTime;
    }

    public MyModelsObj(String name, String status, String chat, boolean shift, JsonObject jobj) {
        this.name = name;
        this.status = status;
        this.chat = chat;
        this.shift = shift;
        this.jobj = jobj;
        
        
        if ( (jobj != null)  && jobj.has("logout_time") ){
            
            JsonElement element = jobj.get("logout_time");
            if (!(element instanceof JsonNull)) {
                     this.shiftEnd = true;   
            } else this.shiftEnd = false;
            
        } else {
            this.shiftEnd = false;
        }
        
        if ( (jobj != null)  && jobj.has("id") ){
            
            this.shiftId = jobj.get("id").getAsInt();
            System.out.println("this shift id: " + this.shiftId);
            
        } 
        
        if ( (jobj != null)  && jobj.has("login_time") ){
            
            this.loginTime = jobj.get("login_time").getAsInt();
            System.out.println("this shift login_time: " + this.loginTime);
            
        } 
        
        
        
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public JsonObject getJobj() {
        return jobj;
    }

    public void setJobj(JsonObject jobj) {
        this.jobj = jobj;
    }

    public boolean isShift() {
        return shift;
    }

    public void setShift(boolean shift) {
        this.shift = shift;
    }
    
    public boolean isShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(boolean shiftEnd) {
        this.shiftEnd = shiftEnd;
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
