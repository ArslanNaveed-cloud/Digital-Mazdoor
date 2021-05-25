const express = require('express');
const {addfreelancer,
        loginfreelancer,
        savegigs,
        getgigs,
        deletegig,
        editgigswithpics,
        editgigswithoutpics,
        getrequest,
        sendoffer,
        getordersrequest,
        acceptorder,
        rejectorder,
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
      } = require('../controllers/freelancercontroller');

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
    
    destination: './public/assets/freelancergigpictures/',
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


    const upload2 = multer({
      storage: storage2,
      });
router.post('/addfreelancer',upload.single('file'), addfreelancer);
router.post('/savegig',upload2.single('file'), savegigs);

router.post('/getgigs',getgigs);
router.post('/deletegig',deletegig);
router.post('/freelancerlogin', loginfreelancer);
router.post('/editgigs',upload2.single('file'),editgigswithpics);

router.post('/editgigswithoutpics',editgigswithoutpics);
router.post('/getrequests',getrequest);
router.post('/sendoffer',sendoffer);
router.post('/getorderrequest',getordersrequest);
router.post('/acceptorder',acceptorder);
router.post('/rejectorder',rejectorder);
router.post('/getoffers',getoffers);
router.post('/getorders',getorders);
router.post('/givebuyerrating',givebuyerrating);
router.post('/updatewithprofile',upload.single('file'),updatewithprofile);
router.post('/updatewithoutprofile',updatewithoutprofile);
router.post('/forgotpassword',forgotpassword);
router.post('/getbuyers',getbuyers);
router.post('/getmessages',getmessages);
router.post('/getdetails',getdetails);
router.post('/getpendingorders',getpendingorders);
router.post('/getbuyerdetails',getbuyerdetails);

// router.get('/students', getAllStudents);
// router.get('/student/:id', getStudent);
// router.put('/student/:id', updateStudent);
// router.delete('/student/:id', deleteStudent);


module.exports = {
    routes: router
}