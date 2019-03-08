/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient.helper;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author dev
 */
public class ModelsObj {
    String  name;
    String  status;

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
    
    
    public ModelsObj(String name, String status) {
        this.name = new String(name);
        this.status = new String(status);
    }
}


