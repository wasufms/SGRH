package com.was.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;
import com.was.led.R;

import fcm.EntregaIdListenerService;

public class LoginActivity extends AppCompatActivity {

    EditText edtCPF;
    Button btnLogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtCPF=(EditText)findViewById(R.id.edtCPF);
        btnLogar=(Button)findViewById(R.id.btnLogin);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logar();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("was->","Entrei");
        Intent it = new Intent(this, EntregaIdListenerService.class);
        it.putExtra("fromNotification", true);
        startService(it);
        Log.d("was->","Entrei");
    }

    private void logar(){
        //Intent it=new Intent(this,MainActivity.class);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("was - RegistrationID", token);

        Intent it=new Intent(this,EntregaActivity.class);
        it.putExtra("pipeiroId",edtCPF.getText().toString());
        startActivity(it);
    }

}
