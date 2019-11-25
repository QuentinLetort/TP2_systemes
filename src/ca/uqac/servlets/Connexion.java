package ca.uqac.servlets;

import ca.uqac.beans.Utilisateur;
import ca.uqac.dao.DAOFactory;
import ca.uqac.dao.UtilisateurDao;
import ca.uqac.forms.ConnexionForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "Connexion")
public class Connexion extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String ATT_USER = "utilisateur";
    public static final String ATT_FORM = "form";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String VUE = "/WEB-INF/connexion.jsp";

    private UtilisateurDao utilisateurDao;

    public void init() throws ServletException {
        this.utilisateurDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getUtilisateurDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Préparation de l'objet formulaire
        ConnexionForm form = new ConnexionForm(utilisateurDao);

        // Traitement de la requête et récupération de l'utilisateur
        Utilisateur utilisateur = form.connecterUtilisateur(request);

        // Récupération de la session
        HttpSession session = request.getSession();

        /**
         * Si aucune erreur de validation n'a eu lieu, alors ajout du bean
         * Utilisateur à la session, sinon suppression du bean de la session.
         */
        if (form.getErreurs().isEmpty()) {
            session.setAttribute(ATT_SESSION_USER, utilisateur);
        } else {
            session.setAttribute(ATT_SESSION_USER, null);
        }

        // Stockage du formulaire et de l'utilisateur dans la requête
        request.setAttribute(ATT_FORM, form);
        request.setAttribute(ATT_USER, utilisateur);

        this.getServletContext().getRequestDispatcher(VUE).forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Affichage de la page de connexion
        this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
    }
}
