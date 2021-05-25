package aust.fyp.pk.project.application.digitalmazdoor;

public class GigsItemDataModel {

    private String title;
    private String category;
    private String description;
    private String price;
    private String deliverytime;
    private String profileimage;
    private String name;
    private String rating;
    private String orderscompleted;
    private String city;
    private String gigimage;
    private String dateofuploading;
    private String profilesummary;
    private String totalreviews;
    private String gigid;
    private  String fid;
    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getDeliverytime() {
        return deliverytime;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getOrderscompleted() {
        return orderscompleted;
    }

    public String getCity() {
        return city;
    }

    public String getGigimage() {
        return gigimage;
    }

    public String getDateofuploading() {
        return dateofuploading;
    }

    public String getProfilesummary() {
        return profilesummary;
    }

    public String getTotalreviews() {
        return totalreviews;
    }

    public String getGigid() {
        return gigid;
    }

    public String getFid() {
        return fid;
    }

    public GigsItemDataModel(String title, String category, String description, String price, String deliverytime, String profileimage, String name, String rating, String orderscompleted, String city, String gigimage, String dateofuploading, String profilesummary, String totalreviews, String gigid, String freeid) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.price = price;
        this.deliverytime = deliverytime;
        this.profileimage = profileimage;
        this.name = name;
        this.rating = rating;
        this.orderscompleted = orderscompleted;
        this.city = city;
        this.gigimage = gigimage;
        this.dateofuploading = dateofuploading;
        this.profilesummary = profilesummary;
        this.totalreviews = totalreviews;
        this.gigid = gigid;
        this.fid = freeid;
    }
}
