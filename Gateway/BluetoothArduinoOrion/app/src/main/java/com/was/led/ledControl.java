package com.was.led;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.was.controller.RecebimentoActivity;

import java.io.IOException;
import java.io.InputStream;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import model.Entrega;

import persistence.CurrentEntregaDAO;
import persistence.EntregaDAO;
import util.Print;

public class ledControl extends AppCompatActivity {
    Button btnOn,btnOff,btnDis,btnReceberDados,btnEnviarOrion;
    String address=null;
    TextView txtValorArduino;
    TextView txtResposta;

    private ProgressDialog progress;
    BluetoothAdapter myBluetooth=null;
    BluetoothSocket btSocket=null;
    private boolean isBtConnected=false;

    Entrega entrega;
    model.Cisterna cisterna;
    String volumeFinal;

    //SPP UUID. Look for it
    static final UUID myUUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    final int handlerState = 0;                        //used to identify handler message


    private StringBuilder recDataString = new StringBuilder();

    String textoInicial;
    String textoFinal;
    private String distancia;
    private String idCisterna;



    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint=getIntent();
        address=newint.getStringExtra(DeviceList.EXTRA_ADDRESS);//recebe o enderesso do blooth device
        new ConnectBT().execute();//call the class to connect
        //view of the ledcontrol
        setContentView(R.layout.activity_led_control);

        btnOn=(Button)findViewById(R.id.btnLigarLED);

        btnOff=(Button)findViewById(R.id.btnDesligarLED);
        btnDis=(Button)findViewById(R.id.btnDesconectar);
        btnEnviarOrion=(Button)findViewById(R.id.btnEnviarOrion);
        btnReceberDados=(Button)findViewById(R.id.btnReceberDados);
        txtValorArduino=(TextView)findViewById(R.id.txtViewRetornoArduino);
        txtResposta=(TextView)findViewById(R.id.txtResposta);



