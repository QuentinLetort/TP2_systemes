package ca.uqac.dao;

import ca.uqac.beans.Utilisateur;

public interface UtilisateurDao {
    void creer(Utilisateur utilisateur) throws DAOException;

    Utilisateur trouver(String email) throws DAOException;
}
