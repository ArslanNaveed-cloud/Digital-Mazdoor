package aust.fyp.pk.project.application.digitalmazdoor;

public class SliderItem {
    private String description;
    private String imageUrl;

    public SliderItem(String description, String imageUrl) {
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }



    public String getImageUrl() {
        return imageUrl;
    }
}
