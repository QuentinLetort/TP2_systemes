package ca.uqac.forms;

import ca.uqac.beans.Itineraire;
import ca.uqac.beans.Utilisateur;
import ca.uqac.dao.DAOException;
import ca.uqac.dao.ItineraireDao;
import com.sun.jdi.connect.Connector;

import javax.print.attribute.standard.Destination;
import javax.servlet.http.HttpServletRequest;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class PublicationItineraireForm {
    private static final String CHAMP_DATE = "date";
    private static final String CHAMP_HEURE = "heure";
    private static final String CHAMP_DESTINATION = "destination";
    private static final String CHAMP_ORIGINE = "origine";
    private static final String CHAMP_NB_PLACES = "nbplaces";
    private static final String CHAMP_TAILLE_BAGGAGE = "taillebaggage";
    private static final String CHAMP_PRIX = "prix";
    private static final int PRIX_MAX = 500;
    private static final int PRIX_MIN = 1;
    private static final int NB_PLACES_MAX = 5;
    private static final int NB_PLACES_MIN = 1;

    private String resultat;
    private Map<String, String> erreurs = new HashMap<>();
    private ItineraireDao itineraireDao;

    public PublicationItineraireForm(ItineraireDao itineraireDao) {
        this.itineraireDao = itineraireDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Itineraire publierItineraire(HttpServletRequest request, Utilisateur conducteur) {
        String date = getValeurChamp(request, CHAMP_DATE);
        String heure = getValeurChamp(request, CHAMP_HEURE);
        String destination = getValeurChamp(request, CHAMP_DESTINATION);
        String origine = getValeurChamp(request, CHAMP_ORIGINE);
        String nbPlaces = getValeurChamp(request, CHAMP_NB_PLACES);
        String tailleBaggage = getValeurChamp(request, CHAMP_TAILLE_BAGGAGE);
        String prix = getValeurChamp(request, CHAMP_PRIX);

        Itineraire itineraire = new Itineraire();
        try {
            traiterTemps(date, heure, itineraire);
            traiterVilles(destination, origine, itineraire);
            traiterNbPlaces(nbPlaces, itineraire);
            traiterTailleBaggage(tailleBaggage, itineraire);
            traiterPrix(prix, itineraire);
            itineraire.setConducteur(conducteur);
            if (erreurs.isEmpty()) {
                itineraireDao.creer(itineraire);
                resultat = "Votre itinéraire a été publié avec succès.";
            } else {
                resultat = "Échec de la publication.";
            }
        } catch (DAOException e) {
            resultat = "Échec de la publication : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }

        return itineraire;
    }

    private void traiterPrix(String prix, Itineraire itineraire) {
        try {
            validationPrix(prix);
        } catch (FormValidationException e) {
            setErreur(CHAMP_PRIX, e.getMessage());
        }
        itineraire.setPrix(prix == null ? null : Float.parseFloat(prix));
    }

    private void traiterTailleBaggage(String tailleBaggage, Itineraire itineraire) {
        try {
            validationTailleBaggage(tailleBaggage);
        } catch (FormValidationException e) {
            setErreur(CHAMP_TAILLE_BAGGAGE, e.getMessage());
        }
        itineraire.setTailleBaggage(tailleBaggage);
    }

    private void traiterNbPlaces(String nbPlaces, Itineraire itineraire) {
        try {
            validationNbPlaces(nbPlaces);
        } catch (FormValidationException e) {
            setErreur(CHAMP_NB_PLACES, e.getMessage());
        }
        itineraire.setNbPlaces(nbPlaces == null ? null : Long.parseLong(nbPlaces));
    }

    private void traiterVilles(String destination, String origine, Itineraire itineraire) {
        try {
            validationVilles(destination, origine);
        } catch (FormValidationException e) {
            setErreur(CHAMP_DESTINATION, e.getMessage());
            setErreur(CHAMP_ORIGINE, null);
        }
        itineraire.setDestination(destination);
        itineraire.setOrigine(origine);
    }

    private void traiterTemps(String date, String heure, Itineraire itineraire) {
        try {
            validationTemps(date, heure);
        } catch (FormValidationException e) {
            setErreur(CHAMP_DATE, e.getMessage());
            setErreur(CHAMP_HEURE, null);
        }
        itineraire.setDate(date == null ? null : LocalDate.parse(date));
        itineraire.setHeure(heure == null ? null : LocalTime.parse(heure));
    }

    private void validationNbPlaces(String nbPlaces) throws FormValidationException {
        if (nbPlaces != null) {
            Long nbPlaceFinal;
            try {
                nbPlaceFinal = Long.parseLong(nbPlaces);
            } catch (Exception e) {
                throw new FormValidationException("Le nombre de places doit être un nombre.");
            }
            if (nbPlaceFinal < NB_PLACES_MIN || nbPlaceFinal > NB_PLACES_MAX) {
                throw new FormValidationException("Le nombre de places doit être compris entre " + NB_PLACES_MIN + " et " + NB_PLACES_MAX + ".");
            }
        } else {
            throw new FormValidationException("Le nombre de places disponibles doit être renseigné.");
        }
    }

    private void validationPrix(String prix) throws FormValidationException {
        if (prix != null) {
            Float prixFinal;
            try {
                prixFinal = Float.parseFloat(prix);
            } catch (Exception e) {
                throw new FormValidationException("Le prix doit être un nombre.");
            }
            if (prixFinal > PRIX_MAX || prixFinal < PRIX_MIN) {
                throw new FormValidationException("Le prix doit être compris entre " + PRIX_MIN + " et " + PRIX_MAX + " dollars.");
            }
        } else {
            throw new FormValidationException("Le prix doit être renseigné.");
        }
    }

    /* Validation du champs de la taille de baggage*/
    private void validationTailleBaggage(String tailleBaggage) throws FormValidationException {
        if (tailleBaggage == null) {
            throw new FormValidationException("La taille de baggage des clients doit être renseigné.");
        }
    }


    /* Validation des champs de temps */
    private void validationTemps(String date, String heure) throws FormValidationException {
        if (date == null || heure == null) {
            throw new FormValidationException("La date et l'heure doivent être renseignées.");
        } else {
            LocalDate dateFinale;
            LocalTime heureFinale;
            try {
                dateFinale = LocalDate.parse(date);
                heureFinale = LocalTime.parse(heure);
            } catch (Exception e) {
                throw new FormValidationException("La date et l'heure doivent être renseignées.");
            }
            if (LocalDateTime.of(dateFinale, heureFinale).isBefore(LocalDateTime.now())) {
                throw new FormValidationException("La date et l'heure ne peuvent pas être avant l'instant présent.");
            }
        }
    }

    /* Validation des champs de ville */
    private void validationVilles(String destination, String origine) throws FormValidationException {
        if (destination == null || origine == null) {
            throw new FormValidationException("Merci de saisir les villes d'origine et de destination.");
        } else if (destination.equals(origine)) {
            throw new FormValidationException("Les villes d'origine et de destination sont identiques, merci de les saisir à nouveau.");
        }
    }

    /*
     * Ajoute un message correspondant au champ spécifié à la map des erreurs.
     */
    private void setErreur(String champ, String message) {
        erreurs.put(champ, message);
    }

    /*
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     */
    private static String getValeurChamp(HttpServletRequest request, String nomChamp) {
        String valeur = request.getParameter(nomChamp);
        if (valeur == null || valeur.trim().length() == 0) {
            return null;
        } else {
            return valeur;
        }
    }
}
