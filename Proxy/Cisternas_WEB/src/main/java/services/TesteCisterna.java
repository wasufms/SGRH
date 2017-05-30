package services;

import javax.servlet.http.HttpServletRequest;

import model.Cisterna;

public class TesteCisterna {
	
	private Cisterna cisterna;
	private CisternaService cisternaService;
	
	
	public static void main(String[] args) {
		TesteCisterna tc=new TesteCisterna();
		tc.cisterna=new Cisterna("123-321",100,200,80384);
		//System.out.println(tc.cisterna.toString());
		tc.cisternaService=new CisternaService();
		//tc.cisternaService.save(tc.cisterna);
		tc.cisterna=tc.cisternaService.getCisterna("123-321");
		System.out.println(tc.cisterna.toString());
	}
	
	
	/*private void preencheCisterna(HttpservletRequest req){
		
		 String id;
		 double altura;//altura real da cisterna
		 double distancia;//distancia entre o Sensor e o espelho d�gua CHEIO
		 double areaBase;
		 
		 id=req.id;
		 altura=Double.parseDouble(req.altura);
		 distancia=Double.parseDouble(req.distancia);
		 areaBase=Double.parseDouble(req.areaBase);
		 this.cisterna=new Cisterna(id,distancia,altura,areaBase);
		 
	}*/
	
	

}
