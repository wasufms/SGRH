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


import model.Entrega;
import util.Config;

public class EntregaHttp {

	 //private static final String SERVIDOR="http://192.168.194.145:1026";
    private static final String SERVIDOR=Config.SERVIDOR;
    private Entrega entrega;

    public static void main(String[] args) {
		/*// TODO Auto-generated method stub
		System.out.println(SERVIDOR);
		EntregaHttp entregaHttp=new EntregaHttp();
		//Entrega ent=new Entrega("4");
		//entregaHttp.salvarEntrega(ent);
		try {

			entregaHttp.delete("3");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    /*	EntregaHttp entregaHttp=new EntregaHttp();
    	List<Entrega>entregas=new ArrayList<Entrega>();
    	entregas=entregaHttp.getEntregasByPipeiroId("1");
    	for (Entrega entrega : entregas) {
			System.out.println(entrega.getId());
		}
    	*/

    }

    private HttpURLConnection abrirConexao(String url,String method)throws Exception{
        URL urlCon=new URL(url);
        HttpURLConnection conexao=(HttpURLConnection)urlCon.openConnection();
        conexao.setRequestMethod(method);
        conexao.setReadTimeout(30000);
        conexao.setConnectTimeout(30000);
        //
        conexao.setDoOutput(true);
        if(!method.equals("DELETE")){
            System.out.println("Entrei");
            conexao.setDoInput(true);
            conexao.addRequestProperty("Content-type","application/json");
        }

        conexao.connect();
        return conexao;
    }


    public  Entrega getEntrega(String id){
        Entrega entrega=new Entrega();
        try{
            String urlRota=SERVIDOR+"/v2/entities/"+id+"?type=Entrega&options=keyValues";
          
            URL url=new URL(urlRota);
            String result=bytesParaString(url.openConnection().getInputStream());
            Gson gson = new Gson();
            entrega=gson.fromJson(result, Entrega.class);
            return entrega;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro aqui was");
            //Log.d("was->","Cai aqui->"+e.toString());
        }
        return entrega;
    }


    public  List<Entrega> getEntregas(){

        List<Entrega>entregas;
        try{
            String urlRota=SERVIDOR+"/v2/entities?type=Entrega&options=keyValues";
            URL url=new URL(urlRota);
            String result=bytesParaString(url.openConnection().getInputStream());
            System.out.println(result);
            Gson gson = new Gson();
            entregas = new ArrayList<Entrega>(Arrays.asList(gson.fromJson(result, Entrega[].class)));;
            return entregas;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro aqui was");
        }
        return null;
    }

    public  List<Entrega> getEntregasByPipeiroId(String id){

        List<Entrega>entregas;
        try{
            String urlRota=SERVIDOR+"/v2/entities?q=pipeiro=='"+id+"'&type=Entrega&options=keyValues";

            URL url=new URL(urlRota);
          
            String result=bytesParaString(url.openConnection().getInputStream());
           // System.out.println(result);
            
            Gson gson = new Gson();
            entregas = new ArrayList<Entrega>(Arrays.asList(gson.fromJson(result, Entrega[].class)));;
            return entregas;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro aqui was");
        }
        return null;
    }

    public  List<Entrega> getEntregasAbertaByPipeiroId(String id){

        List<Entrega>entregas;
        try{
            String urlRota=SERVIDOR+"/v2/entities?q=pipeiro=='"+id+"';status=='0'&type=Entrega&options=keyValues";

            URL url=new URL(urlRota);
            
            String result=bytesParaString(url.openConnection().getInputStream());
            // System.out.println(result);
           
            Gson gson = new Gson();
            entregas = new ArrayList<Entrega>(Arrays.asList(gson.fromJson(result, Entrega[].class)));;
            return entregas;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erro aqui was");
        }
        return null;
    }


    public void salvarEntrega(Entrega entrega){
        try {
            this.entrega=entrega;
            enviarEntregaOrion();
        }catch (Exception e){
            System.out.println("ERRO"+e.toString());
            e.printStackTrace();
        }
    }


