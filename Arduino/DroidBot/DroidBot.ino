/*
Written by Sujan Thapa
Date:July,03,2015
*/
char playMode='s';
int pin12=12,pin11=11,pin10=10,pin5=5;
int led9=9,led8=8;

void setup() {
  //Serial Initilization BLUETOOTH Communication
  Serial.begin(9600);
  //MOTOR A
  pinMode(pin5,OUTPUT);
  pinMode(pin12,OUTPUT);
  //MOTOR B
  pinMode(pin11,OUTPUT);
  pinMode(pin10,OUTPUT);
  //Led A
  pinMode(led9,OUTPUT);
  //Led B
  pinMode(led8,OUTPUT);
  //GLOW FOR SECOND
  digitalWrite(led9,HIGH);
  digitalWrite(led8,HIGH);
  delay(1000);
  digitalWrite(led9,LOW);
  digitalWrite(led8,LOW);
}
void loop() {
  if(Serial.available() > 0){
    playMode = Serial.read();
  }  
  //------------------------------->
  if(playMode=='f'){
    forward();
    lightA();
    lightB();
  }
  else if(playMode=='b'){
    backward();
    lightA();
    lightB();
  }
  else if(playMode=='r'){
    right();
    lightA();
    lightB();
  }
  else if(playMode=='l'){
    lightA();
    lightB();
    left();
  }  
  else if(playMode =='h'){
    stopHandbreak();
    lightAOff();
    lightBOff();
  }
  else{
    stop();
    lightAOff();
    lightBOff();
  }
}
///All function
void forward(){
   //Motor A
   digitalWrite(pin5,HIGH);
   digitalWrite(pin12,LOW);
   //Motor B
   digitalWrite(pin11,HIGH);
   digitalWrite(pin10,LOW);
}
void backward(){
   //Motor A
   digitalWrite(pin5,LOW);
   digitalWrite(pin12,HIGH);
   //Motor B
   digitalWrite(pin11,LOW);
   digitalWrite(pin10,HIGH);
}
void right(){
   //Motor A
   digitalWrite(pin5,HIGH);
   digitalWrite(pin12,LOW);
   //Motor B
   digitalWrite(pin11,LOW);
   digitalWrite(pin10,LOW);
}
void left(){
   //Motor A
   digitalWrite(pin5,LOW);
   digitalWrite(pin12,LOW);
   //Motor B
   digitalWrite(pin11,HIGH);
   digitalWrite(pin10,LOW);
}
void stop(){
  //Motor A
   digitalWrite(pin5,LOW);
   digitalWrite(pin12,LOW);
   //Motor B
   digitalWrite(pin11,LOW);
   digitalWrite(pin10,LOW);
}
void stopHandbreak(){
 //Motor A
   digitalWrite(pin5,HIGH);
   digitalWrite(pin12,HIGH);
   //Motor B
   digitalWrite(pin11,HIGH);
   digitalWrite(pin10,HIGH);
}
void lightA(){
  digitalWrite(led9,HIGH);
}
void lightB(){
  digitalWrite(led8,HIGH);  
}
void lightAOff(){
  digitalWrite(led9,LOW);
}
void lightBOff(){
  digitalWrite(led8,LOW);  
}



