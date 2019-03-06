/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient.helper;

import com.google.gson.JsonArray;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author dev
 */
public class Trainer {
    
        private static Trainer single_instance_model = null; 
        
        public String name;
        public String email;
        
        

        
    private Trainer() 
    { 
        name = "notInit"; 
        email = "notInit@notInit.com";
       
    }
    
    // static method to create instance of Singleton class 
    public static Trainer getInstance() 
    { 
        if (single_instance_model == null) 
            single_instance_model = new Trainer(); 
  
        return single_instance_model; 
    } 
    
    
}
