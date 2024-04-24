package com.dairyfarm.customer.APIHelper;

public class JsonFields {
    public static final String AUTHORIZATION_KEY = "Authorization";

    public static final String FLAG = "flag";
    public static final String MESSAGE = "message";

    public static final String COMMON_REQUEST_PARAM_CURRENT_PAGE = "currentpage";
    public static final String COMMON_REQUEST_PARAM_CUSTOMER_ID = "cust_id";
    public static final String COMMON_REQUEST_PARAM_DEVICE_TYPE = "device_type";
    public static final String COMMON_REQUEST_PARAM_DEVICE_ID = "device_id";
    public static final String COMMON_REQUEST_PARAM_DEVICE_TOKEN = "device_token";
    public static final String COMMON_REQUEST_PARAM_DEVICE_OS_DETAILS = "device_os_details";
    public static final String COMMON_REQUEST_PARAM_DEVICE_IP_ADDRESS = "ip_address";
    public static final String COMMON_REQUEST_PARAM_DEVICE_MAC_ADDRESS = "mac_address";
    public static final String COMMON_REQUEST_PARAM_DEVICE_MODEL_DETAILS = "device_modal_details";
    public static final String COMMON_REQUEST_PARAM_APP_VERSION_DETAILS = "app_version_details";

    public static final String COMMON_LOGOUT_RESPONSE_TITLE = "logout_title";
    public static final String COMMON_LOGOUT_RESPONSE_MESSAGE = "logout_message";
    public static final String COMMON_LOGOUT_RESPONSE_ICON = "logout_icon";

    public static final String GET_DEVICE_OS_NAME_FROM_SDK_REQUEST_PARAMS_SDK_NUMBER = "sdk_number";
    public static final String GET_DEVICE_OS_NAME_FROM_SDK_RESPONSE_OS_NAME_FROM_SDK_NUMBER = "os_name";

    public static final String LOGIN_REQUEST_PARAM_CUSTOMER_EMAIL = "customer_email";
    public static final String LOGIN_REQUEST_PARAM_CUSTOMER_PASSWORD = "customer_password";

    public static final String LOGIN_RESPONSE_CUSTOMER_ID = "customer_id";
    public static final String LOGIN_RESPONSE_CUSTOMER_NAME = "customer_name";
    public static final String LOGIN_RESPONSE_CUSTOMER_EMAIL = "customer_email";
    public static final String LOGIN_RESPONSE_CUSTOMER_MOBILE = "customer_mobile";
    public static final String LOGIN_RESPONSE_CUSTOMER_PROFILE_PICTURE_URL = "customer_photo";

    public static final String SIGN_UP_REQUEST_PARAM_CUSTOMER_NAME = "customer_name";
    public static final String SIGN_UP_REQUEST_PARAM_CUSTOMER_GENDER = "customer_gender";
    public static final String SIGN_UP_REQUEST_PARAM_CUSTOMER_EMAIL = "customer_email";
    public static final String SIGN_UP_REQUEST_PARAM_CUSTOMER_MOBILE = "customer_mobile";
    public static final String SIGN_UP_REQUEST_PARAM_CUSTOMER_PASSWORD = "customer_password";

    public static final String REQUEST_FORGOT_PASSWORD_EMAIL_OTP_REQUEST_PARAMS_CUSTOMER_EMAIL = "customer_email";

    public static final String VERIFY_FORGOT_PASSWORD_EMAIL_OTP_REQUEST_PARAMS_CUSTOMER_EMAIL = "customer_email";
    public static final String VERIFY_FORGOT_PASSWORD_EMAIL_OTP_REQUEST_PARAMS_EMAIL_OTP = "otp";