    //v3
    private boolean enviarEntregaOrion()throws Exception{

        String url =SERVIDOR+"/v1/updateContext";
        boolean sucesso=false;
        HttpURLConnection conexao=abrirConexao(url,"POST");
        OutputStream os=conexao.getOutputStream();
        os.write(entregaToJsonBytes());
        os.flush();
        os.close();
        int responseCode=conexao.getResponseCode();
        if (responseCode==HttpURLConnection.HTTP_OK){
            sucesso=true;
        }
        conexao.disconnect();
        return sucesso;
    }

    public byte[] entregaToJsonBytes(){
        String json;

        /*//TESTE
        	this.entrega=this.getEntrega("2");
        	this.entrega.setAssinaturaProxy("was1");
        //*/



        json="{"
                + " \"contextElements\": ["
                + "{"
                + " \"type\": \"Entrega\","
                + "\"isPattern\": \"false\","
                + "\"id\": \""+this.entrega.getId()+"\","
                + "\"attributes\": ["
                + "{"
                + "\"name\": \"status\","
                + "\"type\": \"Integer\","
                + "\"value\": \""+this.entrega.getStatus()+"\""
                + " },"
                + " {"
                + "\"name\": \"acaoProxy\","
                + "\"type\": \"Integer\","
                + "\"value\": \""+this.entrega.getAcaoProxy()+"\""
                + "},"
                + "{"
                + "\"name\": \"volumePrevisto\","
                + "\"type\": \"Float\","
                + "\"value\": \""+this.entrega.getVolumePrevisto()+"\""
                + " },"
                + "{"
                + "\"name\": \"pipeiro\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.entrega.getPipeiro()+"\""
                + "},"
                + "{"
                + "\"name\": \"manancial\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.entrega.getManancial()+"\""
                + "},"
                + "{"
                + "\"name\": \"cisterna\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.entrega.getCisterna()+"\""
                + "},"

                + "{"
                + "\"name\": \"volumeEntregue\","
                + "\"type\": \"Float\","
                + "\"value\": \""+this.entrega.getVolumeEntregue()+"\""
                + "},"

                + "{"
                + "\"name\": \"pureza\","
                + "\"type\": \"Float\","
                + "\"value\": \""+this.entrega.getPureza()+"\""
                + "},"

                + "{"
                + "\"name\": \"carradaValida\","
                + "\"type\": \"Integer\","
                + "\"value\": \""+this.entrega.getCarradaValida()+"\""
                + "},"

                + "{"
                + "\"name\": \"pacoteEntregavalido\","
                + "\"type\": \"Integer\","
                + "\"value\": \""+this.entrega.getPacoteEntregaValido()+"\""
                + "},"

                + "{"
                + "\"name\": \"pacoteRecebimentoValido\","
                + "\"type\": \"Integer\","
                + "\"value\": \""+this.entrega.getPacoteRecebimentoValido()+"\""
                + "},"

                + "{"
                + "\"name\": \"dataPrevista\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.entrega.getDataPrevista()+"\""
                + "},"

                + "{"
                + "\"name\": \"dataRealizada\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.entrega.getDataRealizada()+"\""
                + "},"

                + "{"
                + "\"name\": \"assinaturaProxy\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.entrega.getAssinaturaProxy()+"\""
                + "},"

                + "{"
                + "\"name\": \"assinaturaNo\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.entrega.getAssinaturaNo()+"\""
                + "},"

                + "{"
                + "\"name\": \"localEntrega\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.entrega.getLocalEntrega()+"\""
                + "},"

                + "{"
                + "\"name\": \"regstrationId\","
                + "\"type\": \"String\","
                + "\"value\": \""+this.entrega.getRegstrationId()+"\""
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




    public boolean delete(String id) throws Exception {

        String url =SERVIDOR+"/v2/entities/"+id+"?type=Entrega";
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
