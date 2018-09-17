package viroopa.com.medikart.app;

public class AppConfig {
//Waseem commited for test 3
    public static final String IMAGE_DIRECTORY_NAME = "Medikart";

    //public static String URL_IPADDRESS = "http://198.50.198.184:86";
    //public static String URL_IPADDRESS = "http://198.50.198.184:85";

    //public static String URL_IPADDRESS = "http://192.168.1.112:83";
    // public static String URL_IPADDRESS = "http://198.50.198.184/medikartapimobile";

    // public static String URL_IPADDRESS = "http://192.168.1.150:8920";

    //public static String URL_IPADDRESS = "http://115.124.124.162:85";
    //public static String URL_IPADDRESS = "http://115.124.124.162:91";


    // public static String URL_IPADDRESS = "https://www.rxmedikart.com/medikartapimobile";


   // public static String URL_IPADDRESS = "http://192.168.1.26/medikartapi" ;
   // public static String URL_IPADDRESS = "http://192.168.1.26/medikartapi";

    // public static String URL_IPADDRESS = "http://198.50.198.184/medikartapi" ;
    

   // public static String URL_IPADDRESS = "https://www.rxmedikart.com/medikartapimobile";
    public static String URL_IPADDRESS = "https://www.rxmedikart.com/medikartapi";



    public static Integer SERVERTIMEOUT = 50000;

    // GET CHECK DUPLICATE MOBILENO

    public static String URL_GET_CHECKDUPLICATEMOBILENO = URL_IPADDRESS + "/api/login/CheckDuplicateMobileNumber?iMemberId=%1$s&sMobileNo=%2$s";
    // GET CHECK DUPLICATE EMAILID & MOBILE NO


    // POST - Server user register url

    public static String URL_POST_BP = URL_IPADDRESS +"/api/Monitor/BPMonitorMobile";

    public static String URL_POST_DM = URL_IPADDRESS +"/api/Monitor/DMMonitorMobile";

//
      //for water settings
    public static String URL_POST_WATERSETTING = URL_IPADDRESS +"/api/Monitor/WSMonitorMobile";
    //for water Entries
    public static String URL_POST_WATERENTRY= URL_IPADDRESS +"/api/Monitor/WEMonitorMobile";
    // GET - Login user



    // GET - Image Detail
    public static String URL_GET_MemberImageDetail = URL_IPADDRESS +"/Api/AddMember/MemberImageDetail?iMemberId=%1$s";
    // public static String URL_GET_ADDTOCARTWITHQTY = URL_IPADDRESS +"/Api/Product/MobileAddToCartWithQty?iProductId=%1$s&iMemberId=%2$s&iQty=%3$s";



    // GET - Apply Promocode
    //public static String URL_GET_APPLYPROMOCODE = URL_IPADDRESS +"/Api/checkout/ApplyPromocode?PromoCode=%1$s&iCartId=%2$s&iMemberId=%3$s";

    // GET - Shipping Data
    public static String URL_GET_SHIPPINGDATA = URL_IPADDRESS +"/Api/Checkout/GetshippingData?TotalAmount=%1$s&iMemberId=%2$s";





    // GET - Save Doctor  Medicine reminder
    public static String URL_GET_SAVEDOCTOR = URL_IPADDRESS +"/api/Prescription/SaveDoctorJson?sDoctorName=%1$s&sClinicName=%2$s&iDistrictId=%3$s&iMemberId=%4$s";
    //public static String URL_GET_SAVEDOCTOR = URL_IPADDRESS +"/api/Prescription/SaveDoctorJson?";


    // GET - Local MEMBER list not used in buying app
    public static String URL_GET_MEMBERLIST = URL_IPADDRESS +"/api/common/PatientListJson?iMemberId=%1$s";

    // post add doctor

    public static String URL_POST_ADDBPDATAJson = URL_IPADDRESS +"/api/Monitor/BPMonitorMobile";

