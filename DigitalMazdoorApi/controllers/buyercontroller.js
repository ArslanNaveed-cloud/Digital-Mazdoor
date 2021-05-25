'use strict';

const firebase = require('../db');
const firestore = firebase.firestore();
const randomInt = require('random-int');
const bcrypt = require("bcrypt");
const saltRounds = 10;                          //We are setting salt rounds, higher is safer.
const myPlaintextPassword = 's0/\/\P4$$w0rD'; 
var forEach = require('async-foreach').forEach;
const moment = require("moment");
var Buyer = require("../models/BuyerModel");
var nodemailer = require('nodemailer');
const addbuyer = async (req, res, next) => {

   
       try { 
           const count = 0;
           var random  = randomInt(10, 50000);
        var filename = req.file.originalname;
        var id, fullname, phone, email, password, profilepicture, rating, dateofjoining,city, status;
        var password = req.body.password;
        var hashpassword;
        await bcrypt.hash(password, saltRounds, (err, hash) => {
            hashpassword = hash 
});
        email = req.body.email;

        firestore.collection('buyers').where('email','==',email).get().then((snapshot)=>{
            
           var result = snapshot.docs;
            if(result.length === 0){
                var currenttime = moment().format("h:mma");
       
    
                var now = moment().format("DD-MM-YYYY");
                var date = now+"("+currenttime+")";
                id="sar_"+random;
                fullname = req.body.fullname;
                phone = req.body.phone;
                email = req.body.email;
                password = hashpassword;
                profilepicture = filename;
                dateofjoining = date;
                status = req.body.status;
                city = req.body.city;
                var data = {
                    id:id,
                    fullname:fullname,
                    phone:phone,
                    email:email,
                    password:password,
                    profilepicture:profilepicture,
                    rating:0,
                    totalreviews:0,
                    dateofjoining:dateofjoining,
                    city:city,
                    status:status
            }

            firestore.collection('buyers').doc().set(data).then(()=>{
               res.json({
                   status:200
               });
            });
            
           }else{
                res.json({
                    status:409
                });
           }
        });;
        
             
       } catch (error) {
           res.json({
               status:500
           });
       }
   
        
   
}
const loginbuyer= async (req, res, next) => {

   
    try { 
        var email = req.body.email;
        var password = req.body.password;


     firestore.collection('buyers').where('email','==',email).get().then((snapshot)=>{
         
        var result = snapshot.docs;
         if(result.length === 0){
                res.json({
                    status:404
                });
         
        }else{
            snapshot.docs.forEach(doc=>{
                var hash = doc.data().password;
                bcrypt.compare(password, hash, function(error, response1) {
                    if(error){
                        console.log(error);
                        res.json({
                            status:500
                        });
                    }else{
                       if(response1){
                        var id, phone, email, profilepicture, rating, dateofjoining,city, status;
                      
                        var username = doc.data().fullname;
                        id = doc.data().id;
                        email = doc.data().email;
                        profilepicture = doc.data().profilepicture;
                        rating = doc.data().rating;
                        dateofjoining = doc.data().dateofjoining;
                        status = doc.data().status;
                        phone = doc.data().phone;
                        city = doc.data().city;

                        res.json({
                            status:200,
                            id:id,
                            username:username,
                             email:email,
                            phone:phone,
                            password:password,
                            profilepicture:profilepicture,
                            rating:rating,
                            dateofjoining:dateofjoining,
                            city:city,
                            accountstatus:status
                        });
                       }else{
                        res.json({
                            status:404,
                          
                        });
                       }
                    }
                });
            });
        }
     });;
     
          
    } catch (error) {
        console.log(error);
        res.json({
            status:500
        });
    }

     

}