        //comands to be sent to bluet
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnLed();
            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOffLed();
            }
        });
        btnOff.setEnabled(false);
        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisConnect();//close conn
            }
        });
        btnReceberDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    beginListenForData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //automatiza a busca de dados



        //



        btnEnviarOrion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new CisternaTask().execute(distancia);
                enviarEntrega();
            }
        });
        textoInicial=txtValorArduino.getText().toString();
        textoFinal="cm";
        CurrentEntregaDAO currentEntregaDAO=new CurrentEntregaDAO(getApplicationContext());
        entrega=currentEntregaDAO.getCurrentEntrega(getApplicationContext());
        cisterna=currentEntregaDAO.getCurrentCisterna(getApplicationContext());


       /* address=cisterna.getOutro();
        Log.d("address","was");
        Log.d("address","was");*/

        //volumeFinal="";
        try {
            beginListenForData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    void beginListenForData() throws IOException {
        txtResposta.setText("Token: aguardando autorização");
        txtResposta.setVisibility(View.VISIBLE);
        mmInputStream = btSocket.getInputStream();
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] buffer = new byte[1024];
                            int bytes;
                            int bytesRead = -1;

                    /*  Lê os bytes recebidos e os armazena no buffer até que
                    uma quebra de linha seja identificada. Nesse ponto, assumimos
                    que a mensagem foi transmitida por completo.
                     */     int count=0;
                            do {
                                bytes = mmInputStream.read(buffer, bytesRead+1, 1);
                                bytesRead+=bytes;
                            } while(buffer[bytesRead] != '\n');
                            final String msgDecode = new String(buffer,"US-ASCII");

                            final char autorizacao_Token=msgDecode.charAt(0);
                            Log.d("TOKEN_NO",msgDecode);
                           handler.post(new Runnable()
                            {
                                public void run()
                                {
                                   String finalTexto="cm";
                                    if ((autorizacao_Token=='#') || (autorizacao_Token=='@') || (autorizacao_Token=='$')|| (autorizacao_Token=='*')){
                                        if (autorizacao_Token=='#'){
                                            txtResposta.setTextColor(Color.BLUE);
                                            txtResposta.setText("TOKEN: AUTORIZADO \n Pode começar a descarregar");
                                          //  entrega.setPacoteEntregavalido(1);
                                            entrega.setPacoteEntregaValido(1);
                                            finalTexto="Litros";
                                            btnOff.setEnabled(true);
                                        }else if(autorizacao_Token=='@'){
                                            txtResposta.setTextColor(Color.RED);
                                            txtResposta.setText("TOKEN: NEGADO \n Tente manualmente");
                                            entrega.setPacoteEntregaValido(0);
                                        }else if(autorizacao_Token=='$'){
                                            txtResposta.setTextColor(Color.BLUE);

                                           String valoresEntrega=msgDecode.trim();

                                            //String valoresEntrega="$5;16987";
                                            double pureza, volumeEntregue;
                                            valoresEntrega=valoresEntrega.substring(1, valoresEntrega.length());
                                            String[] valoresEntregaArray=valoresEntrega.split(";");
                                            pureza=Double.parseDouble(valoresEntregaArray[0]);
                                            volumeEntregue=Double.parseDouble(valoresEntregaArray[1]);
                                            try {
                                               entrega.setVolumeEntregue(volumeEntregue);
                                               entrega.setPureza(pureza);
                                                txtResposta.setText("Recebimento encerrado: "+volumeEntregue+" Litros \n"+
                                                        "Grau de Pureza: "+pureza+
                                                        "\n Calculando TOKEN...");
                                            }catch (Exception e){
                                                txtValorArduino.setText("Erro salvar valores da Entrega");
                                            }

                                            //txtValorArduino.setVisibility(View.INVISIBLE);
                                        }
                                        else if(autorizacao_Token=='*'){
                                            txtResposta.setTextColor(Color.GREEN);
                                            txtResposta.setText("TOKEN Recebido: "+msgDecode.trim());

                                            String assinatura_no=msgDecode.trim();
                                            assinatura_no=assinatura_no.substring(1,assinatura_no.length());
                                            entrega.setAssinaturaNo(assinatura_no);
                                            encerra();
                                        }
                                    }else if(!msgDecode.isEmpty()){
                                        String texto=" ";
                                        try {
                                            Double i =Double.parseDouble(msgDecode.trim());
                                            DecimalFormat df = new DecimalFormat("#.#");
                                            df.setRoundingMode(RoundingMode.CEILING);
                                            String vol=df.format(i);
                                            volumeFinal=vol;
                                            texto+="Cisterna: "+vol;
                                            texto+=finalTexto;
                                            txtValorArduino.setText(texto);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            txtValorArduino.setText("Erro no sensor");
                                        }
                                    }

                                }
                            });

                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });
        workerThread.start();
    }



    private void encerra(){
        EntregaDAO entregaDAO=new EntregaDAO(getApplicationContext());
        //entrega.setLocalEntrega(getMyLocation);
       /* identifica se a localização da entrega
       está dentro de um raio da cisterna
        if(getMyLocation().equals())

        */
        Date hoje = new Date();
        SimpleDateFormat df;
        df = new SimpleDateFormat("dd-MM-yyyy");

        entrega.setDataRealizada(new String(df.format(hoje)));
        entrega.setLocalEntrega(getMyLocation());
        entregaDAO.update(entrega);
        DisConnect();

        Intent it=new Intent(this,RecebimentoActivity.class);
       // it.putExtra("pipeiroId",edtCPF.getText().toString());
        startActivity(it);
    }



    private class CisternaTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            progress=ProgressDialog.show(ledControl.this, "Enviando...","Espere");//show progress
        }

        @Override
        protected Boolean  doInBackground(String... params) {
            CisternaHttp cisternaHttp=new CisternaHttp();
            Cisterna cisterna=new Cisterna();
            cisterna=cisternaHttp.getCisterna(idCisterna);
            if(cisterna.getId()!=null){
                Log.d("was->","Volume: "+cisterna.getVolume());
                Log.d("was->","Distancia: "+new Double(distancia));
               double volume=cisterna.calculaVolume(new Double(distancia))/1000;
              //  double volume=cisterna.calculaVolume(new Double(distancia));
                cisterna.setVolume(volume);
                cisterna.setLocalizacao(getMyLocation());
                Log.d("was->","Novo Volume: "+cisterna.getVolume());
                Log.d("was->","Nova localização: "+getMyLocation());

                cisternaHttp.salvarCisterna(cisterna);
                return true;
                //Toast.makeText(ledControl.this,"Cisterna Salva",Toast.LENGTH_LONG).show();
                //Log.d("was->","Volume: "+cisterna.getVolume());
            }else{
                Log.d("was->","Sisterna não existe");
                return false;
            }
            //return null;
        }
        @Override
        protected void onPostExecute(Boolean args) {
            //progressBar.setVisibility(View.GONE);
            progress.dismiss();
            if (args==true){
                Toast.makeText(ledControl.this,"Cisterna Salva",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ledControl.this,"Ocorreu algum erro",Toast.LENGTH_LONG).show();
            }


        }
    }//fim do Task

    private void DisConnect(){
        if (btSocket!=null){//btsocket is busy
            try {
                btSocket.close();
            }catch (IOException e){
                msg("Error");
            }
        }
        finish();//return to the first layout
    }

    private void enviarEntrega(){

        String data[]=new String[6];
        data[0]="#";

        data[1]=entrega.getAssinaturaProxy().substring(0, 64) ;
        data[2]=entrega.getAssinaturaProxy().substring(64,128) ;

        data[3]="@";
        data[4]=entrega.getId()+entrega.getPipeiro()+entrega.getManancial()+entrega.getCisterna();
        data[5]="$";
        //entrega.getVolumePrevisto();
        ///msg=entrega.getId()+entrega.getPipeiro()+entrega.getManancial()+entrega.getCisterna();


        int i=0;
        while(i<6){
            int perct=(100/6)*i;
            txtValorArduino.setText("Enviando dados... "+perct);
            if (btSocket!=null){


                String dado=data[i];
                try {
                    if(i==1 ||  i==2){
                        btSocket.getOutputStream().write(Print.stringToHexByte(dado));
                    }else{
                        btSocket.getOutputStream().write(dado.getBytes());
                    }
                    btSocket.getOutputStream().flush();
                    Thread.sleep(10*1000);

                    btnOn.setEnabled(true);
                }catch (Exception e){
                    msg("Error");
                }
            i++;
        }

        }
        txtValorArduino.setText("Aguardando verificação do TOKEN");
    }

    private void turnOffLed(){
        if (btSocket!=null){
            try {
                String data="]";//parar
                btSocket.getOutputStream().write(data.getBytes());
                Log.d("Desligar","]");
                btnOn.setEnabled(true);
            }catch (IOException e){
                msg("Error");
            }
        }
    }
    private void turnOnLed(){
        if (btSocket!=null){
            try {
                String data="[";//iniciar

                btSocket.getOutputStream().write(data.getBytes());
                Log.d("Ligar","[");
                btnOn.setEnabled(true);
            }catch (IOException e){
                msg("Error");
            }
        }
    }
    private void sendMensagem(String mens) {
        if (btSocket != null) {

            try {
                btSocket.getOutputStream().write(mens.toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }
    //fast way to call toast
    private void msg(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        getMenuInflater().inflate(R.menu.menu_led_control,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ConnectBT extends AsyncTask<Void,Void,Void>//UI thread
    {
        private boolean ConnectSucces=true;//if its here, ites almost connected

        @Override
        protected void onPreExecute() {
            progress=ProgressDialog.show(ledControl.this, "Connecting...","Please wait");//show progress
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                if (btSocket==null||!isBtConnected){
                    myBluetooth=BluetoothAdapter.getDefaultAdapter();//get the
                    BluetoothDevice dispotitivo=myBluetooth.getRemoteDevice(address);//conn to the decieces adress an checks if it available
                    btSocket=dispotitivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM(SPP)connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start conn
                }
            }catch (IOException e){
                ConnectSucces=false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(!ConnectSucces){
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }else {
                msg("Connected.");
                isBtConnected=true;
            }
            progress.dismiss();
        }
    }


    private static final int REQUEST_PERMISSOES=3;
    private static final String TAG= "mapaa";

    private Location location;
    private LocationManager locationManager;





    public String getMyLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            //return " was";


           permissoesHabilitadas();
            Toast.makeText(this,"Porr",Toast.LENGTH_SHORT).show();

        }else {
            locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        double longitude=location.getLongitude();
        double latitude=location.getLatitude();
        //String local=""++","+;
        String local=""+latitude+","+longitude;
        return local;
    }


    private boolean permissoesHabilitadas(){
        List<String> permissoes=new ArrayList<String>();

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            permissoes.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(!permissoes.isEmpty()){
            String[] array=new String[permissoes.size()];
            permissoes.toArray(array);
            ActivityCompat.requestPermissions(this,array,3);

        }
        return permissoes.isEmpty();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult( requestCode,  permissions,  grantResults);
        boolean sucesso=true;
        for(int i=0;i<permissions.length;i++){
            if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                sucesso=false;
                break;
            }
        }

        if(!sucesso){
            Toast.makeText(this,"Deve permitir",Toast.LENGTH_SHORT).show();
            finish();
        }

    }


}
