package ca.uqac.dao;

import ca.uqac.beans.Itineraire;
import ca.uqac.beans.Reservation;
import ca.uqac.beans.Utilisateur;

import java.util.List;

public interface ReservationDao {
    void creer(Reservation reservation) throws DAOException;

    Reservation trouver(long id) throws DAOException;

    List<Reservation> lister() throws DAOException;
}