    public static String URL_POST_ADDDMDATAJson = URL_IPADDRESS +"/api/Monitor/DMMonitorMobile";

    public static String URL_POST_ADDSYNCLOGJson = URL_IPADDRESS +"/api/Monitor/SyncLogMobile";


    // POST - Save bp monitor with upload BP MONITOR
    public static String URL_POST_UPLOADBP = URL_IPADDRESS +"/Api/Monitor/BPMonitorMobile";


    //get cart count on login Added by Akhil

    public static String URL_GET_PREVIOUS_DATA = URL_IPADDRESS + "/api/Monitor/GetMonitorDetailJson?iMemberId=%1$s";




    public static final String FILE_UPLOAD_URL = URL_IPADDRESS +"/Api/Prescription/AddImages?imemberid=%1$s";

  // public static String URL_POST_ACCEPT_INVITE = "http://192.168.1.150:8899" +"/Api/AddMember/SaveAddMember";
    // public static String GOOGLE_PROJ_ID = "873228156079";
    public static String GOOGLE_PROJ_ID = "685655180561";


    public static final String FILE_UPLOADPROFILEIMAGE_URL = URL_IPADDRESS +"/Api/AddMember/uploadImageProfile?imemberid=%1$s";
    public static final String FILE_UPLOADFamilyPROFILEIMAGE_URL = URL_IPADDRESS +"/Api/AddMember/uploadImageFamilyMemberProfile?imemberid=%1$s";


    //Get reminder data from server
    public static String URL_GET_RERMINDER_DATA= URL_IPADDRESS + " Api/Monitor/GetMobileMonitorDetailJson?MemberId=%1$s";

    //Get Medfriendlist
    public static String URL_GET_MED_FRIEND_LIST= URL_IPADDRESS + "/api/Monitor/GetMedFriendList?iMemberId=%1$s";


  public static String POST_Url_EAZBUZZ_SAVECHECKOUT= URL_IPADDRESS + "/api/ShoppingCart/SaveCheckoutWithPayUMoneyMobile";

    public static String URL_GET_SEND_INVITE = URL_IPADDRESS + "/Api/PushNotification/Invite_medfriend?sEmailID=%1$s&SMobileNo=%2$s&iMemberId=%3$s";

    public static String URL_GET_ACCEPT_INVITE = URL_IPADDRESS + "/Api/PushNotification/Accept_medfriend?iMemberId=%1$s&req_sender_memberid=%2$s";
    public static String URL_GET_REJECT_INVITE = URL_IPADDRESS + "/Api/PushNotification/Reject_medfriend?iMemberId=%1$s&req_sender_memberid=%2$s";

    public static String URL_GET_SEND_DATA_SCHEDULE = URL_IPADDRESS + "/Api/PushNotification/push_pillbuddyupdate_medfriend";

    // GET - Local MedfrienData from server
    public static String URL_GET_MED_FRIEND_DATA = URL_IPADDRESS +"/Api/Monitor/GetMobileMonitorDetailJson?MemberId=%1$s";

    //update gcmid
    public static String URL_POST_GCM_ID = URL_IPADDRESS + "/api/login/Update_User_GCM_ID_Mobile";




    public static String URL_GET_MEMBER_PHOTO = URL_IPADDRESS + "/api/AddMember/MemberImageDetail?iMemberId=%1$s";

    // GET - get city list
    public static String URL_GET_STATE = URL_IPADDRESS + "/api/Common/StateListJson";

    // GET - get city list
    public static String URL_GET_CITY = URL_IPADDRESS + "/api/Common/CityListJson?sCityName=%1$s&iStateid=%2$s";

    //get cart count on login Added by Akhil
    public static String URL_GET_CARTCOUNT_ONLOGIN = URL_IPADDRESS + "/api/Product/GetTotalCartJson?iMemberId=%1$s";

    public static String URL_GET_CITY_ON_PINCODE = URL_IPADDRESS + "/api/Login/PincodewisecityJson?sPincode=%1$s";

