const express = require('express');
const {addbuyer,
        loginbuyer,
        getgigs,
        placeorder,
        placerequests,
        getrequests,
        deleterequest,
        getorderrequest,
        getorders,
        getoffers,
        acceptorder,
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
      } = require('../controllers/buyercontroller');

const multer = require('multer');
const router = express.Router();

const randomInt = require('random-int');

const storage = multer.diskStorage({
    
    destination: './public/assets/freelancersprofilepictures/',
    filename: function(req, file, cb) {
      cb(null,file.originalname);
    }
  });

  const storage2 = multer.diskStorage({
    
    destination: './public/assets/messagesmedia/',
    filename: function(req, file, cb) {
      cb(null,file.originalname);
    }
  });
  const fileFilter = (req, file, cb) => {
    // reject a file
    
      cb(null, true);
    
  };

//Init Upload
const upload = multer({
    storage: storage,
    });

    //Init Upload
const upload2 = multer({
  storage: storage2,
  });
router.post('/addbuyer',upload.single('file'), addbuyer);
 router.post('/loginbuyer', loginbuyer);
 router.post('/getgigs',getgigs);
 router.post('/placeorder',placeorder);
 router.post('/placerequests',placerequests);
 router.post('/getrequests',getrequests);
 router.post('/deleterequest',deleterequest);
 router.post('/getorderrequest',getorderrequest);
 router.post('/getorders',getorders);
 router.post('/getoffers',getoffers);
 router.post('/acceptoffer',acceptorder);
 router.post('/rejectoffer',rejectoffer);
 router.post('/updateorderstatus',updateorderstatus);
 router.post('/givefreelancerrating',givefreelancerrating);
router.post('/updatewithprofile',upload.single('file'),updatewithprofile);
router.post('/updatewithoutprofile',updatewithoutprofile);
router.post('/forgotpassword',forgotpassword);
router.post('/SendFreelancerMessage',SendFreelancerMessage);
router.post('/SendFreelancerImages',upload2.single('file'),SendFreelancerImages);
router.post('/getfreelancers',getfreelancers);
 
router.post('/getfreelancerdetails',getfreelancerdetails);


module.exports = {
    routes: router
}