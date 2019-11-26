package ca.uqac.servlets;

import ca.uqac.beans.Itineraire;

import ca.uqac.beans.Reservation;
import ca.uqac.beans.Utilisateur;
import ca.uqac.dao.DAOFactory;
import ca.uqac.dao.ItineraireDao;
import ca.uqac.dao.ReservationDao;
import ca.uqac.forms.ReservationForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Reservation")
public class ReservationTrajet extends HttpServlet {
    public static final String CONF_DAO_FACTORY = "daofactory";
    public static final String PARAM_ID_ITINERAIRE = "itineraireId";
    public static final String ATT_ITINERAIRE = "itineraire";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ATT_RESERVATION = "reservation";
    public static final String ATT_FORM = "form";
    public static final String VUE = "/WEB-INF/creerReservation.jsp";
    public static final String VUE_SUCCES = "/WEB-INF/afficherReservation.jsp";
    public static final String REDIRECTION = "/accueil";

    private ItineraireDao itineraireDao;
    private ReservationDao reservationDao;
    private Itineraire itineraire;

    public void init() throws ServletException {
        this.itineraireDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getItineraireDao();
        this.reservationDao = ((DAOFactory) getServletContext().getAttribute(CONF_DAO_FACTORY)).getReservationDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ReservationForm form = new ReservationForm(reservationDao, itineraireDao);

        Utilisateur client = (Utilisateur) request.getSession().getAttribute(ATT_SESSION_USER);
        Reservation reservation = form.reserverTrajet(request, client, itineraire);
        String vue;
        request.setAttribute(ATT_FORM, form);
        request.setAttribute(ATT_RESERVATION, reservation);
        if (form.getErreurs().isEmpty()) {
            vue = VUE_SUCCES;
        } else {
            request.setAttribute(ATT_ITINERAIRE, itineraire);
            vue = VUE;
        }
        System.out.println(vue);
        this.getServletContext().getRequestDispatcher(vue).forward(request, response);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Récupération du paramètre */
        Long idItineraire = null;
        itineraire = null;
        try {
            idItineraire = Long.parseLong(getValeurParametre(request, PARAM_ID_ITINERAIRE));
            itineraire = itineraireDao.trouver(idItineraire);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (itineraire != null) {
            request.setAttribute(ATT_ITINERAIRE, itineraire);
            this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
        } else {
            response.sendRedirect(this.getServletContext().getContextPath() + REDIRECTION);
        }
    }

    private static String getValeurParametre(HttpServletRequest request, String nomChamp) {
        String valeur = request.getParameter(nomChamp);
        if (valeur == null || valeur.trim().length() == 0) {
            return null;
        } else {
            return valeur;
        }
    }
}
