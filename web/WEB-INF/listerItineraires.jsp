<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Liste des itinéraires existants</title>
        <link type="text/css" rel="stylesheet" href="style.css" />
    </head>
    <body>
        <c:import url="/WEB-INF/menu.jsp" />
        <br/>
        <div id="corps">
            <c:choose>
                <%-- Si aucun itinéraire n'existe en base, affichage d'un message par défaut. --%>
                <c:when test="${ empty listeItineraires }">
                    <p class="erreur">Aucun itinéraire enregistré.</p>
                </c:when>
                <%-- Sinon, affichage du tableau. --%>
                <c:otherwise>
                    <table>
                        <tr>
                            <th>Origine</th>
                            <th>Destination</th>
                            <th>Date</th>
                            <th>Heure</th>
                            <th>Places restantes</th>
                            <th>Prix</th>
                            <th>Taille baggage</th>
                            <th class="action">Action</th>
                        </tr>
                        <c:forEach items="${ listeItineraires }" var="itineraire" varStatus="boucle">
                            <%-- Simple test de parité sur l'index de parcours, pour alterner la couleur de fond de chaque ligne du tableau. --%>
                            <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                                <td><c:out value="${ itineraire.origine }"/></td>
                                <td><c:out value="${ itineraire.destination }"/></td>
                                <td><c:out value="${ itineraire.date }"/></td>
                                <td><c:out value="${ itineraire.heure }"/></td>
                                <td><c:out value="${ itineraire.nbPlaces }"/></td>
                                <td><c:out value="${ itineraire.prix }"/></td>
                                <td><c:out value="${ itineraire.tailleBaggage }"/></td>

                            <%-- Lien vers la servlet de réservation, avec passage de l'id en paramètre grâce à la balise <c:param/>. --%>
                                <td class="action">
                                    <a href="<c:url value="/reservation"><c:param name="itineraireId" value="${ itineraire.id }" /></c:url>">Réserver</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>