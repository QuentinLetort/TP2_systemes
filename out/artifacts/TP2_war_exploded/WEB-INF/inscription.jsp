<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Inscription</title>
        <link type="text/css" rel="stylesheet" href="style.css"/>
    </head>
    <body>
        <c:import url="/WEB-INF/menu.jsp"/>
        <div>
            <form method="post" action="inscription">
                <fieldset>
                    <legend>Inscription</legend>
                    <p>Vous pouvez vous inscrire via ce formulaire.</p>
                    <label for="homme">Sexe <span class="requis">*</span></label>
                    <input type="radio" name="sexe" id="homme" value="Homme"> Homme</input>
                    <input type="radio" name="sexe" id="femme" value="Femme"> Femme</input>
                    <span class="erreur">${form.erreurs['sexe']}</span>
                    <br/>

                    <label for="prenom">Prénom <span class="requis">*</span></label>
                    <input type="text" id="prenom" name="prenom" value="<c:out value="${utilisateur.prenom}"/>" size="20"
                           maxlength="20"/>
                    <span class="erreur">${form.erreurs['prenom']}</span>
                    <br/>

                    <label for="nom">Nom <span class="requis">*</span></label>
                    <input type="text" id="nom" name="nom" value="<c:out value="${utilisateur.nom}"/>" size="20"
                           maxlength="20"/>
                    <span class="erreur">${form.erreurs['nom']}</span>
                    <br/>

                    <label for="nom">Pays <span class="requis">*</span></label>
                    <input type="text" id="pays" name="pays" value="<c:out value="${utilisateur.pays}"/>" size="20"
                           maxlength="20"/>
                    <span class="erreur">${form.erreurs['pays']}</span>
                    <br/>

                    <label for="adresse">Pays <span class="requis">*</span></label>
                    <input type="text" id="adresse" name="adresse" value="<c:out value="${utilisateur.adresse}"/>" size="20"
                           maxlength="100"/>
                    <span class="erreur">${form.erreurs['adresse']}</span>
                    <br/>

                    <label for="email">Adresse email <span class="requis">*</span></label>
                    <input type="email" id="email" name="email" value="<c:out value="${utilisateur.email}"/>" size="20"
                           maxlength="60"/>
                    <span class="erreur">${form.erreurs['email']}</span>
                    <br/>

                    <label for="motdepasse">Mot de passe <span class="requis">*</span></label>
                    <input type="password" id="motdepasse" name="motdepasse" value="" size="20" maxlength="20"/>
                    <span class="erreur">${form.erreurs['motdepasse']}</span>
                    <br/>

                    <label for="confirmation">Confirmation du mot de passe <span class="requis">*</span></label>
                    <input type="password" id="confirmation" name="confirmation" value="" size="20" maxlength="20"/>
                    <span class="erreur">${form.erreurs['confirmation']}</span>
                    <br/>

                    <input type="submit" value="Inscription" class="sansLabel"/>
                    <br/>

                    <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
                </fieldset>
            </form>
        </div>

    </body>
</html>