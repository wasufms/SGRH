package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import model.Cisterna;
import services.CisternaService;

/**
 * Servlet implementation class CisternasSevlet
 */
@WebServlet("/CisternasSevlet")
public class CisternasSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CisternasSevlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			/*
			 * QUANDO FOR A VERA FICA ESSE CÓDIGO
		    CisternaService cisternaService=new CisternaService();
			String json = new Gson().toJson(cisternas=cisternaService.getCisternas());
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
			 */
		
		//ESSE CÓDIGO É PARA TESTE COM VOLUME RANDOMICO
		
			CisternaService cisternaService=new CisternaService();
		   
		    List<Cisterna>cisternas=new ArrayList<Cisterna>();
		    cisternas=cisternaService.getCisternas();
		    Cisterna cistena1=cisternas.get(1);
		    Random gerador = new Random();
		    
	        double volume = gerador.nextDouble()*cistena1.getCapacidade();
		    cistena1.setVolume(volume);
		    
		    cisternas.add(cistena1);
		    String json = new Gson().toJson(cisternas);
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
	}

}
