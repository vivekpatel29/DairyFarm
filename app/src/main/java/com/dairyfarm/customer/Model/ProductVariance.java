package com.dairyfarm.customer.Model;

import java.util.ArrayList;

public class ProductVariance {
    String AttributeID, AttributeString, AttributeGroupID, AtttributeGroupName, AttributeValueID, AttributeValueName, MarketPrice, SellingPrice, AddToCart, CartString, isActive, CartID, CartQty, ManageStock, StockQty, WishlistID, SavingPrice, SavingPercentage, ImagePath, ThumbPath, HasOffer, OfferString, OfferID, isFeatured, FeatureString;
    ArrayList<String> listImages;

    public ProductVariance(String attributeID, String attributeString, String attributeGroupID, String atttributeGroupName, String attributeValueID, String attributeValueName, String marketPrice, String sellingPrice, String addToCart, String cartString, String isActive, String cartID, String cartQty, String manageStock, String stockQty, String wishlistID, String savingPrice, String savingPercentage, String imagePath, String thumbPath, String hasOffer, String offerString, String offerID, String isFeatured, String featureString, ArrayList<String> listImages) {
        AttributeID = attributeID;
        AttributeString = attributeString;
        AttributeGroupID = attributeGroupID;
        AtttributeGroupName = atttributeGroupName;
        AttributeValueID = attributeValueID;
        AttributeValueName = attributeValueName;
        MarketPrice = marketPrice;
        SellingPrice = sellingPrice;
        AddToCart = addToCart;
        CartString = cartString;
        this.isActive = isActive;
        CartID = cartID;
        CartQty = cartQty;
        ManageStock = manageStock;
        StockQty = stockQty;
        WishlistID = wishlistID;
        SavingPrice = savingPrice;
        SavingPercentage = savingPercentage;
        ImagePath = imagePath;
        ThumbPath = thumbPath;
        HasOffer = hasOffer;
        OfferString = offerString;
        OfferID = offerID;
        this.isFeatured = isFeatured;
        FeatureString = featureString;
        this.listImages = listImages;
    }

    public ProductVariance() {
    }

    public String getAttributeID() {
        return AttributeID;
    }

    public void setAttributeID(String attributeID) {
        AttributeID = attributeID;
    }

    public String getAttributeString() {
        return AttributeString;
    }

    public void setAttributeString(String attributeString) {
        AttributeString = attributeString;
    }

    public String getAttributeGroupID() {
        return AttributeGroupID;
    }

    public void setAttributeGroupID(String attributeGroupID) {
        AttributeGroupID = attributeGroupID;
    }

    public String getAtttributeGroupName() {
        return AtttributeGroupName;
    }

    public void setAtttributeGroupName(String atttributeGroupName) {
        AtttributeGroupName = atttributeGroupName;
    }

    public String getAttributeValueID() {
        return AttributeValueID;
    }

    public void setAttributeValueID(String attributeValueID) {
        AttributeValueID = attributeValueID;
    }

    public String getAttributeValueName() {
        return AttributeValueName;
    }

    public void setAttributeValueName(String attributeValueName) {
        AttributeValueName = attributeValueName;
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

    public String getAddToCart() {
        return AddToCart;
    }

    public void setAddToCart(String addToCart) {
        AddToCart = addToCart;
    }

    public String getCartString() {
        return CartString;
    }

    public void setCartString(String cartString) {
        CartString = cartString;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCartID() {
        return CartID;
    }

    public void setCartID(String cartID) {
        CartID = cartID;
    }

    public String getCartQty() {
        return CartQty;
    }

    public void setCartQty(String cartQty) {
        CartQty = cartQty;
    }

    public String getManageStock() {
        return ManageStock;
    }

    public void setManageStock(String manageStock) {
        ManageStock = manageStock;
    }

    public String getStockQty() {
        return StockQty;
    }

    public void setStockQty(String stockQty) {
        StockQty = stockQty;
    }

    public String getWishlistID() {
        return WishlistID;
    }

    public void setWishlistID(String wishlistID) {
        WishlistID = wishlistID;
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

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getThumbPath() {
        return ThumbPath;
    }

    public void setThumbPath(String thumbPath) {
        ThumbPath = thumbPath;
    }

    public String getHasOffer() {
        return HasOffer;
    }

    public void setHasOffer(String hasOffer) {
        HasOffer = hasOffer;
    }

    public String getOfferString() {
        return OfferString;
    }

    public void setOfferString(String offerString) {
        OfferString = offerString;
    }

    public String getOfferID() {
        return OfferID;
    }

    public void setOfferID(String offerID) {
        OfferID = offerID;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public String getFeatureString() {
        return FeatureString;
    }

    public void setFeatureString(String featureString) {
        FeatureString = featureString;
    }

    public ArrayList<String> getListImages() {
        return listImages;
    }

    public void setListImages(ArrayList<String> listImages) {
        this.listImages = listImages;
    }
}

