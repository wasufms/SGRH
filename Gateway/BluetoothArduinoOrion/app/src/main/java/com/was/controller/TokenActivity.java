package com.was.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.was.led.MapsActivity;
import com.was.led.R;

import model.Cisterna;
import model.Entrega;
import model.Manancial;
import persistence.CisternaDAO;
import persistence.CurrentEntrega;
import persistence.CurrentEntregaDAO;
import persistence.EntregaDAO;
import persistence.ManancialDAO;
import services.CisternaHttp;
import services.EntregaHttp;
import services.ManancialHttp;

public class TokenActivity extends AppCompatActivity {
    private ProgressDialog progress;
    private ProgressBar progressBarHoriz;
    TextView txtProgressBar;
    TextView txtEntrega;
    TextView txtToken;
    Button btnIniciarDeslocamento;


    Entrega mEntrega;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        txtProgressBar=(TextView) findViewById(R.id.txtProgressBar);
        progressBarHoriz=(ProgressBar)findViewById(R.id.progressHoriz);
        txtEntrega=(TextView)findViewById(R.id.txtEntrega);
        txtToken=(TextView)findViewById(R.id.txtToken);
        btnIniciarDeslocamento=(Button)findViewById(R.id.btnIniciarDeslocamento);

        Intent it=getIntent();
        mEntrega=(Entrega)it.getSerializableExtra("entrega");

        if(mEntrega!=null){
            String pipeiroId=mEntrega.getId()+"="+mEntrega.getAssinaturaProxy();
            //txtPipeiroId.setText("Entrega"+pipeiroId);
            progressBarHoriz.setIndeterminate(true);
            btnIniciarDeslocamento.setEnabled(false);

            txtToken.setText("Aguardando Token");
            txtToken.setEnabled(false);
            txtToken.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_lock_idle_lock,0, 0, 0);
            init();
        }else{
            progressBarHoriz.setVisibility(View.GONE);
            Intent extras = getIntent();
            String entregaId=extras.getStringExtra("entregaId");
            BuscaTokenTask buscaTokenTask=new BuscaTokenTask();
            buscaTokenTask.execute(entregaId);

            txtToken.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(TokenActivity.this,mEntrega.getAssinaturaProxy().toString(),Toast.LENGTH_LONG).show();
                }
            });
            btnIniciarDeslocamento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prepararParaDeslocar();
                }
            });
        }

    }
    private Cisterna mCisterna;
    private Manancial mManancial;
    private void prepararParaDeslocar(){
        BuscaTask buscaTask=new BuscaTask();
        buscaTask.execute();

       /* Intent it=new Intent(this,MapsActivity.class);
        it.putExtra("entrega",mEntrega);
        it.putExtra("cisterna",cisterna);
        it.putExtra("manancial",manancial);
        startActivity(it);*/


    }


    private class BuscaTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            progress= ProgressDialog.show(TokenActivity.this, "Atualizando Entrega...","Aguarde");//show progress
            // Log.d("was","Entrei as coordenadas");
        }

        @Override
        protected Boolean  doInBackground(String... params) {
            CisternaHttp cisternaHttp= new CisternaHttp();
            mCisterna=cisternaHttp.getCisterna(mEntrega.getCisterna());

            ManancialHttp manancialHttp=new ManancialHttp();
            mManancial=manancialHttp.getManancial(mEntrega.getManancial());

            return true;
        }
        @Override
        protected void onPostExecute(Boolean args) {
            progress.dismiss();
            //Toast.makeText(TokenActivity.this,"Cisterna: "+cisterna.getPosition()+"Manancial"+manancial.getPosition(),Toast.LENGTH_LONG).show();
           // Log.d("was",manancial.getPosition());
            //Log.d("was",manancial.getPosition());

            //COLOCO NO PERSISTO NO BD
            persistirEntidades();

           Intent it=new Intent(TokenActivity.this,MapsActivity.class);
            //it.putExtra("entrega",mEntrega);
            //it.putExtra("cisterna",mCisterna);
            //it.putExtra("manancial",mManancial);
            startActivity(it);

        }
    }//fim do Task

    private void persistirEntidades(){
        EntregaDAO entregaDAO=new EntregaDAO(getApplicationContext());
        entregaDAO.insert(mEntrega);
        Log.d("TokenActivite","aqui->"+mEntrega.toString());
        //Entrega entrega=entregaDAO.getById("4");
        //Log.d("TokenActivite","aqui 2->"+entrega.toString());

        CurrentEntregaDAO currentEntregaDAO=new CurrentEntregaDAO(getApplicationContext());
        CurrentEntrega currentEntrega=currentEntregaDAO.getById(1);
        if(currentEntrega==null){
            currentEntrega=new CurrentEntrega(1,mEntrega.getId());
            currentEntregaDAO.insert(currentEntrega);
        }else {
            currentEntrega.setEntregaId(mEntrega.getId());
            currentEntregaDAO.update(currentEntrega);
        }

        CisternaDAO cisternaDAO=new CisternaDAO(getApplicationContext());
        cisternaDAO.insert(mCisterna);

        ManancialDAO manancialDAO=new ManancialDAO(getApplicationContext());
        manancialDAO.insert(mManancial);
    }


    private void init(){

        if (mEntrega != null) {
            String entrega="Entrega: "+mEntrega.getId()+"\n"+
                    "[Pipeiro: "+mEntrega.getPipeiro()+"\n"+
                    "Cisterna: "+mEntrega.getCisterna()+"\n"+
                    "Manancial: "+mEntrega.getManancial()+"\n"+
                    "Volume a ser Entregue: "+mEntrega.getVolumePrevisto()+"\n"+
                    "Data Prevista: "+mEntrega.getDataPrevista()+"]";
            txtEntrega.setText(entrega);
        }
    }


    private class BuscaTokenTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            progress=ProgressDialog.show(TokenActivity.this, "Solicitando Token...","Aguarde");//show progress
        }

        @Override
        protected Boolean  doInBackground(String... params) {
            EntregaHttp entregaHttp=new EntregaHttp();
            mEntrega=entregaHttp.getEntrega(params[0]);
            Log.d("was","procurar:"+params[0]);
            if(!mEntrega.getAssinaturaProxy().equals("")){
                return true;
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean args) {
            progress.dismiss();
            if (args==true){
                //Toast.makeText(getActivity(),"Token Recebido",Toast.LENGTH_LONG).show();
                txtToken.setText("Token Recebido");
                txtToken.setEnabled(true);
                txtToken.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_secure,0, 0, 0);
                txtToken.setTextColor(Color.GREEN);
                btnIniciarDeslocamento.setEnabled(true);
                init();
            }else{
                Toast.makeText(TokenActivity.this,"Ocorreu algum erro",Toast.LENGTH_LONG).show();
                txtToken.setText("Token Não Recebido");
                txtToken.setEnabled(true);
                txtToken.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_partial_secure,0, 0, 0);
                txtToken.setTextColor(Color.RED);
                btnIniciarDeslocamento.setEnabled(true);
            }
        }
    }//fim do Task


}
