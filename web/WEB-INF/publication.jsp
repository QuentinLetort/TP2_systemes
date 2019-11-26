<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Publication</title>
        <link type="text/css" rel="stylesheet" href="style.css"/>
    </head>
    <body>
        <c:import url="/WEB-INF/menu.jsp"/>
        <div>
            <form method="post" action="publication">
                <fieldset>
                    <legend>Publication d'itinéraire</legend>
                    <p>Vous pouvez ajouter un itinéraire via ce formulaire.</p>

                    <label for="date">Date <span class="requis">*</span></label>
                    <input type="date" id="date" name="date"/>
                    <span class="erreur">${form.erreurs['date']}</span>
                    <br/>

                    <label for="heure">Heure <span class="requis">*</span></label>
                    <input type="time" id="heure" name="heure"/>
                    <span class="erreur">${form.erreurs['heure']}</span>
                    <br/>

                    <label for="destination">Destination <span class="requis">*</span></label>
                    <input type="text" id="destination" name="destination"
                           value="<c:out value="${itineraire.destination}"/>" size="20"
                           maxlength="20"/>
                    <span class="erreur">${form.erreurs['destination']}</span>
                    <br/>

                    <label for="origine">Origine <span class="requis">*</span></label>
                    <input type="text" id="origine" name="origine" value="<c:out value="${itineraire.origine}"/>"
                           size="20" maxlength="20"/>
                    <span class="erreur">${form.erreurs['origine']}</span>
                    <br/>

                    <label for="nbplaces">Nombre de places <span class="requis">*</span></label>
                    <input type="number" id="nbplaces" name="nbplaces" value="<c:out value="${itineraire.nbPlaces}"/>" size="20" min="1" max="5"/>
                    <span class="erreur">${form.erreurs['nbplaces']}</span>
                    <br/>

                    <label for="prix">Prix <span class="requis">*</span></label>
                    <input type="number" id="prix" name="prix" value="<c:out value="${itineraire.prix}"/>" size="20" min="0" max="500" step="0.1"/>
                    <span class="erreur">${form.erreurs['prix']}</span>
                    <br/>

                    <label for="petit">Taille de baggage <span class="requis">*</span></label>
                    <input type="radio" name="taillebaggage" id="petit" value="Petit"> Petit</input>
                    <input type="radio" name="taillebaggage" id="moyen" value="Moyen"> Moyen</input>
                    <input type="radio" name="taillebaggage" id="grand" value="Grand"> Grand</input>
                    <span class="erreur">${form.erreurs['taillebaggage']}</span>
                    <br/>

                    <input type="submit" value="Publier" class="sansLabel"/>
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