const getgigs = async(req,res,next)=>{

    try {
        var category = req.body.category;
        var freelancerid=[];
        var freelancernames=[];
         var freelancerrating=[];
        var freelancerorders=[];
        var freelancerprofilepics=[];
        var freelancercity=[];
        var freelancertotalreviews=[];
        var freelancerworkingcateogry=[];
        var freelancersubcategories=[];
        var freelancerdateofjoining=[];
        var freelancerprofilesummary=[];
        
        
        var flid;
          var gigtitle=[];
          var gigcategory=[];
         var gigprice=[];
         var gigdeliverytime=[];
         var  gigimage=[];
         var  gigdescription=[];
         var gigid=[];
         var gigdateofuploading = [];
         var city = req.body.city;
       await firestore.collection('gigs').where('category','==',category).where('city','==',city).get().then((snapshot)=>{
            var result = snapshot.docs;
            if(result.length === 0){
                res.json({
                    status:404
                });
            }else{
               
                 result.forEach(doc=>{
                    flid = doc.data().id;
                    gigtitle.push(doc.data().gigtitle);
                    

                    gigcategory.push(doc.data().category);
                    gigprice.push(doc.data().gigprice);
                    gigdeliverytime.push(doc.data().gigdays);
                    gigimage.push(doc.data().coverimage);
                    gigdescription.push(doc.data().gigdescription);
                    gigid.push(doc.data().gigid);
                    gigdateofuploading.push(doc.data().dateofuploading);
                    firestore.collection('freelancers').where('id','==',flid).where('status','==','Active').get().then((snapshot2)=>{
                        var results = snapshot2.docs;
                        results.forEach(doc=>{
                            console.log(doc.data().id);
                            freelancerid.push(doc.data().id)
                            freelancernames.push(doc.data().fullname)
                            freelancerrating.push(doc.data().rating)
                            freelancerorders.push(doc.data().completedorders)
                            freelancerprofilepics.push(doc.data().profilepicture)
                            freelancercity.push(doc.data().city)
                            freelancertotalreviews.push(doc.data().totalreviews)
                            freelancerworkingcateogry.push(doc.data().workingcategory)
                            freelancersubcategories.push(doc.data().subcategories)
                            freelancerdateofjoining.push(doc.data().dateofjoining);
                            freelancerprofilesummary.push(doc.data().profilesummary);

                         
                        });
                        res.json({
                            status:200,
                            gigtitle:gigtitle,
                            gigcategory:gigcategory,
                            gigprice:gigprice,
                            gigdeliverytime:gigdeliverytime,
                            gigcoverimage:gigimage,
                            gigdescription:gigdescription,
                            gigid:gigid,
                            gigdateofuploading:gigdateofuploading,
                             freelancerid:freelancerid,
                             freelancernames:freelancernames,
                              freelancerrating:freelancerrating,
                             freelancerorders:freelancerorders,
                             freelancerprofilepics:freelancerprofilepics,
                             freelancercity:freelancercity,
                             freelancertotalreviews:freelancertotalreviews,
                             freelancerworkingcateogry:freelancerworkingcateogry,
                             freelancersubcategories:freelancersubcategories,
                             freelancerdateofjoining:freelancerdateofjoining,
                             freelancerprofilesummary:freelancerprofilesummary
                        });


                        
                            
                    });
                  
                });

             
                 
            
            }
        });
    } catch (error) {
        console.log(error);
        res.json({
            status:500
        });
    }


}

const placeorder = async(req,res,next)=>{

   
    try {
        var currenttime = moment().format("h:mma");
       
    
    var now = moment().format("DD-MM-YYYY");
    var date = now+"("+currenttime+")";
        var buyername = req.body.buyername;
        var buyerid = req.body.buyerid;
        var gigid  = req.body.gigid;
        var gigtitle = req.body.gigtitle;
        var orderid = "order_"+""+ randomInt(10, 50000);
        var budget = req.body.budget;
        var days = req.body.time;
        var orderstatus;
        var profilepicture = req.body.profilepicture;
        var freelancerimage = req.body.freelancerimage;
        var freelancerid = req.body.freelancerid;
        var freelancername = req.body.freelancername;
        var category = req.body.category;
        var data = {
            ordderid:orderid,
            buyername:buyername,
            buyerid:buyerid,
            gigtitle:gigtitle,
            gigid:gigid,
            budget:budget,
            days:days,
            profilepicture:profilepicture,
            orderstatus:"Pending",
            orderdate:date,
            freelancerid:freelancerid,
            freelancername:freelancername,
            freelancerimage:freelancerimage,
            category:category
        }
        firestore.collection('orders').doc().set(data).then(()=>{
                    res.json({
                            status:200,
                            gigid:gigid,
                            orderstatus:orderstatus
                    });

        });
    } catch (error) {
        console.log(error);
        res.json({
            status:500
        });
    }
   

}

