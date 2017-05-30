#include <SoftwareSerial.h>
#include <Ultrasonic.h> //biblioteca para ultrassom

//assinatura e verificação
#include <Crypto.h>
#include <Ed25519.h>
#include <RNG.h>

#include <string.h>
//fim da assinatura e verificação
//#include <avr/pgmspace.h>


#define TRIGGER_PIN 12
#define ECHO_PIN 13 

#define DELAY 1000

#define LED 7 
SoftwareSerial bluetooth(10,11);//10 rx 11 tx
Ultrasonic ultrassonico(TRIGGER_PIN,ECHO_PIN);

const char id[] PROGMEM="00001101-0000-1000-8000-00805F9B34FB";

//ESSES DADOS DEVEM SER SETADOS QUANDO DA CRIAÇÃO DA CISTERNA
const String id_ PROGMEM="123-321";
//double altura=220;//em cm
const double area_da_base PROGMEM=80384;//em cm
//double distancia_sensor_agua_cheia=100;//em cm


//const uint8_t publicKey_proxy[32] PROGMEM ={215,90,152,1,130,177,10,183,213,75,254,211,201,100,7,58,14,225,114,243,218,166,35,37,175,2,26,104,247,7,81,26};
const uint8_t publicKey_proxy[32]  ={215,90,152,1,130,177,10,183,213,75,254,211,201,100,7,58,14,225,114,243,218,166,35,37,175,2,26,104,247,7,81,26};
const uint8_t privateKey_no[32] = {157,97,177,157,239,253,90,96,186,132,74,244,146,236,44,196,68,73,197,105,123,50,105,25,112,59,172,3,28,174,127,96};
//const uint8_t publicKey_no[32]PROGMEM  = {215,90,152,1,130,177,10,183,213,75,254,211,201,100,7,58,14,225,114,243,218,166,35,37,175,2,26,104,247,7,81,26};
//const dataType variableName[] PROGMEM = {data0, data1, data3...};

void setup() {
  Serial.begin(9600);
  bluetooth.begin(9600);
  pinMode(LED,OUTPUT);
  digitalWrite(LED,LOW);
}

//String msg="";
int acao=0;//FAZ NADA
int count=0;

//uint8_t assinatura_no[64];

boolean encerra_entrega=false;

 
boolean continua=true;
int distancia_inicial;
double volume_previsto;
double volume_recebido;
uint8_t message[10];
uint8_t assinatura[64];


