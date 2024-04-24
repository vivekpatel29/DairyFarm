package com.dairyfarm.customer.Model;

import java.io.Serializable;

public class ProductDetailsImage implements Serializable {
    String ProductImageURL;

    public ProductDetailsImage(String productImageURL) {
        ProductImageURL = productImageURL;
    }

    public ProductDetailsImage() {
    }

    public String getProductImageURL() {
        return ProductImageURL;
    }

    public void setProductImageURL(String productImageURL) {
        ProductImageURL = productImageURL;
    }
}
