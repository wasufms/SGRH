package services;

import java.util.List;

import model.Entrega;

public class EntregaService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private EntregaHttp entregaHttp;
	
	public EntregaService(){
		entregaHttp=new EntregaHttp();
	}
	
	public List<Entrega> getEntregas(){
		List<Entrega>entregas=entregaHttp.getEntregas();
		return entregas;
	}
	
	public Entrega getEntrega(String id){
		return entregaHttp.getEntrega(id);
	}
	
	public boolean delete(String id){
		try {
			return entregaHttp.delete(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean save(Entrega entrega){
		 entregaHttp.salvarEntrega(entrega);
		 return true;
	}

	

}
