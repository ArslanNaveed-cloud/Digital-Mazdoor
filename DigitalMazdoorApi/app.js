'use strict';
const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const config = require('./config');
const freelancerRoute = require('./routes/freelancerroutes');
const buyerRoutes = require('./routes/buyerroutes');
// app.use(express.json());

const app = express();
app.use(cors());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use('/assets',express.static('public/assets'));
var path = require('path');
app.use(express.static(path.join(__dirname,'public')));

const session = require('express-session');
app.use(session({
    key: 'user_sid',
    secret: 'KLAKLSDAmlad1233A^&*?:>DADLpkvnjvnbsbjz@#@',
    rolling: true,
    resave: false,
    saveUninitialized: false,
  }));
app.use('/freelancerapi', freelancerRoute.routes);
app.use('/buyerapi', buyerRoutes.routes);




app.listen(config.port, () => console.log('App is listening on url http://localhost:' + config.port));