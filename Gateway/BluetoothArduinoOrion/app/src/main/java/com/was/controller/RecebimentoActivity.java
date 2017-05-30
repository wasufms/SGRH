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

import com.google.firebase.iid.FirebaseInstanceId;
import com.was.led.R;

import model.Cisterna;
import model.Entrega;
import model.Manancial;
import persistence.CisternaDAO;
import persistence.CurrentEntregaDAO;
import persistence.EntregaDAO;
import persistence.ManancialDAO;
import services.EntregaHttp;

public class RecebimentoActivity extends AppCompatActivity {

    private TextView txtRecebimento;
    private TextView txtResposta;
    private Button btnEnviarEntrega;
    private ProgressBar progressBarHoriz;
    private ProgressDialog progress;

    Entrega entrega;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recebimento);

        txtRecebimento=(TextView)findViewById(R.id.txtRecebimento);
        txtResposta=(TextView)findViewById(R.id.txtResposta);
        btnEnviarEntrega=(Button)findViewById(R.id.btnEnviarEntrega);
        progressBarHoriz=(ProgressBar)findViewById(R.id.progressBarHoriz);



        btnEnviarEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarEntrega();
            }
        });

       //VERIFICA SE VEIO POR NOTIFICAÇÃO
        Intent extras = getIntent();
        String entregaId=extras.getStringExtra("entregaId");
        Log.d("Notificacao","entregaId->"+entregaId);
        if((entregaId!=null)&&(!entregaId.equals(""))){
            Log.d("Notificacao","CHEGOU->"+entregaId);
            BuscaTaskEntrega buscaTaskEntrega=new BuscaTaskEntrega();
            buscaTaskEntrega.execute(entregaId);
            progressBarHoriz.setIndeterminate(false);

        }else{
            CurrentEntregaDAO currentEntregaDAO=new CurrentEntregaDAO(getApplicationContext());
            entrega=currentEntregaDAO.getCurrentEntrega(getApplicationContext());
            txtRecebimento.setText(entrega.todoString());
        }

    }
    private void salvarEntrega(){
        SalvaEntregaTask salvaEntregaTask=new SalvaEntregaTask();
        salvaEntregaTask.execute();
    }


    private class SalvaEntregaTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            progress= ProgressDialog.show(RecebimentoActivity.this, "Enviando...","Aguarde");//show progress
        }

        @Override
        protected Boolean  doInBackground(String... params) {
            entrega.setAcaoProxy(2);//verificar
            String token = FirebaseInstanceId.getInstance().getToken();
            entrega.setRegstrationId(token);
            EntregaHttp entregaHttp=new EntregaHttp();
            entregaHttp.salvarEntrega(entrega);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean args) {
            //progressBar.setVisibility(View.GONE);
            progress.dismiss();
            progressBarHoriz.setIndeterminate(true);
            txtResposta.setText("Aguardando verificação da Assinatura");
            limpaBanco();//limpa a entrega que está no banco interno
        }
    }//fim do Task


    private class BuscaTaskEntrega extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            progress= ProgressDialog.show(RecebimentoActivity.this, "Atualizando Entrega...","Aguarde");//show progress
            // Log.d("was","Entrei as coordenadas");
        }

        @Override
        protected Boolean  doInBackground(String... params) {
            EntregaHttp entregaHttp=new EntregaHttp();
            entrega=entregaHttp.getEntrega(params[0]);
            return true;

        }
        @Override
        protected void onPostExecute(Boolean args) {
            progress.dismiss();
            txtRecebimento.setText(entrega.todoString());
            int pacoteRecebimentoValido=entrega.getPacoteRecebimentoValido();
            if(pacoteRecebimentoValido==1){
                txtResposta.setTextColor(Color.GREEN);
                txtResposta.setText("Entrega Recebida. \n Assinatura OK");
            }else{
                txtResposta.setTextColor(Color.RED);
                txtResposta.setText("Entrega Recebida. \n Assinatura NÃO AUTENTICADA");
            }

        }
    }//fim do Task

    private void limpaBanco(){
        CurrentEntregaDAO currentEntregaDAO=new CurrentEntregaDAO(getApplicationContext());
        Entrega entrega=currentEntregaDAO.getCurrentEntrega(getApplicationContext());
        Cisterna cistena=currentEntregaDAO.getCurrentCisterna(getApplicationContext());
        Manancial manancial=currentEntregaDAO.getCurrentManancial(getApplicationContext());

        CisternaDAO cisternaDAO=new CisternaDAO(getApplicationContext());
        cisternaDAO.delete(cistena);

        ManancialDAO manancialDAO=new ManancialDAO(getApplicationContext());
        manancialDAO.delete(manancial);

        EntregaDAO entregaDAO=new EntregaDAO(getApplicationContext());
        entregaDAO.delete(entrega);
    }
}
