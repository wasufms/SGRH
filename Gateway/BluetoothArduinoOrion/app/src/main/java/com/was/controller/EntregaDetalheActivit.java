package com.was.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.was.led.R;

import model.Entrega;

public class EntregaDetalheActivit extends AppCompatActivity {
    public static final String EXTRA_ENTREGA="entrega";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega_detalhe);

        if(savedInstanceState==null){
            Intent intent=getIntent();
            Entrega entrega=(Entrega)intent.getSerializableExtra(EXTRA_ENTREGA);
            EntregaDetalheFragment fragment=EntregaDetalheFragment.novaInstancia(entrega);
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.detalhe,fragment,EntregaDetalheFragment.TAG_DETALHE);
            ft.commit();
        }
    }
}
