package aust.fyp.pk.project.application.digitalmazdoor;

public class MyOrderDataModel {
    String offerid;
    String buyername;
    String buyerid;
    String buyerimage;

    String freelancerid;
    String freelancername;
    String freelancerimage;

    String budget;
    String title;
    private String category;
    private String days1;
    private String date;

    public String getOfferid() {
        return offerid;
    }


    String days;

    public String getBuyername() {
        return buyername;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public String getBuyerimage() {
        return buyerimage;
    }

    public String getFreelancerid() {
        return freelancerid;
    }

    public String getFreelancername() {
        return freelancername;
    }

    public String getFreelancerimage() {
        return freelancerimage;
    }

    public String getBudget() {
        return budget;
    }

    public String getTitle() {
        return title;
    }


    public String getCategory() {
        return category;
    }

    public String getDays1() {
        return days1;
    }

    public String getDate() {
        return date;
    }

    public String getDays() {
        return days;
    }

    public String getStatus() {
        return status;
    }

    String status;

    public MyOrderDataModel(String offerid, String buyername, String buyerid, String buyerimage, String freelancerid, String freelancername, String freelancerimage, String budget, String title, String category, String days1, String date, String days, String status) {
        this.offerid = offerid;
        this.buyername = buyername;
        this.buyerid = buyerid;
        this.buyerimage = buyerimage;
        this.freelancerid = freelancerid;
        this.freelancername = freelancername;
        this.freelancerimage = freelancerimage;
        this.budget = budget;
        this.title = title;

        this.category = category;
        this.days1 = days1;
        this.date = date;
        this.days = days;
        this.status = status;
    }
}

