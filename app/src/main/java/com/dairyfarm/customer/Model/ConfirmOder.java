package com.dairyfarm.customer.Model;

public class ConfirmOder {
    String CartID, ProductTitle, ProductName, AttributeName, MarketPrice, SellingPrice, SavingPrice, SavingPercentage, ProductQty, HasOffer, TotalProductPrice, ProductImagePath, ProductThumbPath, TotalMarketPrice, TotalSellingPrice, TotalSavingPercentage, OfferID;

    public ConfirmOder(String cartID, String productTitle, String productName, String attributeName, String marketPrice, String sellingPrice, String savingPrice, String savingPercentage, String productQty, String hasOffer, String totalProductPrice, String productImagePath, String productThumbPath, String totalMarketPrice, String totalSellingPrice, String totalSavingPercentage, String offerID) {
        CartID = cartID;
        ProductTitle = productTitle;
        ProductName = productName;
        AttributeName = attributeName;
        MarketPrice = marketPrice;
        SellingPrice = sellingPrice;
        SavingPrice = savingPrice;
        SavingPercentage = savingPercentage;
        ProductQty = productQty;
        HasOffer = hasOffer;
        TotalProductPrice = totalProductPrice;
        ProductImagePath = productImagePath;
        ProductThumbPath = productThumbPath;
        TotalMarketPrice = totalMarketPrice;
        TotalSellingPrice = totalSellingPrice;
        TotalSavingPercentage = totalSavingPercentage;
        OfferID = offerID;
    }

    public ConfirmOder() {
    }

    public String getCartID() {
        return CartID;
    }

    public void setCartID(String cartID) {
        CartID = cartID;
    }

    public String getProductTitle() {
        return ProductTitle;
    }

    public void setProductTitle(String productTitle) {
        ProductTitle = productTitle;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getAttributeName() {
        return AttributeName;
    }

    public void setAttributeName(String attributeName) {
        AttributeName = attributeName;
    }

    public String getMarketPrice() {
        return MarketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        MarketPrice = marketPrice;
    }

    public String getSellingPrice() {
        return SellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        SellingPrice = sellingPrice;
    }

    public String getSavingPrice() {
        return SavingPrice;
    }

    public void setSavingPrice(String savingPrice) {
        SavingPrice = savingPrice;
    }

    public String getSavingPercentage() {
        return SavingPercentage;
    }

    public void setSavingPercentage(String savingPercentage) {
        SavingPercentage = savingPercentage;
    }

    public String getProductQty() {
        return ProductQty;
    }

    public void setProductQty(String productQty) {
        ProductQty = productQty;
    }

    public String getHasOffer() {
        return HasOffer;
    }

    public void setHasOffer(String hasOffer) {
        HasOffer = hasOffer;
    }

    public String getTotalProductPrice() {
        return TotalProductPrice;
    }

    public void setTotalProductPrice(String totalProductPrice) {
        TotalProductPrice = totalProductPrice;
    }

    public String getProductImagePath() {
        return ProductImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        ProductImagePath = productImagePath;
    }

    public String getProductThumbPath() {
        return ProductThumbPath;
    }

    public void setProductThumbPath(String productThumbPath) {
        ProductThumbPath = productThumbPath;
    }

    public String getTotalMarketPrice() {
        return TotalMarketPrice;
    }

    public void setTotalMarketPrice(String totalMarketPrice) {
        TotalMarketPrice = totalMarketPrice;
    }

    public String getTotalSellingPrice() {
        return TotalSellingPrice;
    }

    public void setTotalSellingPrice(String totalSellingPrice) {
        TotalSellingPrice = totalSellingPrice;
    }

    public String getTotalSavingPercentage() {
        return TotalSavingPercentage;
    }

    public void setTotalSavingPercentage(String totalSavingPercentage) {
        TotalSavingPercentage = totalSavingPercentage;
    }

    public String getOfferID() {
        return OfferID;
    }

    public void setOfferID(String offerID) {
        OfferID = offerID;
    }
}