const placerequests = async(req,res,next)=>{
    var postid = "post"+""+ randomInt(10, 50000);
    var buyerid = req.body.buyerid;
    var buyername = req.body.buyername;
    var city = req.body.city;
    var description = req.body.description;
    var budget = req.body.budget;
    var category = req.body.category;
    var buyerpic = req.body.buyerpic;
    var title = req.body.title;
    var days = req.body.days;
    
    var currenttime = moment().format("h:mma");
       
    
    var now = moment().format("DD-MM-YYYY");
    var date = now+"("+currenttime+")";
    var poststatus;
    try {
        
      var data={
            postid:postid,
            buyerid :buyerid,
            title:title,
            buyername :buyername,
            city :city,
            description :description,
            budget :budget,
            category :category,
            buyerpic :buyerpic,
            days :days,
            date :date,
            bids:0,
            status:"Live"

        }

        firestore.collection('request').doc().set(data).then(()=>{
            res.json({
                    status:200,
                    postid:postid,
                    poststatus:poststatus
            });

});
    } catch (error) {
        res.json({
            status:500
        });
    }

    
};
const getrequests = async(req,res,next)=>{
    var buyername = req.body.buyername;
    var buyerid = req.body.buyerid;

    console.log(buyername+"=="+buyerid);

    var postid =[];
    var id = [];
    var name = [];
    var city = [];
    var description = [];
    var budget = [];
    var category = [];
    var buyerpic = [];
    var title = [];
    var days =[];
    var bids = [];
    var status=[];
    var date=[];
    try {
        firestore.collection('request').where('buyername','==',buyername).where('buyerid','==',buyerid).get().then((snapshot)=>{
            var results = snapshot.docs;
            if(results.length ===0){
                res.json({
                    status:404
                });
            }else{
                    snapshot.docs.forEach(doc=>{
                            name.push(doc.data().buyername);
                            id.push(doc.data().buyerid);
                            postid.push(doc.data().postid);
                            city.push(doc.data().city);
                            description.push(doc.data().description);
                            budget.push(doc.data().budget);
                            category.push(doc.data().category);
                            buyerpic.push(doc.data().buyerpic);
                            title.push(doc.data().title);
                            days.push(doc.data().days);
                            bids.push(doc.data().bids);
                            date.push(doc.data().date);
                            status.push(doc.data().status);
                    });
    
                    res.json({
                        status:200,
                        postid:postid,
                        id :id,
                        name :name,
                        city :city,
                        description :description,
                        budget :budget,
                        category :category,
                        buyerpic :buyerpic,
                        title :title,
                        days:days,
                        bids :bids,
                        poststatus:status,
                        date:date,
                    });
            }
    
    
        });
    } catch (error) {
        res.json({
            status:500
        });
    }
   
};

const deleterequest = async(req,res,next)=>{
    var postid= req.body.postid;

    try {
        firestore.collection('request').where('postid','==',postid).get().then((snapshot)=>{
            snapshot.docs.forEach(doc=>{
                firestore.collection('request').doc(doc.id).delete().then((snapshot2)=>{
                    res.json({
                        status:200
                    });
                });
            });

        });
    } catch (error) {
        res.json({
            status:500
        });
    }
}

const getorderrequest=async(req,res,next)=>{
    var bid = req.body.buyerid;

    var buyername =[];
    var buyerid=[]
    var gigid =[]
    var gigtitle=[]
    var orderid =[];
    var budget =[];
    var days=[];
    var orderstatus=[];
    var buyerpic=[];
    var date = [];
    var flancerid=[]
try {
    firestore.collection('orders').where('buyerid','==',bid).where('orderstatus','==','Pending').get().then((snapshot)=>{
        var results = snapshot.docs;
        if(results.length === 0){
            res.json({
                status:404
            });
        }else{
                snapshot.docs.forEach(doc=>{
                    buyername.push(doc.data().freelancername);
                    buyerid.push(doc.data().freelancerid);
                    gigid.push(doc.data().gigid);
                    gigtitle.push(doc.data().gigtitle);
                    orderid.push(doc.data().ordderid);
                    days.push(doc.data().days);
                    orderstatus.push(doc.data().orderstatus);
                    date.push(doc.data().orderdate);
                    buyerpic.push(doc.data().freelancerimage);
                    flancerid.push(doc.data().freelancerid);
                    budget.push(doc.data().budget);
                });
                res.json({
                    status:200,
                    orderid:orderid,
                    buyerid:buyerid,
                    buyername :buyername,
                    gigname:gigtitle,
                    budget:budget,
                    date:date,
                    days:days,
                    buyerpic:buyerpic,
                    orderstatus:orderstatus,
                    freelancerid:flancerid
                });
        }
    });
} catch (error) {
    console.log(error);
    res.json({
        status:500
    });
}

}


