package ca.uqac.dao;

import ca.uqac.beans.Reservation;
import ca.uqac.beans.Utilisateur;

import static ca.uqac.dao.DAOUtilitaire.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDaoImpl implements ReservationDao {

    private static final String SQL_SELECT = "SELECT id, id_client, id_itineraire, nb_sieges, date_reservation FROM Reservation ORDER BY id";
    private static final String SQL_SELECT_PAR_ID = "SELECT id, id_client, id_itineraire, nb_sieges, date_reservation FROM Reservation WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO Reservation (id_client, id_itineraire, nb_sieges, date_reservation) VALUES (?, ?, ?, NOW())";

    private DAOFactory daoFactory;

    ReservationDaoImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void creer(Reservation reservation) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet valeursAutoGenerees = null;

        try {
            connexion = daoFactory.getConnection();
            preparedStatement = initialisationRequetePreparee(connexion, SQL_INSERT, true,
                    reservation.getClient().getId(), reservation.getItineraire().getId(), reservation.getNbSieges());
            int statut = preparedStatement.executeUpdate();
            if (statut == 0) {
                throw new DAOException("Échec de la création de la réservation, aucune ligne ajoutée dans la table.");
            }
            valeursAutoGenerees = preparedStatement.getGeneratedKeys();
            if (valeursAutoGenerees.next()) {
                reservation.setId(valeursAutoGenerees.getLong(1));
            } else {
                throw new DAOException("Échec de la création de la réservation en base, aucun ID auto-généré retourné.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(valeursAutoGenerees, preparedStatement, connexion);
        }
    }

    @Override
    public Reservation trouver(long id) throws DAOException {
        return trouver(SQL_SELECT_PAR_ID, id);
    }

    @Override
    public List<Reservation> lister() throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Reservation> reservations = new ArrayList<Reservation>();

        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(SQL_SELECT);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reservations.add(map(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(resultSet, preparedStatement, connection);
        }

        return reservations;
    }

    /*
     * Méthode générique utilisée pour retourner une réservation depuis la base de
     * données, correspondant à la requête SQL donnée prenant en paramètres les
     * objets passés en argument.
     */
    private Reservation trouver(String sql, Object... objets) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Reservation reservation = null;

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
                reservation = map(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            fermeturesSilencieuses(resultSet, preparedStatement, connexion);
        }

        return reservation;
    }

    /*
     * Simple méthode utilitaire permettant de faire la correspondance (le
     * mapping) entre une ligne issue de la table des réservations (un ResultSet) et
     * un bean Reservation.
     */
    private Reservation map(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(resultSet.getLong("id"));
        reservation.setNbSieges(resultSet.getLong("nb_sieges"));
        reservation.setDateReservation(resultSet.getTimestamp("date_reservation"));

        UtilisateurDao utilisateurDao = daoFactory.getUtilisateurDao();
        reservation.setClient(utilisateurDao.trouver(resultSet.getLong("id_client")));

        ItineraireDao itineraireDao = daoFactory.getItineraireDao();
        reservation.setItineraire(itineraireDao.trouver(resultSet.getLong("id_itineraire")));
        return reservation;
    }

}
