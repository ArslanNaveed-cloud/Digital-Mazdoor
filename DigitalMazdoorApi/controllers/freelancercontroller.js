'use strict';

const firebase = require('../db');
const firestore = firebase.firestore();
const randomInt = require('random-int');
const bcrypt = require("bcrypt");
const saltRounds = 10;                          //We are setting salt rounds, higher is safer.
const myPlaintextPassword = 's0/\/\P4$$w0rD'; 
var forEach = require('async-foreach').forEach;
const moment = require("moment");


const addfreelancer = async (req, res, next) => {

   
       try { 
           const count = 0;
           var random  = randomInt(10, 50000);
        var filename = req.file.originalname;
        var id, fullname, phone, email
        , password,workingcategory, subcategories,
         profilepicture, rating, dateofjoining,city, status,profilesummary;
        var password = req.body.password;
        var hashpassword;
        await bcrypt.hash(password, saltRounds, (err, hash) => {
            hashpassword = hash 
});
        email = req.body.email;

        firestore.collection('freelancers').where('email','==',email).get().then((snapshot)=>{
            
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
                workingcategory = req.body.workingcategory;
                subcategories = req.body.subcategories;
                profilepicture = filename;
                rating= req.body.rating;
                dateofjoining = date;
                profilesummary = req.body.profilesummary;
                city = req.body.city;
                status = req.body.status;
                var data = {
                    id:id,
                    fullname:fullname,
                    phone:phone,
                    email:email,
                    password:password,
                    workingcategory:workingcategory,
                    subcategories:subcategories,
                    profilepicture:profilepicture,
                    rating:0,
                    dateofjoining:dateofjoining,
                    city:city,
                    totalgigs:0,
                    completedorders:0,
                    earning:0,
                    totalorders:0,
                    totalreviews:0,
                    inqueueorders:0,
                    profilesummary:profilesummary,
                    status:status
            }

            firestore.collection('freelancers').doc().set(data).then(()=>{
               res.json({
                   status:200,
                   
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
const loginfreelancer = async (req, res, next) => {

   
    try { 
        var email = req.body.email;
        var password = req.body.password;


     firestore.collection('freelancers').where('email','==',email).get().then((snapshot)=>{
         
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
                            var city, phone, email, id,workingcategory, subcategories,
                             profilepicture, rating, dateofjoining,summary,totalgigs,totalorders
                             ,completedorders,earnings,totalreviews, status;
                      
                            var username = doc.data().fullname;
                            id = doc.data().id;
                            workingcategory  = doc.data().workingcategory;
                            subcategories = doc.data().subcategories;
                            email = doc.data().email;
                            profilepicture = doc.data().profilepicture;
                            rating = doc.data().rating;
                            dateofjoining = doc.data().dateofjoining;
                            status = doc.data().status;
                            phone = doc.data().phone;
                            city = doc.data().city;
                            totalgigs = doc.data().totalgigs
                            summary = doc.data().profilesummary;
                            earnings = doc.data().earnings;
                            totalorders = doc.data().totalorders;
                            completedorders = doc.data().completedorders;
                            totalreviews = doc.data().totalreviews;

                            res.json({
                                status:200,
                                id:id,
                                username:username,
                                workingcat:workingcategory,
                                subcat:subcategories,
                                email:email,
                                phone:phone,
                                password:password,
                                profilepicture:profilepicture,
                                rating:rating,
                                dateofjoining:dateofjoining,
                                city:city,
                                totalgigs:totalgigs,
                                accountstatus:status,
                                summary:summary,
                                earnings:earnings,
                                totalorders:totalorders,
                                completedorders:completedorders,
                                totalreviews:totalreviews
                            });
                        }else{
                            res.json({
                                status:404
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

const savegigs= async (req,res,next)=>{
    try { 
        const count = 0;
        var random  = randomInt(10, 50000);
     var filename = req.file.originalname;
     var city="",gigtitle="",gigprice="",gigdescription="",gigdays="",id,username,gigid,category,dateofuploading;
    gigtitle = req.body.title;
    id = req.body.id;

     firestore.collection('gigs').where('gigtitle','==',gigtitle).where('id','==',id).get().then((snapshot)=>{
         
        var result = snapshot.docs;
         if(result.length === 0){
             var currenttime = moment().format("h:mma");
    
 
             var now = moment().format("DD-MM-YYYY");
             var date = now+"("+currenttime+")";
             id = req.body.id;
             username = req.body.username;
             gigid="gig_"+random;
             gigtitle = req.body.title;
             gigprice = req.body.price;
             filename = filename;
             category = req.body.category;
             gigdescription = req.body.description;
             gigdays= req.body.deliverydays;
             dateofuploading = date;
            city = req.body.city;
             var data = {
                id : id,
                username : username,
                gigid:gigid,
                gigtitle : gigtitle,
                gigprice : gigprice,
                coverimage : filename,
                category : category,
                gigdescription :gigdescription,
                gigdays: gigdays,
                dateofuploading : dateofuploading,
                city:city,
                
         }

         firestore.collection('gigs').doc().set(data).then(()=>{
            
            firestore.collection('freelancers').where('id','==',id).get().then((snapshot)=>{
                snapshot.docs.forEach(doc=>{
                    var docid = doc.id
                    var totalgigs = doc.data().totalgigs;
                    totalgigs=totalgigs+1;
                    var updatemap = {
                        totalgigs:totalgigs
                     }
                     firestore.collection('freelancers').doc(docid).update(updatemap).then(()=>{
                        res.json({
                            status:200,
                            totalgigs:totalgigs
                        });
                     });
                });
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
const getgigs = async(req,res,next)=>{

    try {
        var freelancerid = req.body.id;
        var freelancername = req.body.username;
        console.log(freelancerid+""+freelancername);
       await firestore.collection('gigs').where('id','==',freelancerid).get().then((snapshot)=>{
            var result = snapshot.docs;
            if(result.length === 0){
                res.json({
                    status:404
                });
            }else{
                  var gigtitle=[];
                  var gigcategory=[];
                 var gigprice=[];
                 var gigdeliverytime=[];
                 var  gigimage=[];
                 var  gigdescription=[];
                 var gigid=[];
                 var gigdateofuploading = [];
                 result.forEach(doc=>{
                    gigtitle.push(doc.data().gigtitle);
                    gigcategory.push(doc.data().category);
                    gigprice.push(doc.data().gigprice);
                    gigdeliverytime.push(doc.data().gigdays);
                    gigimage.push(doc.data().coverimage);
                    gigdescription.push(doc.data().gigdescription);
                    gigid.push(doc.data().gigid);
                    gigdateofuploading.push(doc.data().dateofuploading);
                    
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
                    gigdateofuploading:gigdateofuploading
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

const deletegig = async(req,res,next)=>{
    var username = req.body.username;
    var userid = req.body.userid;
    try {
        await firestore.collection('gigs').where('id','==',userid).where('username','==',username).get().then((snapshot)=>{
            snapshot.forEach(function(doc) {
                firestore.collection('gigs').doc(doc.id).delete().then(()=>{
                    firestore.collection('freelancers').where('id','==',userid).get().then((snapshot)=>{
                        snapshot.docs.forEach(doc=>{
                            var docid = doc.id
                            var totalgigs = doc.data().totalgigs;
                            totalgigs=totalgigs-1;
                            var updatemap = {
                                totalgigs:totalgigs
                             }
                             firestore.collection('freelancers').doc(docid).update(updatemap).then(()=>{
                                res.json({
                                    status:200,
                                    totalgigs:totalgigs
                                });
                             });
                        });
                    });
                });
              });
          
        });
    } catch (error) {
        console.log(error.message);
        res.json({
            status:500
        });
    }
    

};

const editgigswithpics = async (req,res,next)=>{
    var filename = req.file.originalname;
    var maincat="",gigtitle="",gigprice="",gigdescription="",gigdays="",id,username,gigid,category,dateofuploading;
    gigid = req.body.gigid;
    console.log(gigid);
    try {
        firestore.collection('gigs').where('gigid','==',gigid).get().then((snapshot)=>{
            snapshot.docs.forEach(doc=>{

                var docid = doc.id
                console.log(docid);
                username = req.body.username;
                 gigtitle = req.body.title;
                gigprice = req.body.price;
                filename = filename;
                category = req.body.category;
                gigdescription = req.body.description;
                gigdays= req.body.deliverydays;
                
               
                var updatemap = {
                    gigtitle : gigtitle,
                    gigprice : gigprice,
                    coverimage : filename,
                    category : category,
                    gigdescription :gigdescription,
                    gigdays: gigdays,
                  
                 }
                 firestore.collection('gigs').doc(docid).update(updatemap).then(()=>{
                    res.json({
                        status:200,
                        
                    });
                 });
            });
        });
    } catch (error) {
        console.log("Error");
    }
  

}
const editgigswithoutpics = async (req,res,next)=>{
    var maincat="",gigtitle="",gigprice="",gigdescription="",gigdays="",id,username,gigid,category,dateofuploading;
    gigid = req.body.gigid;
    console.log(gigid);
    try {
        firestore.collection('gigs').where('gigid','==',gigid).get().then((snapshot)=>{
            snapshot.docs.forEach(doc=>{

                var docid = doc.id
                console.log(docid);
                username = req.body.username;
                 gigtitle = req.body.title;
                gigprice = req.body.price;
               
                category = req.body.category;
                gigdescription = req.body.description;
                gigdays= req.body.deliverydays;
                
               
                var updatemap = {
                    gigtitle : gigtitle,
                    gigprice : gigprice,
                   
                    category : category,
                    gigdescription :gigdescription,
                    gigdays: gigdays,
                  
                 }
                 firestore.collection('gigs').doc(docid).update(updatemap).then(()=>{
                    res.json({
                        status:200,
                        
                    });
                 });
            });
        });
    } catch (error) {
        console.log("Error");
    }
  

}

const getrequest= async(req,res,next)=>{
   
    try {
        var mcategory = req.body.category;
        var mcity = req.body.city;
        console.log(mcategory+"=="+city);
    
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
        firestore.collection('request').where('category','==',mcategory).where('city','==',mcity).get().then((snapshot)=>{
                var results = snapshot.docs;
                    console.log(results);
            if(results.length === 0){
                res.json({
                    status:404                
                });
            }else{
                snapshot.docs.forEach(doc=>{
                        name.push(doc.data().buyername);
                        id.push(doc.data().buyerid);
                        postid.push(doc.data().postid);
                         description.push(doc.data().description);
                        budget.push(doc.data().budget);
                        category.push(doc.data().category);
                        buyerpic.push(doc.data().buyerpic);
                        title.push(doc.data().title);
                        days.push(doc.data().days);
                        bids.push(doc.data().bids);
                        date.push(doc.data().date);
                        orderstatus.push(doc.data().status);
                });

                res.json({
                    status:200,
                    postid:postid,
                    id :id,
                    name :name,
                    description :description,
                    budget :budget,
                    category :category,
                    buyerpic :buyerpic,
                    title :title,
                    days:days,
                    bids :bids,
                    date:date,
                    orderstatus:orderstatus
                });
        }
        });
    } catch (error) {
        res.json({
            status:500
        });
    }
}
const sendoffer = async(req,res,next)=>{
    var currenttime = moment().format("h:mma");
    
 
    var now = moment().format("DD-MM-YYYY");
    var date = now+"("+currenttime+")";
    var random  = randomInt(10, 50000);
    var buyername = req.body.buyername
    var buyerid=req.body.buyerid;
    var freelancername = req.body.freelancername;
    var freelancerid = req.body.freelancerid
    var description=req.body.description;
    var budget= req.body.budget;
    var category=req.body.category;
    var numberofdays= req.body.numberofdays;
    var postid = req.body.postid;
    var freelancerpicture = req.body.freelancerpicture;
    var buyerpicture = req.body.buyerpicture;
    var title = req.body.title;
    try {
        var data={
            offerid:"offer_"+random,
            postid:postid,
            title:title,
            buyername:buyername,
            buyerid:buyerid,
            freelancername:freelancername,
            freelancerid:freelancerid,
            description:description,
            budget:budget,
            category:category,
            date:date,
            days:numberofdays,
            freelancerpicture:freelancerpicture,
            buyerpicture:buyerpicture,
            status:"Pending"
        }
        firestore.collection('offers').doc().set(data).then((snapshot)=>{
            firestore.collection('request').where("postid",'==',postid).get().then((snapshot)=>{
                    var results = snapshot.docs;
                    if(results===0){
                        res.json({
                            status:404
                        });
                    }else{
                        snapshot.docs.forEach(doc=>{
                            var docid = doc.id;
                            var bids = doc.data().bids;
                            var updatemap = {
                                bids:bids+1
                            }
                            firestore.collection('request').doc(docid).update(updatemap).then(()=>{
                                res.json({
                                    status:200
                                });
                            });
                        });
                    }
            });
    
        });
    } catch (error) {
        res.json({
            status:500
        });
    }
   
}
const getoffers = async(req,res,next)=>{
     try {
       
        var freelancerid = req.body.freelancerid;
   
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
        firestore.collection('offers').where('freelancerid','==',freelancerid).get().then((snapshot)=>{
                var results = snapshot.docs;
                    console.log(results);
            if(results.length === 0){
                res.json({
                    status:404                
                });
            }else{
                snapshot.docs.forEach(doc=>{
                    if(doc.data().status ==='Accepted'){

                    }else{
                        name.push(doc.data().buyername);
                        id.push(doc.data().buyerid);
                        postid.push(doc.data().offerid);
                         description.push(doc.data().description);
                        budget.push(doc.data().budget);
                        category.push(doc.data().category);
                        title.push(doc.data().title);
                        days.push(doc.data().days);
                        bids.push(doc.data().bids);
                        date.push(doc.data().date);
                        orderstatus.push(doc.data().status);
                        buyerpicture.push(doc.data().buyerpicture);
                    }
                     
                });
if(name.length===0){
    res.json({
        status:404
    });
}else{ res.json({
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
});}
               
        }
        });
    } catch (error) {
        res.json({
            status:500
        });
    }

}

const getordersrequest = async(req,res,next)=>{
    var freelancerid = req.body.freelancerid;

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
    firestore.collection('orders').where('freelancerid','==',freelancerid).where('orderstatus','==','Pending').get().then((snapshot)=>{
        var results = snapshot.docs;
        if(results.length === 0){
            res.json({
                status:404
            });
        }else{
                snapshot.docs.forEach(doc=>{
                    buyername.push(doc.data().buyername);
                    buyerid.push(doc.data().buyerid);
                    gigid.push(doc.data().gigid);
                    gigtitle.push(doc.data().gigtitle);
                    orderid.push(doc.data().ordderid);
                    days.push(doc.data().days);
                    orderstatus.push(doc.data().orderstatus);
                    date.push(doc.data().orderdate);
                    buyerpic.push(doc.data().profilepicture);
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

const acceptorder = async(req,res,next)=>{
    var orderid = req.body.orderid;
    console.log(orderid);
    try {
        firestore.collection('orders').where('ordderid','==',orderid).get().then((snapshot)=>{
            var results = snapshot.docs;
            if(results.length===0){
                res.json({
                    status:404
                });
              }else{
                  snapshot.docs.forEach(doc=>{
                      var docid = doc.id;
                      var updatemap = {
                        orderstatus:"Accepted"
                    }
                        firestore.collection('orders').doc(docid).update(updatemap).then(()=>{
                        
                      firestore.collection('orders').where('ordderid','==',orderid).get().then((snapshot)=>{
                        var orderid = "orders_"+ randomInt(10, 50000);
                        var currenttime = moment().format("h:mma");
      
   
                        var now = moment().format("DD-MM-YYYY");
                        var date = now+"("+currenttime+")";
                        var buyername = doc.data().buyername;
                        var buyerid = doc.data().buyerid;
                        var buyerpic = doc.data().profilepicture;
  
                        var freelancername = doc.data().freelancername;
                        var freelancerid = doc.data().freelancerid;
                        var freelancerpicture = doc.data().freelancerimage;
                          var category = doc.data().category;
                        var budget = doc.data().budget;
                        var days = doc.data().days;
                      var orderstatus = doc.data().orderstatus;
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
                                   var inqueueorders = doc.data().inqueueorders;
                                   orders = orders+1;
                                   inqueueorders = inqueueorders+1;
                                   var updatemap={
                                       totalorders:orders,
                                       inqueueorders:inqueueorders
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
const rejectorder = async(req,res,next)=>{
    var orderid = req.body.orderid;
    try {
        firestore.collection('orders').where('ordderid','==',orderid).get().then((snapshot)=>{
            var results = snapshot.docs;
            if(results.length===0){
                res.json({
                    status:404
                });
              }else{
                  snapshot.docs.forEach(doc=>{
                      var docid = doc.id;
                      var updatemap = {
                          orderstatus:"Rejected"
                      }
                      firestore.collection('orders').doc(docid).update(updatemap).then(()=>{
                        res.json({
                            status:200,
                            
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
    var fid = req.body.freelancerid;
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
     
    firestore.collection('myorders').where('freelancerid','==',fid).get().then((snapshot)=>{
        var results = snapshot.docs;

        if(results.length == 0){
            res.json({
                status:404
            });
        }else{
            snapshot.docs.forEach(doc=>{
                 
                 postid .push( doc.data().postid);
                 buyerid .push(doc.data().buyerid) ;
                 buyername .push(doc.data().name) ;
                 buyerpic .push(doc.data().buyerpicture); 
                 freelancerid .push(doc.data().freelancerid) ;
                 freelancername .push(doc.data().freelancername) ;
                 freelancerpic .push(doc.data().freelancerimage);
                 budget .push(doc.data().budget) ;
                 category .push( doc.data().category);
                 days .push(doc.data().days) ;
                  date.push( doc.data().date);
                  status .push(doc.data().orderstatus); 
            });

            res.json({
                status:200,
                fid:fid,
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



    });
}


const givebuyerrating = async(req,res,next)=>{
    var buyerid = req.body.freelancerid;
    var newrating = req.body.rating;
    var rating_id =  "rating"+""+ randomInt(10, 50000);

    console.log(newrating);
    firestore.collection('buyers').where('id','==',buyerid).get().then((snapshot)=>{
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
            firestore.collection('buyers').doc(docid).update(updatemap).then(()=>{
                res.json({
                    status:200,
                    freelancerid:buyerid
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
      profilepicture,profilesummary;
     var password = req.body.password;
     var hashpassword;
     email = req.body.email;

     fullname = req.body.fullname;
     phone = req.body.phone;
     email = req.body.email;
  
     profilepicture = filename;
     profilesummary = req.body.profilesummary;
     await bcrypt.hash(password, saltRounds, (err, hash) => {
         hashpassword = hash 
         firestore.collection('freelancers').where('id','==',ID).get().then((snapshot)=>{
            snapshot.docs.forEach(doc=>{
                var docid= doc.id;
                var updatemap;
                var data = {
                   
                    fullname:fullname,
                    phone:phone,
                    email:email,
                    password:hashpassword,
                     profilepicture:profilepicture,
                    profilesummary:profilesummary,
                    
            }
   
                firestore.collection('freelancers').doc(docid).update(data).then(()=>{
                    res.json({
                        status:200,
                        
                        username:fullname,
                        email:email,
                        phone:phone,
                        password:password,
                        profilepicture:profilepicture,
                         summary:profilesummary,
                      
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
     , password,
    profilesummary;
     var password = req.body.password;
     var hashpassword;

     email = req.body.email;

     fullname = req.body.fullname;
     phone = req.body.phone;
     email = req.body.email;
     
    
     profilesummary = req.body.profilesummary;
     await bcrypt.hash(password, saltRounds, (err, hash) => {
         hashpassword = hash;
         
         firestore.collection('freelancers').where('id','==',ID).get().then((snapshot)=>{
            snapshot.docs.forEach(doc=>{
                var docid= doc.id;
                var updatemap = {
                   
                    fullname:fullname,
                    phone:phone,
                    email:email,
                    password:hashpassword,
                    profilesummary:profilesummary,
                    
            }
   
                firestore.collection('freelancers').doc(docid).update(updatemap).then(()=>{
                    res.json({
                        status:200,
                        
                        username:fullname,
                        email:email,
                        phone:phone,
                        password:password,
                        summary:profilesummary,
                      
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
        firestore.collection('freelancers').where('email','==',email).get().then((snapshot)=>{
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
                        
                                    firestore.collection('freelancers').doc(docid).update(updatemap).then(()=>{
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


const getbuyers = async(req,res,next)=>{
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
                                    firestore.collection('buyers').where('id','==',byid).get().then((buyersnapshot)=>{
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
                                firestore.collection('buyers').where('id','==',byid).get().then((buyersnapshot)=>{
                                         
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
                                            firestore.collection('buyers').where('id','==',byid).get().then((buyersnapshot)=>{
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
const getmessages = async(req,res,next)=>{
    try {
                
                var countera = 0;
        
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
                    var id;
                    snapshot.docs.forEach((doc)=>{
                        snames.push(doc.data().sendername);
                        rnames.push(doc.data().recievername);
                        sids.push(doc.data().senderid);
                        rids.push(doc.data().recieverid);
                        messages.push(doc.data().message);
                        types.push(doc.data().type);
                        if(countera ===0){
                            id =doc.data().id;
                            console.log(id);
                        }else{
                            countera++;
                        }
                    });

                    res.json({
                        status:200,
                        sendername:snames,
                        recievername:rnames,
                        senderid:sids,
                        recieverid:rids,
                        message:messages,
                        type:types,
                        counter:id
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
const getdetails = async(req,res,next)=>{
    var id = req.body.id;
    console.log(id);
    var catvalues = [];
    firestore.collection('freelancers').where('id','==',id).get().then((snapshot)=>{
        var results = snapshot.docs;
        if(results.length===0){
            res.json({
                status:404
            });
        }else{
            snapshot.docs.forEach(doc=>{
                catvalues.push(doc.data().earning);
                catvalues.push(doc.data().totalgigs);
                catvalues.push(doc.data().totalorders);
                catvalues.push(doc.data().totalreviews);
                catvalues.push(doc.data().inqueueorders);
                catvalues.push(doc.data().completedorders);
            });
            res.json({
                status:200,
                catvalues:catvalues
            });
        }
    });
}
const getpendingorders= async(req,res)=>{
    var fid = req.body.freelancerid;
    console.log("ID == "+fid);
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
     
    firestore.collection('myorders').where('freelancerid','==',fid).where('orderstatus','==','Pending').get().then((snapshot)=>{
        var results = snapshot.docs;

        if(results.length == 0){
            res.json({
                status:404
            });
        }else{
            snapshot.docs.forEach(doc=>{
                 
                 postid .push( doc.data().postid);
                 buyerid .push(doc.data().buyerid) ;
                 buyername .push(doc.data().name) ;
                 buyerpic .push(doc.data().buyerpicture); 
                 freelancerid .push(doc.data().freelancerid) ;
                 freelancername .push(doc.data().freelancername) ;
                 freelancerpic .push(doc.data().freelancerimage);
                 budget .push(doc.data().budget) ;
                 category .push( doc.data().category);
                 days .push(doc.data().days) ;
                  date.push( doc.data().date);
                  status .push(doc.data().orderstatus); 
            });

            res.json({
                status:200,
                fid:fid,
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



    });
}
const getbuyerdetails = async(req,res,next)=>{
        var buyerid=req.body.buyerid ;
        var buyername;
        var buyeremail;
        var buyerimage;
        var buyercity;
        var buyerreviews;
        var buyerrating;
        var datejoined;
        var city;
            firestore.collection('buyers').where('id','==',buyerid).get().then((snapshot)=>{
                    snapshot.docs.forEach(doc=>{
                        buyername = doc.data().fullname;
                        buyeremail = doc.data().email;
                        buyerimage = doc.data().profilepicture;
                        buyercity = doc.data().city;
                        buyerreviews = doc.data().totalreviews;
                        buyerrating = doc.data().rating;
                        datejoined = doc.data().dateofjoining;
                        city = doc.data().city;
                        
                    });
                    res.json({
                        status:200,
                        buyername :buyername,
                        buyeremail :buyeremail,
                        buyerimage :buyerimage,
                        reviews:buyerreviews,
                        rating :buyerrating,
                        datejoined :datejoined,
                        city:city
                    });
            });

        };
module.exports = {
    addfreelancer,
    loginfreelancer,
    savegigs,
    getgigs,
    deletegig,
    editgigswithpics,
    editgigswithoutpics,
    getrequest,
    sendoffer,
    getordersrequest,
    rejectorder,
    acceptorder,
    getoffers,
    getorders,
    givebuyerrating,
    updatewithprofile,
    updatewithoutprofile,
    forgotpassword,
    getbuyers,
    getmessages,
    getdetails,
    getpendingorders,
    getbuyerdetails
}