const getoffers = async(req,res,next)=>{
    try {
      
       var buid= req.body.buyerid;
  
       var postid =[];
       var id = [];
       var name = [];
       var city = [];
       var description = [];
       var budget = [];
       var category = [];
       var buyerpic = [];
       var title = [];
       var days =[];
       var bids = [];
        var date=[];
       var orderstatus=[];
       var buyerpicture=[];
       firestore.collection('offers').where('buyerid','==',buid).get().then((snapshot)=>{
               var results = snapshot.docs;
                   console.log(results);
           if(results.length === 0){
               res.json({
                   status:404                
               });
           }else{
               snapshot.docs.forEach(doc=>{
                   if(doc.data().status ==='Accepted'  || doc.data().status ==='Rejected'){

                   }else{
                       name.push(doc.data().freelancername);
                       id.push(doc.data().freelancerid);
                       postid.push(doc.data().offerid);
                        description.push(doc.data().description);
                       budget.push(doc.data().budget);
                       category.push(doc.data().category);
                       title.push(doc.data().title);
                       days.push(doc.data().days);
                       bids.push(doc.data().bids);
                       date.push(doc.data().date);
                       orderstatus.push(doc.data().status);
                       buyerpicture.push(doc.data().freelancerpicture);
                   }
                     
               });

               if(days.length === 0){
                   res.json({
                       status:404
                   });
               }else{
                res.json({
                    status:200,
                    postid:postid,
                    id :id,
                    name :name,
                    description :description,
                    budget :budget,
                    category :category,
                    buyerpicture :buyerpicture,
                    title :title,
                    days:days,
                    bids :bids,
                    date:date,
                    orderstatus:orderstatus
                });
               }
               
       }
       });
   } catch (error) {
       res.json({
           status:500
       });
   }

}
const acceptorder = async(req,res,next)=>{
    var orderid = req.body.offerid;
    console.log(orderid);
    try {
        firestore.collection('offers').where('offerid','==',orderid).get().then((snapshot)=>{
            var results = snapshot.docs;
            if(results.length===0){
                res.json({
                    status:404
                });
              }else{
                  snapshot.docs.forEach(doc=>{
                      var docid = doc.id;
                      var updatemap = {
                        status:"Accepted"
                    }
                        firestore.collection('offers').doc(docid).update(updatemap).then(()=>{
                        
                      firestore.collection('offers').where('offerid','==',orderid).get().then((snapshot)=>{
                        var orderid = "orders_"+ randomInt(10, 50000);
                        var currenttime = moment().format("h:mma");
      
   
                        var now = moment().format("DD-MM-YYYY");
                        var date = now+"("+currenttime+")";
                        var buyername = doc.data().buyername;
                        var buyerid = doc.data().buyerid;
                        var buyerpic = doc.data().buyerpicture;
  
                        var freelancername = doc.data().freelancername;
                        var freelancerid = doc.data().freelancerid;
                        var freelancerpicture = doc.data().freelancerpicture;
                          var category = doc.data().category;
                        var budget = doc.data().budget;
                        var days = doc.data().days;
                      var orderstatus = doc.data().status;
                        var data = {
                          postid:orderid,
                          buyerid:buyerid,
                          name:buyername,
                          budget:budget,
                          category:category,
                          buyerpicture:buyerpic,
                          days:days,
                          date:date,
                          orderstatus:orderstatus,
                          freelancerid:freelancerid,
                          freelancername:freelancername,
                          freelancerimage:freelancerpicture,
                        }
                        firestore.collection('myorders').doc().set(data).then(()=>{
                           firestore.collection('freelancers').where('id','==',freelancerid).get().then((snapshot)=>{
                               snapshot.docs.forEach(doc=>{
                                   var docid = doc.id;
                                   var orders = doc.data().totalorders;
                                   orders = orders+1;
                                   var updatemap={
                                       totalorders:orders
                                   }  
                                   firestore.collection('freelancers').doc(docid).update(updatemap).then(()=>{
                         
                                    res.json({
                                        status:200,
                                        postid:orderid,
                                        id:buyerid,
                                        name:buyername,
                                        budget:budget,
                                        category:category,
                                        buyerpicture:buyerpic,
                                        days:days,
                                        date:date,
                                        orderstatus:orderstatus,
                                        freelancerid:freelancerid,
                                        freelancername:freelancername,
                                        freelancerimage:freelancerpicture,
                                    });
                                });
                               });
                           });
                           
                           
                        });
                       
                    });
                           
                          
                       }); 
                        
                        
                      });

                      
             
                     
              }
        });
    } catch (error) {
        console.log(error);
            res.json({
                status:500
            });
    }
   
};
const rejectoffer = async(req,res,next)=>{
    var orderid = req.body.offerid;
    console.log(orderid);
    try {
        firestore.collection('offers').where('offerid','==',orderid).get().then((snapshot)=>{
            var results = snapshot.docs;
            if(results.length===0){
                res.json({
                    status:404
                });
              }else{
                  snapshot.docs.forEach(doc=>{
                      var docid = doc.id;
                      var updatemap = {
                        status:"Rejected"
                    }
                        firestore.collection('offers').doc(docid).update(updatemap).then(()=>{
                        
                     res.json({
                         status:200
                     });
                           
                          
                       }); 
                        
                        
                      });

                      
             
                     
              }
        });
    } catch (error) {
        console.log(error);
            res.json({
                status:500
            });
    }
   
};
const getorders = async(req,res,next)=>{
    var bid = req.body.buyerid;
    var postid =[];
    var buyerid = [];
    var buyername = [];
    var buyerpic = [];
    var freelancerid = [];
    var freelancername = [];
    var freelancerpic = [];
    var budget = [];
    var category = [];
    var days =[];
     var date=[];
     var status = [];
     
    firestore.collection('myorders').where('buyerid','==',bid).get().then((snapshot)=>{
        var results = snapshot.docs;

        if(results.length == 0){
            res.json({
                status:404
            });
        }else{
            snapshot.docs.forEach(doc=>{
                if(doc.data().orderstatus === 'Rejected'){

                }else{
                    postid .push( doc.data().postid);
                    buyerid .push(doc.data().buyerid) ;
                    buyername .push(doc.data().name) ;
                    buyerpic .push(doc.data().freelancerimage); 
                    freelancerid .push(doc.data().freelancerid) ;
                    freelancername .push(doc.data().freelancername) ;
                    freelancerpic .push(doc.data().freelancerimage);
                    budget .push(doc.data().budget) ;
                    category .push( doc.data().category);
                    days .push(doc.data().days) ;
                     date.push( doc.data().date);
                     status .push(doc.data().orderstatus); 
                }
                 
               
            });
                if(postid.length===0){
                    res.json({
                        status:404
                    });
                }else{
                    res.json({
                        status:200,
                        fid:bid,
                        postid:postid,
                        buyerid :buyerid,
                        buyername :buyername,
                        buyerpic :buyerpic,
                        freelancerid :freelancerid,
                        freelancername :freelancername,
                        freelancerimage :freelancerpic,
                        budget :budget,
                        category :category,
                        days :days,
                         date:date,
                         orderstatus :status,
                    });
                }
           
        }



    });
}

