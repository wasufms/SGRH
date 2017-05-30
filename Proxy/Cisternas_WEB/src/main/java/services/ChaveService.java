package services;

import java.util.List;

import model.Chave;

public class ChaveService {
private ChaveHttp chaveHttp;
	
	public ChaveService(){
		chaveHttp=new ChaveHttp();
	}
	
	public List<Chave> getChaves(){
		List<Chave>chaves=chaveHttp.getChaves();
		return chaves;
	}
	
	public Chave getChave(String id){
		return chaveHttp.getChave(id);
	}
	
	public boolean delete(String id){
		try {
			return chaveHttp.deleteChave(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean save(Chave chave){
		 chaveHttp.salvarChave(chave);
		 return true;
	}
}
