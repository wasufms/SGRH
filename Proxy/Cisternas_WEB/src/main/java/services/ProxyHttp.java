package services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Cisterna;
import model.Entrega;
import util.Config;

public class ProxyHttp {
	
	public static void main(String[] args) {
		ProxyHttp proxyHttp=new ProxyHttp();
		//proxyHttp.getEntregaId("");
		EntregaHttp entregaHttp=new EntregaHttp();
		Entrega entrega1=entregaHttp.getEntrega("4");
		proxyHttp.notificar(entrega1);
	}
	
	
	public String getEntregaId(String notificationBody){
		  String id="0";
		  
		 // String teste="{\"subscriptionId\":\"58c46897db966c8f8b336ddc\",\"data\":[{\"id\":\"1\",\"type\":\"Entrega\",\"acaoProxy\":{\"type\":\"Integer\",\"value\":\"2\",\"metadata\":{}}}]}";
		  		  
		  try {
		    JSONObject jsonObject =  new JSONObject(notificationBody);
		    System.out.println(jsonObject.toString());
		    JSONArray data=jsonObject.getJSONArray("data");
			//System.out.println(data.toString());
			JSONObject dataObj=data.getJSONObject(0);
			
			id=dataObj.getString("id");
			System.out.println("id->"+id);
		  } catch (JSONException e) {
			    System.out.println("Erro");
			    e.printStackTrace();
		  }
		  		
		return id;
		
	}
	
	
	private Entrega entrega;
    public void notificar(Entrega entrega){       
        try {
        	this.entrega=entrega;	
        	enviarServidorFCM();
        }catch (Exception e){
            System.out.println("ERRO"+e.toString());
            e.printStackTrace();
        }
    }

   //v2
    private boolean enviarServidorFCM()throws Exception{
    	
        String url =Config.SERVIDOR_FCM;
        boolean sucesso=false;
        HttpURLConnection conexao=abrirConexao(url,"POST");
        OutputStream os=conexao.getOutputStream();
        os.write(messageJsonBytes());
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
        conexao.addRequestProperty("Authorization","key="+Config.FCM_KEY);
        conexao.connect();
        return conexao;
    }
    
    //v2
    private byte[] messageJsonBytes(){
        String json;
       
        
        String regsterId=this.entrega.getRegstrationId();
        String EntregaId=this.entrega.getId();
        int acaoProxy=entrega.getAcaoProxy();
        //acaoProxy=2;
        json="{"
        		
        		+ "\"to\": \""+regsterId+"\","
        		+ "\"data\": {"
        		+ "\"message\": \""+EntregaId+"\","
        		+ "\"acao\": \""+acaoProxy+"\""
        		+ " }"
        		+ "}";                    
        
        System.out.println(json);
        //Log.d("was",json);
        return json.getBytes();
    }

	
	
	
	
	
}
