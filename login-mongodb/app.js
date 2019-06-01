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
      var student_id = post_data.student_id, student_password = post_data.student_password,
      student_name = post_data.student_name;

      var insertJson={
        'student_id':student_id,
        'student_password':student_password,
        'student_name':student_name
      };

      var db=client.db('TEST_DB');//db name
      db.collection('TB_STUDENT').find({'student_id':student_id}).count(function(err,num){//collection name
        if(num!=0){
          response.json('이미 존재하는 아이디입니다.');//****안드로이드 CompositeDisposable에서 전달받을 response
          console.log('이미 존재하는 아이디입니다.');
        }else{
          db.collection('TB_STUDENT').insertOne(insertJson, function(error, res){ //insert
            console.log(insertJson);
            response.json(1);
            console.log('등록 완료');
          })
        }
      })
    });

    //login
    app.post('/login', (request,response,next)=>{
      var post_data = request.body;
      var student_id = post_data.student_id, student_password = post_data.student_password;

      var db=client.db('TEST_DB');
      db.collection('TB_STUDENT').find({'student_id':student_id}).count(function(err,num){
        if(num==0){
          response.json(2);
          console.log('존재하지 않는 아이디입니다.');
        }else{
          db.collection('TB_STUDENT').findOne({'student_id':student_id},function(err,user){
            // console.log(user.student_password);  //DB에 입력되어있는 password
            // console.log(student_password);       //사용자가 입력한 password
            if(user.student_password==student_password){ //db의 password와 post로 받은 password가 같으면
              response.json(1);
              console.log('로그인 성공');
            }else{
              response.json(0);
              console.log('비밀번호가 틀렸습니다.');
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
