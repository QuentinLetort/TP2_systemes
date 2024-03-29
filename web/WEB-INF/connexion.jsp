<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Connexion</title>
        <link type="text/css" rel="stylesheet" href="style.css"/>
    </head>
    <body>
        <c:import url="/WEB-INF/menu.jsp"/>
        <div>
            <form method="post" action="<c:url value="/connexion" />">
                <fieldset>
                    <legend>Connexion</legend>
                    <p>Vous pouvez vous connecter via ce formulaire.</p>

                    <label for="email">Adresse email <span class="requis">*</span></label>
                    <input type="email" id="email" name="email" value="<c:out value="${utilisateur.email}"/>" size="20"
                           maxlength="60"/>
                    <span class="erreur">${form.erreurs['email']}</span>
                    <br/>

                    <label for="motdepasse">Mot de passe <span class="requis">*</span></label>
                    <input type="password" id="motdepasse" name="motdepasse" value="" size="20" maxlength="20"/>
                    <span class="erreur">${form.erreurs['motdepasse']}</span>
                    <br/>

                    <input type="submit" value="Connexion" class="sansLabel"/>
                    <br/>

                    <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
                </fieldset>
            </form>
        </div>
        <%-- Vérification de la présence d'un objet utilisateur en session --%>
        <c:if test="${!empty sessionScope.sessionUtilisateur}">
            <%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
            <p class="succes">Vous êtes connecté(e) avec l'adresse
                : ${sessionScope.sessionUtilisateur.email}</p>
        </c:if>
    </body>
</html>