/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author Adnan
 */
public class CommunityMessageBean {
    private int msgId;
    private String msgTitle;
    private String msg_Description;
    private String latLong;

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }
    

    private String reportingTime;

    

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsg_Description() {
        return msg_Description;
    }

    public void setMsg_Description(String msg_Description) {
        this.msg_Description = msg_Description;
    }

    public String getReportingTime() {
        return reportingTime;
    }

    public void setReportingTime(String reportingTime) {
        this.reportingTime = reportingTime;
    }
    
    
}
