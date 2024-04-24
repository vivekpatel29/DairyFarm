package com.dairyfarm.customer.Model;

public class Category {
    String CategoryID,CategoryName,CategoryImage;

    public Category(String categoryID, String categoryName, String categoryImage) {
        CategoryID = categoryID;
        CategoryName = categoryName;
        CategoryImage = categoryImage;
    }

    public Category() {
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }
}
