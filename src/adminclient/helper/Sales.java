/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient.helper;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author admin
 */
public class Sales {
 
        private final SimpleStringProperty indexField;
        private final SimpleStringProperty firstDay;
        private final SimpleStringProperty lastSale;
        private final SimpleStringProperty lastHour;
 
        public Sales(String iField, String fDay, String lSale, String lHour) {
            this.indexField = new SimpleStringProperty(iField);
            this.firstDay = new SimpleStringProperty(fDay);
            this.lastSale = new SimpleStringProperty(lSale);            
            this.lastHour = new SimpleStringProperty(lHour);            
        }
        
        public String getIndexField() {
            return indexField.get();
        }
 
        public void setIndexField(String iField) {
            indexField.set(iField);
        }
 
        public String getFirstDay() {
            return firstDay.get();
        }
 
        public void setFirstDay(String fDay) {
            firstDay.set(fDay);
        }
        
        public String getLastSale() {
            return lastSale.get();
        }
 
        public void setLastSale(String lSale) {
            lastSale.set(lSale);
        }
        
        public String getLastHour() {
            return lastHour.get();
        }
 
        public void setLastHour(String lHour) {
            lastHour.set(lHour);
        }
        
     
    }