    public static String URL_POST_SaveEditProfile = URL_IPADDRESS + "/api/AddMember/SaveEditProfile";


    //Send Enquiry
    public static String URL_POST_SEND_INVITE = URL_IPADDRESS + "/Api/ContactUs/ContactUsJson";
    //get details
    public static String URL_GET_USER_DATA = URL_IPADDRESS + "/api/Login/GetMemberStateDistrict?iMemberId=%1$s";
    //Delete Address
    public static String URL_GET_DELETE_ADDRESS = URL_IPADDRESS + "/api/checkout/DeleteMemberAddressJson?AddressId=%1$s&MemberId=%2$s";


    // POST - Submit data
    public static String URL_POST_SUBMITDATA = URL_IPADDRESS + "/api/Request/SaveRequestMobile";

    // GET - Login user
    public static String URL_GET_LOGIN = URL_IPADDRESS + "/api/login/Authenticate?sUserName=%1$s&sPwd=%2$s";

    // GET - Search Product
    public static String URL_GET_SEARCHPRODUCT = URL_IPADDRESS + "/Api/Product/SearchMobileProductsJson?sSearchText=%1$s&iCityId=%2$s&iStatus=%3$s";
    public static String URL_GET_NEW_SEARCHPRODUCT = URL_IPADDRESS + "/api/Product/MobileSearchProductJson?sSearchText=%1$s&sSearchType=%2$s&sSearchFor=%3$s";

    public static String URL_GET_PRODUCTDETAIL = URL_IPADDRESS + "/Api/Product/MobileProductDetail?iProductId=%1$s&iMemberId=%2$s";

    // GET - Add to cart
    public static String URL_GET_ADDTOCART = URL_IPADDRESS + "/Api/Product/AddToCart?iProductId=%1$s&iMemberId=%2$s";


    // POST - Save Checkout At click on "No" Button Place order without upload prescription
    public static String URL_POST_SAVESHIPPINGDETAILS = URL_IPADDRESS + "/Api/Checkout/SaveShippingDetails";

    //save member address
    public static String URL_POST_ADD_MEMBER_ADDRESS = URL_IPADDRESS + "/api/checkout/AddMemberAddressJson";

    //Edit member address
    public static String URL_POST_EDIT_MEMBER_ADDRESS = URL_IPADDRESS + "/api/checkout/EditMemberAddressJson";


    // post add doctor
    public static String URL_POST_ADDDoctorJson = URL_IPADDRESS + "/api/Prescription/ADDDoctorJson";

    // GET - Local doctor list
    public static String URL_GET_LOCALDOCTORLIST = URL_IPADDRESS + "/api/Common/LocalDoctorList?iDistrictId=%1$s&iMemberId=%2$s";

    public static String URL_GET_MEMBERFamilyLIST = URL_IPADDRESS + "/api/common/PatientFamilyListJson?iMemberId=%1$s";

    // GET - Refill The Basket
    public static String URL_GET_REFILLBASKET = URL_IPADDRESS + "/api/Order/GetOrderList?iMemberId=%1$s";

    public static String URL_GET_DELIVERORDERLIST = URL_IPADDRESS + "/api/Order/GetDeliveryOrderList?iMemberId=%1$s&iOrderId=%2$s&Flag=%3$s";

    // GET - View Order Detail
    public static String URL_GET_VIEWORDERDETAIL = URL_IPADDRESS + "/api/Order/OrderDetail?iOrderId=%1$s&iMemberId=%2$s";

    // GET - View Order Detail with product detail and priciong
    public static String URL_GET_PRICINGORDERDETAIL = URL_IPADDRESS + "/api/Order/MobileOrderDetail?iOrderId=%1$s";

    // GET - Add order item to cart not used
    public static String URL_GET_ADDORDERITEMTOCART = URL_IPADDRESS + "/api/Order/AddToCart/?iOrderId=%1$s&iMemberId=%2$s&iProductIDs=%3$s&ApplicableFor=%4$s";


