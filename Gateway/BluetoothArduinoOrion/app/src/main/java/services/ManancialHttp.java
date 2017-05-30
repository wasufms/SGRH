package services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;


import model.Manancial;
import util.Config;

public class ManancialHttp {

    private static final String SERVIDOR=Config.SERVIDOR;
    private Manancial manancial;

    private HttpURLConnection abrirConexao(String url,String method)throws Exception{
        URL urlCon=new URL(url);
        HttpURLConnection conexao=(HttpURLConnection)urlCon.openConnection();
        conexao.setRequestMethod(method);
        conexao.setReadTimeout(30000);
        conexao.setConnectTimeout(30000);
        //
        conexao.setDoOutput(true);
        conexao.setDoInput(true);
        if(!method.equals("DELETE")){
            System.out.println("Entrei");
            conexao.setDoInput(true);
            conexao.addRequestProperty("Content-type","application/json");
        }

        conexao.connect();
        return conexao;
    }

    private static String bytesParaString(InputStream is)throws IOException {
        byte[]buffer=new byte[1024];
        ByteArrayOutputStream bufferzao=new ByteArrayOutputStream();
        int bytesLidos;
        while ((bytesLidos=is.read(buffer))!=-1){
            bufferzao.write(buffer,0,bytesLidos);
        }
        return new String(bufferzao.toByteArray(),"UTF-8");
    }

    public  Manancial getManancial(String id){
        Manancial manancial=new Manancial();
        try{
            String urlRota=SERVIDOR+"/v2/entities/"+id+"?type=Manancial&options=keyValues";
            URL url=new URL(urlRota);
            String result=bytesParaString(url.openConnection().getInputStream());
            Gson gson = new Gson();
            manancial=gson.fromJson(result, Manancial.class);
            return manancial;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro no getManancial");
            //Log.d("was->","Cai aqui->"+e.toString());
        }
        return manancial;
    }

    public  List<Manancial> getManancials(){

        List<Manancial>manancials;
        try{
            String urlRota=SERVIDOR+"/v2/entities?type=Manancial&options=keyValues";
            URL url=new URL(urlRota);
            String result=bytesParaString(url.openConnection().getInputStream());
            System.out.println(result);
            Gson gson = new Gson();
            manancials = new ArrayList<Manancial>(Arrays.asList(gson.fromJson(result, Manancial[].class)));;
            return manancials;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro aqui getManancials");
        }
        return null;
    }


    public void salvarManancial(Manancial manancial){
        try {
            this.manancial=manancial;
            enviarManancialOrion();
        }catch (Exception e){
            System.out.println("ERRO"+e.toString());
            e.printStackTrace();
        }
    }


    //v3
    private boolean enviarManancialOrion()throws Exception{

        String url =SERVIDOR+"/v1/updateContext";
        boolean sucesso=false;
        HttpURLConnection conexao=abrirConexao(url,"POST");
        OutputStream os=conexao.getOutputStream();
        os.write(manancialToJsonBytes());
        os.flush();
        os.close();
        int responseCode=conexao.getResponseCode();
        if (responseCode==HttpURLConnection.HTTP_OK){
            sucesso=true;
        }
        conexao.disconnect();
        return sucesso;
    }

    public byte[] manancialToJsonBytes(){
        String json;

        json="{"
                + " \"contextElements\": ["
                + "{"
                + " \"type\": \"Manancial\","
                + "\"isPattern\": \"false\","
                + "\"id\": \""+this.manancial.getId()+"\","
                + "\"attributes\": ["

                + "{"
                + "\"name\": \"position\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.manancial.getPosition()+"\""
                + " },"

                + "{"
                + "\"name\": \"nome\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.manancial.getNome()+"\""
                + " },"

                + "{"
                + "\"name\": \"outro\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.manancial.getOutro()+"\""
                + " }"

                + "]"
                + " }"
                + "],"
                + "\"updateAction\": \"APPEND\""
                + "}";

        System.out.println(json);
        //Log.d("was",json);
        return json.getBytes();
    }

    public boolean deleteManancial(String id) throws Exception {

        String url =SERVIDOR+"/v2/entities/"+id+"?type=Chave";
        System.out.println(url);
        boolean sucesso=false;
        HttpURLConnection conexao=abrirConexao(url,"DELETE");
        OutputStream os=conexao.getOutputStream();
        // os.write(cisternaToJsonBytes());
        os.flush();
        os.close();
        int responseCode=conexao.getResponseCode();
        if (responseCode==HttpURLConnection.HTTP_OK){
            sucesso=true;
        }
        conexao.disconnect();
        return sucesso;
    }
}