    public static final String RESET_PASSWORD_REQUEST_PARAMS_CUSTOMER_EMAIL = "customer_email";
    public static final String RESET_PASSWORD_REQUEST_PARAMS_NEW_PASSWORD = "npass";
    public static final String RESET_PASSWORD_REQUEST_PARAMS_NEW_CONFIRM_PASSWORD = "cpass";


    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_AVAILABLE = "home_content_available";
    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_ONE_AVAILABLE = "home_content_one_available";
    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_ONE_TITLE = "home_content_one_title";

    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_ONE_SLIDER_ARRAY = "home_content_one_slider_list";
    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_ONE_SLIDER_ID = "slider_id";
    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_ONE_SLIDER_IMAGE = "slider_image";

    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_TWO_AVAILABLE = "home_content_two_available";
    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_TWO_TITLE = "home_content_two_title";
    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_TWO_CONTENT = "home_content_two_content";

    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_THREE_AVAILABLE = "home_content_three_available";
    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_THREE_TITLE = "home_content_three_title";

    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_THREE_CATEGORY_ARRAY = "home_content_three_category_list";
    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_THREE_CATEGORY_ID = "category_id";
    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_THREE_CATEGORY_NAME = "category_name";
    public static final String DASHBOARD_RESPONSE_HOME_CONTENT_THREE_CATEGORY_IMAGE = "category_image";

    public static final String CATEGORY_REQUEST_PARAMS_CATEGORY_SEARCH_QUERY = "category_name";

    public static final String CATEGORY_RESPONSE_CATEGORY_ARRAY = "category_list";
    public static final String CATEGORY_RESPONSE_CATEGORY_ID = "category_id";
    public static final String CATEGORY_RESPONSE_CATEGORY_NAME = "category_name";
    public static final String CATEGORY_RESPONSE_CATEGORY_IMAGE = "category_image";

    public static final String SUB_CATEGORY_REQUEST_PARAMS_SUB_CATEGORY_SEARCH_QUERY = "sub_category_name";
    public static final String SUB_CATEGORY_REQUEST_PARAMS_CATEGORY_ID = "category_id";

    public static final String SUB_CATEGORY_RESPONSE_SUB_CATEGORY_ARRAY = "subcategory_list";
    public static final String SUB_CATEGORY_RESPONSE_SUB_CATEGORY_ID = "sub_category_id";
    public static final String SUB_CATEGORY_RESPONSE_SUB_CATEGORY_NAME = "sub_category_name";
    public static final String SUB_CATEGORY_RESPONSE_SUB_CATEGORY_IMAGE = "sub_category_image";

    public static final String PRODUCTS_REQUEST_PARAMS_PRODUCTS_SEARCH_QUERY = "pro_name";
    public static final String PRODUCTS_REQUEST_PARAMS_SUB_CATEGORY_ID = "sub_category_id";

    public static final String PRODUCT_DETAILS_REQUEST_PARAMS_PRODUCT_ID = "pro_id";
    public static final String PRODUCT_DETAILS_REQUEST_PARAMS_OFFER_ID = "offer_id";

