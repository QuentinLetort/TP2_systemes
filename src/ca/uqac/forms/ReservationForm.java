package ca.uqac.forms;

import ca.uqac.beans.Itineraire;
import ca.uqac.beans.Reservation;
import ca.uqac.beans.Utilisateur;
import ca.uqac.dao.DAOException;
import ca.uqac.dao.ItineraireDao;
import ca.uqac.dao.ReservationDao;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.SysexMessage;
import java.util.HashMap;
import java.util.Map;

public class ReservationForm {
    private static final String CHAMP_NB_SIEGES = "nbsieges";

    private String resultat;
    private Map<String, String> erreurs = new HashMap<String, String>();
    private ReservationDao reservationDao;
    private ItineraireDao itineraireDao;

    public ReservationForm(ReservationDao reservationDao, ItineraireDao itineraireDao) {
        this.reservationDao = reservationDao;
        this.itineraireDao = itineraireDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Reservation reserverTrajet(HttpServletRequest request, Utilisateur client, Itineraire itineraire) {
        /* Récupération des champs du formulaire */
        String nbSieges = getValeurChamp(request, CHAMP_NB_SIEGES);
        System.out.println(nbSieges);
        Reservation reservation = new Reservation();
        try {
            traiterNbSieges(nbSieges, reservation);
            reservation.setItineraire(itineraire);
            reservation.setClient(client);

            if (erreurs.isEmpty()) {
                reservationDao.creer(reservation);
                resultat = "Votre réservation a été enregistré avec succès.";
            } else {
                resultat = "Échec de la réservation.";
            }
        } catch (DAOException e) {
            resultat = "Échec de la réservation : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }
        return reservation;
    }

    private void traiterNbSieges(String nbSieges, Reservation reservation) {
        try {
            if (nbSieges == null) {
                throw new FormValidationException("Le nombre de personnes doit être renseigné.");
            }
        } catch (FormValidationException e) {
            setErreur(CHAMP_NB_SIEGES, e.getMessage());
        }
        reservation.setNbSieges(nbSieges == null ? null : Long.parseLong(nbSieges));
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
