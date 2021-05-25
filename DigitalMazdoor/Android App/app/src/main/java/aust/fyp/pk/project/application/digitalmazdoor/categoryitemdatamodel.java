package aust.fyp.pk.project.application.digitalmazdoor;

public class categoryitemdatamodel {

    String categoryname;
    int imagename;

    public String getCategoryname() {
        return categoryname;
    }

    public int getImagename() {
        return imagename;
    }

    public categoryitemdatamodel(String categoryname, int imagename) {
        this.categoryname = categoryname;
        this.imagename = imagename;
    }
}
