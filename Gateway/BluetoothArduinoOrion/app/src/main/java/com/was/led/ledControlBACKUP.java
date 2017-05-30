package com.was.led;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ledControlBACKUP extends AppCompatActivity {
    Button btnOn,btnOff,btnDis,btnReceberDados,btnEnviarOrion;
    String address=null;
    TextView txtValorArduino;

    private ProgressDialog progress;
    BluetoothAdapter myBluetooth=null;
    BluetoothSocket btSocket=null;
    private boolean isBtConnected=false;



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

        //view of the ledcontrol
        setContentView(R.layout.activity_led_control);

        btnOn=(Button)findViewById(R.id.btnLigarLED);

        btnOff=(Button)findViewById(R.id.btnDesligarLED);
        btnDis=(Button)findViewById(R.id.btnDesconectar);
        btnEnviarOrion=(Button)findViewById(R.id.btnEnviarOrion);
        btnReceberDados=(Button)findViewById(R.id.btnReceberDados);
        txtValorArduino=(TextView)findViewById(R.id.txtViewRetornoArduino);

        new ConnectBT().execute();//call the class to connect

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
        btnEnviarOrion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CisternaTask().execute(distancia);
            }
        });
        textoInicial=txtValorArduino.getText().toString();
        textoFinal="cm";
    }





    void beginListenForData() throws IOException {
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
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            String string = data;
                                            String[] parts = string.split("#");
                                            distancia = parts[0]; // 004
                                            idCisterna = parts[1]; // 034556
                                            txtValorArduino.setText(textoInicial+distancia+textoFinal);
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
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


    private class CisternaTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            progress=ProgressDialog.show(ledControlBACKUP.this, "Enviando...","Espere");//show progress
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
                Toast.makeText(ledControlBACKUP.this,"Cisterna Salva",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ledControlBACKUP.this,"Ocorreu algum erro",Toast.LENGTH_LONG).show();
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
    private void turnOffLed(){
        if (btSocket!=null){
            try {
                btSocket.getOutputStream().write("nao".toString().getBytes());
                btnOn.setEnabled(true);
            }catch (IOException e){
                msg("Error");
            }
        }
    }
    private void turnOnLed(){
        byte[] signature =new byte []{
                (byte)0xE4,(byte)0x6F,(byte)0xC7,(byte)0x3E,(byte)0x03,(byte)0xDC,(byte)0x07,(byte)0x82,
                (byte)0x7F,(byte)0x23,(byte)0x3C,(byte)0x6E,(byte)0xB1,(byte)0xFF,(byte)0xEA,(byte)0x65,
                (byte)0xE2,(byte)0x49,(byte)0xD6,(byte)0x20,(byte)0xEC,(byte)0x47,(byte)0x46,(byte)0x28,
                (byte)0x8D,(byte)0xE7,(byte)0x47,(byte)0x31,(byte)0xE7,(byte)0xA2,(byte)0x58,(byte)0x6F,
                (byte)0x5E,(byte)0x28,(byte)0x89,(byte)0xE5,(byte)0x6C,(byte)0x51,(byte)0xFA,(byte)0x62,
                (byte)0xBE,(byte)0xA8,(byte)0x3B,(byte)0x27,(byte)0xB9,(byte)0xA6,(byte)0x05,(byte)0x52,
                (byte)0x93,(byte)0xEA,(byte)0xE7,(byte)0xAF,(byte)0x0D,(byte)0xE6,(byte)0x7B,(byte)0xE2,
                (byte)0x72,(byte)0x3D,(byte)0x95,(byte)0x8E,(byte)0x4B,(byte)0xDF,(byte)0xFA,(byte)0x01
        };
        /*
                (byte)0xBE,(byte)0xA8,(byte)0x3B,(byte)0x27,(byte)0xB9,(byte)0xA6,(byte)0x05,(byte)0x52,
                (byte)0x93,(byte)0xEA,(byte)0xE7,(byte)0xAF,(byte)0x0D,(byte)0xE6,(byte)0x7B,(byte)0xE2,
                (byte)0x72,(byte)0x3D,(byte)0x95,(byte)0x8E,(byte)0x4B,(byte)0xDF,(byte)0xFA,(byte)0x01
        };*/


        String message="nao";
        if(btSocket!=null){

            try {
                //btSocket.getOutputStream().write(sign.toString().getBytes());
                btSocket.getOutputStream().write(signature);
                btSocket.getOutputStream().flush();
                Thread.sleep(5000);
               // btSocket.getOutputStream().;

              //  btSocket.getOutputStream().write(signature);
                //btSocket.getOutputStream().write("#".toString().getBytes());
                btSocket.getOutputStream().write(message.toString().getBytes());
                btSocket.getOutputStream().flush();
               // btSocket.getOutputStream().write(signature);
                btnOn.setEnabled(false);

            }catch (IOException e){
                msg("Error");
            } //catch (InterruptedException e) {
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            //   e.printStackTrace();
            //}
        }

      //  sendMensagem("was");
        //turnOffLed();

    }
    private void sendMensagem(String mens) {
        if (btSocket != null) {

            try {
                btSocket.getOutputStream().write(mens.toString().getBytes());
                //  btnOn.setEnabled(false);

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
            progress=ProgressDialog.show(ledControlBACKUP.this, "Connecting...","Please wait");//show progress
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