const updateorderstatus = async(req,res,next)=>{
    var orderid = req.body.orderid;
    var orderstatus = req.body.orderstatus;
    var freelancerid;
    firestore.collection('myorders').where('postid','==',orderid).get().then((snapshot)=>{
        var results = snapshot.docs;
        
        if(results.length ===0){
            res.json({
                status:404
            });
        }else{
            snapshot.docs.forEach(doc=>{
                var docid = doc.id;
                console.log(docid);
                freelancerid = doc.data().freelancerid;
                var budget=doc.data().budget;
                var updatemap = {
                  orderstatus:orderstatus
              }
                  firestore.collection('myorders').doc(docid).update(updatemap).then(()=>{
                            if(orderstatus === "Delivered"){
                                    firestore.collection('freelancers').where('id','==',freelancerid).get().then((snapshot)=>{
                                        snapshot.docs.forEach(doc=>{
                                            var freelancerdocid = doc.id;
                                            var completedorders = doc.data().completedorders;
                                            var inqueueorders = doc.data().inqueueorders;
                                            var earning = doc.data().earning;
                                            earning = earning+budget
                                            completedorders = completedorders+1;
                                            inqueueorders = inqueueorders-1;
                                            if(inqueueorders<0){
                                                inqueueorders=0;
                                            }
                                                var updatemap={
                                                    completedorders:completedorders,
                                                    inqueueorders:inqueueorders,
                                                    earning:earning
                                                }
                                            firestore.collection('freelancers').doc(freelancerdocid).update(updatemap).then(()=>{
                                                res.json({
                                                    status:200,
                                                    orderid:orderid
                                                });
                                                      
                                            });

                                        });
                                    });
                            }else{
                              
                                    firestore.collection('freelancers').where('id','==',freelancerid).get().then((snapshot)=>{
                                        snapshot.docs.forEach(doc=>{
                                            var freelancerdocid = doc.id;
                                           var inqueueorders = doc.data().inqueueorders;
                                            inqueueorders = inqueueorders-1;
                                            if(inqueueorders<0){
                                                inqueueorders=0;
                                            }
                                                var updatemap={
                                                   inqueueorders:inqueueorders,
                                                   
                                                }
                                            firestore.collection('freelancers').doc(freelancerdocid).update(updatemap).then(()=>{
                                                res.json({
                                                    status:200,
                                                    orderid:orderid
                                                });
                                                      
                                            });

                                        });
                                    });
                            
                            }
              
                    
                 }); 
                  
                  
                });
        }
    });
}

