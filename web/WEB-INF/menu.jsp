<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div>
    <a href="<c:url value="/listeItineraires"/>">Itineraires</a>
    <c:choose>
        <c:when test="${empty sessionScope.sessionUtilisateur}">
            <a href="<c:url value="/connexion"/>">Connexion</a>
            <a href="<c:url value="/inscription"/>">Inscription</a>
        </c:when>
        <c:otherwise>
            <a href="<c:url value="/recherche"/>">Rechercher</a>
            <a href="<c:url value="/publication"/>">Publier</a>
            <a href="<c:url value="/deconnexion"/>">Deconnexion</a>
        </c:otherwise>
    </c:choose>
</div>