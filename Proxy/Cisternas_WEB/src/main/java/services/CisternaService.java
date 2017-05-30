package services;

import java.util.List;

import model.Cisterna;

public class CisternaService {
	private CisternaHttp cisternaHttp;
	
	public CisternaService(){
		cisternaHttp=new CisternaHttp();
	}
	
	public List<Cisterna> getCisternas(){
		List<Cisterna>cisternas=cisternaHttp.getCisternas();
		return cisternas;
	}
	
	public Cisterna getCisterna(String id){
		return cisternaHttp.getCisterna(id);
	}
	
	public boolean delete(String id){
		try {
			return cisternaHttp.delete(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean save(Cisterna cisterna){
		// cisternaHttp.save(cisterna);
		cisternaHttp.salvarCisterna(cisterna);
		 return true;
		
	}
}
