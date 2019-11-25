package ca.uqac.servlets;

import ca.uqac.beans.Utilisateur;
import ca.uqac.dao.DAOFactory;
import ca.uqac.dao.UtilisateurDao;
import ca.uqac.forms.InscriptionForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Inscription")
public class Inscription extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String VUE = "/WEB-INF/inscription.jsp";
    public static final String ATT_USER = "utilisateur";
    public static final String ATT_FORM = "form";

    private UtilisateurDao utilisateurDao;

    public void init() throws ServletException {
        this.utilisateurDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getUtilisateurDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Préparation de l'objet formulaire */
        InscriptionForm form = new InscriptionForm(utilisateurDao);

        /* Appel au traitement et à la validation de la requête, et récupération du bean en résultant */
        Utilisateur utilisateur = form.inscrireUtilisateur(request);

        /* Stockage du formulaire et du bean dans l'objet request */
        request.setAttribute(ATT_FORM, form);
        request.setAttribute(ATT_USER, utilisateur);

        this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
    }
}