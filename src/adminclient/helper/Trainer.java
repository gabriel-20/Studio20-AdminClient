/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclient.helper;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 *
 * @author dev
 */
public class Trainer {
    
        private static Trainer single_instance_model = null; 
        
        public String name;
        public int id;
        public String email;
        public String profile;
        public int studio;
        public String studioName;
        public boolean isShift;
        public boolean isShiftEnd;
        public String shiftStart;
        public String shiftEnd;
        public String BUILD;
        public String proxy;
        public int trainerShiftId;
        
        

        
    private Trainer() 
    { 
        name = "notInit"; 
        email = "notInit@notInit.com";
        profile = "www.notInit.com";
        studio = 0;
        id = 0;
        studioName = "Studio Error";
        isShift = false;
        isShiftEnd = false;
        shiftStart = "00:00:00";
        shiftEnd = "00:00:00";
        proxy = "http=142.93.103.225:3128;https=142.93.103.225:3128";
        trainerShiftId = 0;
        
         try {
            BUILD = getManifestInfo();
        } catch (IOException ex) {
            //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    // static method to create instance of Singleton class 
    public static Trainer getInstance() 
    { 
        if (single_instance_model == null) 
            single_instance_model = new Trainer(); 
  
        return single_instance_model; 
    } 
    
    public static String getManifestInfo() throws IOException {
              
    Enumeration resEnum;
    resEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
    while (resEnum.hasMoreElements()) {
        try {
            URL url = (URL)resEnum.nextElement();
            InputStream is = url.openStream();
            if (is != null) {
                Manifest manifest = new Manifest(is);
                Attributes mainAttribs = manifest.getMainAttributes();
                String version = mainAttribs.getValue("Implementation-Version");
                if(version != null) {
                    return version;
                }
            }
        }
        catch (Exception e) {
            // Silently ignore wrong manifests on classpath?
        }
    }
    return null; 
    
}
    
    
}
