package com.was.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.was.led.R;

import model.Entrega;

public class MainActivity extends AppCompatActivity {
    TextView txtPipeiroId;
    Entrega mEntrega;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtPipeiroId=(TextView)findViewById(R.id.txtPipeiroId);

        Intent it=getIntent();
        mEntrega=(Entrega)it.getSerializableExtra("entrega");
        String pipeiroId=mEntrega.getId()+"="+mEntrega.getAssinaturaProxy();
        txtPipeiroId.setText("Entrega"+pipeiroId);
    }


}
