<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Nova Cisterna</title>
	 <link href="css/default.css" rel="stylesheet" type="text/css" />
</head>

<body>

	<header id="header"><p>Cisternas</p></header>

	<div id="container">

		<main id="center" class="column">
			<article>
				<h1>Nova Cisterna</h1>
				
	<!--  
	private String id;
	private double altura;//altura real da cisterna
	private double raio;
	private double capacidade;
	private double volume;
	private double distancia;//distancia entre o Sensor e o espelho dágua CHEIO-->
	
	<form method="post" action="<%=request.getContextPath()%>/AddServlet">
	
	<table>
	<tr>
	<td>Id da Sensor:</td><td><input type="text" name="id" placeholder="00001101-0000-1000-8000-00805F9B34FB
	" /></td>
	</tr>
	<tr>
	<td>Distância do Sensor:</td><td><input type="text" name="distancia" placeholder="100"/>cm</td>
	</tr>
	<tr>
	<td>Altura da Cisterna:</td><td><input type="text" name="altura" placeholder="200"/>cm</td>
	</tr>
	<tr>
	<td>Área da Base:</td><td><input type="text" name="areaDaBase" placeholder="80384"/>cm&sup2;</td>
	</tr>
	<tr>
	<td><input type="submit" name="Enviar"/></td><td></td>
	</tr>
	</table>
	
	</form>
	
			
			</article>								
		</main>

		<nav id="left" class="column">
			<%@include file="includes/menu.html" %>			
		</nav>

		<div id="right" class="column">
			
		</div>

	</div>

	<div id="footer-wrapper">
		<footer id="footer"><p>Footer...</p></footer>
	</div>

</body>

</html>