package com.dairyfarm.customer.Model;

import java.util.ArrayList;

public class Products {
    String ProductID, ProductName, CategoryName, SubCategoryName, HasAttributes, ProductAttributeID, AttributeString, ManageStock, StockQty, ImagePath, ThumbPath, AddToCart, CartString, CartID, CartQty, WishListID, MarketPrice, SellingPrice, SavingPercentage, SavingPrice, ProductDetails, isActive, CartTotalQty, LastCartID, LastCartName, HasOffer, OfferString, OfferID, isFeatured, FeaturedString;
    ArrayList<String> listProductImages;
    ArrayList<ProductVariance> listProductVariance;

    public Products(String productID, String productName, String categoryName, String subCategoryName, String hasAttributes, String productAttributeID, String attributeString, String manageStock, String stockQty, String imagePath, String thumbPath, String addToCart, String cartString, String cartID, String cartQty, String wishListID, String marketPrice, String sellingPrice, String savingPercentage, String savingPrice, String productDetails, String isActive, String cartTotalQty, String lastCartID, String lastCartName, String hasOffer, String offerString, String offerID, String isFeatured, String featuredString, ArrayList<String> listProductImages, ArrayList<ProductVariance> listProductVariance) {
        ProductID = productID;
        ProductName = productName;
        CategoryName = categoryName;
        SubCategoryName = subCategoryName;
        HasAttributes = hasAttributes;
        ProductAttributeID = productAttributeID;
        AttributeString = attributeString;
        ManageStock = manageStock;
        StockQty = stockQty;
        ImagePath = imagePath;
        ThumbPath = thumbPath;
        AddToCart = addToCart;
        CartString = cartString;
        CartID = cartID;
        CartQty = cartQty;
        WishListID = wishListID;
        MarketPrice = marketPrice;
        SellingPrice = sellingPrice;
        SavingPercentage = savingPercentage;
        SavingPrice = savingPrice;
        ProductDetails = productDetails;
        this.isActive = isActive;
        CartTotalQty = cartTotalQty;
        LastCartID = lastCartID;
        LastCartName = lastCartName;
        HasOffer = hasOffer;
        OfferString = offerString;
        OfferID = offerID;
        this.isFeatured = isFeatured;
        FeaturedString = featuredString;
        this.listProductImages = listProductImages;
        this.listProductVariance = listProductVariance;
    }

    public Products() {
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public String getHasAttributes() {
        return HasAttributes;
    }

    public void setHasAttributes(String hasAttributes) {
        HasAttributes = hasAttributes;
    }

    public String getProductAttributeID() {
        return ProductAttributeID;
    }

    public void setProductAttributeID(String productAttributeID) {
        ProductAttributeID = productAttributeID;
    }

    public String getAttributeString() {
        return AttributeString;
    }

    public void setAttributeString(String attributeString) {
        AttributeString = attributeString;
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

    public String getWishListID() {
        return WishListID;
    }

    public void setWishListID(String wishListID) {
        WishListID = wishListID;
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

    public String getSavingPercentage() {
        return SavingPercentage;
    }

    public void setSavingPercentage(String savingPercentage) {
        SavingPercentage = savingPercentage;
    }

    public String getSavingPrice() {
        return SavingPrice;
    }

    public void setSavingPrice(String savingPrice) {
        SavingPrice = savingPrice;
    }

    public String getProductDetails() {
        return ProductDetails;
    }

    public void setProductDetails(String productDetails) {
        ProductDetails = productDetails;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCartTotalQty() {
        return CartTotalQty;
    }

    public void setCartTotalQty(String cartTotalQty) {
        CartTotalQty = cartTotalQty;
    }

    public String getLastCartID() {
        return LastCartID;
    }

    public void setLastCartID(String lastCartID) {
        LastCartID = lastCartID;
    }

    public String getLastCartName() {
        return LastCartName;
    }

    public void setLastCartName(String lastCartName) {
        LastCartName = lastCartName;
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

    public String getFeaturedString() {
        return FeaturedString;
    }

    public void setFeaturedString(String featuredString) {
        FeaturedString = featuredString;
    }

    public ArrayList<String> getListProductImages() {
        return listProductImages;
    }

    public void setListProductImages(ArrayList<String> listProductImages) {
        this.listProductImages = listProductImages;
    }

    public ArrayList<ProductVariance> getListProductVariance() {
        return listProductVariance;
    }

    public void setListProductVariance(ArrayList<ProductVariance> listProductVariance) {
        this.listProductVariance = listProductVariance;
    }
}


