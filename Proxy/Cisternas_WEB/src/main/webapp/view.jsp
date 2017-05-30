<%@page import="java.util.List"%>
<%@page import="model.Cisterna"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>View Cisternas</title>
	 <link href="css/default.css" rel="stylesheet" type="text/css" /> 
</head>

<body>

	<header id="header"><p>Cisternas</p></header>

	<div id="container">

		<main id="center" class="column">
			<article>
				<h1>Cisternas</h1>
				<!-- <iframe id="framMap" name="framMap" src="<%=request.getContextPath()%>/CisternasMapView.jsp" width="100%" frameborder="0" style="min-height:500px"></iframe> -->
			<iframe id="framMap" name="framMap" src="http://130.206.125.57/cisternas/CisternasMapView.jsp" width="100%" frameborder="0" style="min-height:500px"></iframe>
			</article>								
		</main>
		
		
		
		
		<nav id="left" class="column">
			<%@include file="includes/menu.html" %>			
		</nav>

		

	</div>

	

</body>

</html>