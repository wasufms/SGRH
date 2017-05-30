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

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import model.Cisterna;
import model.Entrega;
import util.Config;





//import com.google.android.gms.maps.model.LatLng;

public class CisternaHttp {
	public static void main(String[] args) {
		
		
		CisternaHttp cisternaHttp=new CisternaHttp();
		List<Cisterna>cisternas;
		cisternas= cisternaHttp.getCisternas();
		Cisterna cisterna2=cisternas.get(0);
		cisterna2.setId("2");
		cisterna2.setOutro("Teste da classe");
		cisternaHttp.salvarCisterna(cisterna2);
		for (Cisterna cisterna : cisternas) {
			System.out.println(cisterna.toString());
		}
		
		
        
	}
	
	private static final String SERVIDOR=Config.SERVIDOR;
    private Cisterna cisterna;
	
    public void salvarCisterna(Cisterna cisterna){       
        try {
        	this.cisterna=cisterna;
        	enviarCisternaOrion();
        }catch (Exception e){
            System.out.println("ERRO"+e.toString());
            e.printStackTrace();
        }
    }

   //v2
    private boolean enviarCisternaOrion()throws Exception{
    	
        String url =SERVIDOR+"/v1/updateContext";
        boolean sucesso=false;
        HttpURLConnection conexao=abrirConexao(url,"POST");
        OutputStream os=conexao.getOutputStream();
        os.write(cisternaToJsonBytes());
        os.flush();
        os.close();
        int responseCode=conexao.getResponseCode();
        if (responseCode==HttpURLConnection.HTTP_OK){
            sucesso=true;
        }
        conexao.disconnect();
        return sucesso;
    }
    

    private HttpURLConnection abrirConexao(String url,String method)throws Exception{
        URL urlCon=new URL(url);
        HttpURLConnection conexao=(HttpURLConnection)urlCon.openConnection();
        conexao.setRequestMethod(method);
        conexao.setReadTimeout(30000);
        conexao.setConnectTimeout(30000);
        conexao.setDoInput(true);
        conexao.setDoOutput(true);
        conexao.addRequestProperty("Content-type","application/json");
        conexao.connect();
        return conexao;
    }
    
    //v2
    private byte[] cisternaToJsonBytes(){
        String json;
        json="{"
        		+ " \"contextElements\": ["
        		+ "{"
        		+ " \"type\": \"Cisterna\","
        		+ "\"isPattern\": \"false\","
        		+ "\"id\": \""+this.cisterna.getId()+"\","
        		+ "\"attributes\": ["
        		
        		+ "{"
        		+ "\"name\": \"altura\","
        		+ "\"type\": \"float\","
        		+ "\"value\": \""+this.cisterna.getAltura()+"\""
        		+ " },"
        		
        		+ " {"
        		+ "\"name\": \"areaBase\","
        		+ "\"type\": \"float\","
        		+ "\"value\": \""+this.cisterna.getAreaBase()+"\""
        		+ "},"
        		
        		+ "{"
        		+ "\"name\": \"capacidade\","
        		+ "\"type\": \"float\","
        		+ "\"value\": \""+this.cisterna.getCapacidade()+"\""
        		+ " },"
        		
        		+ "{"
        		+ "\"name\": \"volume\","
        		+ "\"type\": \"float\","
        		+ "\"value\": \""+this.cisterna.getVolume()+"\""
        		+ "},"
        		
        		+ "{"
        		+ "\"name\": \"position\","
        		+ "\"type\": \"geo:point\","
        		+ "\"value\": \""+this.cisterna.getPosition()+"\""
        		+ "},"
        		
        		+ "{"
        		+ "\"name\": \"distancia\","
        		+ "\"type\": \"float\","
        		+ "\"value\": \""+this.cisterna.getDistancia()+"\""
        		+ "},"
        		
				+ "{"
				+ "\"name\": \"raio\","
				+ "\"type\": \"float\","
				+ "\"value\": \""+this.cisterna.getRaio()+"\""
				+ "},"
				
				+ "{"
				+ "\"name\": \"outro\","
				+ "\"type\": \"String\","
				+ "\"value\": \""+this.cisterna.getOutro()+"\""
				+ "}"
				        		
        		+ "]"
        		+ " }"
        		+ "],"
        		+ "\"updateAction\": \"APPEND\""
        		+ "}";                    
        
        System.out.println(json);
        //Log.d("was",json);
        return json.getBytes();
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
    
    
	public  Cisterna getCisterna(String id){
		Cisterna cisterna=new Cisterna();
        try{
        	String urlRota=SERVIDOR+"/v2/entities/"+id+"?type=Cisterna&options=keyValues";
        	URL url=new URL(urlRota);
            String result=bytesParaString(url.openConnection().getInputStream());
            Gson gson = new Gson();
            cisterna=gson.fromJson(result, Cisterna.class);
            return cisterna;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro aqui was");
            //Log.d("was->","Cai aqui->"+e.toString());
        }
        return cisterna;
    }
	
	
	public boolean delete(String id) throws Exception {
		//http://130.206.125.57:1026/v2/entities/id?type=Cisterna
		
		String url =SERVIDOR+"/v2/entities/"+id+"?type=Cisterna";
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
	//public Cisterna createSisterna(Cisterna cisterna){
		//return null;
	//}
	//public void save(Cisterna cisterna){
		//this.salvarCisterna(cisterna);
	//}
    
	public  List<Cisterna> getCisternas(){

		 List<Cisterna>cisternas;
	        try{
	            String urlRota=SERVIDOR+"/v2/entities?type=Cisterna&options=keyValues";
	        	URL url=new URL(urlRota);
	            String result=bytesParaString(url.openConnection().getInputStream());
	            System.out.println(result);
	            Gson gson = new Gson();
	            cisternas = new ArrayList<Cisterna>(Arrays.asList(gson.fromJson(result, Cisterna[].class)));;
	            return cisternas;
	        }catch (Exception e){
	            e.printStackTrace();
	            System.out.println("Erro aqui was");
	        }
	        return null;
    }
    
    
}
