

<%@page import="model.Cisterna"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
  <head>
    <style type="text/css">
      html, body { height: 100%; margin: 0; padding: 0; }
      #map { height: 100%; }
    </style>
  </head>
  <body>
  
  <%
					String cisternasJson=(String)request.getAttribute("cisternasJson");
   %>
    <div id="map"></div>
    
    <script type="text/javascript">
  // console.log(5 + 6);
   var obj = JSON.parse('<%=cisternasJson%>');
  
               function initMap() {
                   var latlng = new google.maps.LatLng(-8.055861, -34.951083);
                   var myOptions = {
                       zoom: 10,
                       center: latlng,
                       mapTypeId: google.maps.MapTypeId.ROADMAP,
                      
                   };
                   var map = new google.maps.Map(document.getElementById("map"),myOptions);
                   var infowindow = new google.maps.InfoWindow(), marker, i;
                   
                   for (i = 0; i < obj.length; i++) {  
                	   var latLong=obj[i].localizacao.split(",");
                	  
                	   percent=(obj[i].volume/obj[i].capacidade)*100;
                	   
                	   
                       marker = new google.maps.Marker({
                           position: new google.maps.LatLng(latLong[0],latLong[1]),
                           map: map,
                           title: percent+'% do volume'
                       });
                       marker.setIcon(paintIcon(obj[i].capacidade,obj[i].volume));
                       
                       google.maps.event.addListener(marker, 'click', (function(marker, i) {
                           return function() {
                               infowindow.setContent(makeInfo(obj[i]));
                               infowindow.open(map, marker);
                           }
                       })(marker, i));
                   }
               }
               
               function makeInfo(cisterna){
            	   //console.log(cisterna)
            	   return contentString = '<div id="content">'+
                   '<div id="siteNotice">'+
                   '</div>'+
                   '<h1 id="firstHeading" class="firstHeading">Cisterna '+cisterna.id+'</h1>'+
                   '<div id="bodyContent">'+
                   '<p><b>Cisterna Altura: </b>' + cisterna.altura+'m'+
                   '<p><b>Cisterna Área da Base: </b>' +cisterna.areaBase + 'm2'+
                   '<p><b>Cisterna Distância Inicial: </b>' +cisterna.distancia+'m'+
                   '<p><b>Cisterna Capacidade: </b>' + cisterna.capacidade+' Litros'+
                   '<p><b>Cisterna Volume: </b>' + cisterna.volume+' Litros'+
                   
                   '<p>Detalhes: <a href="#">'+
                   'detalhes</a> ,</p>'+
                   '</div>'+
                   '</div>';
               }
               function paintIcon(capacidade,volume){
            	   urlIcon="http://maps.google.com/mapfiles/ms/icons/";
            	   percent=(volume/capacidade)*100;
            	   if(percent<25){
            		   urlIcon+="red-dot.png";
            		   console.log(urlIcon);
            	   }else if(percent>=25 && percent<50 ){
            		   urlIcon+="yellow-dot.png";
            		   console.log(urlIcon);
            	   }else{
            		   urlIcon+="green-dot.png";
            		   console.log(urlIcon);
            	   }
            	   return urlIcon;
               }
          
 
    </script>
    <script async defer
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyACzh9t9xNZ53-mDlCJ4YHgBu8WjEDMWfA&callback=initMap">
    </script>
  </body>
</html>