    public static final String PRODUCTS_RESPONSE_PRODUCT_ARRAY = "product";
    public static final String PRODUCTS_RESPONSE_PRODUCT_ID = "pro_id";
    public static final String PRODUCTS_RESPONSE_PRODUCT_NAME = "pro_name";
    public static final String PRODUCTS_RESPONSE_PRODUCT_CATEGORY_NAME = "cat_name";
    public static final String PRODUCTS_RESPONSE_PRODUCT_SUB_CATEGORY_NAME = "sub_cat_name";
    public static final String PRODUCTS_RESPONSE_PRODUCT_HAS_ATTRIBUTES = "has_attributes";
    public static final String PRODUCTS_RESPONSE_PRODUCT_ATTRIBUTE_ID = "pro_atr_id";
    public static final String PRODUCTS_RESPONSE_PRODUCT_ATTRIBUTE_STRING = "atr_string";
    public static final String PRODUCTS_RESPONSE_PRODUCT_MANAGE_STOCK = "manage_stock";
    public static final String PRODUCTS_RESPONSE_PRODUCT_STOCK_QTY = "stock_qty";
    public static final String PRODUCTS_RESPONSE_PRODUCT_IMAGE_PATH = "img_path";
    public static final String PRODUCTS_RESPONSE_PRODUCT_THUMB_PATH = "thumb_path";
    public static final String PRODUCTS_RESPONSE_PRODUCT_IMAGES_ARRAY = "images";
    public static final String PRODUCTS_RESPONSE_PRODUCT_IMAGE = "image";
    public static final String PRODUCTS_RESPONSE_PRODUCT_ADD_TO_CART = "add_to_cart";
    public static final String PRODUCTS_RESPONSE_PRODUCT_CART_STRING = "cart_string";
    public static final String PRODUCTS_RESPONSE_PRODUCT_CART_ID = "cart_id";
    public static final String PRODUCTS_RESPONSE_PRODUCT_CART_QTY = "cart_qty";
    public static final String PRODUCTS_RESPONSE_PRODUCT_WISHLIST_ID = "wishlist_id";
    public static final String PRODUCTS_RESPONSE_CART_TOTAL_QTY = "cart_total_qty";
    public static final String PRODUCTS_RESPONSE_PRODUCT_MARKET_PRICE = "market_price";
    public static final String PRODUCTS_RESPONSE_PRODUCT_SELLING_PRICE = "selling_price";
    public static final String PRODUCTS_RESPONSE_PRODUCT_SAVING_PERCENTAGE = "saving_per";
    public static final String PRODUCTS_RESPONSE_PRODUCT_SAVING_PRICE = "saving_price";
    public static final String PRODUCTS_RESPONSE_PRODUCT_DETAILS = "pro_details";
    public static final String PRODUCTS_RESPONSE_PRODUCT_IS_ACTIVE = "is_active";
    public static final String PRODUCTS_RESPONSE_CART_LAST_ID = "last_cart_id";
    public static final String PRODUCTS_RESPONSE_CART_LAST_NAME = "last_cart_name";
    public static final String PRODUCTS_RESPONSE_HAS_OFFER = "has_offer";
    public static final String PRODUCTS_RESPONSE_OFFER_ID = "offer_id";
    public static final String PRODUCTS_RESPONSE_OFFER_STRING = "offer_string";
    public static final String PRODUCTS_RESPONSE_IS_FEATURED = "is_featured";
    public static final String PRODUCTS_RESPONSE_FEATURED_STRING = "featured_string";
    public static final String PRODUCTS_RESPONSE_SHARE_LINK = "share_link";

    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_ARRAY = "attributes";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_ID = "pro_atr_id";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_STRING = "atr_string";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_GROUP_ID = "atr_grp_id";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_GROUP_NAME = "atr_grp_name";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_VAL_ID = "atr_val_id";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_VAL_NAME = "atr_val_name";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_MARKET_PRICE = "market_price";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_SELLING_PRICE = "selling_price";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_ADD_TO_CART = "add_to_cart";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_CART_STRING = "cart_string";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IS_ACTIVE = "is_active";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_CART_ID = "cart_id";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_CART_QTY = "cart_qty";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_MANAGE_STOCK = "manage_stock";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_STOCK_QTY = "stock_qty";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_WISHLIST_ID = "wishlist_id";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_SAVING_PRICE = "saving_price";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_SAVING_PERCENTAGE = "saving_per";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IMAGE_PATH = "img_path";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_THUMB_PATH = "thumb_path";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IMAGES_ARRAY = "images";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IMAGE = "image";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_HAS_OFFER = "has_offer";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_OFFER_STRING = "offer_string";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_OFFER_ID = "offer_id";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_SHARE_LINK = "share_link";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_IS_FEATURED = "is_featured";
    public static final String PRODUCTS_ATTRIBUTE_RESPONSE_PRODUCT_ATTRIBUTE_FEATURE_STRING = "featured_string";

    public static final String ADD_TO_CART_REQUEST_PARAMS_PRODUCT_ID = "pro_id";
    public static final String ADD_TO_CART_REQUEST_PARAMS_OFFER_ID = "offer_id";
    public static final String ADD_TO_CART_REQUEST_PARAMS_HAS_ATTRIBUTES = "has_attributes";
    public static final String ADD_TO_CART_REQUEST_PARAMS_PRODUCT_ATTRIBUTE_ID = "pro_atr_id";

    public static final String CHANGE_CART_REQUEST_PARAMS_CART_ID = "cart_id";
    public static final String CHANGE_CART_REQUEST_PARAMS_CART_OFFER_ID = "offer_id";
    public static final String CHANGE_CART_REQUEST_PARAMS_OFFER_ID = "offer_id";
    public static final String CHANGE_CART_REQUEST_PARAMS_STATUS = "status";

