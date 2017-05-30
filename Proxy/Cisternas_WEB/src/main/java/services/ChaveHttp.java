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

import model.Chave;
import model.Entrega;
import util.Config;

public class ChaveHttp {
	
	private static final String SERVIDOR=Config.SERVIDOR;
	private Chave chave;
	
	public static void main(String[] args) {
		ChaveHttp chaveHttp=new ChaveHttp();
		/*Chave chave=new Chave();
		chave=chaveHttp.getChave("1");
		System.out.println("aqui");
		System.out.println(chave.getPublicKey());*/
		/*List<Chave>chaves=new ArrayList<Chave>();
		chaves=chaveHttp.getChaves();
		for (Chave chave : chaves) {
			System.out.println(chave.getPublicKey());
		}*/
		/*Chave chave=new Chave();
		chave=chaveHttp.getChave("987");
		chave.setPublicKey("Nullo");
		chaveHttp.salvarChave(chave);
		//System.out.println(chave.toHex());*/
		try {
			chaveHttp.deleteChave("987");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
    
    public  Chave getChave(String id){
    	Chave chave=new Chave();
        try{
        	String urlRota=SERVIDOR+"/v2/entities/"+id+"?type=Chave&options=keyValues";
        	URL url=new URL(urlRota);
            String result=bytesParaString(url.openConnection().getInputStream());
            Gson gson = new Gson();
            chave=gson.fromJson(result, Chave.class);
            return chave;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro no getChave");
            //Log.d("was->","Cai aqui->"+e.toString());
        }
        return chave;
    }
    
	public  List<Chave> getChaves(){

        List<Chave>chaves;
        try{
            String urlRota=SERVIDOR+"/v2/entities?type=Chave&options=keyValues";
        	URL url=new URL(urlRota);
            String result=bytesParaString(url.openConnection().getInputStream());
            System.out.println(result);
            Gson gson = new Gson();
            chaves = new ArrayList<Chave>(Arrays.asList(gson.fromJson(result, Chave[].class)));;
            return chaves;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro aqui was");
        }
        return null;
    }
    
	
	public void salvarChave(Chave chave){       
        try {
        	this.chave=chave;
        	enviarChaveOrion();
        }catch (Exception e){
            System.out.println("ERRO"+e.toString());
            e.printStackTrace();
        }
	}
    

   //v3
   private boolean enviarChaveOrion()throws Exception{
    	
        String url =SERVIDOR+"/v1/updateContext";
        boolean sucesso=false;
        HttpURLConnection conexao=abrirConexao(url,"POST");
        OutputStream os=conexao.getOutputStream();
        os.write(chaveToJsonBytes());
        os.flush();
        os.close();
        int responseCode=conexao.getResponseCode();
        if (responseCode==HttpURLConnection.HTTP_OK){
            sucesso=true;
        }
        conexao.disconnect();
        return sucesso;
    }
    
    public byte[] chaveToJsonBytes(){
        String json;
              
        json="{"
        		+ " \"contextElements\": ["
        		+ "{"
        		+ " \"type\": \"Chave\","
        		+ "\"isPattern\": \"false\","
        		+ "\"id\": \""+this.chave.getId()+"\","
        		+ "\"attributes\": ["
        		+ "{"
        		+ "\"name\": \"publicKey\","
        		+ "\"type\": \"String\","
        		+ "\"value\": \""+this.chave.getPublicKey()+"\""
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
  
    public boolean deleteChave(String id) throws Exception {
		
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
