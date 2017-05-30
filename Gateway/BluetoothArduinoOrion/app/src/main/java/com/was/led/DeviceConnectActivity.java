package com.was.led;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import model.*;
import persistence.CisternaDAO;
import persistence.CurrentEntregaDAO;
import persistence.EntregaDAO;

public class DeviceConnectActivity extends AppCompatActivity {

    private static final int BT_ATIVAR=0;

    private BluetoothAdapter mAdaptadorBluetooth;
    private Button btnTestaConnBT;
    private TextView txtTestaConnBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connect);

        btnTestaConnBT=(Button)findViewById(R.id.btnTestaConnBT);
        txtTestaConnBT=(TextView)findViewById(R.id.txtTestaConnBT);
        txtTestaConnBT.setText("Conectando a Cisterna");
        btnTestaConnBT.setEnabled(true);

        mAdaptadorBluetooth=BluetoothAdapter.getDefaultAdapter();
        if(mAdaptadorBluetooth!=null){
            if (mAdaptadorBluetooth.isEnabled()){
                conectar();
            }
            else{
                Intent enableBtIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,BT_ATIVAR);
            }
        }else{
            Toast.makeText(this,"Sem suport a Bluetooth", Toast.LENGTH_LONG).show();
        }
        btnTestaConnBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectar();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(BT_ATIVAR==requestCode){
            if(RESULT_OK==resultCode){
                conectar();
            }else {
                Toast.makeText(this,"Ativação do Bluetooth é essencial", Toast.LENGTH_LONG).show();
                btnTestaConnBT.setEnabled(true);
                txtTestaConnBT.setTextColor(Color.RED);
                txtTestaConnBT.setText("Ativação do Bluetooth é essencial\n" +
                        "Ative o Bluetooth e Clique no Botão \n" +
                        "CONECTAR MANUALMENTE");
            }
            mAdaptadorBluetooth=BluetoothAdapter.getDefaultAdapter();
            if(mAdaptadorBluetooth!=null){
                if (mAdaptadorBluetooth.isEnabled()){
                    conectar();
                }
                else{
                    Intent enableBtIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent,BT_ATIVAR);
                }
            }
        }
    }
    private void conectar(){
        Toast.makeText(this,"Vou conectar", Toast.LENGTH_LONG).show();
        CurrentEntregaDAO currentEntregaDAO=new CurrentEntregaDAO(getApplicationContext());
        model.Cisterna cisterna=currentEntregaDAO.getCurrentCisterna(getApplicationContext());

       // txtTestaConnBT.setText(cisterna.getOutro());
        Intent i =new Intent(this,ledControl.class);
        //change the activity
        i.putExtra("device_adress",cisterna.getOutro());//this will be received at ledControl (class) Activity
       startActivity(i);

    }
}
