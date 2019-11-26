package ca.uqac.dao;

import ca.uqac.beans.Itineraire;
import ca.uqac.beans.Utilisateur;

import java.util.List;

public interface ItineraireDao {
    void creer(Itineraire itineraire) throws DAOException;

    Itineraire trouver(Long id) throws DAOException;

    List<Itineraire> lister() throws DAOException;
}