    public static final String CHANGE_WISHLIST_REQUEST_PARAM_PRODUCT_ID = "pro_id";
    public static final String CHANGE_WISHLIST_REQUEST_PARAM_HAS_ATTRIBUTES = "has_attributes";
    public static final String CHANGE_WISHLIST_REQUEST_PARAM_PRODUCT_ATTRIBUTE_ID = "pro_atr_id";
    public static final String CHANGE_WISHLIST_REQUEST_PARAM_STATUS = "status";
    public static final String CHANGE_WISHLIST_REQUEST_PARAM_WISHLIST_ID = "wishlist_id";
    public static final String CHANGE_WISHLIST_REQUEST_PARAM_OFFER_ID = "offer_id";

    public static final String REMOVE_CART_REQUEST_PARAMS_CART_ID = "cart_id";


    public static final String CART_RESPONSE_CART_ARRAY = "cart";
    public static final String CART_RESPONSE_CART_ID = "cart_id";

    public static final String CART_RESPONSE_PRODUCT_TITLE = "pro_title";
    public static final String CART_RESPONSE_PRODUCT_NAME = "pro_name";
    public static final String CART_RESPONSE_ATTRIBUTE_NAME = "atr_name";
    public static final String CART_RESPONSE_PRODUCT_MARKET_PRICE = "market_price";
    public static final String CART_RESPONSE_PRODUCT_SELLING_PRICE = "selling_price";
    public static final String CART_RESPONSE_PRODUCT_SAVING_PRICE = "saving_price";
    public static final String CART_RESPONSE_PRODUCT_SAVING_PERCENTAGE = "saving_per";
    public static final String CART_RESPONSE_PRODUCT_QTY = "pro_qty";
    public static final String CART_RESPONSE_PRODUCT_HAS_OFFER = "has_offer";
    public static final String CART_RESPONSE_PRODUCT_TOTAL = "total";
    public static final String CART_RESPONSE_PRODUCT_IMAGE_PATH = "img_path";
    public static final String CART_RESPONSE_PRODUCT_THUMB_PATH = "thumb_path";
    public static final String CART_RESPONSE_SUB_TOTAL = "sub_total";
    public static final String CART_RESPONSE_TOTAL_SAVINGS = "total_saving";
    public static final String CART_RESPONSE_DELIVERY_CHARGE = "delivery_charge";
    public static final String CART_RESPONSE_GRAND_TOTAL = "grand_total";
    public static final String CART_RESPONSE_TOTAL_QTY = "total_qty";
    public static final String CART_RESPONSE_CAN_PROCEED = "can_proceed";
    public static final String CART_RESPONSE_HAS_ADDRESS = "has_address";
    public static final String CART_RESPONSE_COUPON_STRING = "coupon_string";
    public static final String CART_RESPONSE_HAS_TIME_SLOT = "has_timeslot";
    public static final String CART_RESPONSE_TOTAL_MARKET_PRICE = "total_market_price";
    public static final String CART_RESPONSE_TOTAL_SELLING_PRICE = "total_selling_price";
    public static final String CART_RESPONSE_TOTAL_SAVING_PERCENTAGE = "total_saving_percentage";
    public static final String CART_RESPONSE_TOTAL_OFFER_ID = "offer_id";
    public static final String CART_RESPONSE_TOTAL_ITEM_QTY_TEXT = "total_item_qty_text";
    public static final String CART_RESPONSE_IS_COUPON_APPLIED = "is_coupon_applied";
    public static final String CART_RESPONSE_COUPON_CODE = "coupon_code";
    public static final String CART_RESPONSE_PAYMENT_METHOD = "payment_method";
    public static final String CART_RESPONSE_PAYMENT_METHOD_ID = "payment_method_id";
    public static final String CART_RESPONSE_IS_PAYMENT_METHOD_SELECTED = "is_payment_method_selected";
    public static final String CART_RESPONSE_SUBMIT_BUTTON_TEXT = "submit_button_text";
    public static final String CART_RESPONSE_IS_ORDER_PAYMENT_ONLINE = "is_order_payment_online";

    public static final String CART_RESPONSE_HAS_WALLET_PAYMENT_AVAILABLE = "has_payment_wallet_available";
    public static final String CART_RESPONSE_WALLET_AMOUNT = "wallet_amount";
    public static final String CART_RESPONSE_WALLET_DESCRIPTION = "wallet_description";
    public static final String CART_RESPONSE_WALLET_IS_DEFAULT = "is_wallet_selected";

