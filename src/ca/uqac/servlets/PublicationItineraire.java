package ca.uqac.servlets;

import ca.uqac.beans.Itineraire;
import ca.uqac.beans.Utilisateur;
import ca.uqac.dao.DAOFactory;
import ca.uqac.dao.ItineraireDao;
import ca.uqac.forms.PublicationItineraireForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Publication")
public class PublicationItineraire extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String VUE = "/WEB-INF/publication.jsp";

    public static final String ATT_ITINERAIRE = "itineraire";
    public static final String ATT_FORM = "form";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";

    private ItineraireDao itineraireDao;

    public void init() throws ServletException {
        this.itineraireDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getItineraireDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Préparation de l'objet formulaire */
        PublicationItineraireForm form = new PublicationItineraireForm(itineraireDao);

        /* Récupération de l'utilisateur en session */
        Utilisateur conducteur = (Utilisateur) request.getSession().getAttribute(ATT_SESSION_USER);

        /* Appel au traitement et à la validation de la requête, et récupération du bean en résultant */
        Itineraire itineraire = form.publierItineraire(request, conducteur);

        /* Stockage du formulaire et du bean dans l'objet request */
        request.setAttribute(ATT_FORM, form);
        request.setAttribute(ATT_ITINERAIRE, itineraire);

        this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
    }
}
