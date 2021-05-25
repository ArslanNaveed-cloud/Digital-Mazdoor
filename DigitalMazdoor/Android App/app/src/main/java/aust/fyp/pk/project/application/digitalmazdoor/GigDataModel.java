package aust.fyp.pk.project.application.digitalmazdoor;

public class GigDataModel {
    private String  gigtitle;
    private String gigcategory;
    private String gigprice;
    private String gigdeliverytime;
    private String gigimage;
    private String gigdescription;
    private String gigid;

    public String getGigtitle() {
        return gigtitle;
    }

    public String getGigcategory() {
        return gigcategory;
    }

    public String getGigprice() {
        return gigprice;
    }

    public String getGigdeliverytime() {
        return gigdeliverytime;
    }

    public String getGigimage() {
        return gigimage;
    }

    public String getGigdescription() {
        return gigdescription;
    }

    public String getGigid() {
        return gigid;
    }

    public String getDateofuploading() {
        return dateofuploading;
    }

    public GigDataModel(String gigtitle, String gigcategory, String gigprice, String gigdeliverytime, String gigimage, String gigdescription, String gigid, String dateofuploading) {
        this.gigtitle = gigtitle;
        this.gigcategory = gigcategory;
        this.gigprice = gigprice;
        this.gigdeliverytime = gigdeliverytime;
        this.gigimage = gigimage;
        this.gigdescription = gigdescription;
        this.gigid = gigid;
        this.dateofuploading = dateofuploading;
    }

    private String dateofuploading;
}