const givefreelancerrating = async(req,res,next)=>{
    var freelancerid = req.body.freelancerid;
    var newrating = req.body.rating;
    var rating_id =  "rating"+""+ randomInt(10, 50000);

    console.log(newrating);
    firestore.collection('freelancers').where('id','==',freelancerid).get().then((snapshot)=>{
        snapshot.docs.forEach(doc=>{
            var docid= doc.id;
            var totalreviews = Number(doc.data().totalreviews);
            var rating = Number(doc.data().rating);
            totalreviews= totalreviews+1;
            rating = (rating+newrating)/totalreviews;
            var updatemap = {
                totalreviews:totalreviews,
                rating:rating
            }
            firestore.collection('freelancers').doc(docid).update(updatemap).then(()=>{
                res.json({
                    status:200,
                    freelancerid:freelancerid
                });
                      
            });
        });
    });
}

const updatewithprofile = async(req,res,next)=>{
    try { 
        const count = 0;
        var ID= req.body.ID;
        var random  = randomInt(10, 50000);
     var filename = req.file.originalname;
     var id, fullname, phone, email
     , password,
      profilepicture;
     var password = req.body.password;
     var hashpassword;
     email = req.body.email;

     fullname = req.body.fullname;
     phone = req.body.phone;
     email = req.body.email;
  
     profilepicture = filename;
     await bcrypt.hash(password, saltRounds, (err, hash) => {
         hashpassword = hash 
         firestore.collection('buyers').where('id','==',ID).get().then((snapshot)=>{
            snapshot.docs.forEach(doc=>{
                var docid= doc.id;
                var updatemap;
                var data = {
                   
                    fullname:fullname,
                    phone:phone,
                    email:email,
                    password:password,
                     profilepicture:profilepicture,
                        
            }
   
                firestore.collection('buyers').doc(docid).update(data).then(()=>{
                    res.json({
                        status:200,
                        
                        username:fullname,
                        email:email,
                        phone:phone,
                        password:hashpassword,
                        profilepicture:profilepicture,
                       
                      
                    });
                });
            });
       
           
            
          
        
     });;
     
});
    
    
          
    } catch (error) {
        res.json({
            status:500
        });
    }
}

