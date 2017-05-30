package com.was.controller;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Entrega;
import services.EntregaHttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * Created by wasuf on 21/03/2017.
 */

public class EntregaListFragment extends ListFragment {
    List<Entrega> mEntregas;
    ArrayAdapter<Entrega> mAdapter;
    CarregaEntregasTask carregaEntregasTask;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        carregaEntregasTask=new CarregaEntregasTask();
        mEntregas=new ArrayList<Entrega>();


        Intent it=getActivity().getIntent();
        String pipeiroId=it.getStringExtra("pipeiroId");
        carregaEntregasTask.execute(pipeiroId);

        mAdapter=new ArrayAdapter<Entrega>(
                getActivity(),android.R.layout.simple_list_item_1,mEntregas );
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Activity activity=getActivity();
        if(activity instanceof AoClicarNaEntrega) {
            Entrega entrega = (Entrega) l.getItemAtPosition(position);
            AoClicarNaEntrega listener = (AoClicarNaEntrega) activity;
            listener.clicouNaEntrega(entrega);
        }
    }
    public interface AoClicarNaEntrega{
        void clicouNaEntrega(Entrega entrega);
    }
    ProgressDialog progress;
    class CarregaEntregasTask extends AsyncTask<String,Void,List<Entrega>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress= ProgressDialog.show(getActivity(), "Carregando Plano de trabalho...","Aguarde");
        }

        @Override
        protected List<Entrega> doInBackground(String... params) {

            List<Entrega>entregas=new ArrayList<Entrega>();
            EntregaHttp entregaHttp=new EntregaHttp();
            entregas=entregaHttp.getEntregasAbertaByPipeiroId(params[0]);

            return entregas;


        }

        @Override
        protected void onPostExecute(List<Entrega> entregas) {
            //super.onPostExecute(entregas);
            progress.dismiss();
            mEntregas.clear();
            mEntregas.addAll(entregas);

            setListAdapter(mAdapter);
        }
    }

}
