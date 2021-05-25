package aust.fyp.pk.project.application.digitalmazdoor;

public class UserListDataModel {
    String buyername;
    String buyerimage;
    String buyeremail;
    String lastmessage;
    String buyerid;

    public String getBuyername() {
        return buyername;
    }

    public String getBuyerimage() {
        return buyerimage;
    }

    public String getBuyeremail() {
        return buyeremail;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public String getType() {
        return type;
    }

    public UserListDataModel(String buyername, String buyerimage, String buyeremail, String lastmessage, String buyerid, String type) {
        this.buyername = buyername;
        this.buyerimage = buyerimage;
        this.buyeremail = buyeremail;
        this.lastmessage = lastmessage;
        this.buyerid = buyerid;
        this.type = type;
    }

    String type;
}
