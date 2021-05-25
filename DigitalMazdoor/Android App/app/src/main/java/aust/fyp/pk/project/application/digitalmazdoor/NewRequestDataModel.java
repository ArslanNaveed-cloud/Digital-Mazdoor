package aust.fyp.pk.project.application.digitalmazdoor;

public class NewRequestDataModel {
    private String title;
    private String description;
    private String category;
    private String budget;
    private String days1;
    private String buyerimage;
    private String postid;
    private String buyerid;
    private String buyername;
    private String date;

    public String getStatus() {
        return status;
    }

    private String status;
    int bid;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getBudget() {
        return budget;
    }

    public String getDays1() {
        return days1;
    }

    public String getBuyerimage() {
        return buyerimage;
    }

    public String getPostid() {
        return postid;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public String getBuyername() {
        return buyername;
    }

    public String getDate() {
        return date;
    }

    public int getBid() {
        return bid;
    }

    public NewRequestDataModel(String title, String description, String category, String budget, String days1, String buyerimage, String postid, String buyerid, String buyername, String date, int bid,String status) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.budget = budget;
        this.days1 = days1;
        this.buyerimage = buyerimage;
        this.postid = postid;
        this.buyerid = buyerid;
        this.buyername = buyername;
        this.date = date;
        this.bid = bid;
        this.status=status;
    }
}
