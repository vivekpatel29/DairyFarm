package com.dairyfarm.customer.Model;

import java.io.Serializable;

public class ProductNotAvailableToPlaceOrder implements Serializable {
    String CartID, ProductID, ProductAttributeID, ProductName, ImageURL, AvailableString;

    public ProductNotAvailableToPlaceOrder(String cartID, String productID, String productAttributeID, String productName, String imageURL, String availableString) {
        CartID = cartID;
        ProductID = productID;
        ProductAttributeID = productAttributeID;
        ProductName = productName;
        ImageURL = imageURL;
        AvailableString = availableString;
    }

    public ProductNotAvailableToPlaceOrder() {
    }

    public String getCartID() {
        return CartID;
    }

    public void setCartID(String cartID) {
        CartID = cartID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductAttributeID() {
        return ProductAttributeID;
    }

    public void setProductAttributeID(String productAttributeID) {
        ProductAttributeID = productAttributeID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getAvailableString() {
        return AvailableString;
    }

    public void setAvailableString(String availableString) {
        AvailableString = availableString;
    }
}