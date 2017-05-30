<!DOCTYPE html>  
<html>  
<head>  
<meta charset="UTF-8">  
<title>jQuery</title>  
<script src="js/jquery-3.1.1.min.js"></script>  

<style type="text/css">
      html, body { height: 100%; margin: 0; padding: 0; }
      #map { height: 100%; }
    </style>
</head>  
<body>      
<div id="map"></div>


<script>
	var INTERVALO_ATUALIZACAO=10*1000;//milisegundos
	//var URL_DADOS="http://192.168.246.4:8080/Cisternas/CisternasSevlet";
	//var URL_DADOS="http://130.206.125.57:8080/cisternas/CisternasSevlet";
	var URL_DADOS="http://130.206.125.57/cisternas/CisternasSevlet";
	
	jQuery(document).ready(function($) {
			buscaCisternasViaAjax();
			setInterval(buscaCisternasViaAjax,INTERVALO_ATUALIZACAO);
	});

	function buscaCisternasViaAjax() {
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : URL_DADOS,
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				console.log("SUCCESS: ", data[0].id);
				addMark(data);
			},
			error : function(e) {
				console.log("ERROR: ", e);
				alert("Erro na obtenção dos dados");
			},
			done : function(e) {
				console.log("DONE");
			}
		});

	}
	
	
	var map;
    var infowindow;
	function initMap() {
        var latlng = new google.maps.LatLng(-8.055861, -34.951083);
        var myOptions = {
            zoom: 10,
            center: latlng,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
           
        };
       map = new google.maps.Map(document.getElementById("map"),myOptions);
      infowindow = new google.maps.InfoWindow(); 
    }
    
	function addMark(obj){
		var marker, i;
		for (i = 0; i < obj.length; i++) {  
	     	   var latLong=obj[i].localizacao.split(",");
	     	  
	     	   percent=(obj[i].volume/obj[i].capacidade)*100;
	     	   
	            marker = new google.maps.Marker({
	                position: new google.maps.LatLng(latLong[0],latLong[1]),
	                map: map,
	                title: percent.toFixed(1)+'% do volume'
	            });
	            marker.setIcon(paintIcon(obj[i].capacidade,obj[i].volume));
	            
	            google.maps.event.addListener(marker, 'click', (function(marker, i) {
	                return function() {
	                    infowindow.setContent(makeInfo(obj[i]));
	                    infowindow.open(map, marker);
	                }
	            })(marker, i));
		}//fim do for
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
        '<p><b>Cisterna Volume: </b>' + cisterna.volume.toFixed(1)+' Litros'+
        
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