    public static final String CART_RESPONSE_ADDRESS_ARRAY = "address";
    public static final String CART_RESPONSE_ADDRESS_ID = "address_id";
    public static final String CART_RESPONSE_ADDRESS_NAME = "name";
    public static final String CART_RESPONSE_ADDRESS_LINE = "address";
    public static final String CART_RESPONSE_MOBILE_NUMBER = "mobile_number";
    public static final String CART_RESPONSE_ADDRESS_TYPE = "address_type";
    public static final String CART_RESPONSE_ADDRESS_LATITUDE = "address_latitude";
    public static final String CART_RESPONSE_ADDRESS_LONGITUDE = "address_longitude";

    public static final String CART_RESPONSE_TIME_SLOT_ARRAY = "timeslot";
    public static final String CART_RESPONSE_TIME_SLOT_ID = "timeslot_id";
    public static final String CART_RESPONSE_TIME_SLOT_TITLE = "timeslot_title";
    public static final String CART_RESPONSE_TIME_SLOT_MESSAGE = "timeslot_message";

    public static final String CART_RESPONSE_CART_BILL_DETAILS_ARRAY = "cart_amount_details";
    public static final String CART_RESPONSE_ADDRESS_BILL_DETAILS_TITLE = "title";
    public static final String CART_RESPONSE_ADDRESS_BILL_DETAILS_VALUE = "value";

    public static final String CHANGE_CART_REQUEST_PARAMS_ADDRESS_ID = "address_id";

    public static final String TIME_SLOT_NOT_VALID_TIME_SLOT_ID = "timeslot_id";

    public static final String ORDER_AMOUNT_NOT_VALID_TITLE = "title";
    public static final String ORDER_AMOUNT_NOT_VALID_DESCRIPTION = "description";

    public static final String PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_ARRAY = "product";
    public static final String PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_CART_ID = "cart_id";
    public static final String PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_NAME = "pro_name";
    public static final String PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_ID = "pro_id";
    public static final String PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_ATR = "pro_atr_id";
    public static final String PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_IMAGE_PATH = "img_path";
    public static final String PRODUCTS_NOT_AVAILABLE_FOR_SALE_RESPONSE_PRODUCT_AVAILABLE_STRING = "available_string";

    public static final String PLACE_ORDER_REQUEST_PARAMS_PAYMENT_METHOD_ID = "payment_method_id";
    public static final String PLACE_ORDER_REQUEST_PARAMS_PAYMENT_METHOD = "payment_method";
    public static final String PLACE_ORDER_REQUEST_PARAMS_ADDRESS_ID = "address_id";
    public static final String PLACE_ORDER_REQUEST_PARAMS_TIME_SLOT_ID = "time_slot_id";

    public static final String APPLY_COUPON_REQUEST_PARAMS_COUPON_ID = "coupon_id";
    public static final String APPLY_COUPON_REQUEST_PARAMS_COUPON_CODE = "coupon_code";

    public static final String APPLY_COUPON_RESPONSE_MESSAGE = "message";

    public static final String ORDER_PAYMENT_PROCESS_ACCESS_CODE = "access_code";
    public static final String ORDER_PAYMENT_PROCESS_MERCHANT_ID = "merchant_id";
    public static final String ORDER_PAYMENT_PROCESS_ORDER_ID = "order_id";
    public static final String ORDER_PAYMENT_PROCESS_REDIRECT_URL = "redirect_url";
    public static final String ORDER_PAYMENT_PROCESS_CANCEL_URL = "cancel_url";
    public static final String ORDER_PAYMENT_PROCESS_RSA_KEY_URL = "rsa_key_url";
    public static final String ORDER_PAYMENT_PROCESS_TRANSACTION_URL = "transaction_url";
    public static final String ORDER_PAYMENT_PROCESS_PAYMENT_DETAILS_ID = "payment_detail_id";
    public static final String ORDER_PAYMENT_PROCESS_PAYMENT_CURRENCY = "currency";
    public static final String ORDER_PAYMENT_PROCESS_PAYMENT_AMOUNT = "amount";
    public static final String ORDER_PAYMENT_PROCESS_BILLING_COUNTRY = "billing_country";
    public static final String ORDER_PAYMENT_PROCESS_BILLING_TELEPHONE = "billing_tel";
    public static final String ORDER_PAYMENT_PROCESS_BILLING_EMAIL = "billing_email";

