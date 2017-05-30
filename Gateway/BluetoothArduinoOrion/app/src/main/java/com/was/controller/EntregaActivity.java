package com.was.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.was.led.R;

import model.Entrega;

public class EntregaActivity extends AppCompatActivity
implements EntregaListFragment.AoClicarNaEntrega{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrega);
    }

    @Override
    public void clicouNaEntrega(Entrega entrega) {
        Intent it=new Intent(this, EntregaDetalheActivit.class);
        it.putExtra(EntregaDetalheActivit.EXTRA_ENTREGA,entrega);
        startActivity(it);
    }
}
