package ca.uqac.forms;

import ca.uqac.beans.Utilisateur;
import ca.uqac.dao.DAOException;
import ca.uqac.dao.UtilisateurDao;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ConnexionForm {
    private static final String CHAMP_EMAIL = "email";
    private static final String CHAMP_PASS = "motdepasse";
    private static final String ALGO_CHIFFREMENT = "SHA-256";

    private String resultat;
    private Map<String, String> erreurs = new HashMap<String, String>();
    private UtilisateurDao utilisateurDao;

    public ConnexionForm(UtilisateurDao utilisateurDao) {
        this.utilisateurDao = utilisateurDao;
    }

    public String getResultat() {
        return resultat;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public Utilisateur connecterUtilisateur(HttpServletRequest request) {
        /* Récupération des champs du formulaire */
        String email = getValeurChamp(request, CHAMP_EMAIL);
        String motDePasse = getValeurChamp(request, CHAMP_PASS);

        Utilisateur utilisateur = new Utilisateur();

        try {
            traiterEmail(email);
            traiterMotDePasse(motDePasse);
            utilisateur = utilisateurDao.trouver(email);

            /* Initialisation du résultat global de la validation. */
            if (erreurs.isEmpty()) {
                utilisateur = utilisateurDao.trouver(email);
                if (utilisateur != null && verifierMotDePasse(motDePasse, utilisateur.getMotDePasse())) {
                    resultat = "Succès de la connexion.";
                } else {
                    resultat = "Échec de la connexion.";
                }
            } else {
                resultat = "Échec de la connexion.";
            }
        } catch (DAOException e) {
            resultat = "Échec de la connexion : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }
        return utilisateur;
    }

    private boolean verifierMotDePasse(String motDePasseSaisi, String motDePasseReel) {
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm(ALGO_CHIFFREMENT);
        boolean res = passwordEncryptor.checkPassword(motDePasseSaisi, motDePasseReel);
        if (!res) {
            setErreur(CHAMP_PASS, "Merci de saisir un mot de passe valide.");
        }
        return res;
    }

    /*
     * Appel à la validation de l'adresse email reçue et initialisation de la
     * propriété email du bean
     */
    private void traiterEmail(String email) {
        try {
            validationEmail(email);
        } catch (FormValidationException e) {
            setErreur(CHAMP_EMAIL, e.getMessage());
        }
    }

    /*
     * Appel à la validation des mots de passe reçus, chiffrement du mot de
     * passe et initialisation de la propriété motDePasse du bean
     */
    private void traiterMotDePasse(String motDePasse) {
        try {
            validationMotDePasse(motDePasse);
        } catch (FormValidationException e) {
            setErreur(CHAMP_PASS, e.getMessage());
        }
    }

    /**
     * Valide l'adresse email saisie.
     */
    private void validationEmail(String email) throws FormValidationException {
        if (email != null && !email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) {
            throw new FormValidationException("Merci de saisir une adresse mail valide.");
        }
    }

    /**
     * Valide le mot de passe saisi.
     */
    private void validationMotDePasse(String motDePasse) throws FormValidationException {
        if (motDePasse != null) {
            if (motDePasse.length() < 3) {
                throw new FormValidationException("Le mot de passe doit contenir au moins 3 caractères.");
            }
        } else {
            throw new FormValidationException("Merci de saisir un mot de passe valide.");
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
