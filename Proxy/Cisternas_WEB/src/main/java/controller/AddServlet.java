package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Cisterna;
import services.CisternaService;

/**
 * Servlet implementation class AddServlet
 */
@WebServlet("/AddServlet")
public class AddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Cisterna cisterna;
	private CisternaService cisternaService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddServlet() {
        super();
        cisternaService=new CisternaService();
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
		preencheCisterna(request);
		//response.getWriter().print(this.cisterna.toString());
		
		cisternaService.save(this.cisterna);
		
		request.setAttribute("resp", "Cisterna Inserida com sucesso");
		try {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/respostas.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
		//response.getWriter().print("oi");
		//doGet(request, response);
	}
	
	private void preencheCisterna(HttpServletRequest req){
		
		 String id;
		 double altura;//altura real da cisterna
		 double distancia;//distancia entre o Sensor e o espelho d�gua CHEIO
		 double areaBase;
		 
		 id=req.getParameter("id");
		 altura=Double.parseDouble(req.getParameter("altura"));
		 distancia=Double.parseDouble(req.getParameter("distancia"));
		 areaBase=Double.parseDouble(req.getParameter("areaDaBase"));
		 this.cisterna=new Cisterna(id,distancia,altura,areaBase);
		 
	}

}