void loop() {
 
 float cmMsec;
 long microsec=ultrassonico.timing();
 int distancia;
 
 String fromAndroid="";
 uint8_t myByte;


size_t len;



 
  
 //----------RECEBER DADOS DO BLUETOOTH e ENVIAR PARA ANDROID------------------ 
  cmMsec=ultrassonico.convert(microsec,Ultrasonic::CM);
  distancia=(int)cmMsec;
  
 

//aqui entra um if se pode enviar----------

 // bluetooth.println(retorno);
 bluetooth.println(distancia);
 // Serial.println(retorno);

//----FIM DO ----RECEBER DADOS DO BLUETOOTH e ENVIAR PARA ANDROID---------

  
//----------------------------------------
//-----------ENTRADA DE DADOS-----------------
  


while(bluetooth.available() > 0) {
      myByte=bluetooth.read();
      fromAndroid+=char(myByte);
    
      if(acao==1){//RECEBE A ASSINATURA
        if(fromAndroid!="@"){
          assinatura[count]=myByte;
          count++;
        }
      }
     
      if(acao==2){//RECEBE A MENSAGEM
        if(fromAndroid!="$"){
          message[count]=myByte;
          volume_previsto=16000;//deve ser pega
          count++;
        }
      }
}

if(fromAndroid=="[" || fromAndroid=="]"){
  
  if(fromAndroid=="["){
    digitalWrite(LED,HIGH);
    
  }else{
    digitalWrite(LED,LOW);
    encerra_entrega=true;
  }
  fromAndroid="";
}else{
  if(fromAndroid=="#"){//MONTA A ASSINATURA
    Serial.println(F("Lendo os dados..."));
    digitalWrite(LED,HIGH);
    acao=1;
    fromAndroid="";
    continua=!continua;
    if(continua==false){
      count=0;
    }
  }
  if(fromAndroid=="@"){//MONTA A MENSAGEM
    acao=2;
    fromAndroid="";
    count=0;
  }
  if(fromAndroid=="$"){ //ACABOU DE RECEBER OS DADOS. COMEÇA AS OUTRAS OPERAÇÕES
    acao=3;
  }
}//fim da entrada de dados
//FIM DA ENTRADA DE DADOS


if(acao==3){ //COMEÇA AS OUTRAS OPERAÇÕES  
  digitalWrite(LED,LOW);
 
/*  Serial.print("Assinatura proxy [");
  for(int i=0;i<64;i++){
     Serial.print(assinatura[i]);
      Serial.print(" "); 
  }
  Serial.println("]");
  Serial.print("Mensagem [");
  for(int i=0;i<10;i++){
     Serial.print(message[i]);
      Serial.print(" "); 
  }
  Serial.println("]");*/

//inicia verificação 

Serial.print(F(" Verificando ... "));
//Serial.flush();
//bluetooth.flush();

len = sizeof(message);

bool verified = Ed25519::verify(assinatura,publicKey_proxy, message, len);
 
Serial.println(F("Resultado: "));
if (verified) { //TOKEN AUTENTICADO
        Serial.println(F("ok"));
        delay(10*1000);
        bluetooth.println("#");//responde com token autorizado
        distancia_inicial=distancia;
        acao=4;
} else {
        delay(10*1000);
        bluetooth.println("@");//responde com token não autorizado
         acao=0;
         fromAndroid="";
        Serial.println(F("failed"));
}
count=0;
}//TERMINA A AUTENTICAÇÃO

//COMEÇA O MONITORAMENTO
if(acao==4){

//volume_recebido=(area_da_base*(distancia_inicial-distancia))/1000;
//Serial.println(volume_recebido);
bluetooth.println(distancia);
// delay(5*1000);

//apenas um mock

//if((distancia<=10) || (encerra_entrega==true)){ //o correto é <=volume_previsto
if(encerra_entrega==true){ //Mock
  //||======ENVIAR PARA O GATEWAY========||
  delay(5*1000);
   
 volume_recebido=(area_da_base*(distancia_inicial-distancia))/1000;
  int pureza=lerPureza();
  

 char char_valores[8];
 sprintf(char_valores, "$%d;%d", lerPureza(),(int)volume_recebido);
 bluetooth.println(char_valores);//envia os valores lidos;*/

 
/* String valoresEntrega="$5;16980";
  bluetooth.println(valoresEntrega);//envia os valores lidos;*/
  
  
  digitalWrite(LED,HIGH);
  //len = sizeof(message);//o correto é montar a mensagem
 // Ed25519::sign(assinatura_no,privateKey,publicKey_no,message,len); //ESSE É O CORRETO
//  Ed25519::sign(assinatura,privateKey_no,publicKey_proxy,message,len); 
  
  //printNumber(assinatura_no, 64);//CORRETO
  printNumber(assinatura, 64);
  digitalWrite(LED,LOW);
   
//  
  
  //||======FIM DO ENVIAR PARA O GATEWAY========||

    //LIMPAR VARIAVEIS
    acao=0; 
    encerra_entrega=false;
    Serial.println(F("Terminou a Entrega de agua"));
  }//fim do encerra entrega  
}//fim do acao==4

 //-------------------------------------------
  delay(DELAY);
}

//outra função
int lerPureza(){ //ler o sensor de pureza
  return 5;
}

void printNumber(const uint8_t *x, uint8_t len)
{
    static const char hexchars[] = "0123456789ABCDEF";
    String token_no="*";
 for (uint8_t posn = 0; posn < len; ++posn) {
        token_no+=(hexchars[(x[posn] >> 4) & 0x0F]);
        token_no+=(hexchars[x[posn] & 0x0F]);
    }
    bluetooth.println(token_no);
}




