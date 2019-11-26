<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Création d'une réservation</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/style.css"/>"/>
    </head>
    <body>
        <c:import url="/WEB-INF/menu.jsp"/>
        <fieldset>
            <legend>Informations sur l'itinéraire</legend>
            <p>Itinéraire</p>
            <p>Date : <c:out value="${ itineraire.date }"/></p>
            <p>Heure : <c:out value="${ itineraire.heure }"/></p>
            <p>Origine : <c:out value="${ itineraire.origine }"/></p>
            <p>Destination : <c:out value="${ itineraire.destination }"/></p>
            <p>Nombre de places restantes : <c:out value="${ itineraire.nbPlaces }"/></p>
            <p>Prix : <c:out value="${ itineraire.prix }"/></p>
            <p>Taille de baggage : <c:out value="${ itineraire.tailleBaggage }"/></p>
        </fieldset>
        <form method="post" action="<c:url value="/reservation"/>">
            <fieldset>
                <legend>Informations réservation</legend>
                <label for="nbsieges">Nombre de personnes <span class="requis">*</span></label>
                <input type="number" id="nbsieges" name="nbsieges" value="<c:out value="${reservation.nbSieges}"/>"
                       size="20" min="1" max="<c:out value="${ itineraire.nbPlaces }"/>"/>
                <span class="erreur">${form.erreurs['nbplaces']}</span>
                <br/>
                <input type="submit" value="Valider"/>
                <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
            </fieldset>
        </form>
        <c:if test="${!empty sessionScope.sessionUtilisateur}">
            <%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
            <p class="succes">Vous êtes connecté(e) avec l'adresse
                : ${sessionScope.sessionUtilisateur.email}</p>
        </c:if>
    </body>
</html>