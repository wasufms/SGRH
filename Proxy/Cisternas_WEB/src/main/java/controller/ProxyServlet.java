package controller;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import ed25519.ed25519;
import model.Chave;
import model.Entrega;
import services.ChaveService;
import services.EntregaHttp;
import services.EntregaService;
import services.ProxyHttp;
import util.Config;
import util.Print;

import javax.xml.bind.DatatypeConverter;

/**
 * Servlet implementation class ProxyServlet
 */
@WebServlet("/ProxyServlet")
public class ProxyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProxyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("was at: ").append(request.getContextPath());
		//response.getWriter().append("<br>Valor: ").append(request.toString());
		
		 EntregaHttp entregaHttp=new EntregaHttp();
		  
		  Entrega entrega=entregaHttp.getEntrega("1");
		  System.out.println(entrega.getId()+"->"+entrega.getAcaoProxy()+":"+entrega.getAssinaturaProxy());
		  entrega.setAssinaturaProxy("no GET");
		  System.out.println(entrega.getId()+"->"+entrega.getAcaoProxy()+":"+entrega.getAssinaturaProxy());
		  entregaHttp.salvarEntrega(entrega);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String notificationBody=getBody(request);
		
		
		EntregaService entregaService=new EntregaService();
		ProxyHttp proxyHttp=new ProxyHttp(); 
		String id=proxyHttp.getEntregaId(notificationBody);
		
		Entrega entrega=entregaService.getEntrega(id);
		
		//ASSINAR=acaoProxy=1 VERIFICAR=acaoProxy=2
		int acaoProxy=entrega.getAcaoProxy();
		if(acaoProxy==1){
			String sign=assina(entrega);
			Print.log(sign);//debug em /tmp/file.txt
			entrega.setAssinaturaProxy(sign);
			entrega.setStatus(1);
		}
		if(acaoProxy==2){
			//entrega.setAssinaturaNo("14:40");
			entrega.setStatus(2);
			if(verifica(entrega)==true){
				entrega.setPacoteRecebimentoValido(1);//VALIDO
				Print.log("olha aqui no proxy 2 POSITIVO: "+entrega.getAcaoProxy());//debug em /tmp/file.txt
			}else{
				entrega.setPacoteRecebimentoValido(0);//INVALIDO
			}
			System.out.println("olha aqui no proxy 2");
		}
				  
		  //entrega.setAssinaturaNo("21:18: id="+id+"acaoProxy="+entrega.getAcaoProxy());
		  entregaService.save(entrega);
		  proxyHttp.notificar(entrega);
		  
		
	}
	
	private String assina(Entrega entrega){
		byte[] sk=Config.SK;
		byte[] pk = ed25519.publickey(sk);
		String msg="";
		msg=entrega.getId()+entrega.getPipeiro()+entrega.getManancial()+entrega.getCisterna();
		System.out.println(msg);
		byte[] message = msg.getBytes();
		byte[] signature = ed25519.signature(message, sk, pk);
		String signatureHex=Print.getHex(signature);
		System.out.println(signatureHex);
		return signatureHex;
	}
	private boolean verifica(Entrega entrega){
		boolean retorno=false;
		
		ChaveService chaveService=new ChaveService();
		
		byte[] pk=chaveService.getChave(entrega.getCisterna()).toByteArray();
		
		String msg="";
		msg=entrega.getId()+entrega.getPipeiro()+entrega.getManancial()+entrega.getCisterna();
		byte[] message = msg.getBytes();
		byte[]signature=DatatypeConverter.parseHexBinary(entrega.getAssinaturaNo());
		
		try {
			retorno=ed25519.checkvalid(signature,message,pk);
			
		} catch (Exception e) {
			e.printStackTrace();
			Print.log(e.toString());
		}
		return retorno;
		
	}

	private  String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
	}
	
	private boolean notifica(){
		
		
		return false;
	}
	


}