    public static final String SELECT_PAYMENT_METHOD_POST_PARAMS_PAYMENT_METHOD_ID = "payment_method_id";

    public static final String PAYMENTS_RESPONSE_PAYMENT_METHOD_ARRAY = "payment_method";
    public static final String PAYMENTS_RESPONSE_PAYMENT_METHOD_ID = "payment_method_id";
    public static final String PAYMENTS_RESPONSE_PAYMENT_METHOD_NAME = "payment_method_title";
    public static final String PAYMENTS_RESPONSE_PAYMENT_METHOD_IMAGE_URL = "payment_method_icon";
    public static final String PAYMENTS_RESPONSE_PAYMENT_METHOD_MESSAGE = "payment_message";
    public static final String PAYMENTS_RESPONSE_PAYMENT_METHOD_IS_DEFAULT = "is_default";
    public static final String PAYMENTS_RESPONSE_PAYMENT_METHOD_SELECTED_PAYMENT_METHOD_ID = "selected_payment_method_id";
    public static final String PAYMENTS_RESPONSE_PAYMENT_METHOD_CAN_PROCEED = "proceed_payment_method";


    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_DATES_ARRAY = "slotdates";
    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_DATES_ID = "timeslot_date_id";
    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_DATES_DATE = "date";
    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_DATES_DAY_NAME = "dayname";
    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_DATES_MONTH_NAME = "monthname";
    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_DATES_MONTH_NO = "monthno";
    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_DATES_YEAR = "year";
    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_DATES_IS_DEFAULT = "is_default";

    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_TIMES_ARRAY = "slotstime";
    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_TIMES_SLOT_TIME_ID = "timeslot_time_id";
    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_TIMES_TIME_SLOT_STRING = "timeslot";
    public static final String TIME_SLOT_RESPONSE_TIME_SLOT_TIMES_IS_DEFAULT = "is_default";

    public static final String SELECT_TIME_SLOT_REQUEST_PARAMS_TIME_SLOT_ID = "timeslot_id";

    public static final String ADDRESS_RESPONSE_ADDRESS_ARRAY = "address";
    public static final String ADDRESS_RESPONSE_ADDRESS_ID = "address_id";
    public static final String ADDRESS_RESPONSE_ADDRESS_NAME = "name";
    public static final String ADDRESS_RESPONSE_ADDRESS_LINE = "address";
    public static final String ADDRESS_RESPONSE_ADDRESS_IS_DEFAULT = "is_default";
    public static final String ADDRESS_RESPONSE_ADDRESS_MOBILE_NUMBER = "mobile_number";
    public static final String ADDRESS_RESPONSE_ADDRESS_IS_AVAILABLE = "is_available";
    public static final String ADDRESS_RESPONSE_ADDRESS_AVAILABLE_STRING = "available_string";
    public static final String ADDRESS_RESPONSE_ADDRESS_TYPE = "address_type";
    public static final String ADDRESS_RESPONSE_ADDRESS_LATITUDE = "latitude";
    public static final String ADDRESS_RESPONSE_ADDRESS_LONGITUDE = "longitude";

    public static final String OFFER_COUPON_RESPONSE_COUPON_ARRAY = "coupon";
    public static final String OFFER_COUPON_RESPONSE_COUPON_ID = "coupon_id";
    public static final String OFFER_COUPON_RESPONSE_COUPON_CODE = "coupon_code";
    public static final String OFFER_COUPON_RESPONSE_COUPON_TITLE = "coupon_title";
    public static final String OFFER_COUPON_RESPONSE_COUPON_DESCRIPTION = "coupon_description";
    public static final String OFFER_COUPON_RESPONSE_COUPON_MIN_AMOUNT = "min_amt";
    public static final String OFFER_COUPON_RESPONSE_COUPON_DISCOUNT = "discount";
    public static final String OFFER_COUPON_RESPONSE_COUPON_TYPE = "coupon_type";
    public static final String OFFER_COUPON_RESPONSE_COUPON_USAGE_PER_PERSON = "usage_per_user";
    public static final String OFFER_COUPON_RESPONSE_COUPON_END_DATE = "coupon_end_date";
    public static final String OFFER_COUPON_RESPONSE_COUPON_MAX_DISCOUNT_AMOUNT = "max_discount_amt";


