package beans;
// Generated May 20, 2013 1:35:53 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * IncidentMsg generated by hbm2java
 */
public class IncidentMsg  implements java.io.Serializable {


     private Integer incidentId;
     private String msgTitle;
     private String msgDescription;
     private Date reportingTime;
     private String latlong;

    public IncidentMsg() {
    }

	
    public IncidentMsg(Date reportingTime) {
        this.reportingTime = reportingTime;
    }
    public IncidentMsg(String msgTitle, String msgDescription, Date reportingTime, String latlong) {
       this.msgTitle = msgTitle;
       this.msgDescription = msgDescription;
       this.reportingTime = reportingTime;
       this.latlong = latlong;
    }
   
    public Integer getIncidentId() {
        return this.incidentId;
    }
    
    public void setIncidentId(Integer incidentId) {
        this.incidentId = incidentId;
    }
    public String getMsgTitle() {
        return this.msgTitle;
    }
    
    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }
    public String getMsgDescription() {
        return this.msgDescription;
    }
    
    public void setMsgDescription(String msgDescription) {
        this.msgDescription = msgDescription;
    }
    public Date getReportingTime() {
        return this.reportingTime;
    }
    
    public void setReportingTime(Date reportingTime) {
        this.reportingTime = reportingTime;
    }
    public String getLatlong() {
        return this.latlong;
    }
    
    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }




}

