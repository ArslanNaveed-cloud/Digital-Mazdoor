package aust.fyp.pk.project.application.digitalmazdoor;

public class Urls {
    public static String IP = "192.168.43.181";
    public static String PORT = "8080";
    public static String DOMAIN1 = IP.trim() + ":" + PORT.trim();
    public static String DOMAIN = "http://" + IP.trim() + ":" + PORT.trim();



    public static String SIGNUP_FREELANCER = "http://" + DOMAIN1 + "/freelancerapi/addfreelancer";
    public static String SIGNUP_BUYER = "http://" + DOMAIN1 + "/buyerapi/addbuyer";
    public static String LOGIN_FREELANCER = "http://" + DOMAIN1 + "/freelancerapi/freelancerlogin";
    public static String LOGIN_BUYER = "http://" + DOMAIN1 + "/buyerapi/loginbuyer";
    public static String GET_GIGSFRBUYER = "http://" + DOMAIN1 + "/buyerapi/getgigs";
    public static String SAVE_GIGS = "http://" + DOMAIN1 + "/freelancerapi/savegig";
    public static String GET_GIGS = "http://" + DOMAIN1 + "/freelancerapi/getgigs";
    public static String DELETEGIG = "http://" + DOMAIN1 + "/freelancerapi/deletegig";
    public static String EDITGIGSWITHIMAGE = "http://" + DOMAIN1 + "/freelancerapi/editgigs";
    public static String EDITGIGWITHOUTIMAGE = "http://" + DOMAIN1 + "/freelancerapi/editgigswithoutpics";
    public static String PLACE_ORDERS = "http://" + DOMAIN1 + "/buyerapi/placeorder";
    public static String PLACE_REQUESTS = "http://" + DOMAIN1 + "/buyerapi/placerequests";
    public static String GET_REQUEST = "http://" + DOMAIN1 + "/buyerapi/getrequests";
    public static String DELETE_REQUEST = "http://" + DOMAIN1 + "/buyerapi/deleterequest";
    public static String GET_REQUESTFORBUYER = "http://" + DOMAIN1 + "/freelancerapi/getrequests";
    public static String SEND_OFFER = "http://" + DOMAIN1 + "/freelancerapi/sendoffer";
    public static String GET_ORDERREQUEST = "http://" + DOMAIN1 + "/freelancerapi/getorderrequest";
    public static String ACCEPT_ORDER = "http://" + DOMAIN1 + "/freelancerapi/acceptorder";
    public static String REJECT_ORDER = "http://" + DOMAIN1 + "/freelancerapi/rejectorder";
    public static String GET_OFFERS = "http://" + DOMAIN1 + "/freelancerapi/getoffers";
    public static String GET_ORDERS = "http://" + DOMAIN1 + "/freelancerapi/getorders";
    public static String GET_BUYERORDRREQUEST = "http://" + DOMAIN1 + "/buyerapi/getorderrequest";
    public static String GET_ORDERSFORBUYERS = "http://" + DOMAIN1 + "/buyerapi/getorders";
    public static String GET_OFFERSFORBUYERS = "http://" + DOMAIN1 + "/buyerapi/getoffers";
    public static String ACCEPT_OFFER = "http://" + DOMAIN1 + "/buyerapi/acceptoffer";
    public static String REJECT_OFFER = "http://" + DOMAIN1 + "/buyerapi/rejectoffer";
    public static String UPDATEORDERSTATUS = "http://" + DOMAIN1 + "/buyerapi/updateorderstatus";
    public static String GIVEFREELANCERRATING = "http://" + DOMAIN1 + "/buyerapi/givefreelancerrating";
    public static String GIVEBUYERRATING = "http://" + DOMAIN1 + "/freelancerapi/givebuyerrating";
     public static String UPDATEWITHPROFILEPICTURE = "http://" + DOMAIN1 + "/freelancerapi/updatewithprofile";
    public static String UPDATEWITHOUTPROFILEPICTURE = "http://" + DOMAIN1 + "/freelancerapi/updatewithoutprofile";
    public static String UPDATEWITHPROFILEPICTURE1 = "http://" + DOMAIN1 + "/buyerapi/updatewithprofile";
    public static String UPDATEWITHOUTPROFILEPICTURE1 = "http://" + DOMAIN1 + "/buyerapi/updatewithoutprofile";
    public static String FORGOTPASSWORD = "http://" + DOMAIN1 + "/buyerapi/forgotpassword";
    public static String FORGOTPASSWORD1 = "http://" + DOMAIN1 + "/freelancerapi/forgotpassword";
    public static String SendFreelancerMessage = "http://" + DOMAIN1 + "/buyerapi/SendFreelancerMessage";
    public static String SendFreelancerImages = "http://" + DOMAIN1 + "/buyerapi/SendFreelancerImages";
    public static String GETFREELANCERMESSAGES = "http://" + DOMAIN1 + "/buyerapi/getfreelancermessages";
    public static String GETBUYERS = "http://" + DOMAIN1 + "/freelancerapi/getbuyers";
    public static String GETFREELANCERS = "http://" + DOMAIN1 + "/buyerapi/getfreelancers";
    public static String GET_MESSAGES = "http://" + DOMAIN1 + "/freelancerapi/getmessages";
    public static String GET_DETAILS = "http://" + DOMAIN1 + "/freelancerapi/getdetails";
    public static String GET_PENDINGORDERS = "http://" + DOMAIN1 + "/freelancerapi/getpendingorders";
    public static String GET_BUYERDETAILS = "http://" + DOMAIN1 + "/freelancerapi/getbuyerdetails";
    public static String GET_FREELANCERDETAILS = "http://" + DOMAIN1 + "/buyerapi/getfreelancerdetails";

}
