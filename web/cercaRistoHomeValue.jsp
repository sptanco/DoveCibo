<%@page import="restaurants.Cusine"%>
<%@page import="restaurants.Coordinate"%>
<%@page import="restaurants.Restaurant"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%--
    Mostra una tabella DataTable dei risultati ottenuti dalla ricerca per tipologia di cucina e posizione
--%>

<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">        
        <link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/dataTables.bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="/DoveCiboGit/css/cercaRistoHomeValue.css" />

        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>      
        <script src="http://code.jquery.com/jquery-1.12.3.js"></script>
        <script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>
        <script src="http://maps.googleapis.com/maps/api/js?key=AIzaSyC2yRPFE60Fp4Q05ezqySYocW9zpmqeIwI&callback=initMap" async defer></script>
        <script type="text/javascript" src="/DoveCiboGit/script/datatable.js"></script>
        <script type="text/javascript" src="/DoveCiboGit/script/datatable2.js"></script>
        
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

    <body style="padding-top: 70px;">
        <%@ include file="navBar.jsp" %>
        <div class="container">
            <div class="row row-centered">
                <br><br>
                <table id="DataTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
                    <thead>
                        <tr style="background-color: rgba(198, 239, 255, 1);">
                            <th>NOME</th>
                            <th>VALUTAZIONE</th>
                            <th>RANK</th>
                            <th>COMMENTI</th>
                            <th>€ MIN</th>
                            <th>€ MAX</th>
                            <th>CUCINA</th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <th>Nome</th>
                            <th>Valutazione</th>
                            <th>Rank</th>
                            <th>Review</th>
                            <th>Min</th>
                            <th>Max</th>
                            <th>Cucine</th>
                        </tr>
                    </tfoot>
                    <tbody style="background-color: white;">
                        <%  ArrayList<Restaurant> ALR = (ArrayList<Restaurant>) request.getAttribute("listaRistoranti");
                            Coordinate mieCoor = (Coordinate) request.getAttribute("mieCoordinate");
                            ArrayList<Integer> classifica = (ArrayList<Integer>) request.getAttribute("classifica");
                            for (Restaurant rest : ALR) {
                        %>
                        <tr>
                            <td>
                                <!-- bottone SUBMIT che contiene il nome del ristorante -->
                                <div class="bottom text-center">
                                    <b><a href='/DoveCiboGit/ServletGetRistorante?idR=<%= rest.getId()%> ' style="color: blue"><%= rest.getName().substring(0, 1).toUpperCase() + rest.getName().substring(1)%></a></b>
                                </div>
                                <br>
                                <div class="bottom text-center">
                                    <button class="btn btn-info btn-sm btn-justified" onclick="moveToLocation(<%= rest.getCordinate().getLatitude()%>,<%= rest.getCordinate().getLongitude()%>)">Mappa</button>
                                </div>
                            </td>
                            <%  BigDecimal roundfinalPrice = new BigDecimal(rest.getGlobal_value()).setScale(1, BigDecimal.ROUND_HALF_UP);%>
                            <td>
                                <div id="cont">
                                    <p id="testo"> <%= roundfinalPrice%> </p>
                                    <img id="immagine" src="
                                         <% if (rest.getPhotos().size() != 0) {%>
                                         ImmaginiCaricate/<%= rest.getPhotos().get(0).getPath()%><% } else {%>
                                         Sfondi/empty_img.png <% }%>
                                         "/>
                                </div>
                            </td>
                            <td> <%= classifica.indexOf(rest.getId()) + 1%> </td>
                            <td><%= rest.getN_reviews()%></td>
                            <td><%= rest.getPrice_range().getMin_value()%></td>
                            <td><%= rest.getPrice_range().getMax_value()%></td>
                            <td>                                                
                                <%
                                    int size = rest.getCusines().size();
                                    for (Cusine c : rest.getCusines()) {
                                        if (size > 1) {
                                %>
                                <%=c.getName() + ", "%>
                                <%
                                } else {
                                %>
                                <%=c.getName()%>
                                <%
                                        }
                                        size -= 1;
                                    }
                                %>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
                
                <!--MAPPPA-->

                <div id="map" class="row">

                    <script>
                        var map;

                        function initMap() {
                            var uluru = {lat: <%= mieCoor.getLatitude()%>, lng: <%= mieCoor.getLongitude()%>};
                            map = new google.maps.Map(document.getElementById('map'), {
                                zoom: 12,
                                center: uluru
                            });

                            var infowindow = new google.maps.InfoWindow({
                                content: "MIA POSIZIONE"
                            });

                            function pinSymbol(color) {
                                return {
                                    path: 'M 0,0 C -2,-20 -10,-22 -10,-30 A 10,10 0 1,1 10,-30 C 10,-22 2,-20 0,0 z M -2,-30 a 2,2 0 1,1 4,0 2,2 0 1,1 -4,0',
                                    fillColor: color,
                                    fillOpacity: 1,
                                    strokeColor: '#000',
                                    strokeWeight: 2,
                                    scale: 1,
                                };
                            }


                            var marker = new google.maps.Marker({
                                position: {lat:<%= mieCoor.getLatitude()%>, lng:<%= mieCoor.getLongitude()%>},
                                map: map,
                                icon: pinSymbol("#FCC602")
                            });

                            marker.addListener('click', function () {
                                infowindow.open(map, marker);
                            });


                        <% for (Restaurant rest : ALR) {%>
                            var infowindow<%= rest.getId()%> = new google.maps.InfoWindow({
                                content: "  <b><a href='/DoveCiboGit/ServletGetRistorante?idR=<%= rest.getId()%> ' style='color: blue'> <%= rest.getName()%></a></b>"
                            });
                        <%  } %>


                        <% for (Restaurant rest : ALR) {%>
                            var marker<%= rest.getId()%> = new google.maps.Marker({
                                position: {lat:<%= rest.getCordinate().getLatitude()%>, lng:<%= rest.getCordinate().getLongitude()%>},
                                map: map
                            });
                        <%  } %>


                        <% for (Restaurant rest : ALR) {%>
                            marker<%= rest.getId()%>.addListener('click', function () {
                                infowindow<%= rest.getId()%>.open(map, marker<%= rest.getId()%>);
                            });
                        <%  }%>
                        }

                        function moveToLocation(lat, lng) {
                            var center = new google.maps.LatLng(lat, lng);
                            // using global variable:
                            map.panTo(center);
                            map.setZoom(17);
                        }

                    </script>
                </div>
            </div>
        </div>
    </body>
</html>
