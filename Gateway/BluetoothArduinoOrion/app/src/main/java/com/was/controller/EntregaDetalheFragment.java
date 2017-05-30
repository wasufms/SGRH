package com.was.controller;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.was.led.Cisterna;
import com.was.led.CisternaHttp;
import com.was.led.R;
import com.was.led.ledControl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.Entrega;
import services.EntregaHttp;

/**
 * Created by wasuf on 21/03/2017.
 */

public class EntregaDetalheFragment extends Fragment {
    public static final String TAG_DETALHE = "tagDetalhe";
    private static final String EXTRA_ENTREGA = "entrega";
    TextView mTextEntrega;
    Button mBtnSolicitarToken;
    Entrega mEntrega;



    private ProgressDialog progress;
    ShareActionProvider mShareActionProvider;


    public static EntregaDetalheFragment novaInstancia(Entrega entrega) {
        Bundle parametros = new Bundle();
        parametros.putSerializable(EXTRA_ENTREGA, entrega);
        EntregaDetalheFragment fragment = new EntregaDetalheFragment();
        fragment.setArguments(parametros);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEntrega = (Entrega) getArguments().getSerializable(EXTRA_ENTREGA);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(
                R.layout.fragment_detalhe_entrega, container, false);
        mTextEntrega = (TextView)layout.findViewById(R.id.txtEntrega);
        mBtnSolicitarToken=(Button)layout.findViewById(R.id.btnSolicitarToken);


        this.init();


        mBtnSolicitarToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarToken();
            }
        });



        return layout;
    }
    private void init(){

        // txtToken.setTextColor(Color.RED);
        if (mEntrega != null) {
            String entrega="Entrega: "+mEntrega.getId()+"\n"+
                    "[Pipeiro: "+mEntrega.getPipeiro()+"\n"+
                    "Cisterna: "+mEntrega.getCisterna()+"\n"+
                    "Manancial: "+mEntrega.getManancial()+"\n"+
                    "Volume a ser Entregue: "+mEntrega.getVolumePrevisto()+"\n"+
                    "Data Prevista: "+mEntrega.getDataPrevista()+"]";
            mTextEntrega.setText(entrega);
        }


    }



    private void solicitarToken(){
        mBtnSolicitarToken.setEnabled(false);
        String teste=mEntrega.getId()+" Proxy Acao: "+mEntrega.getAcaoProxy();
        mEntrega.setStatus(1);
        mEntrega.setAcaoProxy(1);
        String regsterId= FirebaseInstanceId.getInstance().getToken();
        Log.d("was","regsterId: "+regsterId);
        mEntrega.setRegstrationId(regsterId);

        SalvaEntregaTask salvaEntregaTask=new SalvaEntregaTask();
        salvaEntregaTask.execute();

       // EntregaHttp entregaHttp=new EntregaHttp();
        //entregaHttp.salvarEntrega(mEntrega);


        //Toast.makeText(getActivity(),teste , Toast.LENGTH_SHORT).show();
    }

    private class SalvaEntregaTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            progress=ProgressDialog.show(getActivity(), "Enviando...","Aguarde");//show progress
        }

        @Override
        protected Boolean  doInBackground(String... params) {
            EntregaHttp entregaHttp=new EntregaHttp();
            entregaHttp.salvarEntrega(mEntrega);
            return true;

        }
        @Override
        protected void onPostExecute(Boolean args) {
            //progressBar.setVisibility(View.GONE);
            progress.dismiss();
            if (args==true){

               /*esse funciona sem Notificação
                buscaToken();
                */
            //com notificação
                Intent it=new Intent(getContext(),TokenActivity.class);
                it.putExtra("entrega",mEntrega);
                startActivity(it);

            }else{
                Toast.makeText(getActivity(),"Ocorreu algum erro",Toast.LENGTH_LONG).show();
            }
        }
    }//fim do Task

/* USADO QUANDO SEM NOTIFICAÇÃO
    private boolean buscaToken(){
        BuscaTokenTask buscaTokenTask=new BuscaTokenTask();
        buscaTokenTask.execute();
        return true;
    }

    private class BuscaTokenTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            progress=ProgressDialog.show(getActivity(), "Solicitando Token...","Aguarde");//show progress
        }

        @Override
        protected Boolean  doInBackground(String... params) {
            //aguarda a assinatura
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            EntregaHttp entregaHttp=new EntregaHttp();
            mEntrega=entregaHttp.getEntrega(mEntrega.getId());
            if(!mEntrega.getAssinaturaProxy().equals("")){
                return true;
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean args) {
            //progressBar.setVisibility(View.GONE);
            progress.dismiss();
            if (args==true){
                //Toast.makeText(getActivity(),"Token Recebido",Toast.LENGTH_LONG).show();


            }else{
                Toast.makeText(getActivity(),"Ocorreu algum erro",Toast.LENGTH_LONG).show();

                mBtnSolicitarToken.setEnabled(true);
            }
        }
    }//fim do Task

    */



}
