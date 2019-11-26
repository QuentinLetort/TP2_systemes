package ca.uqac.dao;

import ca.uqac.beans.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ca.uqac.dao.DAOUtilitaire.*;

public class UtilisateurDaoImpl implements UtilisateurDao {
    private DAOFactory daoFactory;
    private static final String SQL_SELECT_PAR_EMAIL = "SELECT id, nom, prenom, sexe, pays, adresse, email, mot_de_passe, date_inscription FROM Utilisateur WHERE email = ?";
    private static final String SQL_SELECT_PAR_ID = "SELECT id, nom, prenom, sexe, pays, adresse, email, mot_de_passe, date_inscription FROM Utilisateur WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO Utilisateur (nom, prenom, sexe, pays, adresse, email, mot_de_passe, date_inscription) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

    UtilisateurDaoImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void creer(Utilisateur utilisateur) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            String sexeIntiale = utilisateur.getSexe().equals("Homme") ? "h" : "f";
            preparedStatement = initialisationRequetePreparee(connexion, SQL_INSERT, true, utilisateur.getNom(), utilisateur.getPrenom(), sexeIntiale, utilisateur.getPays(), utilisateur.getAdresse(), utilisateur.getEmail(), utilisateur.getMotDePasse());
            int statut = preparedStatement.executeUpdate();
            /* Analyse du statut retourné par la requête d'insertion */
            if (statut == 0) {
                throw new DAOException("Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table.");
            }
            /* Récupération de l'id auto-généré par la requête d'insertion */
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if (valeursAutoGenerees.next()) {
                /* Puis initialisation de la propriété id du bean Utilisateur avec sa valeur */
                utilisateur.setId(valeursAutoGenerees.getLong(1));
            } else {
                throw new DAOException("Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
        }
    }

    @Override
    public Utilisateur trouver(Long id) throws DAOException {
        return trouver(SQL_SELECT_PAR_ID, id);
    }

    @Override
    public Utilisateur trouver(String email) throws DAOException {
        return trouver(SQL_SELECT_PAR_EMAIL, email);
    }

    private Utilisateur trouver(String sql, Object... objets) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Utilisateur utilisateur = null;

        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = daoFactory.getConnection();
            /*
             * Préparation de la requête avec les objets passés en arguments
             */
            preparedStatement = initialisationRequetePreparee(connexion, sql, false, objets);
            resultSet = preparedStatement.executeQuery();
            /* Parcours de la ligne de données retournée dans le ResultSet */
            if (resultSet.next()) {
                utilisateur = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(resultSet, preparedStatement, connexion);
        }

        return utilisateur;
    }

    private static Utilisateur map(ResultSet resultSet) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(resultSet.getLong("id"));
        utilisateur.setNom(resultSet.getString("nom"));
        utilisateur.setPrenom(resultSet.getString("prenom"));
        String sexe = resultSet.getString("sexe").equals("h") ? "Homme" : "Femme";
        utilisateur.setSexe(sexe);
        utilisateur.setPays(resultSet.getString("pays"));
        utilisateur.setAdresse(resultSet.getString("adresse"));
        utilisateur.setEmail(resultSet.getString("email"));
        utilisateur.setMotDePasse(resultSet.getString("mot_de_passe"));
        utilisateur.setDateInscription(resultSet.getTimestamp("date_inscription"));
        return utilisateur;
    }
}
