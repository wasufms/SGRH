package com.was.testes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.j256.ormlite.stmt.QueryBuilder;
import com.was.led.DeviceList;
import com.was.led.R;
import com.was.led.ledControl;

import java.util.ArrayList;
import java.util.List;

import model.Cisterna;
import model.Entrega;
import model.Manancial;
import persistence.CisternaDAO;
import persistence.CurrentEntrega;
import persistence.CurrentEntregaDAO;
import persistence.EntregaDAO;
import persistence.ManancialDAO;

public class TesteDAOActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_dao);
        textView=(TextView)findViewById(R.id.textView2);
        button=(Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testeDAO();
            }
        });
    }
    private void testeDAO(){
        /*    EntregaDAO entregaDAO=new EntregaDAO(getApplication());
            Entrega entrega=new Entrega("1", 0, 0, 0, "1", "1","1", "05-04-2017");
            entregaDAO.insert(entrega);
        Entrega entrega1=entregaDAO.getById("1");*/
        // cisternaDAO=new CisternaDAO(getApplicationContext());
       // List<Cisterna>cisternas=cisternaDAO.getAll();
       //Log.d("DAO-3","entrega->"+cisternas.toString());
       /*
       /*CurrentEntregaDAO currentEntregaDAO=new CurrentEntregaDAO(getApplicationContext());
        CurrentEntrega currentEntrega=new CurrentEntrega(2,"1");
        currentEntregaDAO.insert(currentEntrega);
        CurrentEntrega currentEntrega1=currentEntregaDAO.getById(2);*/
      /*  ManancialDAO manancialDAO=new ManancialDAO(getBaseContext());
        Manancial manancial=new Manancial("1", "111", "Teste", "");
        manancialDAO.insert(manancial);
        //Manancial manancial=manancialDAO.getById("1");
       // manancialDAO.insert(manancial);
        Manancial manancial1=manancialDAO.getById("1");
        Log.d("DAO-3","man->"+manancial1.toString());*/
     /* EntregaDAO entregaDAO=new EntregaDAO(getApplicationContext());
        Entrega entrega=entregaDAO.getById("4");
        Log.d("DAO-3","man->"+entrega.toString());*/
        /*CisternaDAO cisternaDAO=new CisternaDAO(getApplicationContext());
        Cisterna cisterna=cisternaDAO.getById("123-321");
        List<Cisterna>cisternas =cisternaDAO.getAll();
        Log.d("DAO-3","man->");
        for(int i=0;i<cisternas.size();i++){
            Log.d("DAO-3","man->"+cisternas.get(i).toString());
        }*/
        /*CisternaDAO cisternaDAO=new CisternaDAO(getApplicationContext());
        Cisterna cisterna=new Cisterna("1", 4, 4.0,4.0);
        cisternaDAO.insert(cisterna);
        Cisterna cisterna1=cisternaDAO.getById("1");*/
        ManancialDAO manancialDAO=new ManancialDAO(getApplicationContext());
        Manancial manancial=manancialDAO.getById("1");
        manancial.setNome("Manancial Agua Boa");
        manancial.setPosition("-8.0528339,-34.9165064");
        manancialDAO.update(manancial);
        Log.d("DAO-3","man->"+manancial.toString());

        CurrentEntregaDAO currentEntregaDAO=new CurrentEntregaDAO(getApplicationContext());
        Entrega entrega=currentEntregaDAO.getCurrentEntrega(getApplicationContext());
        Log.d("DAO-3","entre->"+entrega.toString());
        Cisterna cisterna=currentEntregaDAO.getCurrentCisterna(getApplicationContext());
        cisterna.setOutro("98:D3:31:40:87:C7");
        CisternaDAO cisternaDAO=new CisternaDAO(getApplicationContext());
        cisternaDAO.update(cisterna);
        Log.d("DAO-3","cis->"+cisterna.toString());

    }
}
