package com.dairyfarm.customer.Model;

public class SubCategories {
    String SubCategoryID, SubCategoryName, SubCategoryImage;

    public SubCategories(String subCategoryID, String subCategoryName, String subCategoryImage) {
        SubCategoryID = subCategoryID;
        SubCategoryName = subCategoryName;
        SubCategoryImage = subCategoryImage;
    }

    public SubCategories() {
    }

    public String getSubCategoryID() {
        return SubCategoryID;
    }

    public void setSubCategoryID(String subCategoryID) {
        SubCategoryID = subCategoryID;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public String getSubCategoryImage() {
        return SubCategoryImage;
    }

    public void setSubCategoryImage(String subCategoryImage) {
        SubCategoryImage = subCategoryImage;
    }
}
