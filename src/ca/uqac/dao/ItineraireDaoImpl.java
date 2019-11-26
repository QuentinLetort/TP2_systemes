package ca.uqac.dao;

import ca.uqac.beans.Itineraire;
import ca.uqac.beans.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ca.uqac.dao.DAOUtilitaire.fermeturesSilencieuses;
import static ca.uqac.dao.DAOUtilitaire.initialisationRequetePreparee;

public class ItineraireDaoImpl implements ItineraireDao {
    private static final String SQL_SELECT = "SELECT id, id_conducteur, date, heure, destination, origine, nb_places, prix, taille_baggage, date_annonce FROM Itineraire ORDER BY id";
    private static final String SQL_SELECT_PAR_ID = "SELECT id, id_conducteur, date, heure, destination, origine, nb_places, prix, taille_baggage, date_annonce FROM Itineraire WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO Itineraire (id_conducteur, date, heure, destination, origine, nb_places, prix, taille_baggage, date_annonce) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
    private static final String SQL_UPDATE = "UPDATE Itineraire SET nb_places = ? WHERE id = ?";
    
    private DAOFactory daoFactory;

    ItineraireDaoImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void creer(Itineraire itineraire) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee(connexion, SQL_INSERT, true,
                    itineraire.getConducteur().getId(), itineraire.getDate(), itineraire.getHeure(),
                    itineraire.getDestination(), itineraire.getOrigine(), itineraire.getNbPlaces(),
                    itineraire.getPrix(), itineraire.getTailleBaggage());
            int statut = preparedStatement.executeUpdate();
            if (statut == 0) {
                throw new DAOException("Échec de la création de l'itinéraire, aucune ligne ajoutée dans la table.");
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if (valeursAutoGenerees.next()) {
                itineraire.setId(valeursAutoGenerees.getLong(1));
            } else {
                throw new DAOException("Échec de la création de l'itinéraire en base, aucun ID auto-généré retourné.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
        }
    }

    @Override
    public Itineraire trouver(Long id) throws DAOException {
        return trouver(SQL_SELECT_PAR_ID, id);
    }

    @Override
    public List<Itineraire> lister() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Itineraire> itineraires = new ArrayList<Itineraire>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(SQL_SELECT);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                itineraires.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(resultSet, preparedStatement, connection);
        }

        return itineraires;
    }

    @Override
    public void modifier(Itineraire itineraire, Long nbPlaces) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee(connexion, SQL_UPDATE, false,
                    itineraire.getId(), nbPlaces);
            int statut = preparedStatement.executeUpdate();
            if (statut == 0) {
                throw new DAOException("Échec de la modification de l'itinéraire, aucune ligne modifiée dans la table.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(preparedStatement, connexion);
        }
    }

    /*
     * Méthode générique utilisée pour retourner une réservation depuis la base de
     * données, correspondant à la requête SQL donnée prenant en paramètres les
     * objets passés en argument.
     */
    private Itineraire trouver(String sql, Object... objets) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Itineraire itineraire = null;

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
                itineraire = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(resultSet, preparedStatement, connexion);
        }

        return itineraire;
    }

    /*
     * Simple méthode utilitaire permettant de faire la correspondance (le
     * mapping) entre une ligne issue de la table des réservations (un ResultSet) et
     * un bean Reservation.
     */
    private Itineraire map(ResultSet resultSet) throws SQLException {
        Itineraire itineraire = new Itineraire();
        itineraire.setId(resultSet.getLong("id"));
        itineraire.setDate(resultSet.getDate("date").toLocalDate());
        itineraire.setHeure(resultSet.getTime("heure").toLocalTime());
        itineraire.setDestination(resultSet.getString("destination"));
        itineraire.setOrigine(resultSet.getString("origine"));
        itineraire.setNbPlaces(resultSet.getLong("nb_places"));
        itineraire.setPrix((resultSet.getFloat("prix")));
        itineraire.setTailleBaggage(resultSet.getString("taille_baggage"));
        itineraire.setDateAnnonce(resultSet.getTimestamp("date_annonce"));
        UtilisateurDao utilisateurDao = daoFactory.getUtilisateurDao();
        itineraire.setConducteur(utilisateurDao.trouver(resultSet.getLong("id_conducteur")));

        return itineraire;
    }
}