    //    ===========================
    // GET - get salt detail
    public static String URL_GET_SALT_DATA = URL_IPADDRESS + "/api/Salt/SaltDetailJson?iSaltId=%1$s";

    // GET - get  forgot password
    public static String URL_GET_FORGOT_PASSWORD = URL_IPADDRESS + "/api/Login/ForgotPassword?sEmailID=%1$s";

    public static String URL_GET_CHECKDUPLICATEEMAILIDMOBILENO = URL_IPADDRESS + "/api/login/CheckDuplicateEmailMobileNo?iMemberId=%1$s&sEmail=%2$s&sMobileNo=%3$s";


    // POST - Server user register url
    public static String URL_POST_REGISTER = URL_IPADDRESS + "/api/Login/RegisterUserMobile";


    public static String URL_GET_ADDTOCARTWITHQTY = URL_IPADDRESS + "/Api/ShoppingCart/AddToCart?iProductId=%1$s&iMemberId=%2$s&sPlusMinus=%3$s&Qty=%4$s&ApplicableFor=%5$s";


    // GET - Checkout
    public static String URL_GET_CHECKOUT = URL_IPADDRESS + "/Api/ShoppingCart/Checkout?iMemberId=%1$s";

    public static String URL_GET_REMOVEPRODUCT = URL_IPADDRESS + "/Api/ShoppingCart/CancelProduct?iProductId=%1$s&iCartId=%2$s&iMemberId=%3$s";

    // GET - Plus Minus Qty
    public static String URL_GET_PLUSMINUSQTY = URL_IPADDRESS + "/Api/ShoppingCart/EditQty?iMemberId=%1$s&iProductId=%2$s&StrMinusPlus=%3$s&ApplicableFor=%4$s";

    // GET - Apply Promocode
    public static String URL_GET_APPLYPROMOCODE = URL_IPADDRESS + "/Api/ShoppingCart/ApplyPromocode?PromoCode=%1$s&iCartId=%2$s&iMemberId=%3$s&ApplicableFor=%4$s";

    // GET - Cancel Promocode
    public static String URL_GET_CANCELPROMOCODE = URL_IPADDRESS + "/Api/ShoppingCart/CancelPromocode?PromoCode=%1$s&iCartId=%2$s&iMemberId=%3$s&ApplicableFor=%4$s";


    //get_naddress_multiple
    public static String URL_GET_MULTIPLE_ADDRESS = URL_IPADDRESS + "/api/Checkout/GetMultipleAddressListJson?iMemberId=%1$s";



    // POST - Save Checkout At click on "No" Button Place order without upload prescription
    public static String URL_POST_SAVECHECKOUT = URL_IPADDRESS + "/Api/ShoppingCart/SaveCheckoutDetailsMobile";


    // GET - Is emergency
    public static String URL_GET_ISEMERGENCY = URL_IPADDRESS + "/api/ShoppingCart/AddIsEmergency?iMemberId=%1$s&iCartId=%2$s&IsEmergency=%3$s";



    // GET - Get prescription list
    public static String URL_GET_PRESCRIPTION_LIST = URL_IPADDRESS + "/api/Prescription/PrescriptionListJson?iMemberId=%1$s";

    // GET - Get prescription detail
    public static String URL_GET_PRESCRIPTION_DETAILS = URL_IPADDRESS + "/api/Prescription/PrescriptionDetailJson?iUploadId=%1$s";


    // POST - Upload Prescription
    public static String URL_POST_UPLOADPRESCRIPTION = URL_IPADDRESS + "/Api/Prescription/SaveUploadPrescriptionMobile";




    public static String URL_POST_ACCEPT_INVITE = URL_IPADDRESS + "/Api/AddMember/SaveAddMember";

    public static String URL_POST_CHANGE_PASSWORD = URL_IPADDRESS + "/api/ChangePassword/SaveChangePassword";


