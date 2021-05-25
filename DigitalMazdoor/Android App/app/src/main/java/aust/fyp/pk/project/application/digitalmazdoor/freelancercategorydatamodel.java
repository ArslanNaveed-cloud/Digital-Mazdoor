package aust.fyp.pk.project.application.digitalmazdoor;

public class freelancercategorydatamodel {
    String catname,value;
    int cat_icon;

    public String getCatname() {
        return catname;
    }

    public String getValue() {
        return value;
    }

    public int getCat_icon() {
        return cat_icon;
    }

    public freelancercategorydatamodel(String catname, String value, int cat_icon) {
        this.catname = catname;
        this.value = value;
        this.cat_icon = cat_icon;
    }
}
