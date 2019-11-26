package ca.uqac.servlets;

import ca.uqac.beans.Itineraire;
import ca.uqac.dao.DAOException;
import ca.uqac.dao.DAOFactory;
import ca.uqac.dao.ItineraireDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ListeItineraires")
public class ListeItineraires extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String VUE = "/WEB-INF/listerItineraires.jsp";
    public static final String ATT_RESULTAT = "resultat";
    public static final String ATT_ITINERAIRES ="listeItineraires";

    private ItineraireDao itineraireDao;

    public void init() throws ServletException {
        this.itineraireDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getItineraireDao();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Itineraire> listeItineraires = null;
        try {
             listeItineraires = itineraireDao.lister();
        } catch (DAOException e) {
            String resultat = "Échec de l'affichage de la liste : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
            request.setAttribute(ATT_RESULTAT, resultat);
        }
        request.setAttribute(ATT_ITINERAIRES, listeItineraires);
        this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
    }
}
