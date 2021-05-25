package aust.fyp.pk.project.application.digitalmazdoor;

public class OrderDataModel {
    String offerid;
    String buyername;
    String budget;
    String title;
    String date;
    String days;
    String buyerimage;
    String status;
    String buyerid;

    public String getOfferid() {
        return offerid;
    }

    public String getBuyername() {
        return buyername;
    }

    public String getBudget() {
        return budget;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDays() {
        return days;
    }

    public String getBuyerimage() {
        return buyerimage;
    }

    public String getStatus() {
        return status;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public String getFreeid() {
        return freeid;
    }

    public OrderDataModel(String offerid, String buyername, String budget, String title, String date, String days, String buyerimage, String status, String buyerid, String freeid) {
        this.offerid = offerid;
        this.buyername = buyername;
        this.budget = budget;
        this.title = title;
        this.date = date;
        this.days = days;
        this.buyerimage = buyerimage;
        this.status = status;
        this.buyerid = buyerid;
        this.freeid = freeid;
    }

    String freeid;
}
