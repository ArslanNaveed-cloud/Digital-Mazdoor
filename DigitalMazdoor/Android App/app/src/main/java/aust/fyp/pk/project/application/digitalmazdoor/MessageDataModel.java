package aust.fyp.pk.project.application.digitalmazdoor;

public class MessageDataModel {
    String sendername,recievername,message,senderid,recieverid,messagetype;

    public String getSendername() {
        return sendername;
    }

    public String getRecievername() {
        return recievername;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderid() {
        return senderid;
    }

    public String getRecieverid() {
        return recieverid;
    }

    public String getMessagetype() {
        return messagetype;
    }

    public MessageDataModel(String sendername, String recievername, String message, String senderid, String recieverid, String messagetype) {
        this.sendername = sendername;
        this.recievername = recievername;
        this.message = message;
        this.senderid = senderid;
        this.recieverid = recieverid;
        this.messagetype = messagetype;
    }
}
