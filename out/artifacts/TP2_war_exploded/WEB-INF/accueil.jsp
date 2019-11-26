<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Accueil</title>
        <link type="text/css" rel="stylesheet" href="style.css"/>
    </head>
    <body>
        <c:import url="/WEB-INF/menu.jsp"/>
        <%-- Vérification de la présence d'un objet utilisateur en session --%>
        <c:if test="${!empty sessionScope.sessionUtilisateur}">
            <%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
            <p class="succes">Vous êtes connecté(e) avec l'adresse
                : ${sessionScope.sessionUtilisateur.email}</p>
        </c:if>
    </body>
</html>