const updatewithoutprofile= async(req,res,next)=>{
    console.log("UPDATE PROFILE");
    try { 
        var ID = req.body.ID;
        const count = 0;
        var random  = randomInt(10, 50000);
     var fullname, phone, email
     , password;
     var password = req.body.password;
     var hashpassword;

     email = req.body.email;

     fullname = req.body.fullname;
     phone = req.body.phone;
     email = req.body.email;
     
    
     await bcrypt.hash(password, saltRounds, (err, hash) => {
         hashpassword = hash;
         
         firestore.collection('buyers').where('id','==',ID).get().then((snapshot)=>{
            snapshot.docs.forEach(doc=>{
                var docid= doc.id;
                var updatemap = {
                   
                    fullname:fullname,
                    phone:phone,
                    email:email,
                    password:hashpassword,
                    
            }
   
                firestore.collection('buyers').doc(docid).update(updatemap).then(()=>{
                    res.json({
                        status:200,
                        
                        username:fullname,
                        email:email,
                        phone:phone,
                        password:password,
                      
                    });
                });
            });
       
           
            
          
        
     });;
     

});
    
     
          
    } catch (error) {
        console.log(error);
        res.json({
            status:500
        });
    }
}
const forgotpassword = async(req,res,next)=>{
    var email = req.body.email;
    var password = "DigitalMazdoor"+randomInt(10, 50000);
    var hashpassword;
    try {
        firestore.collection('buyers').where('email','==',email).get().then((snapshot)=>{
            var result = snapshot.docs;
            if(result.length ===0){
                res.json({
                    status:404
                });
            }else{
                snapshot.docs.forEach(doc=>{
                    var docid = doc.id;
                    
                    bcrypt.hash(password, saltRounds, (err, hash) => {
                        hashpassword = hash 
                        var updatemap={
                            password:hashpassword
                        }
                        
                                    firestore.collection('buyers').doc(docid).update(updatemap).then(()=>{
                                        let transporter = nodemailer.createTransport({
                                            service: 'gmail',
                                            host: "smtp.ethereal.email",
                                            port: 587,
                                            secure: false,// true for 465, false for other ports
                                            auth: {
                                                user: 'mystudymanger@gmail.com',
                                                pass: 'knightkingalpha'
                                              },
                                          });
                                      
                                          
                                          var mailOptions = {
                                            from: "Smart Access Restaurant",
                                            to: req.body.email,
                                            subject: 'Your Credentials',
                                            text: "New Password :"+password
                                          };
                                          
                                          transporter.sendMail(mailOptions, function(error, info){
                                            if (error) {
                                                console.log(error);
                                                
                                                res.send({
                                                    status:500
                                                });
                                            } else {
                                              console.log('Email sent: ' + info.response);
                                              res.json({
                                                  status:200
                                              });
                                            }
                                          });
                                    });
            });
           
                });
            }
        });
    } catch (error) {
        console.log(error);
            res.json({
                status:500
            });
    }   
   
}
const SendFreelancerMessage=async(req,res,next)=>{

        var senderid = req.body.senderid;
        var recieverid = req.body.recieverid;
        var message = req.body.message;
        var type = req.body.type;
        var sendername = req.body.sendername;
        var recievername = req.body.recievername;
        var counter = Number(req.body.counter);
        
        var data = {
            senderid:senderid,
            recieverid:recieverid,
            sendername:sendername,
            recievername:recievername,
            message:message,
            type:type,
            id:counter
        }
            try {
                

                firestore.collection('chats').doc().set(data).then(()=>{
                    firestore.collection('chats')
                    .orderBy('id','desc')
                   
                    .get().then((snapshot)=>{
                        var results=snapshot.docs;
                        if(results.length===0){
                            res.json({
                                status:404
                            });
                        }else{
                            var snames = [];
                            var rnames=[];
                            var sids=[];
                            var rids=[];
                            var messages = [];
                            var types=[];

                            snapshot.docs.forEach((doc)=>{
                                snames.push(doc.data().sendername);
                                rnames.push(doc.data().recievername);
                                sids.push(doc.data().senderid);
                                rids.push(doc.data().recieverid);
                                messages.push(doc.data().message);
                                types.push(doc.data().type);
                            });

                            res.json({
                                status:200,
                                sendername:snames,
                                recievername:rnames,
                                senderid:sids,
                                recieverid:rids,
                                message:messages,
                                type:types,
                                counter:counter
                            });
                        }
                    });
                 });
            } catch (error) {
                console.log(error);
                res.json({
                    status:500
                });
            }
};
const SendFreelancerImages = async(req,res,next)=>{
    
    var senderid = req.body.senderid;
    var recieverid = req.body.recieverid;
    var message =req.file.originalname;
    var type = req.body.type;
    var sendername = req.body.sendername;
    var recievername = req.body.recievername;
    var counter = Number(req.body.counter);
    
    var data = {
        senderid:senderid,
        recieverid:recieverid,
        sendername:sendername,
        recievername:recievername,
        message:message,
        type:type,
        id:counter
    }
    try {
                

        firestore.collection('chats').doc().set(data).then(()=>{
            firestore.collection('chats')
            .orderBy('id','desc')
           
            .get().then((snapshot)=>{
                var results=snapshot.docs;
                if(results.length===0){
                    res.json({
                        status:404
                    });
                }else{
                    var snames = [];
                    var rnames=[];
                    var sids=[];
                    var rids=[];
                    var messages = [];
                    var types=[];

                    snapshot.docs.forEach((doc)=>{
                        snames.push(doc.data().sendername);
                        rnames.push(doc.data().recievername);
                        sids.push(doc.data().senderid);
                        rids.push(doc.data().recieverid);
                        messages.push(doc.data().message);
                        types.push(doc.data().type);
                    });

                    res.json({
                        status:200,
                        sendername:snames,
                        recievername:rnames,
                        senderid:sids,
                        recieverid:rids,
                        message:messages,
                        type:types,
                        counter:counter
                    });
                }
            });
         });
    } catch (error) {
        console.log(error);
        res.json({
            status:500
        });
    }
}