    public static final String AREA_REQUEST_PARAMS_SEARCH_QUERY = "term";

    public static final String AREA_RESPONSE_AREA_ARRAY = "area_list";
    public static final String AREA_RESPONSE_PINCODE_ID = "pincode_id";
    public static final String AREA_RESPONSE_PINCODE = "pincode";
    public static final String AREA_RESPONSE_AREA = "area";
    public static final String AREA_RESPONSE_CITY = "city";
    public static final String AREA_RESPONSE_STATE = "state";


    public static final String ADD_ADDRESS_REQUEST_PARAMS_ADDRESS_TYPE = "address_type";
    public static final String ADD_ADDRESS_REQUEST_PARAMS_ADDRESS_LATITUDE = "latitude";
    public static final String ADD_ADDRESS_REQUEST_PARAMS_ADDRESS_LONGITUDE = "longitude";
    public static final String ADD_ADDRESS_REQUEST_PARAMS_ADDRESS_NAME = "name";
    public static final String ADD_ADDRESS_REQUEST_PARAMS_ADDRESS = "address";
    public static final String ADD_ADDRESS_REQUEST_PARAMS_LOCALITY = "locality";
    public static final String ADD_ADDRESS_REQUEST_PARAMS_PINCODE_ID = "pincode_id";
    public static final String ADD_ADDRESS_REQUEST_PARAMS_CITY = "city";
    public static final String ADD_ADDRESS_REQUEST_PARAMS_STATE = "state";
    public static final String ADD_ADDRESS_REQUEST_PARAMS_MOBILE_NUMBER = "mobile_number";
    public static final String ADD_ADDRESS_REQUEST_PARAMS_IS_DEFAULT = "is_default";

    public static final String PINCODE_RESPONSE_PINCODE_ARRAY = "pincode";
    public static final String PINCODE_RESPONSE_PINCODE_ID = "pincode_id";
    public static final String PINCODE_RESPONSE_PINCODE_TITLE = "title";
    public static final String PINCODE_RESPONSE_PINCODE = "pincode";
    public static final String PINCODE_RESPONSE_AREA = "area";
    public static final String PINCODE_RESPONSE_CITY = "city";
    public static final String PINCODE_RESPONSE_STATE = "state";

    public static final String ADDRESS_DETAILS_REQUEST_PARAMS_ADDRESS_ID = "address_id";

    public static final String ADDRESS_DETAILS_RESPONSE_ADDRESS_ARRAY = "address";
    public static final String ADDRESS_DETAILS_RESPONSE_ADDRESS_ID = "address_id";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_NAME = "name";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_ADDRESS = "address";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_ADDRESS_DETAILS = "locality";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_CITY = "city";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_STATE = "state";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_PINCODE_ID = "pincode_id";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_PINCODE = "pincode";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_IS_DEFAULT = "is_default";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_MOBILE_NUMBER = "mobile_number";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_ADDRESS_TYPE = "address_type";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_ADDRESS_LATITUDE = "latitude";
    public static final String ADDRESS_DETAILS_RESPONSE_PARAMS_ADDRESS_LONGITUDE = "longitude";

    public static final String EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS_ID = "address_id";
    public static final String EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS_NAME = "name";
    public static final String EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS = "address";
    public static final String EDIT_ADDRESS_REQUEST_PARAMS_LOCALITY = "locality";
    public static final String EDIT_ADDRESS_REQUEST_PARAMS_PINCODE_ID = "pincode_id";
    public static final String EDIT_ADDRESS_REQUEST_PARAMS_CITY = "city";
    public static final String EDIT_ADDRESS_REQUEST_PARAMS_STATE = "state";
    public static final String EDIT_ADDRESS_REQUEST_PARAMS_MOBILE_NUMBER = "mobile_number";
    public static final String EDIT_ADDRESS_REQUEST_PARAMS_IS_DEFAULT = "is_default";
    public static final String EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS_TYPE = "address_type";
    public static final String EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS_LATITUDE = "latitude";
    public static final String EDIT_ADDRESS_REQUEST_PARAMS_ADDRESS_LONGITUDE = "longitude";

    public static final String DELETE_ADDRESS_REQUEST_PARAMS_ADDRESS_ID = "address_id";

}