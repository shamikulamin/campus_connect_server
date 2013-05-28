package beans;

import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Adnan
 */
public class IncidentMessageBean {
    private int incidentId;
    private String messageTitle;
    private String messageDescription;
    private String latLong;

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }
    private int noOfImages;

    public int getNoOfImages() {
        return noOfImages;
    }

    public void setNoOfImages(int noOfImages) {
        this.noOfImages = noOfImages;
    }


    
   
    

    
    public int getIncidentId(){
        return incidentId;
    }
    
    public String getMessageTitle(){
        return messageTitle;
    }
    public String getMessageDescription(){
        return messageDescription;
    }
    
    public void setIncidentId(int incidentId){
        this.incidentId = incidentId;
    }
    
    public void setMessageTitle(String messageTitle){
        this.messageTitle = messageTitle;
    }
    public void setMessageDescription(String messageDescription){
        this.messageDescription = messageDescription;
    }
}