const getfreelancers = async(req,res,next)=>{
    var freelancerid = req.body.freelancerid;

    var buyername=[];
    var buyerimage=[];
    var messages=[];
    var buyeremail=[];
    var types=[];
    var buyerid=[];

        firestore.collection('chats').where('senderid','==',freelancerid).get().then((snapshot)=>{
                    var results = snapshot.docs;
                    if(results.length ===0){
                        firestore.collection('chats').where('recieverid','==',freelancerid).get().then((snapshot2)=>{
                            var results = snapshot2.docs;
                            if(results.length === 0){
                                res.json({
                                    status:404
                                });
                            }    else{
                                snapshot2.docs.forEach(doc=>{
                                    var byid = doc.data().senderid;
                                    buyerid.push(doc.data().senderid);
                                buyername.push(doc.data().sendername);
                                messages.push(doc.data().message);
                                types.push(doc.data().type);
                                    firestore.collection('freelancers').where('id','==',byid).get().then((buyersnapshot)=>{
                                        buyersnapshot.docs.forEach(doc=>{

                                            buyerimage.push(doc.data().profilepicture);
                                            buyeremail.push(doc.data().email);
                                        });
                                       
                                    });
                                  
                                });
                               setTimeout(() => {
                                res.json({
                                    status:200,
                                    buyerid:buyerid,
                                    buyername:buyername,
                                    buyeremail:buyeremail,
                                    buyerimage:buyerimage,
                                    messages:messages,
                                    type:types,
                                    
                                });
                               }, 1000);
                            }
                            
                           
                        });
                    }else{

                            snapshot.docs.forEach(doc=>{
                                var byid = doc.data().recieverid;
                                buyerid.push(doc.data().recieverid);
                                buyername.push(doc.data().recievername);
                                messages.push(doc.data().message);
                                types.push(doc.data().type);
                                firestore.collection('freelancers').where('id','==',byid).get().then((buyersnapshot)=>{
                                         
                                    buyersnapshot.docs.forEach(doc=>{
                                                buyerimage.push(doc.data().profilepicture);
                                                buyeremail.push(doc.data().email);
                                                console.log(doc.data().profilepicture);
                                            });
                                            
                                           
                                    });
                               
                            });
                            setTimeout(() => {
                                firestore.collection('chats').where('recieverid','==',freelancerid).get().then((snapshot2)=>{
                                   
                                   var results = snapshot2.docs;

                                    if(results.length == 0){
                                        res.json({
                                            status:200,
                                            buyerid:buyerid,
                                            buyername:buyername,
                                            buyeremail:buyeremail,
                                            buyerimage:buyerimage,
                                            messages:messages,
                                            type:types,
                                            
                                        });
                                    }else{
                                        snapshot2.docs.forEach(doc=>{
                                            var byid = doc.data().senderid;
                                            buyerid.push(doc.data().senderid);
                                        buyername.push(doc.data().sendername);
                                        messages.push(doc.data().message);
                                        types.push(doc.data().type);
                                            firestore.collection('freelancers').where('id','==',byid).get().then((buyersnapshot)=>{
                                                buyersnapshot.docs.forEach(doc=>{
        
                                                    buyerimage.push(doc.data().profilepicture);
                                                    buyeremail.push(doc.data().email);
                                                });
                                               
                                            });
                                          
                                        });
                                       setTimeout(() => {
                                        res.json({
                                            status:200,
                                            buyerid:buyerid,
                                            buyername:buyername,
                                            buyeremail:buyeremail,
                                            buyerimage:buyerimage,
                                            messages:messages,
                                            type:types,
                                            
                                        });
                                       }, 500);
                                    }
                                   
                            });
                            }, 800);
                    }
        });

        
}

const getfreelancerdetails = async(req,res,next)=>{
    var freelancerid = req.body.freelancerid;
    console.log(freelancerid);
    var freelancername ;
   var  freelanceremail;
   var  freelancerimage ;
   var  freelancercity;
   var  freelancerreviews;
   var  freelancerrating;
   var  datejoined;
   var  profilesummary ;
   var  maincategory;
   var  subcategory;
   var  ordercompleted;
        firestore.collection('freelancers').where('id','==',freelancerid).get().then((snapshot)=>{
                snapshot.docs.forEach(doc=>{
                     freelancername = doc.data().fullname;
                     freelanceremail = doc.data().email;
                     freelancerimage = doc.data().profilepicture;
                     freelancercity = doc.data().city;
                     freelancerreviews = doc.data().totalreviews;
                     freelancerrating = doc.data().rating;
                     datejoined = doc.data().dateofjoining;
                     profilesummary = doc.data().profilesummary;
                     maincategory = doc.data().workingcategory;
                     subcategory = doc.data().subcategories;
                     ordercompleted = doc.data().completedorders;
                    
                    
                });
                res.json({
                    status:200,
                    freelancername:freelancername,
                    freelanceremail:freelanceremail,
                    freelancerimage :freelancerimage,
                    freelancercity:freelancercity,
                    freelancerreviews :freelancerreviews,
                    freelancerrating :freelancerrating,
                    datejoined :datejoined,
                    profilesummary :profilesummary,
                    maincategory :maincategory,
                    subcategory:subcategory,
                    ordercompleted:ordercompleted

                });
        });

    };
module.exports = {
    addbuyer,
    loginbuyer,
    getgigs,
    placeorder,
    placerequests,
    getrequests,
    deleterequest,
    getorderrequest,
    getorders,
    acceptorder,
    getoffers,
    rejectoffer,
    updateorderstatus,
    givefreelancerrating,
    updatewithprofile,
    updatewithoutprofile,
    forgotpassword,
    SendFreelancerMessage,
    SendFreelancerImages,
    getfreelancers,
    getfreelancerdetails
}