    //get pricing
    public static String URL_GET_PRICING_DETAIL = URL_IPADDRESS + "/api/ShoppingCart/GetPriceSummaryDtl?iCartId=%1$s";

    //get version
    public static String URL_GET_VERSION_NUMBER = URL_IPADDRESS + "/AndroidJson/android.json";

    //get salt related medicine details
    public static String URL_GET_SALT_PRODUCT_DETAIL = URL_IPADDRESS + "/api/Salt/GetSaltProductDetails?iSaltId=%1$s&iLastProductId=%2$s";

    // GET - get general product detail
    public static String URL_GET_GENERAL_PRODUCT_DATA = URL_IPADDRESS + "/api/Category/GeneralCatProductDetails?iProductId=%1$s";

    //get Cancel product detail
    public static String URL_GET_CANCEL_PRODUCT_DATA = URL_IPADDRESS + "/api/OrderCancelApi/getOrderDetail?iOrderId=%1$s&iProductId=%2$s&iMemberId=%3$s";


    ///post Cancel product
    public static String URL_POST_CANCEL_ORDER = URL_IPADDRESS + "/api/OrderCancelApi/CancelOrderItem_json";

    //get general product search details
    public static String URL_GET_GENERAL_PRODUCT_SEARCH_DATA = URL_IPADDRESS + "/api/Category/GetSubCategoryWiseProductList?SubCategoryid=%1$s&iLastSrNo=%2$s&sSortBy=%3$s&sBrandIds=%4$s&iFromPrice=%5$s&iToPrice=%6$s&isAllProdutse=%7$s&CategoryId=%8$s&sManufacturerIds=%9$s";

    // GET - get general product Categorylist
    public static String URL_GET_GENERAL_PRODUCT_CATEGORY_LIST = URL_IPADDRESS + "/api/Category/GetCategoryList?CategoryId=%1$s";


    // GET - get general product Brandlist
    public static String URL_GET_GENERAL_PRODUCT_BRAND_LIST = URL_IPADDRESS + "/api/Category/GetSubCategoryWiseBrandList?iSubCategoryId=%1$s&iCategoryId=%2$s";


    // GET - get reason list
    public static String URL_GET_REASONS = URL_IPADDRESS + "/Api/OrderCancelApi/GeReasonList?ReasonFor=%1$s";

  // GET - get relationships
  public static String URL_GET_RELATIONSHIPS = URL_IPADDRESS + "/Api/OrderCancelApi/GetRelationshipList?Relation=%1$s";

    // GET - get reason list
    public static String URL_GET_CHECK_PINCODE = URL_IPADDRESS + "/api/Checkout/checkAvailibilityPincode?PinCode=%1$s&sFlag=true";

    //POST - invite_medfriend on code

  public static String URL_POST_INVITE_CODE_PILLBUDDY = URL_IPADDRESS + "/api/PushNotification/Invite_med_friend_from_code";

  //GET- accept medfriend on code

  public static String URL_GET_ACCEPT_PIIBUDDY_ON_CODE = URL_IPADDRESS + "/api/PushNotification/Accept_medfriend_from_code?iMemberId=%1$s&InvitationCode=%2$s&FriendMobileNo=%3$s&FriendEmailId=%4$s";


  //get wipe data

  public static String URL_WIPE_DATA_FROM_SERVER = URL_IPADDRESS + "/api/Monitor/Wipe_data_user_level?iMemberId=%1$s&module=%2$s";


  public static String POST_Url_HashKey = URL_IPADDRESS + "/api/HashKeyJson/Post";

  public static String POST_Url_PAYUMONEY_ORDERPAYMENT= URL_IPADDRESS + "/api/ShoppingCart/OrderPayment";

  public static String POST_Url_PAYUMONEY_SAVECHECKOUT= URL_IPADDRESS + "/api/shoppingcart/SaveCheckoutWithPayUMoneyMobile";

}

