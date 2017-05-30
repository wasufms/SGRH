<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Nova Cisterna Resposta</title>
	 <link href="css/default.css" rel="stylesheet" type="text/css" />
</head>

<body>

	<header id="header"><p>Cisternas</p></header>

	<div id="container">

		<main id="center" class="column">
			<article>
				<h1>Excluir Cisterna</h1>
				<form method="post" action="<%=request.getContextPath()%>/DeleteServlet">
	
	<table>
	<tr>
	<td>Id do Sensor:</td><td><input type="text" name="id" placeholder="00001101-0000-1000-8000-00805F9B34FB
	" /></td>
	</tr>
	<td><input type="submit" name="Enviar"/></td><td></td>
	</tr>
	</table>
	
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