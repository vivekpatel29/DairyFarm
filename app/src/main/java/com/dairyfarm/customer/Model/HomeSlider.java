package com.dairyfarm.customer.Model;

public class HomeSlider {
    String SliderID, SliderTitle, SliderDescription, SliderType, VideoURL, ImageURL;


    public HomeSlider(String sliderID, String sliderTitle, String sliderDescription, String sliderType, String videoURL, String imageURL) {
        SliderID = sliderID;
        SliderTitle = sliderTitle;
        SliderDescription = sliderDescription;
        SliderType = sliderType;
        VideoURL = videoURL;
        ImageURL = imageURL;
    }

    public HomeSlider() {
    }

    public String getSliderID() {
        return SliderID;
    }

    public void setSliderID(String sliderID) {
        SliderID = sliderID;
    }

    public String getSliderTitle() {
        return SliderTitle;
    }

    public void setSliderTitle(String sliderTitle) {
        SliderTitle = sliderTitle;
    }

    public String getSliderDescription() {
        return SliderDescription;
    }

    public void setSliderDescription(String sliderDescription) {
        SliderDescription = sliderDescription;
    }

    public String getSliderType() {
        return SliderType;
    }

    public void setSliderType(String sliderType) {
        SliderType = sliderType;
    }

    public String getVideoURL() {
        return VideoURL;
    }

    public void setVideoURL(String videoURL) {
        VideoURL = videoURL;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
