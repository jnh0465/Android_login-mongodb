//npm install mongodb
//npm install express
//npm install body-parser

var mongodb = require('mongodb');
//var ObjectID = mongodb.ObjectID;
var express = require('express');
var bodyParser = require('body-parser');

var app=express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));

var MongoClient = mongodb.MongoClient;
var url = 'mongodb://localhost:27017';
MongoClient.connect(url,{useNewUrlParser:true}, function(err,client){
  if(err)
    console.log('disconnect mongodb', err);
  else {
    console.log('connect mongodb');

    //user register
    app.post('/register', (request,response,next)=>{
      var post_data = request.body; //post로 넘어온 data 받기
      var name = post_data.name, password = post_data.password, email = post_data.email;
      var insertJson={
        'email':email, 'password':password, 'name':name
      };

      var db=client.db('testlogin');//db name
      db.collection('user').find({'email':email}).count(function(err,num){//collection name
        if(num!=0){
          response.json('Email already exists');//안드로이드 CompositeDisposable에서 전달받을 response
          console.log('Email already exists');
        }else{
          db.collection('user').insertOne(insertJson, function(error, res){ //insert
            console.log(insertJson);
            response.json('Registration success');
            console.log('Registration success');
          })
        }
      })
    });

    //login
    app.post('/login', (request,response,next)=>{
      var post_data = request.body;
      var email = post_data.email, userPassword = post_data.password;

      var db=client.db('testlogin');
      db.collection('user').find({'email':email}).count(function(err,num){
        if(num==0){
          response.json('Email not exists');
          console.log('Email not exists');
        }else{
          db.collection('user').findOne({'email':email},function(err,user){
            if(user.password==userPassword){ //db의 password와 post로 받은 password가 같으면
              response.json('Login success');
              console.log('Login success');
            }else{
              response.json('Wrong password');
              console.log('Wrong password');
            }
          })
        }
      })
    });
  }
});

//server
app.listen(3000, ()=>{
  console.log('server running on 3000');
});
